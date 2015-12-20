package com.letb.museek.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.letb.museek.Models.Artist;
import com.letb.museek.R;

import java.util.List;


public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private Context mContext;
    private List<Artist> mArtists;

    public ArtistAdapter(Context context, List<Artist> tracks) {
        mContext = context;
        mArtists = tracks;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView artistImageView;
        public final TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            artistImageView = (ImageView) view.findViewById(R.id.artist_image);
            nameTextView = (TextView) view.findViewById(R.id.artist_name);
        }
    }

    public Artist getItem(int position) {
        return mArtists.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_artist_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTextView.setText(getItem(position).getName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }
}
