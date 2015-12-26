package com.letb.museek.RequestProcessor;

import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.letb.museek.BaseClasses.BaseSpiceService;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.SearchEventSuccess;
import com.letb.museek.Events.TokenEventSuccess;
import com.letb.museek.Events.TrackEventSuccess;
import com.letb.museek.Events.TrackUrlEventSuccess;
import com.letb.museek.Models.Playlist;
import com.letb.museek.Models.Token;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.PlaylistRequest;
import com.letb.museek.Requests.TokenRequest;
import com.letb.museek.Requests.TrackRequest;
import com.letb.museek.Requests.TrackUrlRequest;
import com.letb.museek.SplashActivity;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class RequestProcessorService extends BaseSpiceService {

    private static final String ACTION_TOKEN_REQUEST = "com.letb.museek.Listeners.action.token.request";
    private static final String ACTION_TRACK_REQUEST = "com.letb.museek.Listeners.action.track.request";
    private static final String ACTION_PLAYLIST_REQUEST = "com.letb.museek.Listeners.action.playlist.request";
    private static final String ACTION_SEARCH_TRACKS_REQUEST = "com.letb.museek.Listeners.action.search.request";
    private static final String ACTION_TRACK_URL_REQUEST = "com.letb.museek.Listeners.action.trackurl.request";

    private static final String TRACK_ID = "com.letb.museek.Listeners.extra.track.id";
    private static final String TRACK_REASON = "com.letb.museek.Listeners.extra.track.reason";

    private static final String PLAYLIST_PERIOD = "com.letb.museek.Listeners.extra.playlist.period";
    private static final String PLAYLIST_PAGE = "com.letb.museek.Listeners.extra.playlist.page";
    private static final String PLAYLIST_LANGUAGE = "com.letb.museek.Listeners.extra.playlist.language";

    private static final String SEARCH_QUERY = "com.letb.museek.Listeners.extra.search.query";
    private static final String SEARCH_RESULTS_ON_PAGE = "com.letb.museek.Listeners.extra.search.results";
    private static final String SEARCH_QUALITY = "com.letb.museek.Listeners.extra.search.quality";



    private EventBus bus = EventBus.getDefault();

//    Определили особый метод для нового реквеста, в кот. формируется интент для сервиса
//    TODO: перенести формаирование интента в функцию-построитель
    public static void startTokenRequestAction(Context context) {
        Intent intent = new Intent(context, RequestProcessorService.class);
        intent.setAction(ACTION_TOKEN_REQUEST);
//        Дернули сервис
        context.startService(intent);
    }

    public static void startTrackRequestAction(Context context, String trackId, String reason) {
        Intent intent = new Intent(context, RequestProcessorService.class);
        intent.setAction(ACTION_TRACK_REQUEST);
        intent.putExtra(TRACK_ID, trackId);
        intent.putExtra(TRACK_REASON, reason);
        context.startService(intent);
    }

    public static void startTopTracksRequestAction(Context context, int timePeriod, int page, String language) {
        Intent intent = new Intent(context, RequestProcessorService.class);
        intent.setAction(ACTION_PLAYLIST_REQUEST);
        intent.putExtra(PLAYLIST_PERIOD, timePeriod);
        intent.putExtra(PLAYLIST_PAGE, page);
        intent.putExtra(PLAYLIST_LANGUAGE, language);
        context.startService(intent);

    }

    public static void startSearchTracksRequestAction(Context context, String query, int resultsOnPage, String quality) {
        Intent intent = new Intent(context, RequestProcessorService.class);
        intent.setAction(ACTION_SEARCH_TRACKS_REQUEST);
        intent.putExtra(SEARCH_QUERY, query);
        intent.putExtra(SEARCH_RESULTS_ON_PAGE, resultsOnPage);
        intent.putExtra(SEARCH_QUALITY, quality);
        context.startService(intent);
    }

    public static void startTrackUrlRequestAction(Context context, String trackId, String reason) {
        Intent intent = new Intent(context, RequestProcessorService.class);
        intent.setAction(ACTION_TRACK_URL_REQUEST);
        intent.putExtra(TRACK_ID, trackId);
        intent.putExtra(TRACK_REASON, reason);
        context.startService(intent);
    }

//    Сервис дернулся, разобрались, что нам пришло
//    Без интента сервис не дергаем! Иначе исключение
    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        final String action = intent.getAction();
        switch (action) {
            case ACTION_TOKEN_REQUEST:
                initiateTokenRequest();
                break;
            case ACTION_TRACK_REQUEST:
                final String trackId = intent.getStringExtra(TRACK_ID);
                final String trackReason = intent.getStringExtra(TRACK_REASON);
                initiateSingleTrackRequest(trackId, trackReason);
                break;
            case ACTION_TRACK_URL_REQUEST:
                final String trackUrlId = intent.getStringExtra(TRACK_ID);
                final String trackUrlReason = intent.getStringExtra(TRACK_REASON);
                initiateTrackUrlRequest(trackUrlId, trackUrlReason);
                break;
            case ACTION_PLAYLIST_REQUEST:
                final int timePeriod = intent.getIntExtra(PLAYLIST_PERIOD, 1);
                final int page = intent.getIntExtra(PLAYLIST_PAGE, 1);
                final String language = intent.getStringExtra(PLAYLIST_LANGUAGE);
                initiateTopTracksRequest(timePeriod, page, language);
                break;
            case ACTION_SEARCH_TRACKS_REQUEST:
                final String query = intent.getStringExtra(SEARCH_QUERY);
                final int resultsOnPage = intent.getIntExtra(SEARCH_RESULTS_ON_PAGE, 10);
                final String quality = intent.getStringExtra(SEARCH_QUALITY);
                initiateSearchTracksRequest(query, resultsOnPage, quality);
                break;
        }
        return START_STICKY;
    }

