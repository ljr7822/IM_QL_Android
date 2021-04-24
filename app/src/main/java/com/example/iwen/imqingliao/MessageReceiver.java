package com.example.iwen.imqingliao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.iwen.factory.Factory;
import com.example.iwen.factory.data.helper.AccountHelper;
import com.example.iwen.factory.persistence.Account;
import com.igexin.sdk.PushConsts;

/**
 * 个推消息处理
 * 广播接收器
 *
 * @author : iwen大大怪
 * @create : 12-8 008 1:30
 */
public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null){
            return;
        }
        Bundle bundle = intent.getExtras();
        // 判断当前意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:{
                // 当Id初始化的时候,获取设备ID
                Log.e(TAG, "GET_CLIENTID: " + bundle.toString());
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA: {
                // 常规消息送达
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    onMessageArrived(message);
                    Log.e(TAG, "GET_MSG_DATA: " + message);
                }
                break;
            }
            default:
                Log.e(TAG, "OTHERS: " + bundle.toString());
                break;
        }
    }

    /**
     * 当Id初始化的时候,获取clientId
     *
     * @param cid clientId
     */
    private void onClientInit(String cid) {
        // 设置设备ID
        Account.setPushId(cid);
        if (Account.isLogin()) {
            // 账户登录状态，进行一次PushId绑定，没有登录是不能绑定PushId的
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息到达时
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        // 交由Factory处理
        Factory.dispatchPush(message);
    }
}
