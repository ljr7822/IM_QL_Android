package com.example.iwen.factory.presenter.contace;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.data.helper.AccountHelper;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.api.account.LogoutModel;
import com.example.iwen.factory.model.api.account.LogoutRspModel;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 个人信息界面的presenter层
 *
 * @author iwen大大怪
 * Create to 2021/02/13 0:22
 */
public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter, DataSource.Callback<LogoutRspModel>{

    private User user;
    private String id;

     public PersonalPresenter(PersonalContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        super.start();
        // 个人界面用户数据优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                final PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(user);
                }
            }
        });
    }

    /**
     * 进行界面的设置
     *
     * @param user 用户信息
     */
    private void onLoaded(final User user) {
        this.user = user;
        // 是否就是我自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        // 是否已经关注
        final boolean isFollow = isSelf || user.isFollow();
        // 已经关注同时不是自己才能聊天
        final boolean allowSayHello = isFollow && !isSelf;
        // 切换到Ui线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final PersonalContract.View view = getView();
                if (view == null) {
                    return;
                }
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSayHello);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return null;
    }

    @Override
    public void logout(String userId) {
        // 退出登录逻辑
        LogoutModel logoutModel = new LogoutModel(userId);
        // 发起网络请求
        AccountHelper.logout(logoutModel, this);
    }


    @Override
    public void onDataLoad(LogoutRspModel logoutRspModel) {
        // 告知界面退出登录成功
        final PersonalContract.View view = getView();
        if (view == null)
            return;
        // 该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.logoutSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final PersonalContract.View view = getView();
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
}
