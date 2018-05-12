package com.lichangxin.xiuchat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lichangxin.xiuchat.utils.RecyclerAdapter;

import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

class StoryRecyclerAdapter extends RecyclerAdapter {
    private Context context;
    private VideoPlayer videoPlayer;

    public StoryRecyclerAdapter(int layout, Context context) {
        super(layout);

        this.context = context;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        videoPlayer = holder.itemView.findViewById(R.id.video_view);

        videoPlayer.setTitle("视频 " + position);
        videoPlayer.setVideoUri((Activity) context, "http://p8mh3zw09.bkt.clouddn.com/test.mp4");
        videoPlayer.startOrRestartPlay();
    }
    @Override
    public int getItemCount() {
        return 1;
    }
}

public class StoryFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.story_fragment, container, false);

        context = getContext();

        recyclerView = view.findViewById(R.id.story_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new StoryRecyclerAdapter(R.layout.story_fragment_item, context));

        return view;
    }
}
