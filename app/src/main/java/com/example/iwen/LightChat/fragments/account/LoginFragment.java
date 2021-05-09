package com.example.iwen.LightChat.fragments.account;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.factory.presenter.account.LoginContract;
import com.example.iwen.factory.presenter.account.LoginPresenter;
import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录的fragment
 */
public class LoginFragment
        extends PresenterFragment<LoginContract.Presenter>
        implements LoginContract.View {

    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edt_phone)
    EditText mPhone;
    @BindView(R.id.edt_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        // 调用p层进行注册
        mPresenter.login(phone, password);
    }

    @OnClick(R.id.tv_go_register)
    void onShowRegisterClick() {
        // 让AccountTrigger进行界面切换
        mAccountTrigger.triggerView();
    }

    @Override
    public void loginSuccess() {
        // 如果登录成功，这个时候账户已经等登陆，进行跳转
        MainActivity.show(getContext());
        // 关闭当前界面
        getActivity().finish();
    }

    /**
     * 显示等待
     */
    @Override
    public void showLoading() {
        super.showLoading();
        // 进行时，界面不可以操作
        mLoading.start();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    /**
     * 需要显示错误信息
     *
     * @param str 信息
     */
    @Override
    public void showError(int str) {
        super.showError(str);
        // 当需要显示错误的时候触发，一定是结束了
        mLoading.stop(); // 停止loading
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
    }
}