package com.example.iwen.imqingliao.fragments.main;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.common.widget.EmptyView;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.presenter.contace.ContactContract;
import com.example.iwen.factory.presenter.contace.ContactPresenter;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.activities.MessageActivity;
import com.example.iwen.imqingliao.activities.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactFragment
        extends PresenterFragment<ContactContract.Presenter>
        implements ContactContract.View {
    // 空布局
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    // 列表recycler
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    // 适配器
    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        // 初始化recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User user) {
                // 返回cell布局
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });

        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(),user);
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
    protected ContactContract.Presenter initPresenter() {
        // 初始化presenter
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 进行界面操作
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {
        // 头像
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        // 名字
        @BindView(R.id.txt_name)
        TextView mName;
        // 描述
        @BindView(R.id.txt_desc)
        TextView mDesc;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            // 头像加载
            mPortraitView.setup(Glide.with(ContactFragment.this), user);
            // 名称加载
            mName.setText(user.getName());
            // 简介加载
            mDesc.setText(user.getDesc());
        }

        // 进入个人界面
        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(getContext(),mData.getId());
        }
    }
}