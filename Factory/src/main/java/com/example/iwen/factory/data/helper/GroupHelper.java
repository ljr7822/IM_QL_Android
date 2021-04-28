package com.example.iwen.factory.data.helper;

import android.util.Log;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.group.GroupCreateModel;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.card.GroupMemberCard;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.GroupMember;
import com.example.iwen.factory.model.db.GroupMember_Table;
import com.example.iwen.factory.model.db.Group_Table;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.model.db.User_Table;
import com.example.iwen.factory.model.db.view.MemberUserModel;
import com.example.iwen.factory.net.Network;
import com.example.iwen.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 群聊的简单辅助工具类
 *
 * @author iwen大大怪
 * Create to 2021/02/18 16:45
 */
public class GroupHelper {
    // 查询群的信息，先本地后网络
    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null) {
            group = findFromNet(groupId);
        }
        return group;
    }

    // 本地查询群信息
    public static Group findFromLocal(String groupId) {
        return SQLite.select().from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
    }

    // 从网络找
    public static Group findFromNet(String groupId) {
        RemoteService remoteService = Network.mRemoteService();
        try {
            Response<RspModel<GroupCard>> response = remoteService.groupFind(groupId).execute();
            GroupCard card = response.body().getResult();
            if (card != null) {
                // 数据库的存储并通知
                Factory.getGroupCenter().dispatch(card);
                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 群的创建
     *
     * @param model    创建model
     * @param callback 回调
     */
    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        RemoteService service = Network.mRemoteService();
        service.groupCreate(model).enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                RspModel<GroupCard> rspModel = response.body();
                if (rspModel.success()) {
                    GroupCard groupCard = rspModel.getResult();
                    // 唤起进行保存的操作
                    Factory.getGroupCenter().dispatch(groupCard);
                    // 返回数据
                    callback.onDataLoad(groupCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 搜索方法
     *
     * @param name     要搜索的用户名字
     * @param callback DataSource.Callback<List<GroupCard>>
     */
    public static Call search(String name, final DataSource.Callback<List<GroupCard>> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.mRemoteService();
        // 得到一个call进行注册
        Call<RspModel<List<GroupCard>>> call = service.groupSearch(name);
        // 进行异步请求
        call.enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoad(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
                Log.e("ljr", "onFailure message: " + t);
            }
        });
        // 把当前的调度者返回
        return call;
    }

    // 刷新我的群组列表
    public static void refreshGroups() {
        RemoteService service = Network.mRemoteService();
        service.groups("").enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                if (rspModel.success()) {
                    // 进行数据调度
                    List<GroupCard> groupCards = rspModel.getResult();
                    if (groupCards != null && groupCards.size() > 0) {
                        // 进行调度显示
                        Factory.getGroupCenter().dispatch(groupCards.toArray(new GroupCard[0]));
                    }
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                Log.e("ljr", "onFailure message: " + t);
            }
        });
    }

    /**
     * 获取一个群的成员数量
     *
     * @param id 群id
     * @return 成员数量
     */
    public static long getMemberCount(String id) {
        return SQLite.selectCountOf()
                .from(GroupMember.class)
                .where(GroupMember_Table.group_id.eq(id))
                .count();
    }

    // 从网络去刷新一个群的成员信息
    public static void refreshGroupMember(Group group) {
        RemoteService service = Network.mRemoteService();
        service.groupMembers(group.getId()).enqueue(new Callback<RspModel<List<GroupMemberCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupMemberCard>>> call, Response<RspModel<List<GroupMemberCard>>> response) {
                RspModel<List<GroupMemberCard>> rspModel = response.body();
                if (rspModel.success()) {
                    List<GroupMemberCard> groupMemberCards = rspModel.getResult();
                    if (groupMemberCards  != null && groupMemberCards.size() > 0) {
                        //进行调度显示
                        Factory.getGroupCenter().dispatch(groupMemberCards.toArray(new GroupMemberCard[0]));
                    }
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupMemberCard>>> call, Throwable t) {

            }
        });
    }

    // 关联查询用户和群的表，返回一个 MemberUserModel 表的集合
    public static List<MemberUserModel> getMemberUsers(String groupId, int size) {
        return SQLite.select(GroupMember_Table.alias.withTable().as("alias"),
                User_Table.id.withTable().as("userId"),
                User_Table.name.withTable().as("name"),
                User_Table.portrait.withTable().as("portrait"))
                .from(GroupMember.class)
                .join(User.class, Join.JoinType.INNER)
                .on(GroupMember_Table.user_id.withTable().eq(User_Table.id.withTable()))
                .where(GroupMember_Table.group_id.withTable().eq(groupId))
                .orderBy(GroupMember_Table.user_id,true)
                .limit(size)
                .queryCustomList(MemberUserModel.class);
    }
}
