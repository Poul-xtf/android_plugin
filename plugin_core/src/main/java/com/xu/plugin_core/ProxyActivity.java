package com.xu.plugin_core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Created by xutengfei
 * on 2020/12/25
 */
public class ProxyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        Button tv_name = findViewById(R.id.tv_name);
        tv_name.setOnClickListener(v -> {
            Toast.makeText(this,"这是代理activity",Toast.LENGTH_SHORT).show();
        });
    }
}
