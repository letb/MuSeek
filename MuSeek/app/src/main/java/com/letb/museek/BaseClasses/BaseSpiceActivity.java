package com.letb.museek.BaseClasses;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.letb.museek.R;
import com.letb.museek.Services.SpiceRequestService;
import com.octo.android.robospice.SpiceManager;

// Sliding Activity - это базовый класс для любого активити с красивым меню
// В джаве экстендить можно только один класс, поэтому придется экстендить рано
// Не работает пока меню не используется
//public abstract class BaseSpiceActivity extends SlidingActivity {
public abstract class BaseSpiceActivity extends AppCompatActivity  {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

}
