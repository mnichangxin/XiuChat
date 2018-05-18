package com.lichangxin.xiuchat;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lichangxin.xiuchat.utils.RecyclerAdapter;

import java.util.HashMap;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

class StoryRecyclerAdapter extends RecyclerAdapter {
    private JCVideoPlayerStandard jcVideoPlayerStandard;
    private ImageView viewImage;

    private Bitmap getVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        int kind = MediaStore.Video.Thumbnails.MINI_KIND;

        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }

        return bitmap;
    }

    public StoryRecyclerAdapter(int layout, int id) {
        super(layout, id);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        jcVideoPlayerStandard = holder.itemView.findViewById(R.id.video_view);
        viewImage = holder.itemView.findViewById(R.id.video_image);

        jcVideoPlayerStandard.setUp("http://p8mh3zw09.bkt.clouddn.com/test.mp4", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        viewImage.setImageBitmap(getVideoThumbnail("http://p8mh3zw09.bkt.clouddn.com/test.mp4", jcVideoPlayerStandard.getWidth(), 200));
    }
    @Override
    public int getItemCount() {
        return 10;
    }
    @Override
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        super.setmOnItemClickListener(mOnItemClickListener);
    }
}

public class StoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.story_fragment, container, false);

        recyclerView = view.findViewById(R.id.story_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        StoryRecyclerAdapter storyRecyclerAdapter = new StoryRecyclerAdapter(R.layout.story_fragment_item, R.id.video_image);

        storyRecyclerAdapter.setmOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FrameLayout videoContainer = (FrameLayout) view.getParent();
                JCVideoPlayerStandard jcVideoPlayerStandard = videoContainer.findViewById(R.id.video_view);

                view.setVisibility(View.GONE);
                jcVideoPlayerStandard.startVideo();
            }
        });

        recyclerView.setAdapter(storyRecyclerAdapter);

        return view;
    }
}
