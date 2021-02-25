package com.example.iwen.imqingliao.fragments.message;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.presenter.message.ChatContact;
import com.example.iwen.factory.presenter.message.ChatUserPresenter;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.activities.PersonalActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户聊天界面
 */
public class ChatUserFragment extends ChatFragment<User> implements ChatContact.UserView {
    private MenuItem menuItem;

    @BindView(R.id.iv_avatar)
    PortraitView iv_avatar;

    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        Glide.with(getContext()).load(R.mipmap.default_banner_chat)
                .centerCrop()
                .into(new CustomViewTarget<CollapsingToolbarLayout,Drawable>(ctl_app_bar) {
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

    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = super.toolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_person) {
                onAvatarClick();
            }
            return true;
        });
        // 拿到菜单Icon
        menuItem = toolbar.getMenu().findItem(R.id.action_person);
    }

    // 进行高度的综合运算，透明我们的头像和Icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = iv_avatar;
        MenuItem item = menuItem;
        if (view == null || item == null)
            return;
        if (verticalOffset == 0) {
            // 完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

            // 隐藏Menu
            item.setVisible(false);
            item.getIcon().setAlpha(0);

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

                // 显示Menu
                item.setVisible(true);
                item.getIcon().setAlpha(255);

            } else {
                // 中间状态
                float progress = 1 - (verticalOffset / (float) totalScrollRange);
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);

                // 和头像相反的操作
                item.setVisible(true);
                item.getIcon().setAlpha((int) (255 * (1 - progress)));
            }
        }
    }

    // 头像的点击
    @OnClick(R.id.iv_avatar)
    void onAvatarClick() {
        PersonalActivity.show(getContext(), mReceiverId);
    }

    /**
     * 初始化presenter
     *
     * @return ChatContact.Presenter
     */
    @Override
    protected ChatContact.Presenter initPresenter() {
        // 初始化Presenter
        return new ChatUserPresenter(this,mReceiverId);
    }

    /**
     * 初始化回调，对和你聊天的朋友的信息进行初始化操作
     *
     * @param user 用户
     */
    @Override
    public void onInit(User user) {
        // 对和你聊天的朋友进行的初始化操作
        iv_avatar.setup(Glide.with(getContext()), user);
        ctl_app_bar.setTitle(user.getName());
    }
}