package com.example.iwen.factory.presenter.account;

import android.text.TextUtils;

import com.example.iwen.common.Common;
import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.R;
import com.example.iwen.factory.data.helper.AccountHelper;
import com.example.iwen.factory.model.api.account.RegisterModel;
import com.example.iwen.factory.model.db.User;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * 具体注册逻辑的实现类
 *
 * @author : iwen大大怪
 * create : 12-7 007 14:31
 */
public class RegisterPresenter
        extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View mView) {
        super(mView);
    }

    @Override
    public void register(String phone, String name, String password) {
        start();
        RegisterContract.View view = getView();
        // 校验
        if (!checkMobile(phone)) {
            // 手机不合法
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            // 名称大于2位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            // 密码大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            // 开始请求调用
            RegisterModel model = new RegisterModel(phone, password, name);
            // 进行网络请求并回送
            AccountHelper.register(model, this);
        }
    }

    /**
     * 网络请求成功的回调
     *
     * @param user 用户
     */
    @Override
    public void onDataLoad(User user) {
        // 告知界面，注册成功
        final RegisterContract.View view = getView();
        if (view==null){
            return;
        }
        // 进行线程切换
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    /**
     * 网络请求失败的回调
     *
     * @param strRes 失败提示
     */
    @Override
    public void onDataNotAvailable(final int strRes) {
        // 告知界面，注册失败
        final RegisterContract.View view = getView();
        if (view==null){
            return;
        }
        // 进行线程切换
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }

    /**
     * 检查手机号码是否合法
     *
     * @param phone 手机号
     * @return true为合法
     */
    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }
}
