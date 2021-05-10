package com.example.iwen.factory.presenter.personal;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.model.db.User;

/**
 * @author iwen大大怪
 * @Create 2021/05/10 14:36
 */
public class ChangeDataPresenter extends BasePresenter<ChangeDataContract.View>
        implements ChangeDataContract.Presenter, DataSource.Callback<User>{
    public ChangeDataPresenter(ChangeDataContract.View mView) {
        super(mView);
    }

    @Override
    public void changeName(String name) {
        // TODO 修改姓名的逻辑

    }

    @Override
    public void changeDesc(String desc) {
        // TODO 修改个性签名的逻辑
    }

    @Override
    public void onDataLoad(User user) {

    }

    @Override
    public void onDataNotAvailable(int strRes) {

    }
}
