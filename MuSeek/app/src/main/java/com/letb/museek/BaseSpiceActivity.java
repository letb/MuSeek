package com.letb.museek;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.letb.museek.Services.SpiceRequestService;
import com.octo.android.robospice.SpiceManager;

// Sliding Activity - это базовый класс для любого активити с красивым меню
// В джаве экстендить можно только один класс, поэтому придется экстендить рано
// Не работает пока меню не используется
//public abstract class BaseSpiceActivity extends SlidingActivity {
public abstract class BaseSpiceActivity extends Activity {

    private SpiceManager spiceManager = new SpiceManager( SpiceRequestService.class );

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

}
