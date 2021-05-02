package com.example.iwen.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.iwen.common.Common;
import com.example.iwen.common.app.Application;
import com.example.iwen.common.utils.PicturesCompressor;
import com.example.iwen.common.utils.StreamUtil;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.model.api.RspModel;
import com.example.iwen.factory.model.api.message.MsgCreateModel;
import com.example.iwen.factory.model.card.MessageCard;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.Message_Table;
import com.example.iwen.factory.net.Network;
import com.example.iwen.factory.net.RemoteService;
import com.example.iwen.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息 辅助工具类
 *
 * @author iwen大大怪
 * @Create to 2021/02/18 16:51
 */
public class MessageHelper {
    /**
     * 从本地找消息
     *
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

                // 文件类型的（图片，语音，文件），需要先上传后才发送
                // 发送消息分为两步：1.上传到oss云服务器 2.消息Push到我们自己的服务器
                if (card.getType() != Message.TYPE_STR) {
                    // 不是文字类型的
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)) {
                        // 没有上传到云服务器，还是本地手机文件
                        String content;
                        switch (card.getType()) {
                            // 上传图片
                            case Message.TYPE_PIC:
                                content = uploadPicture(card.getContent());
                                break;
                            // 上传语音
                            case Message.TYPE_AUDIO:
                                content = uploadAudio(card.getContent());
                                break;
                            // 其他
                            default:
                                content = "";
                                break;
                        }
                        if (TextUtils.isEmpty(content)){
                            // 失败
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                        }
                        // 成功就把网络路径进行替换
                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);
                        // 因为卡片的内容改变了，而我们上传到自己服务器使用的是model，所以model也要更改
                        msgCreateModel.refreshByCard();
                    }
                }
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
     * 上传图片
     *
     * @param content 本地路径
     * @return oss云服务器路径
     */
    private static String uploadPicture(String content) {
        File file = null;
        // 拿到图片
        try {
            // 通过Glide的缓存区间解决了图片外部权限的问题
            file = Glide.with(Factory.app())
                    .load(content)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null){
            // 开始进行压缩
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            // 图片名字
            String tempFile = String.format("%s/image/Cache_%s.png", cacheDir,SystemClock.uptimeMillis());
            try {
                if (PicturesCompressor.compressImage(file.getAbsolutePath(),tempFile, Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)){
                    // 上传
                    String ossPath = UploadHelper.uploadImage(tempFile);
                    StreamUtil.delete(tempFile);
                    return ossPath;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 上传语音
     *
     * @param content
     * @return
     */
    private static String uploadAudio(String content) {
        // TODO
        return null;
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
