package com.example.iwen.factory.presenter.search;

import com.example.iwen.common.factory.presenter.BaseContract;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.card.UserCard;

import java.util.List;

/**
 * 搜索的契约接口
 *
 * @author : iwen大大怪
 * create : 2020-12-28 17:18
 */
public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        // 搜索内容
        void search(String content);
    }

    // 搜索人的界面
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    // 搜索群的界面
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }

}
