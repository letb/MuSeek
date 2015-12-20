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

/**
 * Created by eugene on 13.12.15.
 */
/*
NICE ViewHolder implementation found
 */
public class TrackAdapter extends BaseAdapter {
        private Context mContext;
        private List<Track> mTracks;

        public TrackAdapter(Context context, List<Track> tracks) {
            mContext = context;
            mTracks = tracks;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.play_list_row, parent, false);
                holder = new ViewHolder();
                holder.artistImageView = (ImageView) convertView.findViewById(R.id.track_image);
                holder.nameTestView = (TextView) convertView.findViewById(R.id.track_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

        //  TODO: display picture
        holder.nameTestView.setText(track.getData().getTrack());
            return convertView;
        }

        static class ViewHolder {
            ImageView artistImageView;
            TextView nameTestView;
        }
}
