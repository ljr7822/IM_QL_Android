package com.example.iwen.imqingliao.fragments.account;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.iwen.common.app.Fragment;
import com.example.iwen.imqingliao.R;

/**
 * 注册的fragment
 */
public class RegisterFragment extends Fragment {
    private AccountTrigger mAccountTrigger;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }
}