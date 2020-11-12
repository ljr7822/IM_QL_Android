package com.example.iwen.common.widget.recycler;


/**
 * adapter 回调
 * author : Iwen大大怪
 * create : 2020/11/11 16:59
 */
public interface AdapterCallback<Data> {
    void update(Data date, RecyclerAdapter.ViewHolder<Data> holder);
}
