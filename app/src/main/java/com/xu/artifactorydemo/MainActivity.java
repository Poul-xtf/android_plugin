package com.xu.artifactorydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import com.xu.artifactorydemo.databinding.ActivityMainBinding;

import com.xu.plugin_core.LoadApkManager;
import com.xu.plugin_core.event.MessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



public class MainActivity extends AppCompatActivity {

    private MyBean newListBean;
    private ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        newListBean = new MyBean("调用插件中的方法", "你好吗", "", "", "");
        dataBinding.setViewModel(newListBean);
        dataBinding.tvName.setOnClickListener(v -> getPluginMethod());
    }

    public void getPluginMethod() {
//        getPluginClass();
        try {
            int i = Runtime.getRuntime().availableProcessors();
            Log.e("最大线程数", i + "");
            Runtime rt = Runtime.getRuntime();
            long maxMemory = rt.maxMemory();
            Log.e("maxMemory:", Long.toString(maxMemory / (1024 * 1024)));
            MyApplication.resources = LoadApkManager.getLoadApkManager().loadApk("/sdcard/login-debug.apk");
            jumpActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jumpActivity() {
        //获取到插件的第一个activityName
//        ActivityInfo[] activities =  MyApplication.loadUtil.getPackageArchiveInfo().activities;
//        String activityName = activities[0].name;
        try {
            //跳转activity要做的事情
            //1、AMS要检查目的地activity是否注册了清单
            //2、AMS要通知activityThread来创建目的地的类然后去启动生命周期
            Class<?> aClass = getClassLoader().loadClass("com.xu.login.LoginActivity");
            startActivity(new Intent(this, aClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private void getPluginClass() {
//        try {
//            Class<?> aClass = getClassLoader().loadClass("com.xu.login.Test");
//            Method getToast = aClass.getDeclaredMethod("getText", Context.class);
//            getToast.setAccessible(true);
//            getToast.invoke(aClass.newInstance(), getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void proxyTest() {
//        ProxyTest proxyTest = new ProxyTest();
//        //生成一个代理对象
//        ProxyInterface newProxyInstance = (ProxyInterface) Proxy.newProxyInstance(getClassLoader()
//                , new Class[]{ProxyInterface.class}
//                , new InvocationHandlerImpl(proxyTest));
//        newProxyInstance.getLog("我在mainActivity中调用了getLog");
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMess(MessEvent messEvent) {
        Log.e("xtf->", messEvent.type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}