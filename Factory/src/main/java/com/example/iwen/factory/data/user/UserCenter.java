package com.example.iwen.factory.data.user;

import com.example.iwen.factory.model.card.UserCard;

/**
 * 用戶中心的基本定义
 *
 * @author iwen大大怪
 * Create to 2021/02/14 23:50
 */
public interface UserCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(UserCard... cards);
}

