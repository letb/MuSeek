package com.letb.museek.Fragments;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.letb.museek.Events.PlayerEvents.PlayerResponseEvent;
import com.letb.museek.Events.PlayerEvents.SwitchTrackRequest;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;
import com.letb.museek.Services.MediaPlayerService;

import java.util.List;

import co.mobiwise.library.MaskProgressView;
import co.mobiwise.library.OnProgressDraggedListener;
import de.greenrobot.event.EventBus;

/**
 * Created by marina.titova on 13.12.15.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    public static final String TRACK_LIST = "TRACK_LIST";
    public static final String CURRENT_TRACK = "CURRENT_TRACK";


    private Integer NEXT = 1;
    private Integer PREV = -1;

    private Button buttonPlayPause;
    private Button buttonNext;
    private Button buttonPrevious;
    private OnMediaButtonClickListener mListener;
    private TextView titleView;
    private EventBus bus = EventBus.getDefault();

    private MaskProgressView maskProgressView;

    private List<Track> currentTrackList;
    private Integer currentTrackIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        if (getArguments() != null) {
            currentTrackList = (List<Track>) getArguments().getSerializable(TRACK_LIST);
            currentTrackIndex = getArguments().getInt(CURRENT_TRACK);
        }
        initializeProgressBar(view);
        initializeButtons(view);
        initializeLayout(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonControl:
                togglePlayPause();
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
        void onPositionChanged(Integer index, Integer position);
    }

    /**
     *
     * @param direction
     * -1 = previous track
     * +1 = next track
     */
    private void switchTrack(int direction) {
        maskProgressView.stop();
        buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
        bus.post(new SwitchTrackRequest(direction));
    }

    private void togglePlayPause() {
        if (maskProgressView.isPlaying()) {
            buttonPlayPause.setBackgroundResource(R.drawable.icon_play);
            maskProgressView.pause();
        }
        else {
            buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
            maskProgressView.start();
        }
        mListener.onPlayPauseClicked(currentTrackIndex);
    }

    private void initializeProgressBar(View view) {
        maskProgressView = (MaskProgressView) view.findViewById(R.id.maskProgressView);
        maskProgressView.setmMaxSeconds(currentTrackList.get(currentTrackIndex).getData().getLength());
        maskProgressView.setOnProgressDraggedListener(new CustomProgressDraggedListener());
    }

    private void initializeLayout(View view) {
        titleView = (TextView) view.findViewById(R.id.textSinger);
        titleView.setText(currentTrackList.get(currentTrackIndex).getTitle());
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
            mListener.onPositionChanged(currentTrackIndex, position);
        }

        @Override
        public void onProgressDragging(int position) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    private void redrawLayout () {
        titleView.setText(currentTrackList.get(currentTrackIndex).getTitle());
        maskProgressView.setmMaxSeconds(currentTrackList.get(currentTrackIndex).getData().getLength());
    }

    public void onEvent(PlayerResponseEvent event){
        currentTrackIndex = event.getTrackIndex();
        redrawLayout();
        if (event.getState() == MediaPlayerService.State.Paused) {
            maskProgressView.pause();
            buttonPlayPause.setBackgroundResource(R.drawable.icon_play);
        }
        else if (event.getState() == MediaPlayerService.State.Playing) {
            maskProgressView.start();
            buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
    }
}
