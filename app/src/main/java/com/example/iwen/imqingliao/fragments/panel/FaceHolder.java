package com.example.iwen.imqingliao.fragments.panel;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.iwen.common.face.Face;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.imqingliao.R;

import butterknife.BindView;

/**
 * @author iwen大大怪
 * @Create to 2021/04/30 19:21
 */
public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {

    @BindView(R.id.im_face)
    ImageView mFace;

    public FaceHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null && ((bean.preview instanceof Integer) || (bean.preview instanceof String))) {

            Glide.with(mFace.getContext())
                    .asBitmap()
                    .load(bean.preview)
                    .format(DecodeFormat.PREFER_ARGB_8888)// 设置解码格式为8888
                    .into(mFace);
        }
    }
}
