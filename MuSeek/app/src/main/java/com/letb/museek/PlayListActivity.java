//package com.letb.museek;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//import android.widget.Toast;
//
//import com.letb.museek.Fragments.PlaylistFragment;
//import com.letb.museek.Models.Track.Track;
//import com.letb.museek.Services.MediaPlayerService;
//
//import java.util.List;
//
//public class PlayListActivity extends BaseSpiceActivity  implements PlaylistFragment.OnTrackSelectedListener {
//
//    private MediaPlayerService mediaPlayerService;
//    private Intent playIntent;
//    private boolean mediaPlayerBound = false;
//    private List<Track> mListItems;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mListItems = (List<Track>) getIntent().getExtras().getSerializable(PlaylistFragment.TRACK_LIST);
//        PlaylistFragment playlistFragment = new PlaylistFragment();
//        playlistFragment.setArguments(getIntent().getExtras());
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(android.R.id.content, playlistFragment);
//        ft.commit();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (playIntent == null) {
//            playIntent = new Intent(this, MediaPlayerService.class);
//            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
//            startService(playIntent);
//        }
//    }
//
//    @Override
//    public void onTrackSelected(String title) {
//        Toast.makeText(PlayListActivity.this, title, Toast.LENGTH_SHORT).show();
//    }
//
////    TODO: Как это работает??
////    Смерджить этот и предыдущий методы - и будет счастье
//    public void songPicked(View view) {
//        // todo: change view get tag to smth useful
//        mediaPlayerService.setSong(Integer.parseInt(view.getTag().toString()));
//        mediaPlayerService.playSong();
//    }
//
////    TODO: Когда это происходит?
//    //connect to the service
//    private ServiceConnection musicConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MediaPlayerService.Binder binder = (MediaPlayerService.Binder) service;
//            mediaPlayerService = binder.getService();
//            mediaPlayerService.setSongList(mListItems);
//            mediaPlayerBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mediaPlayerBound = false;
//        }
//    };
//}
