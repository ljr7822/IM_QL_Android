package com.example.iwen.factory.data.helper;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.group.GroupCreateModel;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.Group_Table;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.net.Network;
import com.example.iwen.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

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
     * @param model 创建model
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
}
