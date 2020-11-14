package com.example.iwen.common.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * activity基类
 * author : Iwen大大怪
 * create : 2020/11/11 14:35
 */
public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前初始化窗口
        initWidows();
        if (initArgs(getIntent().getExtras())) {
            int layId = getContentLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 动态获取权限
     */


    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化窗口
     */
    protected void initWidows() {
    }

    /**
     * 初始化界面相关参数
     *
     * @param bundle 参数bundle
     * @return 参数正确返回true 错误 false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前界面的资源文件id
     *
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当导航栏back被点击后 finish当前页面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前activity下的所有fragment
        List<androidx.fragment.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (androidx.fragment.app.Fragment fragment : fragments) {
                //判断是否为Fragment
                if (fragment instanceof Fragment) {
                    //判断Fragment是否自行处理了事件
                    if (((Fragment) fragment).onBackPressed())
                        return;
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
