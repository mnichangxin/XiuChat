package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private Bundle arg;
    private List<Fragment> viewLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arg = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, null);

//        TextView text_num = view.findViewById(R.id.text_num);
//
//        int num = arg.getInt("num");
//        text_num.setText("Page" + num);

        ViewPager cateViewPager = view.findViewById(R.id.category_view_pager);
//        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        Fragment fg1 = new CategoryFragment();
        Fragment fg2 = new CategoryFragment();
        Fragment fg3 = new CategoryFragment();

        viewLists = new ArrayList<>();

        viewLists.add(fg1);
        viewLists.add(fg2);
        viewLists.add(fg3);

        cateViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), viewLists));
        cateViewPager.setCurrentItem(0);

        return view;
    }

    public static MainFragment newInstance(Bundle args) {
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(args);

        return mainFragment;
    }
}
