package com.xu.plugin_core;

import android.app.Activity;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by xutengfei
 * on 2020/12/25
 * 插件是宿主的一部分
 */

public class BaseActivity extends Activity {
    @Override
    public Resources getResources() {
        if (getApplication() != null && getApplication().getResources() != null) {
            return getApplication().getResources();
        }
        return super.getResources();
    }
}
