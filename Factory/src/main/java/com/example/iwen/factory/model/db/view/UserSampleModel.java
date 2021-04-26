package com.example.iwen.factory.model.db.view;

import com.example.iwen.common.factory.model.Author;
import com.example.iwen.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * 用户基础信息的model，可以和数据库进行查询
 *
 * @author iwen大大怪
 * @Create to 2021/04/25 0:35
 */
@QueryModel(database = AppDatabase.class)
public class UserSampleModel implements Author {
    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String portrait;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPortrait() {
        return portrait;
    }

    @Override
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
