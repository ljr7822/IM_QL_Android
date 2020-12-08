package com.example.iwen.factory.presenter.account;

import com.example.iwen.common.factory.presenter.BasePresenter;

/**
 * 具体登录逻辑的实现类
 *
 * @author : iwen大大怪
 * create : 12-7 007 14:31
 */
public class LoginPresenter
        extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View mView) {
        super(mView);
    }

    @Override
    public void login(String phone, String password) {

    }
}
