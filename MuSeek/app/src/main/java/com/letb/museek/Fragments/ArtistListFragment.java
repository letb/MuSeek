package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.letb.museek.Adapters.ArtistAdapter;
import com.letb.museek.BaseClasses.TwoWayAdapterView;
import com.letb.museek.BaseClasses.TwoWayGridView;
import com.letb.museek.Models.Artist;
import com.letb.museek.R;

import java.util.List;

public class ArtistListFragment extends Fragment implements TwoWayAdapterView.OnItemClickListener {
    public static final String ARTIST_LIST = "ARTIST_LIST";
    public static final String TAG = "ArtistListFragment";

    private List<Artist> mListItems;

    private OnArtistSelectedListener mListener;
    private ArtistAdapter mAdapter;
    private TwoWayGridView mRecyclerView;

    public ArtistListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        if (getArguments() != null) {
            mListItems = (List<Artist>) getArguments().getSerializable(ARTIST_LIST);
        }

        mRecyclerView = (TwoWayGridView) view.findViewById(R.id.gridview);
        mRecyclerView.setLongClickable(true);
        mAdapter = new ArtistAdapter(getActivity(), mListItems);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnArtistSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnArtistSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Clicked item" + position);
        if (null != mListener) {
            mListener.onArtistSelected(position);
        }
    }

    public interface OnArtistSelectedListener {
        void onArtistSelected(Integer position);
    }

}
