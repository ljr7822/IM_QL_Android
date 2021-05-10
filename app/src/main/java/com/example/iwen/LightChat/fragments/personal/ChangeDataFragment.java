package com.example.iwen.LightChat.fragments.personal;

import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.activities.PersonalActivity;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.presenter.personal.ChangeDataContract;
import com.example.iwen.factory.presenter.personal.ChangeDataPresenter;

import butterknife.OnClick;

/**
 * 修改个人信息
 */
public class ChangeDataFragment extends PresenterFragment<ChangeDataContract.Presenter>
        implements ChangeDataContract.View{

    public ChangeDataFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_change_data;
    }

    // 初始化Presenter
    @Override
    protected ChangeDataContract.Presenter initPresenter() {
        return new ChangeDataPresenter(this);
    }

    @Override
    public void changeNameSuccess() {
        // 修改昵称成功

    }

    @Override
    public void changeDescSuccess() {
        // 修改个性签名成功
    }

    // 返回按钮
    @OnClick(R.id.im_back)
    void backClick(){
        PersonalActivity.show(getContext(), Account.getUserId());
    }
}