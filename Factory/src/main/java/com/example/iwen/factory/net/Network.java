package com.example.iwen.factory.net;

import android.text.TextUtils;

import com.example.iwen.common.Common;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.okhttp.OkHttpSSH;
import com.example.iwen.factory.persistence.Account;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 *
 * @author : iwen大大怪
 * @create : 12-7 007 18:19
 */
public class Network {
    private static Network instance;
    private OkHttpClient client;
    private Retrofit retrofit;

    static {
        instance = new Network();
    }

    private Network() {
    }
    /**
     * 构建一个Retrofit
     */
    public static Retrofit getRetrofit() {
        if (instance.retrofit != null){
            return instance.retrofit;
        }
        // 得到一个OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                // 跳过ssh检查
                .sslSocketFactory(OkHttpSSH.createSSLSocketFactory(), new OkHttpSSH.TrustAllCerts())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                // 给所有的请求添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // 拿到我们的请求
                        Request original = chain.request();
                        // 重新进行build
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            // 注入一个token
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        // 返回
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();

        // 设置连接电脑
        // 获取Retrofit对象
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(client) // 设置client
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson())) // 设置数据解析器
                .build();
        return instance.retrofit;
    }

    /**
     * 返回一个请求代理
     * @return RemoteService
     */
    public static RemoteService mRemoteService(){
       return Network.getRetrofit().create(RemoteService.class);
    }
}
