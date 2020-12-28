package com.xu.artifactorydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;

import com.xu.artifactorydemo.databinding.ActivityMainBinding;
import com.xu.artifactorydemo.proxy_test.InvocationHandlerImpl;
import com.xu.artifactorydemo.proxy_test.ProxyInterface;
import com.xu.artifactorydemo.proxy_test.ProxyTest;
import com.xu.plugin_core.LoadUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class MainActivity extends AppCompatActivity {

    private MyBean newListBean;
    private ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        newListBean = new MyBean("调用插件中的方法", "你好吗", "", "", "");
        dataBinding.setViewModel(newListBean);
//        dataBinding.setVariable(BR.name, "");
        dataBinding.tvName.setOnClickListener(v -> getPluginMethod());
    }

    public void getPluginMethod() {
        MyApplication.loadUtil.loadClass();
//        getPluginClass();
        jumpActivity();
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

}