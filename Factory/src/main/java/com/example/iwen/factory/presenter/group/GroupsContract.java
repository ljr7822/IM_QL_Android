package com.example.iwen.factory.presenter.group;

import com.example.iwen.common.factory.presenter.BaseContract;
import com.example.iwen.factory.model.db.Group;

/**
 * 群组界面的契约
 *
 * @author : iwen大大怪
 * @create : 2021/01/29 1:18
 */
public interface GroupsContract {
    // 什么都不需要
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Group, Presenter> {

    }
}
