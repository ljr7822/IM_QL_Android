package com.example.iwen.factory.presenter.search;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.data.helper.GroupHelper;
import com.example.iwen.factory.model.card.GroupCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * 搜索群的p层
 * @author : iwen大大怪
 * @create : 2020-12-28 17:26
 */
public class SearchGroupPresenter extends  BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter, DataSource.Callback<List<GroupCard>>{

    private Call searchCall;

    public SearchGroupPresenter(SearchContract.GroupView mView) {
        super(mView);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        if (searchCall != null && !searchCall.isCanceled()) {
            // 如果有上一次请求且没有取消，就将它取消
            call.cancel();
        }
        searchCall = GroupHelper.search(content, this);
    }

    // 搜索成功
    @Override
    public void onDataLoad(final List<GroupCard> groupCards) {
        final SearchContract.GroupView view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(groupCards);
                }
            });
        }
    }

    // 搜索失败
    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.GroupView view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
