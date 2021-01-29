package com.example.iwen.common.factory.presenter;

import androidx.annotation.StringRes;

import com.example.iwen.common.widget.recycler.RecyclerAdapter;

/**
 * MVP公共基本契约
 *
 * @author : iwen大大怪
 * create : 12-7 007 13:48
 */
public interface BaseContract {
    // 基本界面职责
    interface View<T extends Presenter> {
        // 显示字符串错误
        void showError(@StringRes int str);

        // 显示进度条
        void showLoading();

        // 支持设置一个presenter
        void setPresenter(T presenter);
    }

    // 基本Presenter职责
    interface Presenter {

        // 公用的开始方法
        void start();

        // 公用的销毁方法
        void destroy();
    }

    // 基本的列表职责
    interface RecyclerView<ViewModel,T extends Presenter> extends View<T> {
        // 界面端只能刷新整个数据集合，不能精确到每一条数据刷新
        // void onDone(List<User> users);
        // 拿到一个适配器，然后自己进行自主刷新
        RecyclerAdapter<ViewModel> getRecyclerAdapter();

        // 当适配器数据更改了的时候触发
        void onAdapterDataChanged();
    }
}
