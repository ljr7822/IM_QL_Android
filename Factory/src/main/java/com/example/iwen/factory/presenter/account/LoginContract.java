package com.example.iwen.factory.presenter.account;

import com.example.iwen.common.factory.presenter.BaseContract;

/**
 * 登录部分逻辑
 *
 * @author : iwen大大怪
 * create : 12-7 007 13:44
 */
public interface LoginContract {
    interface View extends BaseContract.View<Presenter> {
        // 登录成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起一个登录
        void login(String phone, String password);
    }
}
