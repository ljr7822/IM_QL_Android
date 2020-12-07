package com.example.iwen.factory.presenter.account;

import com.example.iwen.common.factory.presenter.BaseContract;

/**
 * 注册部分逻辑
 *
 * @author : iwen大大怪
 * create : 12-7 007 13:44
 */
public interface RegisterContract {
    interface View extends BaseContract.View<Presenter> {
        // 注册成功
        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起一个注册
        void register(String phone, String name, String password);

        // 检查手机号是否正确
        boolean checkMobile(String phone);
    }
}
