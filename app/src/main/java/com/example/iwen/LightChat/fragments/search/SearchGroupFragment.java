package com.example.iwen.LightChat.fragments.search;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.common.widget.EmptyView;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.model.card.GroupCard;
import com.example.iwen.factory.presenter.search.SearchContract;
import com.example.iwen.factory.presenter.search.SearchGroupPresenter;
import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.activities.PersonalActivity;
import com.example.iwen.LightChat.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索群的fragment
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.GroupView {

    // recycler列表
    @BindView(R.id.rv_user)
    RecyclerView mRecyclerView;
    // 空布局
    @BindView(R.id.ev_empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<GroupCard> mAdapter;

    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        // 初始化Recycler
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 初始化Adapter
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>() {
            @Override
            protected int getItemViewType(int position, GroupCard groupCard) {
                // 返回一个cell的布局id
                return R.layout.cell_serch_group_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });
        // 绑定空布局
        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 发起第一次搜索
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        // 数据加载成功的情况返回的数据
        mAdapter.replace(groupCards);
        // 如果有数据就ok，没有就空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    /**
     * 每一个cell的布局操作、管理
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {
        // 头像
        @BindView(R.id.iv_avatar)
        PortraitView mPortraitView;
        // 名字
        @BindView(R.id.tv_name)
        TextView mName;

        // 加入世界
        @BindView(R.id.iv_join)
        ImageView mJoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCard groupCard) {
            // 头像加载
            mPortraitView.setup(Glide.with(SearchGroupFragment.this), groupCard.getPicture());
            mName.setText(groupCard.getName());
            // 加入时间判断是否加入群
            mJoin.setEnabled(groupCard.getJoinAt() == null);
        }

        @OnClick(R.id.iv_join)
        void onJoinClick() {
            // 进入个人界面
            PersonalActivity.show(getContext(),mData.getOwnerId());
        }
    }
}