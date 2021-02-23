package com.example.iwen.factory.data.user;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.factory.data.BaseDbRepository;
import com.example.iwen.factory.data.helper.DbHelper;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.model.db.User_Table;
import com.example.iwen.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * 联系人仓库
 *
 * @author iwen大大怪
 * Create to 2021/02/22 13:18
 */
public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {

    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        super.load(callback);
        // 对数据辅助工具类添加一个数据更新的监听
        DbHelper.addChangedListener(User.class, this);
        //加载本地数据库数据
        SQLite.select().from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    /**
     * 检查一个user是否是我需要关注的数据
     *
     * @param user User
     * @return true 是我关注的数据
     */
    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equalsIgnoreCase(Account.getUserId());
    }
}
