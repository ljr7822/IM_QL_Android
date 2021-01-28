package com.example.iwen.factory.presenter.search;

import com.example.iwen.common.factory.data.DataSource;
import com.example.iwen.common.factory.presenter.BasePresenter;
import com.example.iwen.factory.data.helper.UserHelper;
import com.example.iwen.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * 搜索人的p层
 *
 * @author : iwen大大怪
 * create : 2020-12-28 17:26
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {
    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView mView) {
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
        searchCall = UserHelper.search(content, this);
    }

    // 搜索成功
    @Override
    public void onDataLoad(final List<UserCard> userCards) {
        final SearchContract.UserView view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
        }
    }

    // 搜索失败
    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.UserView view = getView();
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
