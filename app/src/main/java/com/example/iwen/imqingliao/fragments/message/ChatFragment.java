package com.example.iwen.imqingliao.fragments.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.widget.adapter.TextWatcherAdapter;
import com.example.iwen.imqingliao.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.iwen.imqingliao.activities.MessageActivity.KEY_RECEIVER_ID;

/**
 * 聊天界面基类
 *
 * @author iwen大大怪
 * Create to 2021/02/23 23:52
 */
public abstract class ChatFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener{

    protected String mReceiverId;
    //protected Adapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_recycler)
    RecyclerView rv_recycler;
    @BindView(R.id.abl_app_bar)
    AppBarLayout abl_app_bar;
    @BindView(R.id.ctl_app_bar)
    CollapsingToolbarLayout ctl_app_bar;
    @BindView(R.id.edt_content)
    EditText edt_content;
    @BindView(R.id.iv_submit)
    ImageView iv_submit;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        initToolbar();
        initAppBar();
        // RecyclerView基本设置
        rv_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //rv_recycler.setAdapter(adapter = new Adapter());
        initEditContent();
    }

    // 初始化Toolbar
    protected void initToolbar() {
        Toolbar toolbar = this.toolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    // 给AppBar设置一个监听，得到关闭与打开时的进度
    private void initAppBar() {
        abl_app_bar.addOnOffsetChangedListener(this);
    }

    private void initEditContent() {
        edt_content.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMessage = !TextUtils.isEmpty(content);
                // 设置状态，改变对应的Icon
                iv_submit.setActivated(needSendMessage);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    @OnClick(R.id.iv_face)
    void onFaceClick() {

    }

    @OnClick(R.id.iv_record)
    void onRecordClick() {

    }

    @OnClick(R.id.iv_submit)
    void onSubmitClick() {
        if (iv_submit.isActivated()) {
            // 发送
        } else {
            // 打开更多
            onMoreActionClick();
        }
    }

    private void onMoreActionClick() {

    }
}
