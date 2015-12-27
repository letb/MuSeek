package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.TokenEventSuccess;
import com.letb.museek.RequestProcessor.AsynchronousRequestProcessor;
import com.letb.museek.Utils.UserInformer;

import de.greenrobot.event.EventBus;

public class SplashActivity extends BaseSpiceActivity {

    private final String TAG = "SplashActivity";

    private EventBus bus = EventBus.getDefault();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestToken();
    }

    @Override
    public void onResume () {
        bus.register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        bus.unregister(this);
        super.onPause();
    }

    public void requestToken() {
        AsynchronousRequestProcessor.startTokenRequestAction(this);
    }

    public void onEvent(TokenEventSuccess event){
        TokenHolder.setData(event.getData().getAccessToken(), event.getData().getExpiresIn());
        proceedToPlayList();
        Log.i(TAG, event.getData().getAccessToken());
    }

    public void onEvent(EventFail event) {
        UserInformer.showMessage(SplashActivity.this, event.getException());
    }

    private void proceedToPlayList () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
