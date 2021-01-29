package com.example.iwen.factory.data.helper;

import android.util.Log;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.user.UserUpdateModel;
import com.example.iwen.factory.model.card.UserCard;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.net.Network;
import com.example.iwen.factory.net.RemoteService;

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
                    // 数据库存储，需要把userCard装换成user
                    User user = userCard.build();
                    // 保存用户信息
                    user.save();
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
                if (rspModel.success()){
                    // 保存用户信息
                    UserCard userCard = rspModel.getResult();
                    User user = userCard.build();
                    user.save();
                    // TODO 通知联系人列表刷新

                    // 返回数据
                    callback.onDataLoad(rspModel.getResult());
                }else {
                    Factory.decodeRspCode(rspModel,callback);
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
     *
     * @param callback DataSource.Callback<List<UserCard>>
     */
    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback) {
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
    }
}
