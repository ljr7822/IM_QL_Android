package com.example.iwen.common.app;

import java.io.File;

/**
 *
 * author : Iwen大大怪
 * create : 2020/11/15 1:44
 */
public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取缓存文件夹地址
     * @return 当前App的缓存文件夹地址
     */
    public static File getCacheDirFile(){
        return instance.getCacheDir();
    }

    /**
     * 获取头像临时缓存地址
     *
     * @return 头像临时缓存地址
     */
    public static File getAvatarTempFile() {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "avatar");
        //创建所有的对应的文件夹
        dir.mkdirs();
        //删除旧的缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        //返回一个当前时间戳的目录文件地址
        File path = new File(dir, System.currentTimeMillis() + "jpg");
        return path.getAbsoluteFile();
    }
}
