package com.example.iwen.factory.data.user;

import android.text.TextUtils;

import com.example.iwen.factory.data.helper.DbHelper;
import com.example.iwen.factory.model.card.UserCard;
import com.example.iwen.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 对UserCard的处理、分发
 *
 * @author iwen大大怪
 * @Create to 2021/02/18 11:03
 */
public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    // 单线程池，处理卡片，一个个的进行消息处理
    // 此线程池保证所有任务的执行顺序按照任务的提交顺序执行
    private final Executor executor = Executors.newSingleThreadExecutor();

    // 單例模式
    public static UserCenter getInstance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        // 交给单线程池去处理
        // 把工作任务添加到线程中
        executor.execute(new UserCardHandler(cards));
    }

    /**
     * 线程调度时会触发run方法
     */
    private class UserCardHandler implements Runnable {
        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            // 当被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                // 过滤空card
                if (card == null || TextUtils.isEmpty(card.getId())) {
                    continue;
                }
                // 添加一个user
                users.add(card.build());
            }
            // 进行数据库存储，并分发通知，异步的操作
            DbHelper.save(User.class, users.toArray(new User[0]));
        }
    }
}
