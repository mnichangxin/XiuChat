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
import android.util.Log;
import android.view.MenuItem;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lichangxin.xiuchat.utils.FragmentAdapter;

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

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("_id", "5ad0d45ce4f3571e907dc3cf")
                .add("type", "share")
                .add("content", "Android 测试测试测试")
                .add("token", "pa4bcbVQyGnQDxOqR9NR4MWmXSblyrDK")
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/api/createDynamic")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("Response:", "Fail");
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d("Response:", response.body().string());
            }
        });

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
