package com.example.iwen.factory;

import com.example.iwen.common.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * author : Iwen大大怪
 * create : 2020/11/15 13:36
 */
public class Factory {
    // 单例模式
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
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // TODO 设置过滤器，数据库级别的model不进行转换
                .create();
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
     * 异步允许方法
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
}
