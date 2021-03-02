package com.example.iwen.factory.data.helper;

import com.example.iwen.factory.Factory;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.message.MsgCreateModel;
import com.example.iwen.factory.model.card.MessageCard;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.Message_Table;
import com.example.iwen.factory.net.Network;
import com.example.iwen.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息 辅助工具类
 *
 * @author iwen大大怪
 * Create to 2021/02/18 16:51
 */
public class MessageHelper {
    /**
     * 从本地找消息
     * @param id message的id
     * @return 找到的消息
     */
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    /**
     * 发送消息
     * 发送是异步进行的
     *
     * @param msgCreateModel 要发送消息的model
     */
    public static void push(final MsgCreateModel msgCreateModel) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                // 成功发送：如果是一个已经发送过的消息，则不能重新发送
                // 正在发送状态：如果是一个消息正在发送，则不能重新发送
                Message message = findFromLocal(msgCreateModel.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {
                    return;
                }

                // 在发送时需要通知界面刷新状态，Card
                final MessageCard card = msgCreateModel.buildCard();
                Factory.getMessageCenter().dispatch(card);

                // TODO 文件类型的（图片，语音，文件），需要先上传后才发送

                // 直接调用接口发送
                RemoteService service = Network.mRemoteService();
                service.msgPush(msgCreateModel).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (card != null) {
                                // 成功的一次调度
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            // 检查账户是否异常
                            Factory.decodeRspCode(rspModel, null);
                            // 走失败流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        // 通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });

            }
        });
    }

    /**
     * 查询一个消息，这个消息是一个群中的最后一条消息
     *
     * @param groupId 群id
     * @return 群众聊天的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    /**
     * 查询一个消息，这个消息是一个user的最后一条消息
     *
     * @param userId userId
     * @return 一个user的最后一条消息
     */

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }
}
