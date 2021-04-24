package com.example.iwen.common.factory.data;

import androidx.annotation.StringRes;

/**
 * 数据源接口定义
 *
 * @author : iwen大大怪
 * @create : 12-7 007 17:24
 */
public interface DataSource {
    /**
     * 同时包括了失败成功两种回调接口
     *
     * @param <T> 泛型
     */
    interface Callback<T> extends SuccessCallback<T>, FailedCallback {

    }

    /**
     * 只关注成功的接口
     *
     * @param <T> 泛型
     */
    interface SuccessCallback<T> {
        //数据加载成功，网络请求成功
        void onDataLoad(T t);
    }

    /**
     * 只关注成功的接口
     */
    interface FailedCallback {
        //数据加载失败，网络请求失败
        void onDataNotAvailable(@StringRes int strRes);
    }

    /**
     * 销毁操作
     */
    void dispose();
}
