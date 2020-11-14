package com.example.iwen.imqingliao.fragments.main;

import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.widget.GalleryView;
import com.example.iwen.imqingliao.R;

import butterknife.BindView;

public class ActiveFragment extends Fragment {
    @BindView(R.id.galleryView)
    GalleryView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGalley.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}