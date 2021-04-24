package com.example.iwen.factory.presenter.group;

import com.example.iwen.common.factory.presenter.BaseRecyclerPresenter;

/**
 * 群创建界面的 Presenter
 *
 * @author iwen大大怪
 * @Create to 2021/04/24 21:11
 */
public class GroupCreatePresenter
        extends BaseRecyclerPresenter<GroupCreateContract.View, GroupCreateContract.ViewModel> implements GroupCreateContract.Presenter {

    public GroupCreatePresenter(GroupCreateContract.View mView) {
        super(mView);
    }

    @Override
    public void create(String name, String desc, String picture) {

    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {

    }
}
