package com.letb.museek.Services;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by marina.titova on 13.12.15.
 */
public class MediaPlayerService {

    public MediaPlayerService() {}

    public void test() throws IOException {

        String url = "http://s5-3.pleer.com/0042febee47d4ec86f21cce4ca00b43d90b2a22f2ba9d2a7f6174e87a94b905dd4669a656432ce965f49ccd85f4b83435faa027b32d62204cd77cb/41f17f7d29.mp3"; // your URL here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
