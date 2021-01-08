package com.xu.login;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xu.plugin_core.BasePluginActivity;
import com.xu.plugin_core.event.MessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BasePluginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_login);
    }

    public void sendMess(View view) {
        EventBus.getDefault().post(new MessEvent("111"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMess(MessEvent messEvent) {
        Log.e("xtf->//", messEvent.type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}