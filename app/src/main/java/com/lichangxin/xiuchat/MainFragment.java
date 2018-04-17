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

import com.lichangxin.xiuchat.utils.FragmentAdapter;

/* 主界面 ViewPager + Fragment */
public class MainFragment extends Fragment {
    private Bundle args;
    private List<Fragment> viewLists;
    private Fragment share;
    private Fragment story;
    private Fragment interest;
    private Fragment encounter;
    private Fragment community;
    private Bundle shareBundle;
    private Bundle storyBundle;
    private Bundle interestBundle;
    private Bundle encounterBundle;
    private Bundle communityBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, null);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager cateViewPager = view.findViewById(R.id.category_view_pager);

        viewLists = new ArrayList<>();

        int page = args.getInt("page");

        if (page == 1) {
            shareBundle = new Bundle();
            shareBundle.putString("category", "share");
            share = CategoryFragment.newInstance(shareBundle);

            storyBundle = new Bundle();
            storyBundle.putString("category", "story");
            story = CategoryFragment.newInstance(shareBundle);

            interestBundle = new Bundle();
            interestBundle.putString("category", "interest");
            interest = CategoryFragment.newInstance(shareBundle);

            viewLists.add(share);
            viewLists.add(story);
            viewLists.add(interest);
        } else if (page == 2) {
            encounterBundle = new Bundle();
            encounterBundle.putString("category", "encounter");
            encounter = CategoryFragment.newInstance(encounterBundle);

            communityBundle = new Bundle();
            communityBundle.putString("category", "community");
            community = CategoryFragment.newInstance(communityBundle);

            viewLists.add(encounter);
            viewLists.add(community);
        }

        cateViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), viewLists));
        cateViewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(cateViewPager);

        if (page == 1) {
            tabLayout.getTabAt(0).setText("分享");
            tabLayout.getTabAt(1).setText("故事");
            tabLayout.getTabAt(2).setText("兴趣");
        } else if (page == 2) {
            tabLayout.getTabAt(0).setText("邂逅");
            tabLayout.getTabAt(1).setText("社群");
        }

        return view;
    }

    // 保存状态的静态类
    public static MainFragment newInstance(Bundle args) {
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(args);

        return mainFragment;
    }
}
