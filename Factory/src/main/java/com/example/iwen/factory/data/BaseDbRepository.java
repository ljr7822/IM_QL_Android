package com.example.iwen.factory.data;

import androidx.annotation.NonNull;

import com.example.iwen.common.factory.data.DbDataSource;
import com.example.iwen.common.utils.CollectionUtil;
import com.example.iwen.factory.data.helper.DbHelper;
import com.example.iwen.factory.model.db.BaseDbModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础的数据库仓库，实现对数据库基本的监听操作
 *
 * @author iwen大大怪
 * Create to 2021/02/22 15:47
 */
public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>,
        DbHelper.ChangedListener<Data>,
        QueryTransaction.QueryResultListCallback<Data> {

    // 和presenter交互的回调
    private SuccessCallback<List<Data>> callback;
    // 当前缓存的数据
    private final List<Data> dataList = new LinkedList<>();
    // 当前泛型对应的class对应的真实的class信息
    private Class<Data> dataClass;

    @SuppressWarnings("unchecked")
    public BaseDbRepository() {
        // 拿当前类的范型数组信息
        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());
        dataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SuccessCallback<List<Data>> callback) {
        this.callback = callback;
        // 进行数据库监听
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        // 取消监听，销毁数据
        this.callback = null;
        DbHelper.removeChangedListener(dataClass, this);
        dataList.clear();
    }

    // 数据库统一通知的地方：增加、更改
    @SuppressWarnings("unchecked")
    @Override
    public void onDataSave(Data... list) {
        boolean isChanged = false;
        // 当数据库数据变更的操作
        for (Data data : list) {
            // 是关注的人，但不是我自己
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }
        // 有数据变更就进行界面刷新
        if (isChanged) {
            notifyDataChange();
        }
    }

    // 数据库统一通知的地方：删除
    @SuppressWarnings("unchecked")
    @Override
    public final void onDataDelete(Data... list) {
        //删除情况下不用进行过滤判断
        boolean isChanged = false;
        for (Data data : list) {
            if (dataList.remove(data)) {
                isChanged = true;
            }
        }
        //有数据变更则进行界面刷新
        if (isChanged)
            notifyDataChange();
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        // 添加到当前的缓冲器
        if (tResult.size() == 0) {
            dataList.clear();
            notifyDataChange();
            return;
        }
        // 转变成数组
        Data[] users = CollectionUtil.toArray(tResult, dataClass);
        // 回到数据集更新的操作中
        onDataSave(users);
    }

    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    /**
     * 替换更新方法
     *
     * @param index 替换位置
     * @param data  替换数据
     */
    protected void replace(int index, Data data) {
        dataList.remove(index);
        dataList.add(index, data);
    }

    /**
     * 添加方法
     *
     * @param data 添加数据
     */
    protected void insert(Data data) {
        dataList.add(data);
    }

    /**
     * 查询一个数据是否在当前的缓存中，在则返回坐标
     */
    protected int indexOf(Data newData) {
        int index = -1;
        for (Data data : dataList) {
            index++;
            if (data.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 检查一个user是否是我需要的数据
     *
     * @param data Data
     * @return True是
     */
    protected abstract boolean isRequired(Data data);

    /**
     * 添加数据库的监听操作
     */
    protected void registerDbChangedListener() {
        DbHelper.addChangedListener(dataClass, this);
    }

    // 通知数据变更
    private void notifyDataChange() {
        SuccessCallback<List<Data>> callback = this.callback;
        if (callback != null) {
            callback.onDataLoad(dataList);
        }
    }
}
