package com.example.iwen.imqingliao.fragments.media;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iwen.common.widget.GalleryView;
import com.example.iwen.imqingliao.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * 图片选择fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener{
    private GalleryView mGalleryView;
    private OnSelectedListener mOnSelectedListener;

    public GalleryFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 先使用默认的
        return new BottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 获取GalleryView
        View root = inflater.inflate(R.layout.fragment_gallery,container,false);
        mGalleryView = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalleryView.setup(getLoaderManager(),this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        // 如果选择一张图片
        if (count>0){
            // 隐藏自己
            dismiss();
            if (mOnSelectedListener != null){
                // 得到所有的选中的图片的路径
                String[] paths = mGalleryView.getSelectedPath();
                // 返回第一张
                mOnSelectedListener.onSelectedImage(paths[0]);
                // 取消和唤起者之间的引用
                mOnSelectedListener = null;
            }
        }
    }

    /**
     * 设置事件监听并返回自己
     * @param listener OnSelectedListener
     * @return GalleryFragment
     */
    public GalleryFragment setListener(OnSelectedListener listener){
        mOnSelectedListener = listener;
        return this;
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener{
        void onSelectedImage(String path);
    }
}