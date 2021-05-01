package com.example.iwen.imqingliao.fragments.panel;

import android.view.View;

import com.example.iwen.common.face.Face;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.imqingliao.R;

import java.util.List;

/**
 * @author iwen大大怪
 * Create to 2021/04/30 19:20
 */
public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> bases,AdapterListener<Face.Bean> listener) {
        super(bases,listener);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
