package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
            mAdapter = new ArtistAdapter(getActivity(), mListItems);
            setListAdapter(mAdapter);
            ListView v = getListView();

//            ListView listView = (ListView) view.findViewById(R.id.artist_list_view);

//            getListView().setOnItemClickListener((AdapterView.OnItemClickListener) this);

//            listView.setAdapter(mAdapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    if (null != mListener) {
//                        mListener.onArtistSelected(position);
//                    }
//                }
//            });
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onArtistSelected(position);
        }
    }
    public interface OnArtistSelectedListener {
        public void onArtistSelected(Integer position);
    }

}
