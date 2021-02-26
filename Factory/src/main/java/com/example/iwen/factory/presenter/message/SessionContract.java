package com.example.iwen.factory.presenter.message;

import com.example.iwen.common.factory.presenter.BaseContract;
import com.example.iwen.factory.model.db.Session;

/**
 * 消息列表契约
 *
 * @author iwen大大怪
 * Create to 2021/02/25 19:50
 */
public interface SessionContract {
    // 无需再额外定义，开始即为调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 基类已经完成全部功能
    interface View extends BaseContract.RecyclerView<Session, Presenter> {

    }
}