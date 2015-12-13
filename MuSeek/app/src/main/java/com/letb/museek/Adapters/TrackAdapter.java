package com.letb.museek.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letb.museek.Models.SongModel;
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
        private List<SongModel> mTracks;

        public TrackAdapter(Context context, List<SongModel> tracks) {
            mContext = context;
            mTracks = tracks;
        }

        @Override
        public int getCount() {
            return mTracks.size();
        }

        @Override
        public SongModel getItem(int position) {
            return mTracks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            SongModel track = getItem(position);

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.play_list_row, parent, false);
                holder = new ViewHolder();
                holder.trackImageView = (ImageView) convertView.findViewById(R.id.track_image);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.track_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//  TODO: Hotfix to display Unicode (because this SoundCloud API is somehow Qur'an-oriented)
        holder.titleTextView.setText(track.getTitle());
//
//        // Trigger the download of the URL asynchronously into the image view.
//        Picasso.with(mContext).load(track.getArtworkURL()).into(holder.trackImageView);

            return convertView;
        }

        static class ViewHolder {
            ImageView trackImageView;
            TextView titleTextView;
        }
}