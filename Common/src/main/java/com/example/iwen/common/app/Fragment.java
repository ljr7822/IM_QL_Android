package com.example.iwen.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 * author : Iwen大大怪
 * create : 2020/11/11 14:35
 */
public abstract class Fragment extends androidx.fragment.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnBinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null){
            // 初始化当前的根布局，但是不在创建时就添加到container里边
            int layId = getContentLayoutId();
            View root = inflater.inflate(layId,container,false);
            initWidget(root);
            mRoot = root;
        }else {
            if (mRoot.getParent() !=null){
                // 把当前root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 当view创建完成后初始化数据
        initData();
    }

    /**
     * 初始化界面相关参数
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 首次初始化数据的时候会调用
     */
    protected void onFirstInit() {

    }

    /**
     * 得到当前资源文件id
     *
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View view) {
        mRootUnBinder = ButterKnife.bind(this,view);
    }
    //初始化数据
    protected void initData() {
    }

    /**
     * 点击返回键后触发
     *
     * @return true表示fragment自身已经处理，activity无须关心，
     * false表示返回事件交由activity处理
     */
    public boolean onBackPressed() {
        return false;
    }
}
