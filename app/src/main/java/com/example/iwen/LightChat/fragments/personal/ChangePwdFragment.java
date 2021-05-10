package com.example.iwen.LightChat.fragments.personal;

import android.widget.Button;
import android.widget.EditText;

import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.activities.PersonalActivity;
import com.example.iwen.common.app.Application;
import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.presenter.user.ChangePwdContract;
import com.example.iwen.factory.presenter.user.ChangePwdPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码的弹出fragment
 */
public class ChangePwdFragment extends PresenterFragment<ChangePwdContract.Presenter>
        implements ChangePwdContract.View {

    @BindView(R.id.edt_old_pwd)
    EditText mPwdOld;
    @BindView(R.id.edt_new_pwd)
    EditText mPwdNew;
    @BindView(R.id.edt_newTwo_pwd)
    EditText mPwdNewTwo;
    @BindView(R.id.btn_pwd_submit)
    Button mPwdSubmit;

    private Fragment mFragment;

    public ChangePwdFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_change_pwd;
    }

    // 修改密码按钮
    @OnClick(R.id.btn_pwd_submit)
    void changedPwdClick() {
        // 获取密码
        String oldPwd = mPwdOld.getText().toString();
        String newPwd = mPwdNew.getText().toString();
        String newTwoPwd = mPwdNewTwo.getText().toString();
        // 调用p层开始修改密码
        mPresenter.changePwd(oldPwd, newPwd, newTwoPwd);
    }

    // 返回按钮
    @OnClick(R.id.im_back)
    void backClick(){
        PersonalActivity.show(getContext(), Account.getUserId());
    }

    @Override
    protected ChangePwdContract.Presenter initPresenter() {
        return new ChangePwdPresenter(this);
    }

    @Override
    public void changePwdSuccess() {
        // 修改密码成功后需要做
        // 跳转到修改密码界面
        Application.showToast("密码修改成功！");
        PersonalActivity.show(getContext(), Account.getUserId());
    }
}