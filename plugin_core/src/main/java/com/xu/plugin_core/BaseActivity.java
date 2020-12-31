package com.xu.plugin_core;

import android.app.Activity;
import android.content.res.Resources;

/**
 * Created by xutengfei
 * on 2020/12/25
 * 插件是宿主的一部分
 */

public class BaseActivity extends Activity {

    @Override
    public Resources getResources() {
        return getApplication() != null && getApplication().getResources() != null
                ? getApplication().getResources() : super.getResources();
    }
}
