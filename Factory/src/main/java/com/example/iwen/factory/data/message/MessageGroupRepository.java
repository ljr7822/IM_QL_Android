package com.example.iwen.factory.data.message;

import androidx.annotation.NonNull;

import com.example.iwen.factory.data.BaseDbRepository;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 跟群聊天时的聊天记录列表，关注的内容是我发送给这个群的，或者是别人发送给群的信息
 *
 * @author iwen大大怪
 * Create to 2021/02/24 22:06
 */
public class MessageGroupRepository extends BaseDbRepository<Message> implements MessageDataSource {

    // 聊天的群id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);
        // 无论是直接发还是别人发，只要是发到这个群的，group_id就是receiverId
        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        // 过滤器规则：
        // 如果消息的Group不为空，则一定是发送到这个群
        // 如果群id等于我们需要的，就通过过滤
        return message.getGroup()!=null && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        // 将数据倒序
        Collections.reverse(tResult);
        // 然后在进行调度
        super.onListQueryResult(transaction, tResult);
    }
}
