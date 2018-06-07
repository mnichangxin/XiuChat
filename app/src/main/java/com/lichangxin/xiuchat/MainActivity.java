package com.lichangxin.xiuchat;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.FragmentAdapter;
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navView;
    private CircleImageView asideHead;
    private ViewPager mainViewPager;
    private ArrayList<ImageView> imageViewList;
    private ImageView dynamicImageView;
    private ImageView encounterImageView;
    private ImageView chatImageView;
    private ArrayList<Integer> primaryDrawable;
    private ArrayList<Integer> activeDrawable;
    private List<Fragment> viewLists;
    private Fragment dynamic;
    private Fragment encounter;
    private Fragment chat;
    private Bundle dynamicBundle;
    private Bundle encounterBundle;
    private Bundle chatBundle;
    private int position;
    private JsonObject userInfo;
    private String URL = ProperTies.getProperties().getProperty("URL");

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

        // 利用 Bundle 传输 Fragment 页码
        dynamicBundle = new Bundle();
        dynamicBundle.putInt("page", 1);
        dynamic = MainFragment.newInstance(dynamicBundle);

        encounterBundle = new Bundle();
        encounterBundle.putInt("page", 2);
        encounter = MainFragment.newInstance(encounterBundle);

        chatBundle = new Bundle();
        chatBundle.putInt("page", 3);
        chat = MainFragment.newInstance(chatBundle);

        viewLists.add(dynamic);
        viewLists.add(encounter);
        viewLists.add(chat);

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
        asideHead = navView.getHeaderView(0).findViewById(R.id.aside_head);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setCheckedItem(R.id.nav_message);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent = null;

                switch (item.getItemId()) {
                    case R.id.nav_group:
                        intent = new Intent(MainActivity.this, CreateGroup.class);
                        break;
                }

                startActivity(intent);

                drawerLayout.closeDrawers();
                return true;
            }
        });

        asideHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                intent.putExtra("userInfo", userInfo.toString());
                startActivity(intent);
            }
        });
    }

    /* 请求数据 */
    private void request() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put("_id", new Global(MainActivity.this).getId());

        NetRequest.getFormRequest(URL + "/api/getUserInfo", parms, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                if (jsonObject.get("status").getAsInt() == 1) {
                    userInfo = jsonObject.get("data").getAsJsonObject();

                    TextView drawer_nickname = findViewById(R.id.drawer_nickname);
                    drawer_nickname.setText(userInfo.get("nickname").getAsString());
                } else {
                    Toast.makeText(MainActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(MainActivity.this, "网络错误，请重试", Toast.LENGTH_SHORT).show();
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
                request();
                break;
        }
        return true;
    }
}