//    Отправили, собсна, реквест
    private void initiateTokenRequest() {
        TokenRequest tokenRequest = new TokenRequest();
        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessorService.TokenRequestListener());
    }

    private void initiateSingleTrackRequest(String trackId, String reason) {
        TrackRequest trackRequest = new TrackRequest(TokenHolder.getAccessToken(), trackId, reason);
        getSpiceManager().execute(trackRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessorService.TrackRequestListener());
    }

    private void initiateTrackUrlRequest(String trackId, String reason) {
        TrackUrlRequest trackRequest = new TrackUrlRequest(TokenHolder.getAccessToken(), trackId, reason);
        getSpiceManager().execute(trackRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessorService.TrackUrlRequestListener());
    }

    private void initiateTopTracksRequest(int timePeriod, int pageNumber, String language) {
        PlaylistRequest playlistRequest = new PlaylistRequest(TokenHolder.getAccessToken(), timePeriod, pageNumber, language);
        getSpiceManager().execute(playlistRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessorService.PlaylistRequestListener());
    }

    private void initiateSearchTracksRequest(String query, int resultsOnPage, String quality) {
        PlaylistRequest playlistRequest = new PlaylistRequest(TokenHolder.getAccessToken(), query, resultsOnPage, quality);
        getSpiceManager().execute(playlistRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new RequestProcessorService.SearchRequestListener());
    }

//    Получили ответ и направили его слушающему классу
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

    public final class TrackUrlRequestListener implements RequestListener<String> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            bus.post(new EventFail(spiceException.getMessage()));
        }

        @Override
        public void onRequestSuccess(final String result) {
            bus.post(new TrackUrlEventSuccess(result));
        }
    }

    public final class PlaylistRequestListener implements RequestListener<JsonElement> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            bus.post(new EventFail(spiceException.getMessage()));
        }

        @Override
        public void onRequestSuccess(final JsonElement result) {
            bus.post(new PlaylistEventSuccess(result));
        }
    }

    public final class SearchRequestListener implements RequestListener<JsonElement> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            bus.post(new EventFail(spiceException.getMessage()));
        }

        @Override
        public void onRequestSuccess(final JsonElement result) {
            bus.post(new SearchEventSuccess(result));
        }
    }

//    Ненужное
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
