package com.letb.museek.Fragments;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letb.museek.Events.PlayerEvents.PlayerResponseEvent;
import com.letb.museek.Events.PlayerEvents.RewindTractToPositionRequest;
import com.letb.museek.Events.PlayerEvents.SwitchTrackRequest;
import com.letb.museek.Events.PlayerEvents.TogglePlayPauseRequest;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;
import com.letb.museek.Services.MediaPlayerService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

    public static final String TAG = "PlayerFragment";


    private Integer NEXT = 1;
    private Integer PREV = -1;

    private Button buttonPlayPause;
    private Button buttonNext;
    private Button buttonPrevious;
    private TextView titleView;
    private TextView artistView;
    private ImageView headerView;

    private EventBus bus = EventBus.getDefault();

    private MaskProgressView maskProgressView;

    private List<Track> currentTrackList;
    private Integer currentTrackIndex = 0;

    private ProgressBar spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        if (getArguments() != null) {
            currentTrackList = (List<Track>) getArguments().getSerializable(TRACK_LIST);
            currentTrackIndex = getArguments().getInt(CURRENT_TRACK);
        }
        spinner = (ProgressBar) view.findViewById(R.id.playerProgress);
        spinner.setVisibility(View.VISIBLE);

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
        bus.post(new TogglePlayPauseRequest());
    }

    private void initializeProgressBar(View view) {
        maskProgressView = (MaskProgressView) view.findViewById(R.id.maskProgressView);
        maskProgressView.setmMaxSeconds(currentTrackList.get(currentTrackIndex).getData().getLength());
        maskProgressView.setOnProgressDraggedListener(new CustomProgressDraggedListener());

        headerView = (ImageView) view.findViewById(R.id.imageviewHeader);
    }

    private void initializeLayout(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(currentTrackList.get(currentTrackIndex).getData().getTrack());

        artistView = (TextView) view.findViewById(R.id.artistView);
        artistView.setText(currentTrackList.get(currentTrackIndex).getData().getArtist());
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
    }

    private class CustomProgressDraggedListener implements OnProgressDraggedListener {
        @Override
        public void onProgressDragged(int position) {
            bus.post(new RewindTractToPositionRequest(position));
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
        titleView.setText(currentTrackList.get(currentTrackIndex).getData().getTrack());
        artistView.setText(currentTrackList.get(currentTrackIndex).getData().getArtist());
        maskProgressView.setmMaxSeconds(currentTrackList.get(currentTrackIndex).getData().getLength());
        Picasso.with(getActivity())
        .load(currentTrackList.get(currentTrackIndex).getPic())
        .into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                maskProgressView.setCoverImage(bitmap);
                headerView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                maskProgressView.setCoverImage(BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.album));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    public void onEvent(PlayerResponseEvent event){
        currentTrackIndex = event.getTrackIndex();
        currentTrackList = event.getCurrentTrackList();
        redrawLayout();
        MediaPlayerService.State currentState = event.getState();

        switch (currentState) {
            case Retrieving:
                spinner.setVisibility(View.VISIBLE);
                break;
            case Stopped:
                maskProgressView.stop();
                buttonPlayPause.setBackgroundResource(R.drawable.icon_play);
                break;
            case Preparing:
                spinner.setVisibility(View.VISIBLE);
                break;
            case Playing:
                maskProgressView.start();
                buttonPlayPause.setBackgroundResource(R.drawable.icon_pause);
                spinner.setVisibility(View.GONE);
                break;
            case Paused:
                maskProgressView.pause();
                buttonPlayPause.setBackgroundResource(R.drawable.icon_play);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
