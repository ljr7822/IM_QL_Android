package com.example.iwen.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import com.example.iwen.factory.data.message.SessionDataSource;
import com.example.iwen.factory.data.message.SessionRepository;
import com.example.iwen.factory.model.db.Session;
import com.example.iwen.factory.presenter.BaseSourcePresenter;
import com.example.iwen.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 最近聊天列表的Presenter
 *
 * @author iwen大大怪
 * Create to 2021/02/25 19:51
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View>
        implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View mView) {
        super(new SessionRepository(), mView);
    }

    @Override
    public void onDataLoad(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null) {
            return;
        }
        // 差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        // 刷新界面
        refreshData(result, sessions);
    }
}
