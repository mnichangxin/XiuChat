package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* 分类界面 ViewPager + Fragment */
public class CategoryFragment extends Fragment {
    private Bundle args;
    private String category;
    private View view;

    public static CategoryFragment newInstance(Bundle args) {
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setArguments(args);

        return categoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        category = args.getString("category");

        switch (category) {
            case "share":
                view = inflater.inflate(R.layout.share_fragment, container, false);
                break;
            case "story":
                view = inflater.inflate(R.layout.category_fragment, container, false);
                break;
            case "interest":
                view = inflater.inflate(R.layout.category_fragment, container, false);
                break;
            case "encounter":
                view = inflater.inflate(R.layout.category_fragment, container, false);
                break;
            case "community":
                view = inflater.inflate(R.layout.category_fragment, container, false);
                break;
        }

        return view;
    }
}
