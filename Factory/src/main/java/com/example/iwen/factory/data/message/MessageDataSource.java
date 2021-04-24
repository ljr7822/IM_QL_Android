package com.example.iwen.factory.data.message;

import com.example.iwen.common.factory.data.DbDataSource;
import com.example.iwen.factory.model.db.Message;

/**
 * 消息的数据源定义，他的实现是：MessageRepository,MessageGroupRepository关注的对象是Message表
 *
 * @author iwen大大怪
 * @Create to 2021/02/24 22:04
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
