package com.letb.museek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.letb.museek.Adapters.ArtistAdapter;
import com.letb.museek.Models.Artist;
import com.letb.museek.R;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.ItemClickSupport.OnItemClickListener;
import org.lucasr.twowayview.ItemClickSupport.OnItemLongClickListener;
import org.lucasr.twowayview.widget.DividerItemDecoration;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.List;

public class ArtistListFragment extends Fragment {
    public static final String ARTIST_LIST = "ARTIST_LIST";

    private List<Artist> mListItems;

    private OnArtistSelectedListener mListener;
    private ArtistAdapter mAdapter;
    private TwoWayView mRecyclerView;
    private Toast mToast;

    public ArtistListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        if (getArguments() != null) {
            mListItems = (List<Artist>) getArguments().getSerializable(ARTIST_LIST);
        }
//        HorizontalGridView listView = (HorizontalGridView) view.findViewById(R.id.horizontalGridView);
//        listView.setAdapter(mAdapter);

//
//
//
        mRecyclerView = (TwoWayView) view.findViewById(R.id.horizontalGridView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);


        final ItemClickSupport itemClick = ItemClickSupport.addTo(mRecyclerView);
        itemClick.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View child, int position, long id) {
                if (null != mListener) {
                    mListener.onArtistSelected(position);
                }
            }
        });

        itemClick.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View child, int position, long id) {
                return true;
            }
        });

        mAdapter = new ArtistAdapter(getActivity(), mListItems);
        mRecyclerView.setAdapter(mAdapter);
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

    public interface OnArtistSelectedListener {
        public void onArtistSelected(Integer position);
    }

}
