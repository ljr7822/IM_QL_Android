package com.example.iwen.imqingliao.fragments.message;

import android.view.View;

import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.imqingliao.R;
import com.google.android.material.appbar.AppBarLayout;

import butterknife.BindView;

/**
 * 用户聊天界面
 */
public class ChatUserFragment extends ChatFragment {
    //private MenuItem menuItem;
    @BindView(R.id.iv_avatar)
    PortraitView iv_avatar;

    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }

    // 进行高度的综合运算，透明我们的头像和Icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = iv_avatar;
        //MenuItem item = menuItem;
//        if (view == null || item == null)
//            return;
        if (verticalOffset == 0) {
            // 完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

//            // 隐藏Menu
//            item.setVisible(false);
//            item.getIcon().setAlpha(0);

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

//                // 显示Menu
//                item.setVisible(true);
//                item.getIcon().setAlpha(255);

            } else {
                // 中间状态
                float progress = 1 - (verticalOffset / (float) totalScrollRange);
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);

//                // 和头像相反的操作
//                item.setVisible(true);
//                item.getIcon().setAlpha((int) (255 * (1 - progress)));
            }
        }
    }
}