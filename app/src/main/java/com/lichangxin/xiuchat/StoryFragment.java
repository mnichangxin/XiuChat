package com.lichangxin.xiuchat;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

public class StoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.story_fragment, container, false);

        VideoView videoView = view.findViewById(R.id.video_view);

        videoView.setVideoURI(Uri.parse("http://rbv01.ku6.com/wifi/o_1c7ri8gd11l751fvm79fucvgs1akvs"));
        videoView.start();
        videoView.requestFocus();

        return view;
    }
}
