package com.example.iwen.factory.data.user;

import androidx.annotation.NonNull;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.data.helper.DbHelper;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.model.db.User_Table;
import com.example.iwen.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.LinkedList;
import java.util.List;

/**
 * 联系人仓库
 *
 * @author iwen大大怪
 * Create to 2021/02/22 13:18
 */
public class ContactRepository implements
        ContactDataSource,
        QueryTransaction.QueryResultListCallback<User>,
        DbHelper.ChangedListener<User> {

    private DataSource.SuccessCallback<List<User>> callback;

    //private final Set<User> users = new HashSet<>();

    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        this.callback = callback;
        // 对数据辅助类添加一个数据更新的监听
        DbHelper.addChangedListener(User.class, this);
        // 加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    // 联系人数据集合的销毁操作
    @Override
    public void dispose() {
        this.callback = null;
        // 取消对数据集合的监听
        DbHelper.removeChangedListener(User.class, this);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
        // 添加到当前的缓冲器
        if (tResult.size()==0){
            users.clear();
            notifyDataChange();
            return;
        }
        // 转变成数组
        User[] users = tResult.toArray(new User[0]);
        // 回到数据集更新的操作中
        onDataSave(users);
    }

    @Override
    public void onDataSave(User... list) {
        boolean isChanged = false;
        // 当数据库数据变更的操作
        for (User user : list) {
            // 是关注的人，但不是我自己
            if (isRequired(user)) {
                insertOrUpdate(user);
                isChanged = true;
            }
        }
        // 有数据变更就进行界面刷新
        if (isChanged) {
            notifyDataChange();
        }
    }

    @Override
    public void onDataDelete(User... list) {
        // 当数据库数据删除的操作
        boolean isChanged = false;
        for (User user : list) {
            if (users.remove(user)) {
                isChanged = true;
            }
        }
        // 有数据变更就进行界面刷新
        if (isChanged) {
            notifyDataChange();
        }
    }

    List<User> users = new LinkedList<>();

    private void insertOrUpdate(User user) {
        int index = indexOf(user);
        if (index >= 0) {
            replace(index, user);
        } else {
            insert(user);
        }
    }

    /**
     * 替换更新方法
     *
     * @param index 替换位置
     * @param user  替换数据
     */
    private void replace(int index, User user) {
        users.remove(index);
        users.add(index, user);
    }

    /**
     * 添加方法
     *
     * @param user 添加数据
     */
    private void insert(User user) {
        users.add(user);
    }

    private int indexOf(User user) {
        int index = -1;
        for (User user1 : users) {
            index++;
            if (user1.isSame(user)) {
                return index;
            }
        }
        return -1;
    }

    // 通知数据变更
    private void notifyDataChange() {
        if (callback != null) {
            callback.onDataLoad(users);
        }
    }

    /**
     * 检查一个user是否是我需要的数据
     *
     * @param user User
     * @return True是
     */
    private boolean isRequired(User user) {
        return user.isFollow() && user.getId().equals(Account.getUserId());
    }
}
