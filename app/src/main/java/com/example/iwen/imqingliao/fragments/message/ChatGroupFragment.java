package com.example.iwen.imqingliao.fragments.message;

import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.view.MemberUserModel;
import com.example.iwen.factory.presenter.message.ChatContact;
import com.example.iwen.factory.presenter.message.ChatGroupPresenter;
import com.example.iwen.imqingliao.R;

import java.util.List;

/**
 * 群的聊天界面
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContact.GroupView {

    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_group;
    }

    @Override
    protected ChatContact.Presenter initPresenter() {
        return new ChatGroupPresenter(this,mReceiverId);
    }

    @Override
    public void onInit(Group group) {

    }

    @Override
    public void showAdminOption(boolean isAdmin) {

    }

    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, long moreCount) {

    }
}