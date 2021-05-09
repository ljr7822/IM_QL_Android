package com.example.iwen.LightChat.helper;

import android.content.Context;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 解决对fragment的调度与重用问题
 * <p>
 * author : Iwen大大怪
 * create : 2020/11/13 12:20
 */
public class NavHelper<T> {
    // 所有的Tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray();
    // 用于初始化的必要参数
    private final Context mContext;
    private final int containerId;
    private final FragmentManager mFragmentManager;
    private final OnTabChangedListener<T> listener;
    // 当前一个选中的tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager, OnTabChangedListener<T> listener) {
        mContext = context;
        this.containerId = containerId;
        mFragmentManager = fragmentManager;
        this.listener = listener;
    }

    /**
     * 添加Tab
     * @param menuId 对应的菜单id
     * @param tab Tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab){
        tabs.put(menuId,tab);
        return this;
    }

    /**
     * 获取当前显示的Tab
     * @return 当前显示的Tab
     */
    public Tab<T> getCurrentTab(){
        return currentTab;
    }

    /**
     * 执行点击菜单操作
     * @param menuId 菜单的id
     * @return 是否能处理这个点击
     */
    public boolean performClickMenu(int menuId) {
        // 集合中寻找点击的菜单对应Tab,如果有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab!=null){
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真实的tab选择操作
     * @param tab Tab
     */
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;
        if (currentTab!=null){
            oldTab = currentTab;
            if (oldTab == tab){
                // 如果当前tab就是点击的Tab，我们不做处理
                notifyTabReselect(tab);
                return;
            }
        }
        // 赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab,oldTab);

    }

    /**
     * 进行Fragment的真实调度操作
     * @param newTab 新的
     * @param oldTab 旧的
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (oldTab != null){
            if (oldTab.mFragment != null){
                // 从界面移除，但是还在fragment的缓存空间中
                transaction.detach(oldTab.mFragment);
            }
        }
        if (newTab != null){
            if (newTab.mFragment == null){
                // 首次新建
                Fragment fragment = Fragment.instantiate(mContext,newTab.clx.getName());
                // 缓存起来
                newTab.mFragment = fragment;
                // 提交到FragmentManger
                transaction.add(containerId,fragment,newTab.clx.getName());
            }else {
                // 从FragmentManger的缓存空间重新加载到界面中
                transaction.attach(newTab.mFragment);
            }
        }
        // 提交事务
        transaction.commit();
        // 通知回调
        notifyTabSelect(newTab,oldTab);
    }

    /**
     * 通知回调的监听器
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    private void notifyTabSelect(Tab<T> newTab,Tab<T> oldTab){
        if (listener != null){
            listener.onTabChanged(newTab,oldTab);
        }
    }

    /**
     * 双击刷新
     * @param tab
     */
    private void notifyTabReselect(Tab<T> tab){
        // TODO 二次点击
    }

    /**
     * 我们所有Tab基础属性
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        // Fragment对应的Class信息
        public Class<?> clx;
        // 额外的字段，用户之际设定需要使用什么
        public T extra;
        // 内部缓存对应的fragment
        Fragment mFragment;
    }

    /**
     * 定义事件处理完成后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
