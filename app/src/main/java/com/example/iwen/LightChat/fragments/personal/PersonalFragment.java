package com.example.iwen.LightChat.fragments.personal;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.activities.ChangeDataActivity;
import com.example.iwen.LightChat.activities.MessageActivity;
import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.app.PresenterFragment;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.factory.Factory;
import com.example.iwen.factory.model.db.User;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.factory.presenter.contace.PersonalContract;
import com.example.iwen.factory.presenter.contace.PersonalPresenter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息界面主页
 */
public class PersonalFragment
        extends PresenterFragment<PersonalContract.Presenter>
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
    @BindView(R.id.logoutView)
    CardView mLogoutCard;
    @BindView(R.id.ChangePwdView)
    CardView mChangePwdCard;
    @BindView(R.id.ChangeDataView)
    CardView mChangeDataCard;

    // 关注
    private MenuItem mFollowItem;
    private boolean mIsFollowUser = false;

    private Fragment mFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        userId = bundle.getString(BOUND_KEY_ID);
    }

    @Override
    protected void initData() {
        super.initData();
        // 开始拉取联系人信息
        mPresenter.start();
    }

    // 初始化Presenter
    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
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

    @Override
    public void logoutSuccess() {
        // 服务器退出登录成功后，将本地sp清空并跳转到登录界面
        Account.setToken("");
        Factory.app().finishAll();
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
        //hideLoading();
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
     * 发起聊天的点击事件
     */
    @OnClick(R.id.btn_say_hello)
    void onSayHelloClick() {
        User user = mPresenter.getUserPersonal();
        if (user == null) {
            return;
        }
        MessageActivity.show(getContext(), user);
    }

    // 退出登录
    @OnClick(R.id.logoutView)
    void logoutClick(){
        // 先显示确认弹窗
        new XPopup.Builder(getContext()).asConfirm("退出登录", "退出登录后，将删除用户消息记录等信息，是否退出？",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        // 用户确定退出
                        // 先进行网络请求，将token进行删除
                        // 收到回调后，将本地保存的SP数据进行删除
                        // 最后进行登录界面的跳转
                        mPresenter.logout(userId);
                    }
                })
                .show();
    }

    // 修改密码
    @OnClick(R.id.ChangePwdView)
    void changePwdClick(){
        // 跳转到ChangePwdFragment
        // TODO 修改密码
        TriggerChangeDataFrag();
    }

    // 修改个人资料
    @OnClick(R.id.ChangeDataView)
    void changeDataClick(){
        // 跳转到ChangePwdFragment
        ChangeDataActivity.show(getContext());
    }

    // 跳转到修改密码界面
    private void TriggerChangeDataFrag(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mFragment = new ChangePwdFragment();
        fm.beginTransaction().replace(R.id.lay_personal_container,mFragment).commit();
    }
}