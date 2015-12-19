package com.letb.museek.BaseClasses;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.letb.museek.Services.SpiceRequestService;
import com.octo.android.robospice.SpiceManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public abstract class BaseSpiceService extends Service {

    private final String LOG_TAG = "BASE_SERVICE";
    private SpiceManager spiceManager = new SpiceManager( SpiceRequestService.class );
    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    public void onCreate() {
        spiceManager.start(this);
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public void onDestroy() {
        spiceManager.shouldStop();
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public BaseSpiceService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }
}
