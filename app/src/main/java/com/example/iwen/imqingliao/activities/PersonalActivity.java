package com.example.iwen.imqingliao.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.example.iwen.common.app.PresenterToolbarActivity;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.presenter.contace.PersonalContract;
import com.example.iwen.factory.presenter.contace.PersonalPresenter;
import com.example.iwen.imqingliao.R;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity
        extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View {
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;

    @BindView(R.id.im_header)
    ImageView mHeader;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_name)
    TextView mName;
    @BindView(R.id.txt_desc)
    TextView mDesc;
    @BindView(R.id.txt_follows)
    TextView mFollows;
    @BindView(R.id.txt_following)
    TextView mFollowing;
    @BindView(R.id.btn_say_hello)
    Button mSayHello;

    // 关注
    private MenuItem mFollowItem;
    private boolean mIsFollowUser = false;

    /**
     * show 方法
     *
     * @param context 上下文
     * @param userId  用户的id
     */
    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    /**
     * 初始化参数
     *
     * @param bundle 参数bundle
     * @return boolean
     */
    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    /**
     * 初始化布局，将title设置为空
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
    }

    /**
     * 初始化数据，从网络拉取联系人信息
     */
    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    /**
     * 初始化菜单
     *
     * @param menu 菜单布局
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal, menu);
        mFollowItem = menu.findItem(R.id.action_follow);
        // 更改关注菜单点击后的状态
        changeFollowItemStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            // TODO 进行关注操作
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发起聊天的点击事件
     */
    @OnClick(R.id.btn_say_hello)
    void onSayHelloClick() {
        User user = mPresenter.getUserPersonal();
        if (user == null)
            return;
        MessageActivity.show(this, user);
    }


    /**
     * 更改关注菜单点击后的状态
     */
    private void changeFollowItemStatus() {
        if (mFollowItem == null)
            return;
        // 根据状态设置颜色
        Drawable drawable = mIsFollowUser ?
                getResources().getDrawable(R.drawable.ic_favorite) :
                getResources().getDrawable(R.drawable.ic_favorite_border);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Resource.Color.WHITE);
        mFollowItem.setIcon(drawable);
    }

    /**
     * 复写获取用户id的方法
     *
     * @return 用户id
     */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * 加载完成后展示用户信息
     *
     * @param user 传进来的用户信息
     */
    @Override
    public void onLoadDone(User user) {
        if (user == null)
            return;
        mPortrait.setup(Glide.with(this), user);
        mName.setText(user.getName());
        mDesc.setText(user.getDesc());
        mFollows.setText(String.format(getString(R.string.label_follows), user.getFollows()));
        mFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));
        hideLoading();
    }

    @Override
    public void allowSayHello(boolean isAllow) {
        mSayHello.setVisibility(isAllow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        mIsFollowUser = isFollow;
        changeFollowItemStatus();
    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }
}
