package com.example.iwen.factory.data.message;

import com.example.iwen.factory.model.card.MessageCard;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 消息中心的实现类
 *
 * @author iwen大大怪
 * Create to 2021/02/18 12:52
 */
public class MessageDispatcher implements MessageCenter{
    private static MessageCenter instance;
    //单线程池，处理卡片，一个个的进行消息处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static MessageCenter getInstance(){
        if (instance==null){
            synchronized (MessageDispatcher.class){
                if (instance==null){
                    instance = new MessageDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(MessageCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        //交给单线程池去处理
        executor.execute(new MessageCardHandler(cards));
    }

    /**
     * 线程调度时会触发run方法
     */
    private class MessageCardHandler implements Runnable{
        private final MessageCard[] cards;

        MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {

        }
    }
}
