package com.example.iwen.imqingliao;

import com.example.iwen.common.app.Application;
import com.example.iwen.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 * author : Iwen大大怪
 * create : 2020/11/15 2:05
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 调用Factory进行初始化
        Factory.setup();
        // 推送初始化
        PushManager.getInstance().initialize(this);
    }
}
