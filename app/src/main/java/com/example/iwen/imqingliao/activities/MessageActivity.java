package com.example.iwen.imqingliao.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.example.iwen.common.app.Activity;
import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.factory.model.Author;
import com.example.iwen.common.widget.SmoothInputLayout;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.Session;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.fragments.message.ChatGroupFragment;
import com.example.iwen.imqingliao.fragments.message.ChatUserFragment;

public class MessageActivity extends Activity {

    // 接收者Id，可以是群，也可以是人的Id
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 显示某个人的聊天界面
     *
     * @param context 上下文
     * @param author  某个用户
     */
    public static void show(Context context, Author author) {
        if (context == null || author == null || author.getId() == null) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 显示群的聊天界面
     *
     * @param context 上下文
     * @param group   某个群
     */
    public static void show(Context context, Group group) {
        if (context == null || group == null || group.getId() == null) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(intent);
    }

    /**
     * 通过session发起聊天
     *
     * @param context 上下文
     * @param session session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null || session.getId() == null) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        SmoothInputLayout.hideInputWhenTouchOtherView(this,ev,null);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if (mIsGroup) {
            fragment = new ChatGroupFragment();
        } else {
            fragment = new ChatUserFragment();
        }
        // 从Activity传递参数到Fragment中去
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.lay_container, fragment).commit();
    }
}