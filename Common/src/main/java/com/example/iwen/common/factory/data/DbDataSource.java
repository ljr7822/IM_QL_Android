package com.example.iwen.common.factory.data;


import java.util.List;

/**
 * 基础的数据库源接口定义
 *
 * @author iwen大大怪
 * @Create to 2021/02/22 15:49
 */
public interface DbDataSource<Data> extends DataSource {
    /**
     * 有一个基本的数据源加载方法
     *
     * @param callback 传递一个callback回调，一般回调到Presenter
     */
    void load(SuccessCallback<List<Data>> callback);
}
