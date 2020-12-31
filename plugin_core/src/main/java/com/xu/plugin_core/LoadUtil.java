package com.xu.plugin_core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by xutengfei
 * on 2020/12/24
 * 加载插件
 * 1、根据存储路径去加载插件
 */
@SuppressLint("StaticFieldLeak")
public class LoadUtil implements ILoadBack{
    //    private final static String apkPath = "/sdcard/plugin.apk";
    private String apkPath = "";
    private static PackageInfo packageArchiveInfo;
    private static LoadUtil loadUtil;
    private static Context mContext;

    public static LoadUtil getInstance(Context context) {
        synchronized (LoadUtil.class) {
            if (loadUtil == null) {
                try {
                    mContext = context;
                    loadUtil = new LoadUtil();
                    HookUtil hookUtil = new HookUtil(context, ProxyActivity.class);
                    hookUtil.hookStartActivity();
                    hookUtil.hookLaunchActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return loadUtil;
    }

    public void loadClass() {

        if (mContext == null) {
            return;
        }
        try {
            //第一步：先获取到宿主的dexElement[]
            PathClassLoader classLoader = (PathClassLoader) mContext.getClassLoader();
            //获取BaseDexClassLoader
            Class<?> baseDexClassLoaderClazz = Class.forName("dalvik.system.BaseDexClassLoader");
            //获取到成员变量
            Field pathList = baseDexClassLoaderClazz.getDeclaredField("pathList");
            pathList.setAccessible(true);
            //获取pathList在当前类加载器中的值
            Object pathListValue = pathList.get(classLoader);
            //获取pathList中的dexElements
            Field dexElements = pathListValue.getClass().getDeclaredField("dexElements");
            dexElements.setAccessible(true);

            Object dexElementValue = dexElements.get(pathListValue);
            //第二步：加载插件，获取插件类加载器中的dexElements
            DexClassLoader dexClassLoader = new DexClassLoader(apkPath, mContext.getCacheDir().getAbsolutePath(), null, mContext.getClassLoader());
            //获取到插件的pathList
            Object pluginPathListValue = pathList.get(dexClassLoader);
            //获取插件中的dexElements
            Object pluginDexElementsValue = dexElements.get(pluginPathListValue);

            //第三步：合并数组
            //获取长度
            int length = Array.getLength(dexElementValue);
            int pluginLength = Array.getLength(pluginDexElementsValue);
            int newLength = length + pluginLength;
            Class<?> componentType = dexElementValue.getClass().getComponentType();
            //创建新数组
            Object newArray = Array.newInstance(componentType, newLength);
            System.arraycopy(dexElementValue, 0, newArray, 0, length);
            System.arraycopy(pluginDexElementsValue, 0, newArray, length, pluginLength);
            //赋值给宿主类加载器
            dexElements.set(pathListValue, newArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    /**
//     * 创建一个管理对象，可以获取到插件的资源对象
//     *
//     * @return
//     */
//    public Resources loadPluginResource(String apkPath) {
//        this.apkPath = apkPath;
//        Resources resources = null;
//        try {
//            //获取到插件的包信息类
//            //获取到包管理器
//            PackageManager packageManager = mContext.getPackageManager();
//            packageArchiveInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
//            AssetManager assetManager = AssetManager.class.newInstance();
//            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
//            addAssetPath.setAccessible(true);
//            addAssetPath.invoke(assetManager, apkPath);
//            resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
//                    mContext.getResources().getConfiguration());
//            return resources;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 获取到包信息类
     *
     * @return
     */
    public PackageInfo getPackageArchiveInfo() {
        return packageArchiveInfo;
    }


    /**
     * 创建一个管理对象，可以获取到插件的资源对象
     * @return
     */
    @Override
    public Resources loadResources(String apkPath) {
        this.apkPath = apkPath;
        Resources resources = null;
        try {
            //获取到插件的包信息类
            //获取到包管理器
            PackageManager packageManager = mContext.getPackageManager();
            packageArchiveInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager, apkPath);
            resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
                    mContext.getResources().getConfiguration());
            return resources;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ILoadBack getILoadBack() {
        return this;
    }
}
