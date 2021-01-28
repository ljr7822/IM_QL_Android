package com.example.iwen.factory.presenter.contace;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 关注的逻辑实现
 *
 * @author : iwen大大怪
 * create : 2021/01/28 23:03
 */
public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter , DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContract.View mView) {
        super(mView);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id,this);
    }

    // 数据加载成功
    @Override
    public void onDataLoad(final UserCard userCard) {
        final FollowContract.View view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSuccess(userCard);
                }
            });
        }
    }

    // 数据加载失败
    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
