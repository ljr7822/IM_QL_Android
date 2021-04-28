package com.example.iwen.factory.presenter.message;

import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.data.message.MessageGroupRepository;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.view.MemberUserModel;
import com.example.iwen.factory.persistence.Account;

import java.util.List;

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
        if (group != null) {
            // TODO 初始化操作
            ChatContact.GroupView view = getView();
            // 判断是否是管理员
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);

            // 基础信息初始化
            view.onInit(group);

            // 群成员初始化
            List<MemberUserModel> models = group.getLatelyGroupMembers();
            // 群成员数量
            final long membersCount = group.getGroupMemberCount();
            // 隐藏的群成员的数量
            long moreCount = membersCount - models.size();
            // 显示顶部成员头像
            view.onInitGroupMembers(models, moreCount);

            // 界面初始化

        }
    }
}
