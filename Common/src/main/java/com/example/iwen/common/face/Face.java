package com.example.iwen.common.face;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.Spannable;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.iwen.common.utils.StreamUtil;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 表情工具类
 *
 * @author iwen大大怪
 * @Create to 2021/04/30 18:33
 */
public class Face {
    // 全局的表情的映射ArrayMap，更加轻量级
    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();
    private static List<FaceTab> FACE_TABS = null;

    /**
     * 初始化方法
     *
     * @param context 上下文
     */
    private static void init(Context context) {
        if (FACE_TABS == null) {
            // 开始初始化
            synchronized (Face.class) {
                if (FACE_TABS == null) {
                    ArrayList<FaceTab> faceTabs = new ArrayList<>();
                    FaceTab tab = initResourceFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    }

                    tab = initAssetsFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    }
                    // init map
                    for (FaceTab faceTab : faceTabs) {
                        faceTab.copyToMap(FACE_MAP);
                    }
                    // init list 不可变封装
                    FACE_TABS = Collections.unmodifiableList(faceTabs);
                }
            }
        }
    }

    /**
     * 从 face—t.zip 中解析我们的表情
     *
     * @param context 上下文
     * @return FaceTab
     */
    private static FaceTab initAssetsFace(Context context) {
        String faceAsset = "face-t.zip";
        // 构建缓存目录
        // data/data/包名/files/face/ft/*
        String faceCacheDir = String.format("%s/face/tf", context.getFilesDir());
        File faceFolder = new File(faceCacheDir);
        if (!faceFolder.exists()) {
            // 不存在进行初始化
            if (faceFolder.mkdirs()) {
                try {
                    // 拿到输入流
                    InputStream inputStream = context.getAssets().open(faceAsset);
                    // 构建一个存储文件
                    File faceSource = new File(faceFolder, "source.zip");
                    // copy
                    StreamUtil.copy(inputStream, faceSource);
                    // 解压
                    unZipFile(faceSource, faceFolder);
                    // 清理文件
                    StreamUtil.delete(faceSource.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 找到 info.json 文件
        File infoFile = new File(faceCacheDir, "info.json");
        // Gson解析
        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = gson.newJsonReader(new FileReader(infoFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        // 解析
        FaceTab tab = gson.fromJson(reader, FaceTab.class);
        // 相对路径到绝对路径
        for (Bean face : tab.faces) {
            face.preview = String.format("%s/%s", faceCacheDir, face.preview);
            face.source = String.format("%s/%s", faceCacheDir, face.source);
        }
        return tab;
    }

    /**
     * 把zipFile解压到desDir目录
     *
     * @param zipFile 开始文件
     * @param desDir  解压后文件
     * @throws IOException 异常
     */
    private static void unZipFile(File zipFile, File desDir) throws IOException {
        final String folderPath = desDir.getAbsolutePath();
        ZipFile zf = new ZipFile(zipFile);
        // 判断节点进行循环
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 过滤缓存的文件
            String name = entry.getName();
            if (name.startsWith(".")) {
                continue;
            }
            // 输入流
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + name;
            // 防止名字错乱
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            // 输出文件
            StreamUtil.copy(in, desFile);
        }
    }

    /**
     * 从drawable资源中加载数据并映射到对应的key
     *
     * @param context 上下文
     * @return FaceTab
     */
    private static FaceTab initResourceFace(Context context) {
        final ArrayList<Bean> faces = new ArrayList<>();
        // 拿到资源
        final Resources resources = context.getResources();
        // 通过反射拿到资源
        String packageName = context.getApplicationInfo().packageName;
        for (int i = 1; i <= 142; i++) {
            // i=1---> 001
            String key = String.format(Locale.ENGLISH, "fb%03d", i);
            String resStr = String.format(Locale.ENGLISH, "face_base_%03d", i);
            // 根据资源名称去拿资源对应的id
            int resId = resources.getIdentifier(resStr, "drawable", packageName);
            if (resId == 0) {
                continue;
            }
            // 添加表情
            faces.add(new Bean(key, resId));
        }

        if (faces.size() == 0) {
            return null;
        }
        return new FaceTab("NAME", faces.get(0).preview, faces);
    }

    /**
     * 拉取表情
     *
     * @param context 上下文
     * @return 表情列表
     */
    public static List<FaceTab> all(@NonNull Context context) {
        init(context);
        return FACE_TABS;
    }

    /**
     * 输入表情到editable
     *
     * @param context  上下文
     * @param editable 输入框
     * @param bean     表情bean
     * @param size     表情占用大小
     */
    public static void inputFace(@NonNull Context context, final Editable editable, final Face.Bean bean, final int size) {

    }

    /**
     * 从spannable解析表情并替换显示
     *
     * @param target    具体的view
     * @param spannable 文字
     * @param size      大小
     * @return List<FaceTab>
     */
    public static List<FaceTab> decode(@NonNull View target, final Spannable spannable, final int size) {
        return null;
    }

    /**
     * 每一个表情盘，含有很多表情
     */
    public static class FaceTab {
        public List<Bean> faces;
        public String name;
        // 预览图, 包括了drawable下面的资源int类型
        public Object preview;

        FaceTab(String name, Object preview, List<Bean> faces) {
            this.faces = faces;
            this.name = name;
            this.preview = preview;
        }

        // 添加到Map
        void copyToMap(ArrayMap<String, Bean> faceMap) {
            for (Bean face : faces) {
                faceMap.put(face.key, face);
            }
        }
    }

    /**
     * 每一个表情
     */
    public static class Bean {
        Bean(String key, int preview) {
            this.key = key;
            this.source = preview;
            this.preview = preview;
        }

        public String key;
        public String desc;
        public Object source;
        public Object preview;
    }
}
