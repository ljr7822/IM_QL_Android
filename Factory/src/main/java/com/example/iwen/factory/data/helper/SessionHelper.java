package com.example.iwen.factory.data.helper;

import com.example.iwen.factory.model.db.Session;
import com.example.iwen.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * 会话辅助工具类
 * @author iwen大大怪
 * Create to 2021/02/21 23:50
 */
public class SessionHelper {
    // 从本地查询Session
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
