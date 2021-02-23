package com.example.iwen.common.widget.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * TextWatcher 实现类
 *
 * @author iwen大大怪
 * Create to 2021/02/24 1:41
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

