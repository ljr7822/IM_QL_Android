package com.example.iwen.imqingliao.fragments.account;

import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.fragments.media.GalleryFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改用户信息的fragment
 */
public class UpdateInfoFragment extends Fragment {
    // 头像
    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    /**
     * 头像点击事件
     */
    @OnClick(R.id.im_portrait)
    void onPortraitViewClick(){
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {

            }
        }).show(getChildFragmentManager(),GalleryFragment.class.getName());
    }
}