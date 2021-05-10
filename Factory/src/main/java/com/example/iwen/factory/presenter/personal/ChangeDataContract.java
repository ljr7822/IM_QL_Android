package com.example.iwen.factory.presenter.personal;

import com.example.iwen.common.factory.presenter.BaseContract;

/**
 * 修改个人信息的契约接口
 *
 * @author iwen大大怪
 * @Create 2021/05/10 14:29
 */
public interface ChangeDataContract {
    interface Presenter extends BaseContract.Presenter {
        // 修改名字
        void changeName(String name);

        // 修改个性签名
        void changeDesc(String desc);
    }

    interface View extends BaseContract.View<ChangeDataContract.Presenter> {
        // 修改姓名成功
        void changeNameSuccess();

        // 修改个性签名成功
        void changeDescSuccess();
    }
}
