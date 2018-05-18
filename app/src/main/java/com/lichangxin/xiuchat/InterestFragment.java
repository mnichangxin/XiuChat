package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lichangxin.xiuchat.utils.RecyclerAdapter;

class InterestRecyclerAdapter extends RecyclerAdapter {
    public InterestRecyclerAdapter(int layout, int id) {
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

public class InterestFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.interest_fragment, container, false);

        recyclerView = view.findViewById(R.id.interest_recyclerview);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new InterestRecyclerAdapter(R.layout.interest_fragment_item, 0));

        return view;
    }
}
