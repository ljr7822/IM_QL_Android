package com.example.iwen.factory.presenter.personal;

import com.example.iwen.common.app.Application;
import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.data.helper.AccountHelper;
import com.example.iwen.factory.model.api.account.ChangePwdModel;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 修改密码的presenter
 *
 * @author iwen大大怪
 * @Create 2021/05/10 0:58
 */
public class ChangePwdPresenter extends BasePresenter<ChangePwdContract.View>
        implements ChangePwdContract.Presenter, DataSource.Callback<User> {
    public ChangePwdPresenter(ChangePwdContract.View mView) {
        super(mView);
    }

    @Override
    public void changePwd(String oldPwd, String newPwd, String newPwdTwo) {
        if (check(oldPwd,newPwd,newPwdTwo)){
            // 检查通过
            ChangePwdModel changePwdModel = new ChangePwdModel();
            changePwdModel.setUserId(Account.getUserId());
            changePwdModel.setOldPwd(oldPwd);
            changePwdModel.setNewPwd(newPwd);
            // 开始修改
            AccountHelper.changePwd(changePwdModel,this);
        }
    }

    @Override
    public void onDataLoad(User user) {
        // 告知界面修改密码成功
        final ChangePwdContract.View view = getView();
        if (view == null)
            return;
        // 该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.changePwdSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final ChangePwdContract.View view = getView();
        if (view == null)
            return;
        //该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //告知界面注册失败，显示错误
                view.showError(strRes);
            }
        });
    }

    /**
     * 检查密码
     *
     * @param oldPwd    老密码
     * @param newPwd    新密码
     * @param newPwdTwo 第二次密码
     * @return 是否正确
     */
    private boolean check(String oldPwd, String newPwd, String newPwdTwo) {
        if (oldPwd.length() < 6) {
            Application.showToast("原密码输入错误");
            return false;
        } else if (newPwd.length() < 6 && newPwdTwo.length() < 6) {
            Application.showToast("密码不能小于六位");
            return false;
        } else if (!newPwd.equals(newPwdTwo)){
            Application.showToast("两次输入不一致");
            return false;
        }else {
            return true;
        }
    }
}
