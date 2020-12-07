package com.example.iwen.factory.net;

import com.example.iwen.common.Common;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.okhttp.OkHttpSSH;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 *
 * @author : iwen大大怪
 * create : 12-7 007 18:19
 */
public class NetWork {
    /**
     * 构建一个Retrofit
     */
    public static Retrofit getRetrofit() {
        // 得到一个OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                // 跳过ssh检查
                .sslSocketFactory(OkHttpSSH.createSSLSocketFactory(), new OkHttpSSH.TrustAllCerts())
//                .readTimeout(20, TimeUnit.SECONDS)
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)//设置超时
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();

        // 设置连接电脑
        // 获取Retrofit对象
        Retrofit retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(client) // 设置client
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson())) // 设置数据解析器
                .build();
        return retrofit;
    }
}
