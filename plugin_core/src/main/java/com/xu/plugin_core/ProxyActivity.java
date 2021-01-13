package com.xu.plugin_core;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Created by xutengfei
 * on 2020/12/25
 */
public class ProxyActivity extends BasePluginActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        Button tv_name = findViewById(R.id.tv_name);
        tv_name.setOnClickListener(v -> {
            Toast.makeText(this,"proxyActivity",Toast.LENGTH_SHORT).show();
        });
    }
}
