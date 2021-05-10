package com.example.iwen.factory.presenter.personal;

import com.example.iwen.common.factory.presenter.BaseContract;

/**
 * 修改密码的契约接口
 *
 * @author iwen大大怪
 * @Create 2021/05/10 0:56
 */
public interface ChangePwdContract {
    interface Presenter extends BaseContract.Presenter {
        // 修改密码
        void changePwd(String oldPwd, String newPwd, String newPwdTwo);
    }

    interface View extends BaseContract.View<ChangePwdContract.Presenter> {
        void changePwdSuccess();
    }
}
