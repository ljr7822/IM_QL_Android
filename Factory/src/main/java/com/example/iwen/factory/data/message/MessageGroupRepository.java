package com.example.iwen.factory.data.message;

import androidx.annotation.NonNull;

import com.example.iwen.factory.data.BaseDbRepository;
import com.example.iwen.factory.model.db.Message;
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
        // TODO
        /*SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(receiverId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();*/
    }

    @Override
    protected boolean isRequired(Message message) {
        return false;
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        // 将数据倒序
        Collections.reverse(tResult);
        // 然后在进行调度
        super.onListQueryResult(transaction, tResult);
    }
}
