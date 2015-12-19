package com.letb.museek.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.R;

import java.util.ArrayList;

import co.mobiwise.library.AnimationCompleteListener;
import co.mobiwise.library.MaskProgressView;
import co.mobiwise.library.OnProgressDraggedListener;

/**
 * Created by marina.titova on 13.12.15.
 */
public class PlayerFragment extends Fragment {

    public static final String TRACK_LIST = "TRACK_LIST";

    private Track currentTrack;
    MaskProgressView maskProgressView;

    public PlayerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        if (getArguments() != null)
            currentTrack = (Track) getArguments().getSerializable(TRACK_LIST);
        maskProgressView = (MaskProgressView) view.findViewById(R.id.maskProgressView);
        maskProgressView.setmMaxSeconds(currentTrack.getData().getmLength());
        maskProgressView.start();

        maskProgressView.setOnProgressDraggedListener(new OnProgressDraggedListener() {
            @Override
            public void onProgressDragged(int position) {

            }

            @Override
            public void onProgressDragging(int position) {

            }
        });
        maskProgressView.setAnimationCompleteListener(new AnimationCompleteListener() {
            @Override
            public void onAnimationCompleted() {

            }
        });
//        maskProgressView.setmMaxSeconds(musicArrayList.get(index).durationInSeconds);
//        maskProgressView.setCoverImage(musicArrayList.get(index).coverImage);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
