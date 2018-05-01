package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public ViewHolder(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.share_nickname);
    }
}

class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<String> dataLists;

//    public RecyclerAdapter(List<String> dataLists) {
//        this.dataLists = dataLists;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_fragment_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.textView.setText(dataLists.get(position));
        holder.textView.setText("NickName");
    }
    @Override
    public int getItemCount() {
        return 10;
    }
}

public class ShareFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> dataLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        dataLists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataLists.add("Item" + i);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerAdapter());

        return view;
    }
}
