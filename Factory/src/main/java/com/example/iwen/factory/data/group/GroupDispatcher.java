package com.example.iwen.factory.data.group;

import com.example.iwen.factory.data.helper.DbHelper;
import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.card.GroupMemberCard;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.GroupMember;
import com.example.iwen.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author iwen大大怪
 * @Create to 2021/02/18 13:07
 */
public class GroupDispatcher implements GroupCenter {
    private static GroupCenter instance;
    // 单线程池，处理卡片，一个个的进行消息处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static GroupCenter getInstance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class) {
                if (instance == null) {
                    instance = new GroupDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(GroupCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupMemberHandler(cards));
    }

    /**
     * 把群card处理成群db类
     */
    private class GroupHandler implements Runnable {
        private final GroupCard[] cards;

        GroupHandler(GroupCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Group> groups = new ArrayList<>();
            for (GroupCard card : cards) {
                // 搜索管理员
                User owner = UserHelper.search(card.getOwnerId());
                if (owner != null) {
                    Group group = card.build(owner);
                    groups.add(group);
                }
            }
            if (groups.size() > 0) {
                DbHelper.save(Group.class, groups.toArray(new Group[0]));
            }
        }
    }

    /**
     * 把群成員card处理成群db类
     */
    private class GroupMemberHandler implements Runnable {
        private final GroupMemberCard[] cards;

        GroupMemberHandler(GroupMemberCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<GroupMember> members = new ArrayList<>();
            for (GroupMemberCard model : cards) {
                // 成员对应的人的信息
                User user = UserHelper.search(model.getUserId());
                // 成员对应的群的信息
                Group group = GroupHelper.find(model.getGroupId());
                if (user != null && group != null) {
                    GroupMember member = model.build(group, user);
                    members.add(member);
                }
            }
            if (members.size() > 0){
                DbHelper.save(GroupMember.class, members.toArray(new GroupMember[0]));
            }
        }
    }
}
