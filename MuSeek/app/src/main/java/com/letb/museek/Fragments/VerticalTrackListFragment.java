package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.letb.museek.Adapters.TrackAdapter;
import com.letb.museek.Events.ClearPlayListEvent;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;
import com.letb.museek.Requests.SynchronousRequests.TrackInfoTask;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by marina.titova on 26.12.15.
 */
public class VerticalTrackListFragment extends ListFragment {
    public static final String TRACK_LIST = "TRACK_LIST";

    private List<Track> mListItems;
    private TrackAdapter mAdapter;
    private OnTrackSelectedListener mListener;
    private EventBus bus = EventBus.getDefault();


    public VerticalTrackListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_vertical, container, false);
        if (getArguments() != null) {
            mListItems = (List<Track>) getArguments().getSerializable(TRACK_LIST);
        }
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        mAdapter = new TrackAdapter(getActivity(), mListItems, TrackAdapter.ViewType.VERTICAL);
        listView.setAdapter(mAdapter);
        setListAdapter(mAdapter);
        new Thread(new TrackInfoTask(mListItems)).start();
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            mListener.onTrackSelected(position, mListItems);
        }
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
    public interface OnTrackSelectedListener {
        void onTrackSelected(Integer position, List<Track> trackList);
    }

    public void onEvent(ClearPlayListEvent event){
        mListItems.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        bus.unregister(this);
    }
}
