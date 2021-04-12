package com.example.iwen.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 配置数据库相关参数
 *
 * @author : iwen大大怪
 * create : 12-8 008 14:34
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    // 数据库名字
    public static final String NAME = "AppDatabase";
    // 数据库版本号
    public static final int VERSION = 2;
}
