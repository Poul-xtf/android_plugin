package com.xu.artifactorydemo.proxy_test;

import android.util.Log;

/**
 * Created by xutengfei
 * on 2020/12/24
 */
public class ProxyTest implements ProxyInterface{
    @Override
    public void getLog(String str) {
        Log.e("xtf->","我在mainActivity中调用了getLog");
    }
}
