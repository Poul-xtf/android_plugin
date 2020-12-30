package com.xu.base.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by xutengfei
 * on 2020/12/22
 */
public abstract class BaseActivity extends AppCompatActivity {
    private View rootView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView(rootView);
        initData();

    }

    protected void initData() {}

    protected abstract void initView(View rootView);

    protected abstract int getLayout();
}
