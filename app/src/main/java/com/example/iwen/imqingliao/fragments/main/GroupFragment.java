package com.example.iwen.imqingliao.fragments.main;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.common.widget.EmptyView;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.presenter.group.GroupsContract;
import com.example.iwen.factory.presenter.group.GroupsPresenter;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.activities.MessageActivity;
import com.example.iwen.imqingliao.activities.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupFragment extends PresenterFragment<GroupsContract.Presenter> implements GroupsContract.View {

    // 空布局
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    // 列表recycler
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    // 适配器
    private RecyclerAdapter<Group> mAdapter;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        // 初始化recycler
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Group>() {
            @Override
            protected int getItemViewType(int position, Group group) {
                // 返回cell布局
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                // 初始化ViewHolder
                return new GroupFragment.ViewHolder(root);
            }
        });

        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Group group) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), group);
            }
        });

        // 初始化空布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 进行界面操作
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {
        // 头像
        @BindView(R.id.iv_avatar)
        PortraitView mPortraitView;
        // 名字
        @BindView(R.id.tv_name)
        TextView mName;
        // 描述
        @BindView(R.id.tv_desc)
        TextView mDesc;
        // 群成员
        @BindView(R.id.tv_member)
        TextView mMember;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {
            // 头像加载
            mPortraitView.setup(Glide.with(GroupFragment.this), group.getPicture());
            // 名称加载
            mName.setText(group.getName());
            // 简介加载
            mDesc.setText(group.getDescription());
            if (group.holder != null && group.holder instanceof String) {
                mMember.setText((String) group.holder);
            } else {
                mMember.setText("");
            }
        }

        // 进入个人界面
        @OnClick(R.id.iv_avatar)
        void onPortraitClick() {
            PersonalActivity.show(getContext(), mData.getId());
        }
    }
}