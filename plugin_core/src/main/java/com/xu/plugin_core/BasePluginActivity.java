package com.xu.plugin_core;

import android.app.Activity;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by xutengfei
 * on 2020/12/25
 * Plug-in apk use
 */

public class BasePluginActivity extends AppCompatActivity {

    @Override
    public Resources getResources() {
        return getApplication() != null && getApplication().getResources() != null
                ? getApplication().getResources() : super.getResources();
    }
}
