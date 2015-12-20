package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.letb.museek.Adapters.ArtistAdapter;
import com.letb.museek.Models.Artist;
import com.letb.museek.R;

import java.util.List;

public class ArtistListFragment extends ListFragment {
    public static final String ARTIST_LIST = "ARTIST_LIST";

    private List<Artist> mListItems;

    private OnArtistSelectedListener mListener;
    private ArtistAdapter mAdapter;

    public ArtistListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        if (getArguments() != null) {
            mListItems = (List<Artist>) getArguments().getSerializable(ARTIST_LIST);
        }
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        mAdapter = new ArtistAdapter(getActivity(), mListItems);
        listView.setAdapter(mAdapter);
        setListAdapter(mAdapter);
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            mListener.onArtistSelected(position);
        }
    }

    public interface OnArtistSelectedListener {
        public void onArtistSelected(Integer position);
    }

}
