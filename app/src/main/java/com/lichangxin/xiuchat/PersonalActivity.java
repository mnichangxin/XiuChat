package com.lichangxin.xiuchat;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

public class PersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal);

        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.personal_bar);
        CollapsingToolbarLayout ctl = findViewById(R.id.personal_bar_layout);

        setSupportActionBar(toolbar);
//        ctl.setCollapsedTitleGravity(Gravity.LEFT);
//        ctl.setExpandedTitleGravity(Gravity.CENTER);
        ctl.setTitle("浮生若梦");

        // 设置 ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }
}
