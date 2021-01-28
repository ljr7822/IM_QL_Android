package com.example.iwen.imqingliao.fragments.search;

import com.example.iwen.common.app.Fragment;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.activities.SearchActivity;

/**
 * 搜索群的fragment
 */
public class SearchGroupFragment extends Fragment
        implements SearchActivity.SearchFragment {

    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}