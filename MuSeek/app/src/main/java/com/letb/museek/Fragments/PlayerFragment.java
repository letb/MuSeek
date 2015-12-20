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
import android.widget.Button;
import android.widget.Toast;

import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.R;

import java.util.ArrayList;
import java.util.List;

import co.mobiwise.library.AnimationCompleteListener;
import co.mobiwise.library.MaskProgressView;
import co.mobiwise.library.OnProgressDraggedListener;

/**
 * Created by marina.titova on 13.12.15.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    public static final String TRACK_LIST = "TRACK_LIST";
    private Button buttonPlayPause;
    private Button buttonNext;
    private Button buttonPrevious;
    private List<Track> currentTrackList;
    private Integer index = 0;
    private MaskProgressView maskProgressView;
    private OnMediaButtonClickListener mListener;

    public PlayerFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMediaButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTranslateAreaListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        if (getArguments() != null)
            currentTrackList = (List<Track>) getArguments().getSerializable(TRACK_LIST);
        maskProgressView = (MaskProgressView) view.findViewById(R.id.maskProgressView);
        maskProgressView.setmMaxSeconds(currentTrackList.get(index).getData().getLength());
        maskProgressView.start();

        maskProgressView.setOnProgressDraggedListener(new OnProgressDraggedListener() {
            @Override
            public void onProgressDragged(int position) {
                mListener.onPositionChanged(index, position);
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
        buttonPlayPause = (Button) view.findViewById(R.id.buttonControl);
        buttonNext = (Button) view.findViewById(R.id.buttonNext);
        buttonPrevious = (Button) view.findViewById(R.id.buttonPrevious);
        buttonPlayPause.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonControl:
                if(maskProgressView.isPlaying()){
                    buttonPlayPause.setBackgroundResource(R.drawable.icon_play);
                    maskProgressView.pause();
                }
                else{
                    buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
                    maskProgressView.start();
                }
                mListener.onPlayPauseClicked(index);

                break;
            case R.id.buttonNext:
                if(index < currentTrackList.size() - 1)
                    index = index + 1;

                maskProgressView.stop();

//                maskProgressView.setmMaxSeconds(currentTrackList.get(index).durationInSeconds);
//                maskProgressView.setCoverImage(currentTrackList.get(index).coverImage);
                maskProgressView.start();

                buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);

                break;
            case R.id.buttonPrevious:

                if(index > 0)
                    index = index - 1;

//                maskProgressView.setmMaxSeconds(currentTrackList.get(index).durationInSeconds);
//                maskProgressView.setCoverImage(currentTrackList.get(index).coverImage);
                maskProgressView.start();
                buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);

                break;
            default:
                break;
        }
    }

    public interface OnMediaButtonClickListener {
        void onPlayPauseClicked(Integer index);
        void onNextClicked(Integer index);
        void onPrevClicked(Integer index);
        void onPositionChanged(Integer index, Integer position);
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
