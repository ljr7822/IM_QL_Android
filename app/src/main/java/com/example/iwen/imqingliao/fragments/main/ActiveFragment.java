package com.example.iwen.imqingliao.fragments.main;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.common.utils.DateTimeUtil;
import com.example.iwen.common.widget.EmptyView;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.factory.model.db.Session;
import com.example.iwen.factory.presenter.message.SessionContract;
import com.example.iwen.factory.presenter.message.SessionPresenter;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.activities.MessageActivity;

import butterknife.BindView;

public class ActiveFragment
        extends PresenterFragment<SessionContract.Presenter>
        implements SessionContract.View{

    // 会话列表的recycler
    @BindView(R.id.rv_session)
    RecyclerView rv_session;
    @BindView(R.id.ev_empty)
    EmptyView ev_empty;

    private RecyclerAdapter<Session> adapter;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        // 初始化recyclerAdapter
        rv_session.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_session.setAdapter(adapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });
        // 设置点击事件监听
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Session session) {
                // 跳转到聊天界面
                MessageActivity.show(requireContext(), session);
            }
        });
        // 初始化占位布局
        ev_empty.bind(rv_session);
        setPlaceHolderView(ev_empty);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        // 进行第一次初始化
        mPresenter.start();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(ActiveFragment.this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(adapter.getItemCount()>0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.tv_time)
        TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            // 绑定数据
            iv_avatar.setup(Glide.with(ActiveFragment.this), session.getPicture());
            tv_name.setText(session.getTitle());
            String content = TextUtils.isEmpty(session.getContent()) ? "" : session.getContent();
            //Spannable spannable = new SpannableString(content);
            // 解析表情
            //Face.decode(tv_content, spannable, (int) tv_content.getTextSize());
            // 内容设置到布局
            tv_content.setText(content);
            // 绑定格式化后的时间
            tv_time.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));
        }
    }
}