package com.example.iwen.imqingliao.fragments.account;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.factory.presenter.account.RegisterContract;
import com.example.iwen.factory.presenter.account.RegisterPresenter;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册的fragment
 */
public class RegisterFragment
        extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {

    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edt_phone)
    EditText mPhone;
    @BindView(R.id.edt_name)
    EditText mName;
    @BindView(R.id.edt_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    /**
     * 初始化Presenter
     */
    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        // 调用p层进行注册
        mPresenter.register(phone,name,password);
    }

    @OnClick(R.id.tv_go_login)
    void onShowLoginClick(){
        // 让AccountTrigger进行界面切换
        mAccountTrigger.triggerView();
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
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    /**
     * 需要显示错误信息
     * @param str 信息
     */
    @Override
    public void showError(int str) {
        super.showError(str);
        // 当需要显示错误的时候触发，一定是结束了
        mLoading.stop(); // 停止loading
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void registerSuccess() {
        // 如果注册成功，这个时候账户已经等登陆，进行跳转
        MainActivity.show(getContext());
        // 关闭当前界面
        getActivity().finish();
    }
}