package com.example.iwen.factory.data.helper;

import android.text.TextUtils;
import android.util.Log;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.account.AccountRspModel;
import com.example.iwen.factory.model.api.account.LoginModel;
import com.example.iwen.factory.model.api.account.RegisterModel;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.net.Network;
import com.example.iwen.factory.net.RemoteService;
import com.example.iwen.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 账户部分的网络请求
 *
 * @author : iwen大大怪
 * create : 12-7 007 17:02
 */
public class AccountHelper {
    /**
     * 注册接口
     *
     * @param model    传递一个注册的Model进来
     * @param callback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        // 得到一个call进行注册
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        // 进行异步请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登录接口
     *
     * @param model    传递一个登录的Model进来
     * @param callback 成功与失败的接口回送
     */
    public static void login(LoginModel model, final DataSource.Callback<User> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        // 得到一个call进行注册
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        // 进行异步请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 对设备id绑定
     *
     * @param callback Callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        // 先判断当前有没有这个id的存在
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 请求的回调部分封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {
        final DataSource.Callback<User> callback;

        AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call,
                               Response<RspModel<AccountRspModel>> response) {
            // 请求成功
            // 从返回中得到我们的全局Model，内部使用的是Gson进行解析
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                AccountRspModel accountRspModel = rspModel.getResult();
                // 获取我的信息
                User user = accountRspModel.getUser();
                // 进行数据库写入
                // 方法1.直接保存
                user.save();
                        /*
                        // 方法2.通过ModelAdapter
                        FlowManager.getModelAdapter(User.class).save(user);
                        // 方法3.通过事务
                        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
                        databaseDefinition.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                FlowManager.getModelAdapter(User.class).save(user);
                            }
                        }).build().execute();
                         */
                // 通过xml存储个人信息
                Account.login(accountRspModel);
                // 判断是否绑定设备
                if (accountRspModel.isBind()) {
                    // 设置绑定状态
                    Account.setIsBind(true);
                    // 回调
                    if (callback != null) {
                        callback.onDataLoad(user);
                    }
                } else {
                    // 进行绑定
                    bindPush(callback);
                }
            } else {
                // 对返回的RspModel中的失败的Code进行解析，解析到对应的String资源中
                Factory.decodeRspCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            // 请求失败
            if (callback != null) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
            Log.e("ljr", "onFailure message: " + t);
        }
    }
}
