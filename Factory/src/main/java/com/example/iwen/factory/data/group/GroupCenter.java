package com.example.iwen.factory.data.group;

import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.card.GroupMemberCard;

/**
 * 群聊中心的接口定义
 *
 * @author iwen大大怪
 * Create to 2021/02/18 13:06
 */
public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
