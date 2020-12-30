package com.xu.plugin_core;

import android.content.res.Resources;

/**
 * Created by xutengfei
 * on 2020/12/30
 */
public abstract class BaseLoadApkManager {
    public LoadUtil loadInstance;
    public Resources resources;
    public String apkPath;

    public BaseLoadApkManager(LoadUtil loadInstance) {
        this.loadInstance = loadInstance;
    }

    protected abstract Resources getResources();
}
