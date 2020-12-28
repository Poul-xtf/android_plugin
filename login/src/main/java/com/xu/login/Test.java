package com.xu.login;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xutengfei
 * on 2020/12/24
 */
public class Test {
    public void getText(Context context) {
        Toast.makeText(context, "我是插件中的类方法，我被加载" +
                "调用了", Toast.LENGTH_SHORT).show();
    }
}
