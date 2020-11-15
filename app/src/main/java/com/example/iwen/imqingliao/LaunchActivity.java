package com.example.iwen.imqingliao;

import com.example.iwen.common.app.Activity;
import com.example.iwen.imqingliao.activities.MainActivity;
import com.example.iwen.imqingliao.fragments.assist.PermissionsFragment;

public class LaunchActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionsFragment.haveAll(this,getSupportFragmentManager())){
            MainActivity.show(this);
            finish();
        }
    }
}