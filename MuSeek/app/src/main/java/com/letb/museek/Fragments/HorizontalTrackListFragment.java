package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public static final String TITLE = "TRACK_LIST_NAME";


    private List<Track> mListItems;
    private String title;
    private TrackAdapter mAdapter;
    private TwoWayGridView mRecyclerView;
    private TextView titleView;
    private OnTrackSelectedListener mListener;

    public HorizontalTrackListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_horizontal, container, false);
        if (getArguments() != null) {
            mListItems = (List<Track>) getArguments().getSerializable(TRACK_LIST);
            title = (String) getArguments().getSerializable(TITLE);
        }

        titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);

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
            Log.d("HorizontalTrackFragment", "Attached" + context.toString());
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
        Log.d("HorizontalTrackFragment", "Clicked item" + position);
        if (null != mListener) {
            mListener.onTrackSelected(position, mListItems);
        }
    }

    public interface OnTrackSelectedListener {
        void onTrackSelected(Integer position, List<Track> trackList);
    }
}
