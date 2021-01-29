package com.example.iwen.factory.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * @author : iwen大大怪
 * create : 2021/01/29 13:15
 */
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    private List<T> mOldList, mNewList;

    public DiffUiDataCallback(List<T> oldList, List<T> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        // 旧数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        // 新数据大小
        return mNewList.size();
    }

    // 两个类是否是同一个东西
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    // 经过相等判断后，进一步判断是否有数据更改
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    // 进行实际比较的数据类型
    // 泛型的目的：你是和一个你这个类型的数据进行比较
    public interface UiDataDiffer<T>{
        // 传递一个旧的数据给你，问你是否和你表示的是同一个数据
        boolean isSame(T old);

        // 你和旧的数据对比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
