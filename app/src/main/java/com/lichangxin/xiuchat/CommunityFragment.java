package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lichangxin.xiuchat.utils.ProperTies;
import com.lichangxin.xiuchat.utils.RecyclerAdapter;

class CommunityRecyclerAdapter extends RecyclerAdapter {
    public CommunityRecyclerAdapter(int layout, int id) {
        super(layout, id);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
    @Override
    public int getItemCount() {
        return 10;
    }
}

public class CommunityFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment, container, false);

        recyclerView = view.findViewById(R.id.community_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        URL = ProperTies.getProperties().getProperty("URL");

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommunityRecyclerAdapter(R.layout.community_fragment_item, 0));

        return view;
    }
}