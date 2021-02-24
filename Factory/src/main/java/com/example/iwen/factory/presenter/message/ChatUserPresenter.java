package com.example.iwen.factory.presenter.message;

import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.data.message.MessageRepository;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.User;

import java.util.List;

/**
 * 人聊天Presenter
 *
 * @author iwen大大怪
 * Create to 2021/02/24 21:58
 */
public class ChatUserPresenter extends ChatPresenter<ChatContact.UserView> implements ChatContact.Presenter {

    public ChatUserPresenter(ChatContact.UserView mView, String receiverId) {
        // 数据源，View，接收者，接收者类型
        super(new MessageRepository(receiverId), mView, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        // 从本地拿这个人的信息
        User mReceiver = UserHelper.findFromLocal(receiverId);
        getView().onInit(mReceiver);
    }

    @Override
    public void onDataLoad(List<Message> messages) {
        super.onDataLoad(messages);
    }
}
