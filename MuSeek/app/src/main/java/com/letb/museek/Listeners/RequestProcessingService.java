package com.letb.museek.Listeners;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.TokenEventSuccess;
import com.letb.museek.Events.TrackEventSuccess;
import com.letb.museek.Models.Token;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.TokenRequest;
import com.letb.museek.Requests.TrackRequest;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.greenrobot.event.EventBus;

public class RequestProcessingService extends BaseSpiceService {
    private static final String ACTION_TOKEN_REQUEST = "com.letb.museek.Listeners.action.token.request";
    private static final String ACTION_TRACK_REQUEST = "com.letb.museek.Listeners.action.track.request";

    private static final String TRACK_ID = "com.letb.museek.Listeners.extra.track.id";
    private static final String TRACK_REASON = "com.letb.museek.Listeners.extra.track.reason";
    private EventBus bus = EventBus.getDefault();

    public static void startTokenRequestAction(Context context) {
        Intent intent = new Intent(context, RequestProcessingService.class);
        intent.setAction(ACTION_TOKEN_REQUEST);
        context.startService(intent);
    }


    public static void startTrackRequestAction(Context context, String trackId, String reason) {
        Intent intent = new Intent(context, RequestProcessingService.class);
        intent.setAction(ACTION_TRACK_REQUEST);
        intent.putExtra(TRACK_ID, trackId);
        intent.putExtra(TRACK_REASON, reason);
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
                final String trackReason = intent.getStringExtra(TRACK_REASON);
                handelSingleTrackRequest(trackId, trackReason);
            }
        }
        return START_STICKY;
    }


    private void handleTokenRequest() {
        TokenRequest tokenRequest = new TokenRequest(Token.authHTTPHeader);
        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessingService.TokenRequestListener());
    }

    private void handelSingleTrackRequest(String trackId, String reason) {
        TrackRequest trackRequest = new TrackRequest(TokenHolder.getAccessToken(), trackId, reason);
        getSpiceManager().execute(trackRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessingService.TrackRequestListener());
    }

    public final class TokenRequestListener implements RequestListener<Token> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            bus.post(new EventFail(spiceException.getMessage()));
        }

        @Override
        public void onRequestSuccess(final Token result) {
            bus.post(new TokenEventSuccess(result));
        }
    }

    public final class TrackRequestListener implements RequestListener<Track> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            bus.post(new EventFail(spiceException.getMessage()));
        }

        @Override
        public void onRequestSuccess(final Track result) {
            bus.post(new TrackEventSuccess(result));
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
