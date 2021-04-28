package com.example.iwen.factory.presenter.group;

import com.example.iwen.common.factory.presenter.BaseRecyclerPresenter;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * @author iwen大大怪
 * Create to 2021/04/29 1:01
 */
public class GroupMembersPresenter extends BaseRecyclerPresenter<GroupMembersContract.View,MemberUserModel>
        implements GroupMembersContract.Presenter{

    public GroupMembersPresenter(GroupMembersContract.View mView) {
        super(mView);
    }

    // 进行界面刷新
    @Override
    public void refresh() {
        start();
        // 异步加载
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            // 获取到view
            GroupMembersContract.View view = getView();
            if (view == null){
                return;
            }
            String groupId = view.getGroupId();

            // 加载本地数据库数据
            // 传递数量为-1，代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId,-1);
            refreshData(models);
        }
    };
}
