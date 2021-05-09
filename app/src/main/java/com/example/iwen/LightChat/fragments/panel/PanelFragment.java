package com.example.iwen.LightChat.fragments.panel;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.iwen.common.app.Application;
import com.example.iwen.common.app.Fragment;
import com.example.iwen.common.face.Face;
import com.example.iwen.common.tools.AudioRecordHelper;
import com.example.iwen.common.tools.UiTool;
import com.example.iwen.common.widget.AudioRecordView;
import com.example.iwen.common.widget.GalleryView;
import com.example.iwen.common.widget.recycler.RecyclerAdapter;
import com.example.iwen.LightChat.R;
import com.google.android.material.tabs.TabLayout;

import net.qiujuer.genius.ui.Ui;

import java.io.File;
import java.util.List;

/**
 * 空气面板的fragment
 * 底部面板实现
 */
public class PanelFragment extends Fragment {
    private View mFacePanel;
    private View mGalleryPanel;
    private View mRecordPanel;
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
        initGallery(view);
        initRecord(view);
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
        //final View facePanel =  mFacePanel = root.findViewById(R.id.lay_panel_face);
        mFacePanel = root.findViewById(R.id.lay_panel_face);
        TabLayout tabLayout = mFacePanel.findViewById(R.id.tab);
        View backspace = mFacePanel.findViewById(R.id.im_backspace);
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
        ViewPager viewPager = mFacePanel.findViewById(R.id.pager);
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

    /**
     * 初始化语音
     *
     * @param root 界面view
     */
    private void initRecord(View root) {
        // 拿到根布局
        View recordView = mRecordPanel = root.findViewById(R.id.lay_panel_record);
        AudioRecordView audioRecordView = recordView.findViewById(R.id.view_audio_record);

        File tmpFile = Application.getAudioTmpFile(true);
        // 录音辅助工具类
        final AudioRecordHelper helper = new AudioRecordHelper(tmpFile, new AudioRecordHelper.RecordCallback() {
            @Override
            public void onRecordStart() {
                // 开始
            }

            @Override
            public void onProgress(long time) {
                // 进度
            }

            @Override
            public void onRecordDone(File file, long time) {
                // 时间是毫秒
                if (time<1000){
                    return;
                }
                File audioFile = Application.getAudioTmpFile(false);
                if (file.renameTo(audioFile)){
                    // 通知到界面
                    PanelCallBack panelCallBack = mCallBack;
                    if (panelCallBack != null){
                        panelCallBack.onRecordDone(audioFile,time);
                    }
                }
            }
        });
        // 开始初始化
        audioRecordView.setup(new AudioRecordView.Callback() {
            @Override
            public void requestStartRecord() {
                // 请求开始录音
                helper.recordAsync();
            }

            @Override
            public void requestStopRecord(int type) {
                // 请求停止录音
                switch (type) {
                    case AudioRecordView.END_TYPE_CANCEL:
                    case AudioRecordView.END_TYPE_DELETE:
                        // 删除取消
                        helper.stop(true);
                        break;
                    case AudioRecordView.END_TYPE_NONE:
                    case AudioRecordView.END_TYPE_PLAY:
                        // 播放
                        helper.stop(false);
                        break;
                }
            }
        });
    }

    /**
     * 初始化图片
     *
     * @param root 界面view
     */
    private void initGallery(View root) {
        //mGalleryPanel = root.findViewById(R.id.lay_gallery_panel);
        mGalleryPanel = root.findViewById(R.id.lay_panel_gallery);
        GalleryView galleryView = mGalleryPanel.findViewById(R.id.view_gallery);
        TextView selectedSize = mGalleryPanel.findViewById(R.id.txt_gallery_select_count);

        galleryView.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {
                String resStr = getText(R.string.label_gallery_selected_size).toString();
                selectedSize.setText(String.format(resStr, count));
            }
        });

        // 点击事件
        mGalleryPanel.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleySendClick(galleryView, galleryView.getSelectedPath());
            }
        });
    }

    // 点击的时候触发，传回一个控件和选中的路径
    private void onGalleySendClick(GalleryView galleryView, String[] paths) {
        // 通知给聊天界面
        // 清理状态
        galleryView.clear();
        // 删除逻辑
        PanelCallBack callback = mCallBack;
        if (callback == null) {
            return;
        }

        callback.onSendGallery(paths);
    }

    // 显示表情
    public void showFace() {
        mFacePanel.setVisibility(View.VISIBLE);
        mGalleryPanel.setVisibility(View.GONE);
        mRecordPanel.setVisibility(View.GONE);
    }

    // 显示语音
    public void showRecord() {
        mFacePanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.GONE);
        mRecordPanel.setVisibility(View.VISIBLE);
    }

    // 显示图片选择
    public void showGallery() {
        mFacePanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.VISIBLE);
        mRecordPanel.setVisibility(View.GONE);
    }

    // 回调聊天界面的callback
    public interface PanelCallBack {
        // 拿到输入框
        EditText getInputEditText();

        // 返回需要发送的图片
        void onSendGallery(String[] paths);

        // 返回录音文件和时常
        void onRecordDone(File file, long time);
    }
}