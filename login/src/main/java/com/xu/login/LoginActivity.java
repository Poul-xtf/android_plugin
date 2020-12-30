package com.xu.login;


import android.os.Bundle;
import android.view.View;

import com.xu.plugin_core.BaseActivity;
import com.xu.plugin_core.event.MessEvent;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void sendMess(View view) {
        EventBus.getDefault().post(new MessEvent("111"));
    }
}