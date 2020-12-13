package com.example.iwen.factory.presenter.user;

import com.example.iwen.common.factory.presenter.BaseContract;

/**
 * 完善用户信息的契约
 *
 * @author : iwen大大怪
 * create : 12-8 008 18:49
 */
public interface UpdateInfoContract {
    // 更新
    interface Presenter extends BaseContract.Presenter{
        void update(String photoFilePath,String desc,boolean isMan);
    }
    // 回调成功
    interface View extends BaseContract.View<Presenter>{
        void UpdateSuccess();
    }
}
