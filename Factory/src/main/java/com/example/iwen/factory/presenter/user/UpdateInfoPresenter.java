package com.example.iwen.factory.presenter.user;

import android.text.TextUtils;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.R;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.api.user.UserUpdateModel;
import com.example.iwen.factory.model.card.UserCard;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 更新用户信息的具体逻辑操作(p层)
 *
 * @author : iwen大大怪
 * create : 12-13 013 20:26
 */
public class UpdateInfoPresenter
        extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {
    public UpdateInfoPresenter(UpdateInfoContract.View mView) {
        super(mView);
    }

    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {
        start();
        final UpdateInfoContract.View view = getView();
        // 校验数据
        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            // 头像上传
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadAvatar(photoFilePath);
                    if (url == null) {
                        // 上传失败
                        view.showError(R.string.data_upload_error);
                    } else {
                        // 构建model
                        UserUpdateModel model = new UserUpdateModel("", url, desc,
                                isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        // 进行网络请求，上传头像
                        UserHelper.update(model,UpdateInfoPresenter.this);
                    }
                }
            });
        }
    }

    @Override
    public void onDataLoad(UserCard userCard) {
        // 告知界面更新成功
        final UpdateInfoContract.View view = getView();
        if (view == null)
            return;
        // 该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.UpdateSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final UpdateInfoContract.View view = getView();
        if (view == null)
            return;
        //该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 告知界面更新失败，显示错误
                view.showError(strRes);
            }
        });
    }
}
