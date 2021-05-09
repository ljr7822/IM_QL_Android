package com.example.iwen.LightChat.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.app.ToolbarActivity;
import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.fragments.search.SearchGroupFragment;
import com.example.iwen.LightChat.fragments.search.SearchUserFragment;

import java.util.Objects;

public class SearchActivity extends ToolbarActivity {

    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    // 用户的搜索
    public static final int TYPE_USER = 1;
    // 群聊的搜索
    public static final int TYPE_GROUP = 2;
    private int type;
    private SearchFragment searchFragment;

    /**
     * 显示搜索界面
     *
     * @param context 上下文
     * @param type    显示的类型，用户还是群
     */
    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        // 是显示搜索人或者是搜索群
        return type == TYPE_USER || type == TYPE_GROUP;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 显示对应的fragment
        Fragment fragment;
        if (type==TYPE_USER){
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            searchFragment = searchUserFragment;
        }else {
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            searchFragment = searchGroupFragment;
        }
        // 事务的提交
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container,fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        // 找到搜索菜单
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        if (searchView!=null){
            // 拿到一个搜索管理器
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
            // 添加搜索监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // 当点击了提交按钮的时候
                    search(query);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    // 当文字改变的时候，咱们不会及时搜索，只在为null的情况下进行搜索
                    if (TextUtils.isEmpty(s)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索的发起点
     *
     * @param query 搜索的文字
     */
    private void search(String query) {
        if (searchFragment == null) {
            return;
        }
        searchFragment.search(query);
    }

    /**
     * 搜索的Fragment必须实现的接口
     */
    public interface SearchFragment {
        void search(String content);
    }
}