package com.xu.plugin_core;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by xutengfei
 * on 2020/12/25
 * Plug-in apk use
 */

public class BasePluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Resources getResources() {
        return getApplication() != null && getApplication().getResources() != null
                ? getApplication().getResources() : super.getResources();
    }
}
