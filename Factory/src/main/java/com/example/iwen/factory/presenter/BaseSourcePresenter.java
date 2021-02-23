package com.example.iwen.factory.presenter;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.data.DbDataSource;
import com.example.iwen.common.factory.presenter.BaseContract;
import com.example.iwen.common.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * 基础的仓库源的Presenter
 *
 * @author iwen大大怪
 * Create to 2021/02/23 15:07
 */
public abstract class BaseSourcePresenter<Data,ViewModel,
        Source extends DbDataSource<Data>,
        view extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<view, ViewModel> implements DataSource.SuccessCallback<List<Data>>{

    protected Source mSource;

    public BaseSourcePresenter(Source source,view mView) {
        super(mView);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource!=null){
            mSource.load(this);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
