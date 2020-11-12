package com.example.iwen.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iwen.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * RecyclerView.Adapter
 * author : Iwen大大怪
 * create : 2020/11/11 17:00
 */
@SuppressWarnings("unused")
public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener,
        AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，本类中约定类型即为 xml 资源id
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     类型
     * @return xml文件的id，用于创建viewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建一个viewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面类型,约定为xml布局的id
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 得到inflater用于将xml初始化为view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // 将xml id为viewType的文件初始化为一个rootView
        View root = layoutInflater.inflate(viewType, parent, false);
        // 通过子类必须实现的方法得到一个viewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);
        //设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        //设置view的tag为viewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        // 绑定callback
        holder.mCallback = this;

        return holder;
    }

    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 布局类型
     * @return ViewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到holder上
     *
     * @param holder   ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        // 得到绑定的数据
        Data data = mDataList.get(position);
        // 触发绑定方法
        holder.bind(data);
    }

    /**
     * 得到当前集合数据量
     *
     * @return 得到当前集合数据量
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        // 更新当前插入
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合
     *
     * @param dataList 新集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到viewHolder当前对应的适配器id
            int pos = viewHolder.getAdapterPosition();
            // 回调
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            //得到viewHolder当前对应的适配器id
            int pos = viewHolder.getAdapterPosition();
            // 回调
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     *
     * @param adapterListener AdapterListener
     */
    public void setListener(AdapterListener<Data> adapterListener) {
        this.mListener = adapterListener;
    }

    /**
     * 自定义监听器
     *
     * @param <Data> 泛型
     */
    public interface AdapterListener<Data> {
        // 点击
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        // 长按
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 自定义的viewHolder
     *
     * @param <Data> 泛型类型
     */
    public abstract static class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        private AdapterCallback<Data> mCallback;
        protected Data mData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 传入的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当出发绑定数据的时候，必须复写
         *
         * @param data 数据
         */
        protected abstract void onBind(Data data);

        /**
         * holder本身和对应的data进行跟新操作
         *
         * @param data 数据
         */
        public void updateData(Data data) {
            if (this.mCallback != null) {
                this.mCallback.update(data, this);
            }
        }
    }
}
