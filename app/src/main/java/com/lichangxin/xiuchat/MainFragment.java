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
    private View view;
    private TabLayout tabLayout;
    private ViewPager cateViewPager;
    private int page;
    private Bundle args;
    private List<Fragment> viewLists;
    private Fragment share;
    private Fragment story;
    private Fragment interest;
    private Fragment encounter;
    private Fragment community;
    private Fragment chat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, null);

        tabLayout = view.findViewById(R.id.tab_layout);
        cateViewPager = view.findViewById(R.id.category_view_pager);

        viewLists = new ArrayList<>();

        page = args.getInt("page");

        if (page == 1) {
            share = new ShareFragment();
            story = new StoryFragment();
            interest = new InterestFragment();

            viewLists.add(share);
            viewLists.add(story);
            viewLists.add(interest);
        } else if (page == 2) {
            encounter = new EncounterFragment();
            community = new CommunityFragment();

            viewLists.add(encounter);
            viewLists.add(community);
        } else if (page == 3) {
            chat = new ChatFragment();

            viewLists.add(chat);
        }

        cateViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), viewLists));
        cateViewPager.setCurrentItem(0);

        if (page == 1 || page == 2) {
            tabLayout.setupWithViewPager(cateViewPager);
        }

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
