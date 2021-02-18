package com.example.iwen.factory.data.group;

import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.model.card.GroupMemberCard;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author iwen大大怪
 * Create to 2021/02/18 13:07
 */
public class GroupDispatcher implements GroupCenter {
    private static GroupCenter instance;
    //单线程池，处理卡片，一个个的进行消息处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static GroupCenter getInstance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class) {
                if (instance == null) {
                    instance = new GroupDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(GroupCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupMemberHandler(cards));
    }

    /**
     * 把群card处理成群db类
     */
    private class GroupHandler implements Runnable {
        private final GroupCard[] cards;

        GroupHandler(GroupCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {

        }
    }

    /**
     * 把群成員card处理成群db类
     */
    private class GroupMemberHandler implements Runnable {
        private final GroupMemberCard[] cards;

        GroupMemberHandler(GroupMemberCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {

        }
    }
}
