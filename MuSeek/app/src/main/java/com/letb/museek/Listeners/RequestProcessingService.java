package com.letb.museek.Listeners;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Models.Token;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.TokenRequest;
import com.letb.museek.Requests.TrackRequest;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RequestProcessingService extends BaseSpiceService {

    public static final String BROADCAST_TOKEN_REQUEST = "com.letb.museek.Listeners.broadcast.token.request";
    public static final String BROADCAST_TOKEN_REQUEST_PARAMETER = "com.letb.museek.Listeners.broadcast.token.request.parameter";
    public static final String BROADCAST_TOKEN_REQUEST_ANSWER = "com.letb.museek.Listeners.broadcast.token.request.answer";

    private static final String BROADCAST_TRACK_REQUEST = "com.letb.museek.Listeners.broadcast.track.request";

    private static final String ACTION_TOKEN_REQUEST = "com.letb.museek.Listeners.action.token.request";
    private static final String ACTION_TRACK_REQUEST = "com.letb.museek.Listeners.action.track.request";

    private static final String TRACK_ID = "com.letb.museek.Listeners.extra.track.id";


    public static void startTokenRequestAction(Context context) {
        Intent intent = new Intent(context, RequestProcessingService.class);
        intent.setAction(ACTION_TOKEN_REQUEST);
        context.startService(intent);
    }


    public static void startTrackRequestAction(Context context, String trackId) {
        Intent intent = new Intent(context, RequestProcessingService.class);
        intent.setAction(ACTION_TRACK_REQUEST);
        intent.putExtra(TRACK_ID, trackId);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TOKEN_REQUEST.equals(action)) {
                handleTokenRequest();
            } else if (ACTION_TRACK_REQUEST.equals(action)) {
                final String trackId = intent.getStringExtra(TRACK_ID);
                handelSingleTrackRequest(trackId);
            }
        }
        return START_STICKY;
    }

    private void handleTokenRequest() {
        TokenRequest tokenRequest = new TokenRequest(Token.authHTTPHeader);
        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessingService.TokenRequestListener());
    }

    private void handelSingleTrackRequest(String trackId) {
        TrackRequest trackRequest = new TrackRequest(TokenHolder.getAccessToken(), trackId, "listen");
        getSpiceManager().execute(trackRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessingService.TrackRequestListener());
    }

    public final class TokenRequestListener implements RequestListener<Token> {

        private final String SUCCESS_RESULT = "SUCCESS";
        private final String FAIL_RESULT = "FAIL";



        @Override
        public void onRequestFailure(SpiceException spiceException) {
        }

        @Override
        public void onRequestSuccess(final Token result) {
            TokenHolder.setAccessToken(result.getAccessToken());
            TokenHolder.setExpiresIn(result.getExpiresIn());
    //        requestTrack(result.getAccessToken(), "4425964VcAZ", "listen");
        }
    }

    public static final class TrackRequestListener implements RequestListener<Track> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
        }

        @Override
        public void onRequestSuccess(final Track result) {
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
