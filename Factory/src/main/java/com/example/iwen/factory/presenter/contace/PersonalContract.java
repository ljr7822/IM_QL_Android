package com.example.iwen.factory.presenter.contace;

import com.example.iwen.common.factory.presenter.BaseContract;
import com.example.iwen.factory.model.db.User;

/**
 * 个人信息界面的契约
 *
 * @author iwen大大怪
 * Create to 2021/02/13 0:05
 */
public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter {
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter> {
        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 是否发起聊天
        void allowSayHello(boolean isAllow);

        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}
