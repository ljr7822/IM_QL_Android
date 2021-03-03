package com.example.iwen.imqingliao.fragments.assist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.iwen.common.app.Application;
import com.example.iwen.imqingliao.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 动态获取权限的fragment
 *
 * @author : Iwen大大怪
 * create : 2020/11/15 2:38
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {
    // 权限回调标识
    private static final int RC = 0x0100;

    public PermissionsFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 返回一个我们复写的
        return new BottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 获取GalleryView
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        // 授权
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进行申请权限
                requestPerm();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 界面显示时进行刷新
        refreshState(getView());
    }

    /**
     * 刷新我们布局中的图片的状态
     *
     * @param root 根布局
     */
    private void refreshState(View root) {
        if (root == null)
            return;
        Context context = getContext();
        root.findViewById(R.id.im_state_permission_network).setVisibility(haveNetwork(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_read).setVisibility(haveReadPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_write).setVisibility(haveWritePerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_record_audio).setVisibility(haveRecordAudioPerm(context) ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取是否有网络权限
     *
     * @param context 上下文
     * @return true代表有
     */
    private static boolean haveNetwork(Context context) {
        // 需要检测的权限
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有读取文件权限
     *
     * @param context 上下文
     * @return true代表有
     */
    private static boolean haveReadPerm(Context context) {
        // 需要检测的权限
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有写文件权限
     *
     * @param context 上下文
     * @return true代表有
     */
    private static boolean haveWritePerm(Context context) {
        // 需要检测的权限
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有麦克风权限
     *
     * @param context 上下文
     * @return true代表有
     */
    private static boolean haveRecordAudioPerm(Context context) {
        // 需要检测的权限
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 私有的show方法
     *
     * @param manager FragmentManager
     */
    private static void show(FragmentManager manager) {
        // 调用 BottomSheetDialogFragment
        new PermissionsFragment()
                .show(manager, PermissionsFragment.class.getName());
    }

    /**
     * 检测是否具有所有权限
     *
     * @param context 上下文
     * @param manager FragmentManager
     * @return 是否有权限
     */
    public static boolean haveAll(Context context, FragmentManager manager) {
        // 检测是否具有所有权限
        boolean haveAll = haveNetwork(context) && haveReadPerm(context) && haveWritePerm(context) && haveRecordAudioPerm(context);
        // 没有则显示当前申请权限的界面
        if (!haveAll) {
            show(manager);
        }
        return haveAll;
    }

    /**
     * 申请权限方法
     */
    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

        };
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Application.showToast(R.string.label_permission_ok);
            // Fragment 中调度getView得到根布局，前提是在onCreateView方法之后
            refreshState(getView());
        } else {
            // 没有权限就进行申请
            EasyPermissions.requestPermissions(this,
                    getString(R.string.title_assist_permissions),
                    RC, perms);
        }
    }


    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        // 如果权限存在没有申请成功的，则弹出弹出框，用户点击后去到设置页面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("未获取到权限")
                    .setRationale("您可以到设置中手动配置权限,点击确定前往设置")
                    .build().show();
        }
    }

    /**
     * 权限申请回调的方法，这个方法中把对应权限申请状态交给EasyPermissions框架
     *
     * @param requestCode  int
     * @param permissions  String[]
     * @param grantResults int[]
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应参数，并告知处理接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}