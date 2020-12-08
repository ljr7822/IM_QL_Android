package com.example.iwen.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.example.iwen.common.Common;
import com.example.iwen.common.utils.HashUtil;
import com.example.iwen.factory.Factory;

import java.io.File;
import java.util.Date;

/**
 * 上传工具类
 * 上传任意文件到阿里云oss存储
 * author : Iwen大大怪
 * create : 2020/11/15 13:37
 */
public class UploadHelper {
    private static final String TAG = "UploadHelper";
    // 与存储区域有关系
    private static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    // oss存储桶名字
    private static final String BUCKET_NAME = "imqingliao";

    private static OSS getClient() {
        // 在移动端建议使用STS的方式初始化OSSClient。
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                Common.Constance.AccessKey_ID,
                Common.Constance.AccessKey_Secret);
        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传文件，成功后返回一个路径
     *
     * @param objKey  上传后在服务器上独立的key
     * @param path 需要上传的文件的路径
     * @return 存储的地址
     */
    private static String upload(String objKey, String path) {
        // 构造上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objKey, path);
        try {
            // 初始化上传的client
            OSS client = getClient();
            // 开始同步上传
            client.putObject(request);
            // 得到一个外网可访问的url
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            Log.e(TAG, "上传后的文件URL: " + url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            // 如果有异常，返回空
            return e.toString();
        }
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path) {
        String key = getImageKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAvatar(String path) {
        String key = getAvatarKey(path);
        return upload(key, path);
    }

    /**
     * 上传音频
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAudio(String path) {
        String key = getAudioKey(path);
        return upload(key, path);
    }

    /**
     * 分月存储，避免一个文件夹文件太多
     *
     * @return YYYY-MM
     */
    private static String getDataString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    //image/202011/iwen.jpg
    private static String getImageKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String date = getDataString();
        return String.format("image/%s/%s.jpg", getDataString(), fileMd5);

    }

    //avatar/202011/iwen.jpg
    private static String getAvatarKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String date = getDataString();
        return String.format("avatar/%s/%s.jpg", getDataString(), fileMd5);

    }

    //audio/202011/iwen.mp3
    private static String getAudioKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String date = getDataString();
        return String.format("audio/%s/%s.mp3", getDataString(), fileMd5);
    }
}
