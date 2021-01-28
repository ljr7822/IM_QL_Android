package com.example.iwen.imqingliao.fragments.search;

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
import com.example.iwen.factory.model.card.UserCard;
import com.example.iwen.factory.presenter.contace.FollowContract;
import com.example.iwen.factory.presenter.contace.FollowPresenter;
import com.example.iwen.factory.presenter.search.SearchContract;
import com.example.iwen.factory.presenter.search.SearchUserPresenter;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索联系人的界面实现fragment
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {

    // recycler列表
    @BindView(R.id.rv_user)
    RecyclerView mRecyclerView;
    // 空布局
    @BindView(R.id.ev_empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        // 初始化Recycler
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 初始化Adapter
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                // 返回一个cell的布局id
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
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

    /**
     * 搜索
     *
     * @param content 搜索内容
     */
    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    /**
     * 初始化presenter
     */
    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    /**
     * 数据成功返回的数据
     *
     * @param userCards 用户列表
     */
    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);
        // 如果有数据就ok，没有就空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    /**
     * 每一个cell的布局操作、管理
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
            implements FollowContract.View {
        // 头像
        @BindView(R.id.iv_avatar)
        PortraitView mPortraitView;
        // 名字
        @BindView(R.id.tv_name)
        TextView mName;
        // 关注按钮
        @BindView(R.id.iv_follow)
        ImageView mFollow;

        private FollowContract.Presenter mPresenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 当前的View和Presenter绑定
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            // 头像加载
            mPortraitView.setup(Glide.with(SearchUserFragment.this), userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }

        // 发起关注
        @OnClick(R.id.iv_follow)
        void onFollowClick() {
            mPresenter.follow(mData.getId());
        }

        @Override
        public void onFollowSuccess(UserCard userCard) {
            // 更改drawable状态
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mFollow.getDrawable()).stop();
                // 设置为默认的drawable
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            // 发起更新
            updateData(userCard);
        }

        @Override
        public void showError(int str) {
            // 更改drawable状态
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                // 失败停止动画，显示一个圆圈
                LoadingDrawable drawable = ((LoadingDrawable) mFollow.getDrawable());
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);
            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            // 设置到控件中
            mFollow.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }
    }
}