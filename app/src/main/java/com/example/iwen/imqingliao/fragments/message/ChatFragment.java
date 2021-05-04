package com.example.iwen.imqingliao.fragments.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.common.face.Face;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.common.widget.SmoothInputLayout;
import com.example.iwen.common.widget.adapter.TextWatcherAdapter;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.model.db.Message;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.presenter.message.ChatContact;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.fragments.panel.PanelFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.iwen.imqingliao.activities.MessageActivity.KEY_RECEIVER_ID;

/**
 * 聊天界面基类
 *
 * @author iwen大大怪
 * Create to 2021/02/23 23:52
 */
public abstract class ChatFragment<InitModel>
        extends PresenterFragment<ChatContact.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,
        ChatContact.View<InitModel>, View.OnClickListener,
        PanelFragment.PanelCallBack {

    // 接受者id
    protected String mReceiverId;
    // 自定义适配器
    protected Adapter mAdapter;

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
    // 发送按钮
    @BindView(R.id.iv_submit)
    ImageView iv_submit;
    // 表情按钮
    @BindView(R.id.iv_face)
    View iv_face;
    // 语音按钮
    @BindView(R.id.iv_record)
    View iv_record;
    // Panel面板
    @BindView(R.id.lay_panel)
    View mPanel;
    // SmoothInputLayout自定义控件
    @BindView(R.id.lay_content)
    SmoothInputLayout lay_content;

    private PanelFragment mPanelFragment;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
    }

    @Override
    protected final int getContentLayoutId() {
        return R.layout.fragment_chat_common;
    }

    /**
     * 给外部设置顶部布局id资源
     */
    @LayoutRes
    protected abstract int getHeaderLayoutId();

    @Override
    protected void initWidget(View view) {
        // 拿到占位布局
        ViewStub stub = view.findViewById(R.id.vs_view_stub);
        // 替换顶部布局一定要发生在super之前
        stub.setLayoutResource(getHeaderLayoutId());
        stub.inflate();

        // 在这里进行控件绑定
        super.initWidget(view);

        // 初始化
        initToolbar();
        initAppBar();
        initEditContent();
        initSetOnClickListener();
        // 获得面板的fragment
        mPanelFragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        // 初始化回调
        mPanelFragment.setup(this);

        // RecyclerView基本设置
        rv_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_recycler.setAdapter(mAdapter = new Adapter());
    }

    @Override
    protected void initData() {
        super.initData();
        // 开始进行初始化操作
        mPresenter.start();
    }

    /**
     * 初始化Toolbar
     */
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

    /**
     * 给AppBar设置一个监听，得到关闭与打开时的进度
     */
    private void initAppBar() {
        abl_app_bar.addOnOffsetChangedListener(this);
    }

    /**
     * 初始化输入框
     */
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
        // 点击输入框时，折叠appbar
        edt_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    abl_app_bar.setExpanded(false);
                }
            }
        });
    }


    /**
     * 复写layout布局，解决出现的软键盘弹出问题
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    /**
     * 初始化点击事件：绑定
     */
    private void initSetOnClickListener() {
        iv_face.setOnClickListener(this);
        iv_record.setOnClickListener(this);
        iv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击表情
            case R.id.iv_face:
                onFaceClick();
                break;
            // 点击语音
            case R.id.iv_record:
                onRecordClick();
                break;
            // 点击发送或者更多
            case R.id.iv_submit:
                onSubmitClick();
                break;
        }
    }

    /**
     * 表情点击
     */
    //@OnClick(R.id.iv_face)
    private void onFaceClick() {
        // 打开隐藏软键盘
        // 顶部CollapsingToolbarLayout实现自动折叠
        abl_app_bar.setExpanded(false);
        lay_content.showInputPane(true);
        mPanelFragment.showFace();
    }

    /**
     * 语音点击
     */
    //@OnClick(R.id.iv_record)
    private void onRecordClick() {
        // 打开隐藏软键盘
        // 顶部CollapsingToolbarLayout实现自动折叠
        abl_app_bar.setExpanded(false);
        lay_content.showInputPane(true);
        mPanelFragment.showRecord();
    }

    /**
     * 发送按钮
     */
    //@OnClick(R.id.iv_submit)
    private void onSubmitClick() {
        if (iv_submit.isActivated()) {
            // 发送
            String content = edt_content.getText().toString().trim();
            edt_content.setText("");
            mPresenter.pushText(content);
            // 消息发送后自动定位到最后一行
            if (mAdapter.getItemCount() > 0) {
                rv_recycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }
        } else {
            // 打开更多
            onMoreActionClick();
        }
    }

    /**
     * 打开更多
     */
    private void onMoreActionClick() {
        // 打开隐藏软键盘
        // 顶部CollapsingToolbarLayout实现自动折叠
        abl_app_bar.setExpanded(false);
        lay_content.showInputPane(true);
        mPanelFragment.showGallery();
    }

    /**
     * 复写获取RecyclerAdapter方法
     *
     * @return 我们自己写的mAdapter
     */
    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 界面没有占位布局，Recycler是一直显示的
        // 消息来了后自动滚到最后一行
        if (mAdapter.getItemCount() > 0) {
            rv_recycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    /**
     * 表情输入回调
     */
    @Override
    public EditText getInputEditText() {
        return edt_content;
    }

    /**
     * 图片回调回来
     *
     * @param paths 路径
     */
    @Override
    public void onSendGallery(String[] paths) {
        mPresenter.pushImages(paths);
    }

    /**
     * 语音回调回来
     *
     * @param file 文件
     * @param time 时长
     */
    @Override
    public void onRecordDone(File file, long time) {
        mPresenter.pushAudio(file.getAbsolutePath(), time);
    }

    /**
     * 实现内容布局的Adapter适配器
     */
    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());
            switch (message.getType()) {
                case Message.TYPE_AUDIO:
                    // 语音消息
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_PIC:
                    // 图片消息
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;// 文件消息
                default:
                    // 默认文字内容,我发送的在右边，收到的在左边
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                // 文字消息
//                case R.layout.cell_chat_text_right:
//                case R.layout.cell_chat_text_left:
//                    return new TextHolder(root);
                // 语音消息
                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);
                // 图片消息
                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);
                default:
                    // 默认情况下返回的就是Text类型的Holder进行处理
                    return new TextHolder(root);
            }
        }
    }

    // holder 基类
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        // 头像
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        // 发送状态，允许为空，左边没有，右边有
        @Nullable
        @BindView(R.id.loading)
        Loading loading;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // 进行数据加载，因为user为懒加载
            sender.load();
            iv_avatar.setup(Glide.with(ChatFragment.this), sender);

            if (loading != null) {
                int status = message.getStatus();
                // 当前布局应该是在右边
                if (status == Message.STATUS_DONE) {
                    // 正常状态，隐藏loading
                    loading.stop();
                    loading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    // 正在发送的状态
                    loading.setVisibility(View.VISIBLE);
                    loading.setProgress(0);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    loading.start();
                } else if (status == Message.STATUS_FAILED) {
                    // 发送失败,点击头像重新发送
                    loading.setVisibility(View.VISIBLE);
                    loading.stop();
                    loading.setProgress(1);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.red_400));
                }
                // 只有当前状态是发送失败，才允许点击
                iv_avatar.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        // 重新发送信息
        @OnClick(R.id.iv_avatar)
        void onRePushClick() {
            if (loading != null && mPresenter.rePush(mData)) {
                // 必须是右边的才有可能重新发送
                // 状态改变需要重新刷新当前界面的信息
                updateData(mData);
            }
        }
    }

    // 文字holder基类
    class TextHolder extends BaseHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            Spannable spannable = new SpannableString(message.getContent());
            // 解析表情
            Face.decode(tv_content, spannable, (int) Ui.dipToPx(getResources(), 20f));
            // 内容设置到布局
            tv_content.setText(spannable);
        }
    }

    // 语音audio holder
    class AudioHolder extends BaseHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.iv_audio_track)
        ImageView iv_audio_track;

        public AudioHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            String attach = TextUtils.isEmpty(message.getAttach()) ? "0" :
                    message.getAttach();
            tv_content.setText(formatTime(attach));

        }

        // 当播放开始
        void onPlayStart() {
            // 显示
            iv_audio_track.setVisibility(View.VISIBLE);
        }

        // 当播放停止
        void onPlayStop() {
            // 占位并隐藏
            iv_audio_track.setVisibility(View.INVISIBLE);
        }

        private String formatTime(String attach) {
            float time;
            try {
                // 毫秒转换为秒
                time = Float.parseFloat(attach) / 1000f;
            } catch (Exception e) {
                time = 0;
            }
            // 12000/1000f = 12.0000000
            // 取整一位小数点 1.234 -> 1.2 1.02 -> 1.0
            String shortTime = String.valueOf(Math.round(time * 10f) / 10f);
            // 1.0 -> 1     1.2000 -> 1.2
            shortTime = shortTime.replaceAll("[.]0+?$|0+?$", "");
            return String.format("%s″", shortTime);
        }

    }

    // 图片holder
    class PicHolder extends BaseHolder {
        @BindView(R.id.iv_image)
        ImageView iv_image;

        public PicHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //当为图片类型的时候，content即为图片地址
            String contentUrl = message.getContent();
            Glide.with(getContext())
                    .load(contentUrl)
                    .fitCenter()
                    .into(iv_image);
        }
    }
}
