package com.example.iwen.factory.presenter.group;

import com.example.iwen.common.factory.presenter.BaseContract;
import com.example.iwen.factory.model.db.view.MemberUserModel;

/**
 * 群成员的契约
 *
 * @author iwen大大怪
 * Create to 2021/04/29 0:55
 */
public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter{
        // 刷新方法
        void refresh();
    }

    // 界面的职责
    interface View extends BaseContract.RecyclerView<MemberUserModel,Presenter>{
        // 获取群id
        String getGroupId();
    }
}
