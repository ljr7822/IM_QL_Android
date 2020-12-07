package com.example.iwen.factory.data.helper;

import android.util.Log;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.account.AccountRspModel;
import com.example.iwen.factory.model.api.account.RegisterModel;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.net.NetWork;
import com.example.iwen.factory.net.RemoteService;

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
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        // 得到一个call进行注册
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        // 进行异步请求
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call,
                                   Response<RspModel<AccountRspModel>> response) {
                // 请求成功
                // 从返回中得到我们的全局Model，内部使用的是Gson进行解析
                RspModel<AccountRspModel> rspModel = response.body();
                if (rspModel.success()){
                    AccountRspModel accountRspModel = rspModel.getResult();
                    // 判断是否绑定设备
                    if (accountRspModel.isBind()){
                        User user = accountRspModel.getUser();
                        // TODO 进行数据库写入
                        callback.onDataLoad(user);
                    }else {
                        // 进行绑定
                        bindPush(callback);
                    }
                }else {
                    // 对返回的RspModel中的失败的Code进行解析，解析到对应的String资源中
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                // 请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.e("ljr","111"+t);
            }
        });
    }

    /**
     * 对设备id绑定
     * @param callback Callback
     */
    public static void bindPush(final DataSource.Callback<User> callback){
        // TODO
        callback.onDataNotAvailable(R.string.app_name);
    }
}
