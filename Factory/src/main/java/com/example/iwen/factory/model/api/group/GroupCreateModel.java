package com.example.iwen.factory.model.api.group;

import java.util.Set;

/**
 * 创建群聊的model
 *
 * @author iwen大大怪
 * @Create to 2021/04/25 1:23
 */
public class GroupCreateModel {
    private String name;// 群名称
    private String desc;// 群描述
    private String picture;// 群图片
    private Set<String> users;

    public GroupCreateModel(String name, String desc, String picture, Set<String> users) {
        this.name = name;
        this.desc = desc;
        this.picture = picture;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
