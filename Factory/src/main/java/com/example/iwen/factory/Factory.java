package com.example.iwen.factory;

import android.util.Log;

import androidx.annotation.StringRes;

import com.example.iwen.common.app.Application;
import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.data.group.GroupCenter;
import com.example.iwen.factory.data.group.GroupDispatcher;
import com.example.iwen.factory.data.message.MessageCenter;
import com.example.iwen.factory.data.message.MessageDispatcher;
import com.example.iwen.factory.data.user.UserCenter;
import com.example.iwen.factory.data.user.UserDispatcher;
import com.example.iwen.factory.model.api.PushModel;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.card.GroupMemberCard;
import com.example.iwen.factory.model.card.MessageCard;
import com.example.iwen.factory.model.card.UserCard;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.utils.DBFlowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author : Iwen大大怪
 * create : 2020/11/15 13:36
 */
public class Factory {
    private static final String TAG = Factory.class.getCanonicalName();
    // Factory单例模式
    private static Factory instance;
    // 初始化线程池
    private final Executor mExecutor;
    // 维持一个全局的Gson
    private final Gson gson;

    static {
        instance = new Factory();
    }

    private Factory() {
        // 新建一个4线程的线程池
        mExecutor = Executors.newFixedThreadPool(4);
        // 配置全局gson格式
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // 设置过滤器，数据库级别的model不进行转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /**
     * Factory中的初始化
     */
    public static void setup() {
        // 初始化 DbFlow数据库
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)
                .build()); // 数据库初始化的时候就开始打开
        // 持久化的数据进行初始化
        Account.load(app());
    }

    /**
     * 返回全局的Application
     *
     * @return Application
     */
    public static Application app() {
        return Application.getInstance();
    }

    /**
     * 异步执行方法
     *
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable) {
        // 拿到单例，拿到线程池，然后异步执行
        instance.mExecutor.execute(runnable);
    }

    /**
     * 返回一个全局的gson
     *
     * @return Gson
     */
    public static Gson getGson() {
        return instance.gson;
    }

    /**
     * 进行错误数据的解析
     * 把网络返回的code值进行统一解析并返回为一个String资源
     *
     * @param model    RspModel
     * @param callback DataSource.FailedCallback 用于返回一个错误的资源id
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null) {
            return;
        }
        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId, final DataSource.FailedCallback callback) {
        if (callback != null) {
            callback.onDataNotAvailable(resId);
        }
    }

    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    public void logout() {
        // TODO 退出登录
        Application.getInstance().finishAll();
    }

    /**
     * 处理推送来的消息
     *
     * @param str String 消息
     */
    public static void dispatchPush(String str) {
        // 首先检查登录状态
        if (!Account.isLogin()) {
            return;
        }
        // 检查是否有返回的数据为空
        PushModel model = PushModel.decode(str);
        if (model==null){
            return;
        }
        Log.e(TAG,model.toString());
        // 对推送集合进行遍历
        for (PushModel.Entity entity : model.getEntities()) {
            Log.e(TAG, "dispatchPush-ENTITY: " + entity.toString());
            // 判断推送过来消息类型
            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT: {
                    instance.logout();
                    // 退出情况下直接返回，并且不可继续
                    break;
                }
                case PushModel.ENTITY_TYPE_MESSAGE: {
                    // 普通消息
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                }
                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    // 添加朋友
                    UserCard card = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }
                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    // 添加群
                    GroupCard card = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(card);
                    break;
                }
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS: {
                    // 群成员变更，回来的是一个群成员的列表
                    Type type = new TypeToken<List<GroupMemberCard>>(){}.getType();
                    List<GroupMemberCard> card = getGson().fromJson(entity.content, type);
                    getGroupCenter().dispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS: {
                    // TODO 成员退出的推送

                    break;
                }
            }
        }
    }

    /**
     * 獲取一個用戶中心的實現類
     *
     * @return UserDispatcher.getInstance()
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.getInstance();
    }

    /**
     * 获取一個消息中心的實現類
     *
     * @return MessageDispatcher.getInstance()
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.getInstance();
    }

    /**
     * 獲取一個群中心的實現類
     *
     * @return GroupDispatcher.getInstance()
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.getInstance();
    }
}
