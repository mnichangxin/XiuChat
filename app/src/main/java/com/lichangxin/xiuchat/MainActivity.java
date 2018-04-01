package com.lichangxin.xiuchat;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/* 数据适配器 */
class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> list;

    public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
    @Override
    public int getCount() {
        return list.size();
    }
}

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private ViewPager mainViewPager;
    private List<Fragment> viewLists;
    private ArrayList<ImageView> imageViewList;
    private ImageView dynamicImageView;
    private ImageView encounterImageView;
    private ImageView chatImageView;
    private ArrayList<Integer> primaryDrawable;
    private ArrayList<Integer> activeDrawable;
    private int position;

    /*  设置 ViewPager */
    private void setPager() {
        mainViewPager = findViewById(R.id.main_view_pager);

        dynamicImageView = findViewById(R.id.dynamic_image);
        encounterImageView = findViewById(R.id.encounter_image);
        chatImageView = findViewById(R.id.chat_image);

        viewLists = new ArrayList<>();
        imageViewList = new ArrayList<>();
        primaryDrawable = new ArrayList<>();
        activeDrawable = new ArrayList<>();

        imageViewList.add(dynamicImageView);
        imageViewList.add(encounterImageView);
        imageViewList.add(chatImageView);

        primaryDrawable.add(R.drawable.ic_dynamic);
        primaryDrawable.add(R.drawable.ic_encounter);
        primaryDrawable.add(R.drawable.ic_chat);

        activeDrawable.add(R.drawable.ic_dynamic_active);
        activeDrawable.add(R.drawable.ic_encounter_active);
        activeDrawable.add(R.drawable.ic_chat_active);

        // 利用 Bundle 传输数据
        Bundle bundle1 = new Bundle();
        bundle1.putInt("num", 1);
        Fragment fg1 = MainFragment.newInstance(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("num", 2);
        Fragment fg2 = MainFragment.newInstance(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putInt("num", 3);
        Fragment fg3 = MainFragment.newInstance(bundle3);

        viewLists.add(fg1);
        viewLists.add(fg2);
        viewLists.add(fg3);

        // 设置 ViewPager Adapter
        mainViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), viewLists));
        mainViewPager.setCurrentItem(0);
        imageViewList.get(0).setImageResource(activeDrawable.get(0));

        // 设置 ImageView 点击事件
        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == dynamicImageView) {
                        position = 0;
                    } else if (view == encounterImageView) {
                        position = 1;
                    } else if (view == chatImageView) {
                        position = 2;
                    }
                    for (int j = 0; j < imageViewList.size(); j++) {
                        imageViewList.get(j).setImageResource(primaryDrawable.get(j));
                    }
                    mainViewPager.setCurrentItem(position);
                    imageViewList.get(position).setImageResource(activeDrawable.get(position));
                }
            });
        }

        // 设置 ViewPager 滑动事件
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageViewList.size(); i++) {
                    imageViewList.get(i).setImageResource(primaryDrawable.get(i));
                }
                imageViewList.get(position).setImageResource(activeDrawable.get(position));
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /* 设置 Toolbar 和 Drawer */
    private void setBar() {
        toolbar =  findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // 设置 Toolbar 和 Drawer
        setBar();
        // 设置 ViewPager
        setPager();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}


