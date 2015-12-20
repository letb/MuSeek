package com.letb.museek.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letb.museek.Models.Artist;
import com.letb.museek.R;

import java.util.List;


public class ArtistAdapter extends BaseAdapter {
    private Context mContext;
    private List<Artist> mArtists;

    public ArtistAdapter(Context context, List<Artist> tracks) {
        mContext = context;
        mArtists = tracks;
    }

    @Override
    public int getCount() {
        return mArtists.size();
    }

    @Override
    public Artist getItem(int position) {
        return mArtists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Artist artist = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_artist_list, parent, false);
            holder = new ViewHolder();
            holder.artistImageView = (ImageView) convertView.findViewById(R.id.artist_image);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.artist_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //  TODO: display picture
        holder.nameTextView.setText(artist.getName());
        return convertView;
    }

    static class ViewHolder {
        ImageView artistImageView;
        TextView nameTextView;
    }
}
