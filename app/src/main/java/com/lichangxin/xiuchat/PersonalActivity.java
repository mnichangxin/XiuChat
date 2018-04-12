package com.lichangxin.xiuchat;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lichangxin.xiuchat.utils.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends AppCompatActivity {
    private ViewPager personalViewPager;
    private List<Fragment> viewLists;
    private Fragment dynamic;
    private Fragment about;
    private Bundle dynamicBundle;
    private Bundle aboutBundle;
    private TabLayout personalTabLayout;

    /* 设置 ViewPager */
    private void setPager() {
        personalViewPager = findViewById(R.id.personal_view_pager);

        dynamicBundle = new Bundle();
        dynamicBundle.putInt("page", 1);
        dynamic = PersonalFragment.newInstance(dynamicBundle);

        aboutBundle = new Bundle();
        aboutBundle.putInt("page", 2);
        about = PersonalFragment.newInstance(aboutBundle);

        personalTabLayout = findViewById(R.id.personal_tab_layout);

        viewLists = new ArrayList<>();
        viewLists.add(dynamic);
        viewLists.add(about);

        personalViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), viewLists));
        personalViewPager.setCurrentItem(0);

        personalTabLayout.setupWithViewPager(personalViewPager);
        personalTabLayout.getTabAt(0).setText("动态");
        personalTabLayout.getTabAt(1).setText("关于我");
    }

    /* 设置 Toolbar */
    private void setBar() {
        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.personal_bar);
        CollapsingToolbarLayout ctl = findViewById(R.id.personal_bar_layout);

        setSupportActionBar(toolbar);
        ctl.setTitle("浮生若梦");

        // 设置 ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal);

        setBar();
        setPager();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PersonalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
