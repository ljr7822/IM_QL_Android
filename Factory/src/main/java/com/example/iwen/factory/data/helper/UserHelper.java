package com.example.iwen.factory.data.helper;

import android.util.Log;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.utils.CollectionUtil;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.user.UserUpdateModel;
import com.example.iwen.factory.model.card.UserCard;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.model.db.User_Table;
import com.example.iwen.factory.net.Network;
import com.example.iwen.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求
 *
 * @author : iwen大大怪
 * create : 12-13 013 20:41
 */
public class UserHelper {

    /**
     * 更新用户信息
     *
     * @param model    UserUpdateModel
     * @param callback DataSource.Callback<UserCard>
     */
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        // 得到一个call进行注册
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        // 进行异步请求
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    // 回调成功
                    UserCard userCard = rspModel.getResult();
                    // 喚起進行保存的操作
                    Factory.getUserCenter().dispatch(userCard);
                    // callabck返回
                    callback.onDataLoad(userCard);
                } else {
                    // 错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.e("ljr", "onFailure message: " + t);
            }
        });
    }


    /**
     * 搜索方法
     *
     * @param name     要搜索的用户名字
     * @param callback DataSource.Callback<List<UserCard>>
     */
    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        // 得到一个call进行注册
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        // 进行异步请求
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoad(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.e("ljr", "onFailure message: " + t);
            }
        });
        // 把当前的调度者返回
        return call;
    }

    /**
     * 关注的网络请求
     *
     * @param id       用户id
     * @param callback 返回
     */
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        // 得到一个call进行注册
        Call<RspModel<UserCard>> call = service.userFollow(id);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    // 保存用户信息
                    UserCard userCard = rspModel.getResult();
                    // 喚起進行保存的操作
                    Factory.getUserCenter().dispatch(userCard);
                    // 返回数据
                    callback.onDataLoad(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 刷新联系人列表方法
     * 不需要callback，直接存储到数据库，并通过数据库观察者进行通知界面刷新，界面更新时进行对比，差异更新
     */
    public static void refreshContacts() {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        // 得到一个call进行注册
        Call<RspModel<List<UserCard>>> call = service.userContacts();
        // 进行异步请求
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    // 拿到集合
                    List<UserCard> cards = rspModel.getResult();
                    if (cards == null || cards.size() == 0) {
                        return;
                    }
                    // 拿到用户中心进行数据存储与分发
                    // 方法1：
                    //Factory.getUserCenter().dispatch(cards.toArray(new UserCard[0]));
                    // 方法2：
                    Factory.getUserCenter().dispatch(CollectionUtil.toArray(cards, UserCard.class));
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                Log.e("ljr", "onFailure message: " + t);
            }
        });
    }

    /**
     * 从本地查询一个用户的信息
     *
     * @param id 用户id
     * @return User
     */
    public static User findFromLocal(String id){
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    /**
     * 从网络查询某用户的信息
     *
     * @param id 用户id
     * @return User
     */
    public static User findFromNet(String id) {
        RemoteService remoteService = Network.mRemoteService();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if (card != null) {
                User user = card.build();
                // 数据库的存储并通知
                Factory.getUserCenter().dispatch(card);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索一个用户，优先本地缓存，没有用然后再从网络拉取
     * @param id 用户id
     * @return User
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }
    /**
     * 搜索一个用户，优先网络查询，没有用然后再从本地缓存拉取
     *
     * @param id 用户id
     * @return User
     */
    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        return user;
    }
}
