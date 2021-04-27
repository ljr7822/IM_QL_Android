package com.example.iwen.factory.presenter.message;

import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.data.message.MessageGroupRepository;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.Message;

/**
 * 群聊天的逻辑 ChatGroupPresenter
 *
 * @author iwen大大怪
 * Create to 2021/02/24 22:00
 */
public class ChatGroupPresenter extends ChatPresenter<ChatContact.GroupView> implements ChatContact.Presenter {

    public ChatGroupPresenter(ChatContact.GroupView mView, String receiverId) {
        // 数据源，View，接收者，接收者类型
        super(new MessageGroupRepository(receiverId), mView, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();
        // 从本地拿这群的信息
        Group group = GroupHelper.findFromLocal(receiverId);
        if (group!= null){
            // TODO 初始化操作
        }
    }
}
