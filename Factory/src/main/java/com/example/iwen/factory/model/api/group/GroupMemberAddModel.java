package com.example.iwen.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * 群聊添加成员的model
 * @author iwen大大怪
 * @Create to 2021/04/25 22:09
 */
public class GroupMemberAddModel {
    private Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
