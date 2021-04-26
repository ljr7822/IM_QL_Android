package com.example.iwen.factory.data.group;

import android.text.TextUtils;

import com.example.iwen.factory.data.BaseDbRepository;
import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.Group_Table;
import com.example.iwen.factory.model.db.view.MemberUserModel;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * 我的群组的仓库
 *
 * @author iwen大大怪
 * @Create to 2021/04/25 23:38
 */
public class GroupsRepository extends BaseDbRepository<Group> implements GroupsDataSource {

    @Override
    public void load(SuccessCallback<List<Group>> callback) {
        super.load(callback);
        // 对数据辅助工具类添加一个数据更新的监听
        //DbHelper.addChangedListener(User.class, this);
        // 加载本地数据库数据
        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    /**
     * 检查一个group是否是我需要关注的数据
     *
     * @param group User
     * @return true 是我关注的数据
     */
    @Override
    protected boolean isRequired(Group group) {
        /**
         * 一个群的信息在数据库中只有两种情况：你加入了群  或者  你创建了群
         * 无论那种情况，拿到的都是群的信息，没有成员信息
         * 需要进行成员信息初始化
         */
        if (group.getGroupMemberCount() > 0) {
            // 已经有了成员信息
            group.holder = buildGroupHolder(group);
        } else {
            // 待初始化
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }
        // 所有加入的群都是要显示的
        return true;
    }

    // 初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias).append(",");
        }
        builder.delete(builder.lastIndexOf(","), builder.length());
        return builder.toString();
    }
}
