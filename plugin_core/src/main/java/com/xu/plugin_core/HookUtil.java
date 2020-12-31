package com.xu.plugin_core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by xutengfei
 * on 2020/12/24
 * Let us use Hook to realize that unregistered activity can also be started
 * 1. First reflect to the AMS instance, and then create a dynamic proxy object
 */
public class HookUtil {
    private Context context;
    //activity代理
    private Class<? extends Activity> mProxyActivity;
    private static final String EXTRA_ORIGIN_INTENT = "EXTRA_ORIGIN_INTENT";

    public HookUtil(Context context, Class<? extends Activity> mProxyActivity) {
        this.context = context;
        this.mProxyActivity = mProxyActivity;
    }

    /**
     * HOOK AMS
     * 得到AMS实例,然后生成AMS的代理对象
     */
    public void hookStartActivity() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {//android版本低于8.0时
                Class<?> aClass = Class.forName("android.app.ActivityManagerNative");
                Field gDefault = aClass.getDeclaredField("gDefault");
                gDefault.setAccessible(true);
                //静态变量可以直接传null来获取
                Object gDefaultValue = gDefault.get(null);
                Class<?> singletonClass = Class.forName("android.util.Singleton");
                Field mInstance = singletonClass.getDeclaredField("mInstance");
                mInstance.setAccessible(true);
                //获取到AMS
                Object amsObject = mInstance.get(gDefaultValue);
                //创建AMS的代理对象
                Class<?> IActivityManagerClass = Class.forName("android.app.IActivityManager");
                Object amsProxy = Proxy.newProxyInstance(HookUtil.class.getClassLoader(), new Class[]{IActivityManagerClass},
                        new StartActivityInVocationHandler(amsObject));
                //通过反射将代理对象替换原来的AMS
                mInstance.set(gDefaultValue, amsProxy);
            } else {//8.0以上
                Class<?> aClass = Class.forName("android.app.ActivityManager");
                Field iActivityManagerSingletonField = aClass.getDeclaredField("IActivityManagerSingleton");
                iActivityManagerSingletonField.setAccessible(true);
                Object iActivityManagerSingletonValue = iActivityManagerSingletonField.get(null);

                Class<?> singletonClass = Class.forName("android.util.Singleton");
                Field mInstance = singletonClass.getDeclaredField("mInstance");
                mInstance.setAccessible(true);
                //获取到AMS
                Object amsObject = mInstance.get(iActivityManagerSingletonValue);
                //创建AMS的代理对象
                //首先获取到它的接口的Class对象
                Class<?> IActivityManagerClass = Class.forName("android.app.IActivityManager");
                Object amsProxy = Proxy.newProxyInstance(HookUtil.class.getClassLoader(), new Class[]{IActivityManagerClass},
                        new StartActivityInVocationHandler(amsObject));
                //通过反射将代理对象替换原来的AMS
                mInstance.set(iActivityManagerSingletonValue, amsProxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class StartActivityInVocationHandler implements InvocationHandler {
        private Object ams;

        public StartActivityInVocationHandler(Object ams) {
            this.ams = ams;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.e("method.name:", method.getName());
            if (method.getName().equals("startActivity")) {
                //获取startActivity方法中传过来的意图
                int position = 0;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        position = i;
                    }
                }
                Intent oldIntent = (Intent) args[position];
                //创建一个新的意图
                Intent newIntent = new Intent(context, mProxyActivity);
                //将旧的意图放入到新的意图
                newIntent.putExtra(EXTRA_ORIGIN_INTENT, oldIntent);
                args[position] = newIntent;
            }
            //调用了AMS的startActivity的方法
            return method.invoke(ams, args);
        }
    }

    public void hookLaunchActivity() throws Exception {
        //首先获取到activityThread的对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        //获取到这个类的实例SCurrentActivityThread
        Field sCurrentActivityThread = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThread.setAccessible(true);
        //获取到ActivityThread的实例
        Object activityThreadValue = sCurrentActivityThread.get(null);
        //获取到mH的成员变量
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        //获取到mH在ActivityThread中的变量
        Object mHValue = mHField.get(activityThreadValue);
        //获取到activityThread里面用来发送消息的Handler
        Class<?> handlerClass = Class.forName("android.os.Handler");
        //获取到Handler中用来处理消息的mCallBack变量
        Field mCallBackField = handlerClass.getDeclaredField("mCallback");
        mCallBackField.setAccessible(true);
        mCallBackField.set(mHValue, new HandlerCallBack());
    }

    private class HandlerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { //如果是9.0以上
                if (msg.what == 159) {
                    handlerLaunchActivityO(msg);
                }
            } else {
                if (msg.what == 100) {
                    handlerLaunchActivity(msg);
                }
            }
            return false;
        }

        private void handlerLaunchActivityO(Message msg) {
            try {
                Object obj = msg.obj;
                Field mActivityCallbacksField = obj.getClass().getDeclaredField("mActivityCallbacks");
                mActivityCallbacksField.setAccessible(true);
                List mActivityCallbacks = (List) mActivityCallbacksField.get(msg.obj);
                for (int i = 0; i < mActivityCallbacks.size(); i++) {
                    if (mActivityCallbacks.get(i).getClass().getName()
                            .equals("android.app.servertransaction.LaunchActivityItem")) {
                        Log.d("getActivity", "get到启动activity消息");
                        Object launchActivityItem = mActivityCallbacks.get(i);
                        Field mIntentField = launchActivityItem.getClass().getDeclaredField("mIntent");
                        mIntentField.setAccessible(true);
                        Intent realIntent = (Intent) mIntentField.get(launchActivityItem);
                        Intent oldIntent = realIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
                        if (oldIntent != null) {
                            realIntent.setComponent(oldIntent.getComponent());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handlerLaunchActivity(Message msg) {
            try {
                Object r = msg.obj;
                Field intentField = r.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                //从r实例中将intent这个变量的值拿出来
                Intent newIntent = (Intent) intentField.get(r);
                Intent oldIntent = newIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
                if (oldIntent != null)
                    intentField.set(r, oldIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
