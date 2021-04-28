package com.example.iwen.imqingliao.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iwen.common.app.PresenterToolbarActivity;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.model.db.view.MemberUserModel;
import com.example.iwen.factory.presenter.group.GroupMembersContract;
import com.example.iwen.factory.presenter.group.GroupMembersPresenter;
import com.example.iwen.imqingliao.R;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupMemberActivity extends PresenterToolbarActivity<GroupMembersContract.Presenter>
        implements GroupMembersContract.View {

    private static final String KEY_GROUP_ID = "KEY_GROUP_ID";

    private static final String KEY_GROUP_ADMIN = "KEY_GROUP_ADMIN";

    // mToolbar控件
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    // RecyclerView控件
    @BindView(R.id.rv_members)
    RecyclerView mRecyclerView;

    // 群id
    private String mGroupId;

    // 是否是管理员
    private boolean mIsAdmin;

    private RecyclerAdapter<MemberUserModel> mAdapter;

    /**
     * 普通用户显示方法：群成员列表
     *
     * @param context 上下文
     * @param groupId 群的id
     */
    public static void show(Context context, String groupId) {
        show(context, groupId, false);
    }

    /**
     * 管理员显示方法
     *
     * @param context 上下文
     * @param groupId 群id
     */
    public static void showAdmin(Context context, String groupId) {
        show(context, groupId, true);
    }

    /**
     * 显示方法
     *
     * @param context 上下文
     * @param groupId 群id
     * @param isAdmin 是否管理员
     */
    private static void show(Context context, String groupId, boolean isAdmin) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        Intent intent = new Intent(context, GroupMemberActivity.class);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_GROUP_ADMIN, isAdmin);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mGroupId = bundle.getString(KEY_GROUP_ID);
        mIsAdmin = bundle.getBoolean(KEY_GROUP_ADMIN);
        return !TextUtils.isEmpty(mGroupId);

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(R.string.title_member_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<MemberUserModel>() {
            @Override
            protected int getItemViewType(int position, MemberUserModel memberUserModel) {
                return R.layout.cell_group_create_contact;
            }

            @Override
            protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
                return new GroupMemberActivity.ViewHolder(root);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.refresh();
    }

    @Override
    protected GroupMembersContract.Presenter initPresenter() {
        return new GroupMembersPresenter(this);
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 隐藏的Loading
        hideLoading();
    }

    @Override
    public String getGroupId() {
        return mGroupId;
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel> {

        @BindView(R.id.iv_avatar)
        PortraitView mPortrait;
        @BindView(R.id.tv_name)
        TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);
        }

        @Override
        protected void onBind(MemberUserModel memberUserModel) {
            mPortrait.setup(Glide.with(GroupMemberActivity.this), memberUserModel.portrait);
            mName.setText(memberUserModel.name);
        }

        // 点击头像跳转
        @OnClick(R.id.iv_avatar)
        void onPortraitClick() {
            // 进行跳转
            PersonalActivity.show(GroupMemberActivity.this, mData.userId);
        }
    }
}