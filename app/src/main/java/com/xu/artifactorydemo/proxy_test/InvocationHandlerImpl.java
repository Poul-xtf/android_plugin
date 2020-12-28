package com.xu.artifactorydemo.proxy_test;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by xutengfei
 * on 2020/12/24
 */
public class InvocationHandlerImpl implements InvocationHandler {
    //要代理的对象
    private ProxyInterface proxy;

    public InvocationHandlerImpl(ProxyInterface proxy) {
        this.proxy = proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (Object arg : args) {
            Log.e("object:",arg.toString());
        }
        //通过动态代理改变原本打印的信息
        String Message = "这是动态代理改变之后的打印信息";
        args[0] = Message;
        Object invoke = method.invoke(proxy,args);
        return invoke;
    }
}
