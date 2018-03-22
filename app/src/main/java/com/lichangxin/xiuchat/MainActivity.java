package com.lichangxin.xiuchat;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private ViewPager mainViewPager;
    private View dynamic;
    private View encounter;
    private View chat;
    private ArrayList<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        toolbar =  findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.nav_view);

        mainViewPager = findViewById(R.id.main_view_pager);
        dynamic = findViewById(R.id.dynamic);
        encounter = findViewById(R.id.encounter);
        chat = findViewById(R.id.chat);

        LayoutInflater inflater = getLayoutInflater().from(this);

        dynamic = inflater.inflate(R.layout.dynamic, null);
        encounter = inflater.inflate(R.layout.encounter, null);
        chat = inflater.inflate(R.layout.chat, null);

        viewList = new ArrayList<>();

        viewList.add(dynamic);
        viewList.add(encounter);
        viewList.add(chat);

        // 设置 Toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // 设置导航
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });

        // 设置主 ViewPager
        mainViewPager.setAdapter(new ViewPagerAdapter(viewList));
        mainViewPager.setCurrentItem(0);
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

    /* 数据适配器 */
    public class ViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public ViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }
    }
}


