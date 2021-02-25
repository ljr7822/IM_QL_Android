package com.example.iwen.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import com.example.iwen.factory.data.helper.MessageHelper;
import com.example.iwen.factory.data.message.MessageDataSource;
import com.example.iwen.factory.model.api.message.MsgCreateModel;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.presenter.BaseSourcePresenter;
import com.example.iwen.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * ChatPresenter 聊天基类
 *
 * @author iwen大大怪
 * Create to 2021/02/24 22:00
 */
public class ChatPresenter<View extends ChatContact.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContact.Presenter {

    // 接收者Id，可能是群，或者人ID
    protected String receiverId;
    // 区分是人还是群的Id
    protected int receiverType;

    public ChatPresenter(MessageDataSource source, View mView, String receiverId, int receiverType) {
        super(source, mView);
        this.receiverId = receiverId;
        this.receiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        // 构建一个新消息
        MsgCreateModel msgCreateModel = new MsgCreateModel.Builder()
                .receiver(receiverId, receiverType)
                .content(content, Message.TYPE_STR)
                .build();
        // 进行网络发送
        MessageHelper.push(msgCreateModel);
    }

    @Override
    public void pushAudio(String path, long time) {
        // TODO 发送语音
    }

    @Override
    public void pushImages(String[] paths) {
        // TODO 发送图片
    }

    /**
     * 重新发送
     *
     * @param message 重新发送的消息
     * @return 是否发送成功
     */
    @Override
    public boolean rePush(Message message) {
        // 确定消息是可重复发送的
        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {
            // 更改状态
            message.setStatus(Message.STATUS_CREATED);
            // 构建发送Model
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }

    @Override
    public void onDataLoad(List<Message> messages) {
        ChatContact.View view = getView();
        if (view == null) {
            return;
        }
        // 拿到老数据
        @SuppressWarnings("unchecked")
        List<Message> old = view.getRecyclerAdapter().getItems();
        // 差异计算
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        // 进行界面刷新
        refreshData(result, messages);
    }
}
