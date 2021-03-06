package com.example.iwen.LightChat.fragments.message;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.iwen.factory.model.db.Group;
import com.example.iwen.factory.model.db.view.MemberUserModel;
import com.example.iwen.factory.presenter.message.ChatContact;
import com.example.iwen.factory.presenter.message.ChatGroupPresenter;
import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.activities.GroupMemberActivity;
import com.example.iwen.LightChat.activities.PersonalActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import butterknife.BindView;

/**
 * 群的聊天界面
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContact.GroupView {

    // 顶部图片
    @BindView(R.id.iv_header)
    ImageView mHeader;
    // LinearLayout布局
    @BindView(R.id.ll_members)
    LinearLayout ll_members;
    // 更多文字提示
    @BindView(R.id.tv_more)
    TextView tv_more;


    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_group;
    }

    @Override
    protected ChatContact.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        Glide.with(getContext()).load(R.drawable.default_banner_group)
                .centerCrop()
                .into(new CustomViewTarget<CollapsingToolbarLayout, Drawable>(ctl_app_bar) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        this.view.setContentScrim(resource.getCurrent());
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    // 进行高度的综合运算，透明我们的头像和Icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = ll_members;
        if (view == null) {
            return;
        }
        if (verticalOffset == 0) {
            // 完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
        } else {
            // abs运算
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                // 关闭状态
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
            } else {
                // 中间状态
                float progress = 1 - (verticalOffset / (float) totalScrollRange);
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);
            }
        }
    }

    /**
     * 初始化
     *
     * @param group 群信息
     */
    @Override
    public void onInit(Group group) {
        ctl_app_bar.setTitle(group.getName());
        Glide.with(getContext())
                .load(group.getPicture())
                .centerCrop()
                .placeholder(R.drawable.default_banner_group)
                .into(mHeader);

    }

    /**
     * 设置管理员菜单
     *
     * @param isAdmin 是否是管理员
     */
    @Override
    public void showAdminOption(boolean isAdmin) {
        if (isAdmin) {
            toolbar.inflateMenu(R.menu.chat_group);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_add) {
                        // 添加新成员
                        // mReceiverId就是群id
                        GroupMemberActivity.showAdmin(getContext(), mReceiverId);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 设置群成员头像
     *
     * @param members   群成员列表
     * @param moreCount 群成员数量
     */
    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, long moreCount) {
        if (members == null && members.size() == 0) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final MemberUserModel member : members) {
            // 添加成员头像
            ImageView p = (ImageView) inflater.inflate(R.layout.lay_chat_group_avatar, ll_members, false);
            ll_members.addView(p, 0);

            Glide.with(this)
                    .load(member.portrait)
                    .placeholder(R.mipmap.default_portrait)
                    .centerCrop()
                    .dontAnimate()
                    .into(p);

            // 跳转个人信息界面
            p.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonalActivity.show(getContext(), member.userId);
                }
            });
        }

        // 显示更多
        if (moreCount > 0) {
            tv_more.setText(String.format("+%s", moreCount));
            tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 显示成员列表
                    // mReceiverId就是群id
                    GroupMemberActivity.show(getContext(), mReceiverId);
                }
            });
        } else {
            tv_more.setVisibility(View.GONE);
        }
    }
}