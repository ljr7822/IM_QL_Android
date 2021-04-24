package com.example.iwen.factory.data.message;

import com.example.iwen.factory.model.card.MessageCard;

/**
 * 信息中心的基本定义，进行消息卡片的消费
 *
 * @author iwen大大怪
 * @Create to 2021/02/18 12:51
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}