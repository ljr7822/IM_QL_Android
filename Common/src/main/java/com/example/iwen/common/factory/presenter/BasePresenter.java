package com.example.iwen.common.factory.presenter;

/**
 * MVP公共的基础Presenter
 *
 * @author : iwen大大怪
 * create : 12-7 007 13:49
 */
public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    private T mView;

    public BasePresenter(T mView) {
        setView(mView);
    }

    /**
     * 设置一个view，子类可以复写
     *
     * @param mView view
     */
    @SuppressWarnings("unchecked")
    protected void setView(T mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    /**
     * 给子类使用的view，不可以复写
     *
     * @return View
     */
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        if (mView != null) {
            mView.showLoading();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void destroy() {
        if (mView != null) {
            mView.setPresenter(null);
            mView = null;
        }
    }
}

