package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.letb.museek.Adapters.TrackAdapter;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;

import java.util.List;

public class PlaylistFragment extends ListFragment {
    public static final String TRACK_LIST = "TRACK_LIST";

    private List<Track> mListItems;

    private OnTrackSelectedListener mListener;
    private TrackAdapter mAdapter;

    public PlaylistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        if (getArguments() != null) {
            mListItems = (List<Track>) getArguments().getSerializable(TRACK_LIST);
        }
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        mAdapter = new TrackAdapter(getActivity(), mListItems);
        listView.setAdapter(mAdapter);
        setListAdapter(mAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnTrackSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTranslateAreaListener");
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
            mListener.onTrackSelected(mListItems.get(position).getTitle());
        }
    }

    public interface OnTrackSelectedListener {
        public void onTrackSelected(String title);
    }

}
