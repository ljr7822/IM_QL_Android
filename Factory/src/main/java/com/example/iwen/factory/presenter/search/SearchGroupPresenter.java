package com.example.iwen.factory.presenter.search;

import com.example.iwen.common.factory.presenter.BasePresenter;

/**
 * 搜索群的p层
 * @author : iwen大大怪
 * create : 2020-12-28 17:26
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter{

    public SearchGroupPresenter(SearchContract.GroupView mView) {
        super(mView);
    }

    @Override
    public void search(String content) {

    }
}
