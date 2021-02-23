package com.example.iwen.common.widget;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 修改LinearLayout的fitsSystemWindows属性
 * 使其能够保持沉浸式状态栏和弹出输入框
 *
 * @author iwen大大怪
 * Create to 2021/02/24 1:10
 */
public class MessageLayout extends LinearLayout {
    public MessageLayout(Context context) {
        super(context);
    }

    public MessageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MessageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            insets.left = 0;
            insets.right = 0;
            insets.top = 0;
        }
        return super.fitSystemWindows(insets);
    }
}
