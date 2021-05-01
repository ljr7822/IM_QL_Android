package com.example.iwen.imqingliao.fragments.panel;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.face.Face;
import com.example.iwen.common.tools.UiTool;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.imqingliao.R;
import com.google.android.material.tabs.TabLayout;

import net.qiujuer.genius.ui.Ui;

import java.util.List;

/**
 * 空气面板的fragment
 * 底部面板实现
 */
public class PanelFragment extends Fragment {

    private PanelCallBack mCallBack;

    public PanelFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_panel;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        initFace(view);
        initRecord(view);
        initGallery(view);
    }

    /**
     * 初始化PanelCallBack方法
     *
     * @param panelCallBack PanelCallBack
     */
    public void setup(PanelCallBack panelCallBack) {
        mCallBack = panelCallBack;
    }

    /**
     * 初始化表情
     *
     * @param root 界面view
     */
    private void initFace(View root) {
        View facePanel = root.findViewById(R.id.lay_panel_face);
        TabLayout tabLayout = facePanel.findViewById(R.id.tab);
        View backspace = facePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 表情删除逻辑
                PanelCallBack panelCallBack = mCallBack;
                if (panelCallBack == null) {
                    return;
                }
                // 模拟键盘删除点击
                KeyEvent event = new KeyEvent(0, 0, 0,
                        KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                panelCallBack.getInputEditText().dispatchKeyEvent(event);
            }
        });
        ViewPager viewPager = facePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        // 每一个表情显示48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        // 屏幕大小
        final int totalScreen = UiTool.getScreenWidth(getActivity());
        // 计算一行有多少列
        final int spanCount = totalScreen / minFaceSize;

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                // 添加
                Context context;
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
                // 设置adapter
                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                FaceAdapter adapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListenerImpl<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        // 具体的回调
                        if (mCallBack == null) {
                            return;
                        }
                        // 拿到输入框
                        // 表情添加
                        EditText editText = mCallBack.getInputEditText();
                        // 调用inputFace
                        Face.inputFace(getContext(), editText.getText(), bean, (int) (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));

                    }
                });
                recyclerView.setAdapter(adapter);
                container.addView(recyclerView);
                return recyclerView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                // 销毁、移除
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                // 拿到表情的描述
                return Face.all(getContext()).get(position).name;
            }
        });
    }

    private void initRecord(View root) {

    }

    private void initGallery(View root) {

    }

    // 显示表情
    public void showFace() {

    }

    // 显示语音
    public void showRecord() {

    }

    // 显示图片选择
    public void showGallery() {

    }

    // 回调聊天界面的callback
    public interface PanelCallBack {
        // 拿到输入框
        EditText getInputEditText();
    }
}