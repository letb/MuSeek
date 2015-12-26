package com.letb.museek.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;

import java.util.List;

import static com.letb.museek.Adapters.TrackAdapter.ViewType.*;

/**
 * Created by eugene on 13.12.15.
 */

public class TrackAdapter extends BaseAdapter {

    public enum ViewType {
        HORIZONTAL,
        VERTICAL
    }

    private Context mContext;
    private ViewType viewType;
    private List<Track> mTracks;

    public TrackAdapter(Context context, List<Track> tracks, ViewType viewType) {
        this.mContext = context;
        this.mTracks  = tracks;
        this.viewType = viewType;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            switch (viewType) {
                case HORIZONTAL:
                    convertView = LayoutInflater.from(mContext)
                            .inflate(R.layout.item_track_list_horizontal, parent, false);
                    break;
                case VERTICAL:
                    convertView = LayoutInflater.from(mContext)
                            .inflate(R.layout.item_track_list_vertical, parent, false);
                    break;
            }
            holder = new ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.track_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.track_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

    //  TODO: display picture
    holder.titleTextView.setText(track.getData().getTrack());
        return convertView;
    }

    static class ViewHolder {
        ImageView trackImageView;
        TextView titleTextView;
    }
}
