package com.lichangxin.xiuchat;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.FragmentAdapter;

public class PersonalActivity extends AppCompatActivity {
    private ViewPager personalViewPager;
    private List<Fragment> viewLists;
    private Fragment dynamic;
    private Fragment about;
    private Bundle dynamicBundle;
    private Bundle aboutBundle;
    private TabLayout personalTabLayout;

    private JsonObject userInfo;

    /* 设置 ViewPager */
    private void setPager() {
        personalViewPager = findViewById(R.id.personal_view_pager);

        dynamicBundle = new Bundle();
        dynamicBundle.putInt("page", 1);
        dynamicBundle.putString("userInfo", userInfo.toString());
        dynamic = PersonalFragment.newInstance(dynamicBundle);

        aboutBundle = new Bundle();
        aboutBundle.putInt("page", 2);
        aboutBundle.putString("userInfo", userInfo.toString());
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
        ctl.setTitle(userInfo.get("nickname").getAsString());

        // 设置 ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        TextView following_num = findViewById(R.id.personal_following_num);
        TextView followed_num  = findViewById(R.id.personal_followed_num);
        LinearLayout edit_info = findViewById(R.id.edit_info);

        following_num.setText("关注 " + userInfo.get("following_num"));
        followed_num.setText("粉丝 " + userInfo.get("followed_num"));

        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this, EditInfoActivity.class);
                intent.putExtra("userInfo", userInfo.toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal);

        userInfo = new JsonParser().parse(getIntent().getStringExtra("userInfo")).getAsJsonObject();

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
