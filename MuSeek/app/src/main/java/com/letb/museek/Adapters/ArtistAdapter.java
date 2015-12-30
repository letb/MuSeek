package com.letb.museek.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letb.museek.Models.Artist;
import com.letb.museek.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArtistAdapter extends BaseAdapter {
    private Context mContext;
    private List<Artist> mArtists;

    public ArtistAdapter(Context context, List<Artist> tracks) {
        mContext = context;
        mArtists = tracks;
    }

    static class ViewHolder  {
        ImageView artistImageView;
        TextView nameTextView;
    }

    @Override
    public int getCount() {
        return mArtists.size();
    }

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

        holder.nameTextView.setText(artist.getName());
        //  TODO: display picture
        if (holder.artistImageView != null) {
            if (artist.getPic() == null) {
                Picasso.with(mContext).load(R.drawable.album).into(holder.artistImageView);
            } else {
                Picasso.with(mContext).load(artist.getPic()).into(holder.artistImageView);
            }
        }
        return convertView;
    }
}
