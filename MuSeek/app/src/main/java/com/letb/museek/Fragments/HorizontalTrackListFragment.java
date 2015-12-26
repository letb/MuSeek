package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letb.museek.Adapters.TrackAdapter;
import com.letb.museek.BaseClasses.TwoWayAdapterView;
import com.letb.museek.BaseClasses.TwoWayGridView;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;

import java.util.List;

/**
 * Created by marina.titova on 26.12.15.
 */
public class HorizontalTrackListFragment extends Fragment implements TwoWayAdapterView.OnItemClickListener {
    public static final String TRACK_LIST = "TRACK_LIST";
    public static final String TRACK_LIST_NAME = "TRACK_LIST_NAME";


    private List<Track> mListItems;
    private TrackAdapter mAdapter;
    private TwoWayGridView mRecyclerView;
    private OnTrackSelectedListener mListener;

    public HorizontalTrackListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_list_horizontal, container, false);
        if (getArguments() != null) {
            mListItems = (List<Track>) getArguments().getSerializable(TRACK_LIST);
        }

        mRecyclerView = (TwoWayGridView) view.findViewById(R.id.gridview);
        mRecyclerView.setLongClickable(true);
        mAdapter = new TrackAdapter(getActivity(), mListItems, TrackAdapter.ViewType.HORIZONTAL);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnTrackSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTrackSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onTrackSelected(position, mListItems);
        }
    }

    public interface OnTrackSelectedListener {
        void onTrackSelected(Integer position, List<Track> trackList);
    }
}
