package com.example.iwen.imqingliao.fragments.message;

import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.presenter.message.ChatContact;
import com.example.iwen.imqingliao.R;

/**
 * 群的聊天界面
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContact.GroupView {

    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContact.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }

    @Override
    public void showAdminOption(boolean isAdmin) {

    }
}