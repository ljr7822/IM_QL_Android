package com.example.iwen.factory.presenter.message;

import com.example.iwen.factory.model.db.Session;

/**
 * 最近聊天列表的Presenter
 *
 * @author iwen大大怪
 * Create to 2021/02/25 19:51
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionContract.View, SessionDataSource>
        implements SessionContract.Presenter {}
