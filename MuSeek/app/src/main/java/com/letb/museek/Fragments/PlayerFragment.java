package com.letb.museek.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;

import java.util.List;

import co.mobiwise.library.MaskProgressView;
import co.mobiwise.library.OnProgressDraggedListener;

/**
 * Created by marina.titova on 13.12.15.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    public static final String TRACK_LIST = "TRACK_LIST";

    private Integer NEXT = 1;
    private Integer PREV = -1;

    private Button buttonPlayPause;
    private Button buttonNext;
    private Button buttonPrevious;
    private OnMediaButtonClickListener mListener;

    private MaskProgressView maskProgressView;

    private List<Track> currentTrackList;
    private Integer trackIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        if (getArguments() != null)
            currentTrackList = (List<Track>) getArguments().getSerializable(TRACK_LIST);
        initializeProgressBar(view);
        initializeButtons(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonControl:
                togglePlay();
                break;
            case R.id.buttonNext:
                switchTrack(NEXT);
                break;
            case R.id.buttonPrevious:
                switchTrack(PREV);
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

    /**
     *
     * @param direction
     * -1 = previous track
     * +1 = next track
     * TODO: index check to be done in service
     */
    private void switchTrack(int direction) {
        trackIndex = trackIndex + direction;
        maskProgressView.stop();
        maskProgressView.setmMaxSeconds(currentTrackList.get(trackIndex).getData().getLength());
        maskProgressView.start();
        buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
    }

    private void togglePlay() {
        if (maskProgressView.isPlaying()) {
            buttonPlayPause.setBackgroundResource(R.drawable.icon_play);
            maskProgressView.pause();
        }
        else {
            buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
            maskProgressView.start();
        }
        mListener.onPlayPauseClicked(trackIndex);
    }

    private void initializeProgressBar(View view) {
        maskProgressView = (MaskProgressView) view.findViewById(R.id.maskProgressView);
        maskProgressView.setmMaxSeconds(currentTrackList.get(trackIndex).getData().getLength());
        maskProgressView.start();
        maskProgressView.setOnProgressDraggedListener(new CustomProgressDraggedListener());
    }

    private void initializeButtons(View view) {
        buttonPlayPause = (Button) view.findViewById(R.id.buttonControl);
        buttonNext = (Button) view.findViewById(R.id.buttonNext);
        buttonPrevious = (Button) view.findViewById(R.id.buttonPrevious);
        buttonPlayPause.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
    }

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

    private class CustomProgressDraggedListener implements OnProgressDraggedListener {
        @Override
        public void onProgressDragged(int position) {
            mListener.onPositionChanged(trackIndex, position);
        }

        @Override
        public void onProgressDragging(int position) {
        }
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
