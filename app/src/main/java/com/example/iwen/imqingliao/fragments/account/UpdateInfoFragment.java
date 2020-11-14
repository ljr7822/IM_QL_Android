package com.example.iwen.imqingliao.fragments.account;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.iwen.common.app.Application;
import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.fragments.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

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
                UCrop.Options options = new UCrop.Options();
                // 设置图片处理格式
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // 设置压缩后图片的精度
                options.setCompressionQuality(96);
                // 得到头像缓存地址
                File dPath = Application.getAvatarTempFile();
                UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))
                        .withAspectRatio(1,1) // 1:1比例
                        .withMaxResultSize(520,520) // 最大尺寸
                        .withOptions(options)
                        .start(getActivity());
            }
        }).show(getChildFragmentManager(),GalleryFragment.class.getName());
    }
}