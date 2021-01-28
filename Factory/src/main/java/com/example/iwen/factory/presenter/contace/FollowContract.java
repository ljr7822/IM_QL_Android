package com.example.iwen.factory.presenter.contace;

import com.example.iwen.common.factory.presenter.BaseContract;
import com.example.iwen.factory.model.card.UserCard;

/**
 * @author : iwen大大怪
 * create : 2021/01/28 22:51
 */
public interface FollowContract {
    // 任务调度者
    interface Presenter extends BaseContract.Presenter{
        // 关注一个人
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter>{
        // 关注成功返回一个用户信息
        void onFollowSuccess(UserCard userCard);
    }
}
