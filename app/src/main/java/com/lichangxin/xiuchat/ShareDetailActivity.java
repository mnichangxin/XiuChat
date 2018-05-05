package com.lichangxin.xiuchat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ShareDetailActivity extends AppCompatActivity {
    private JsonObject userDynamic;
    private TextView nickname;
    private TextView content;
    private TextView commit;
    private TextView fav;

    // 设置 Toolbar 和事件
    private void setBar() {
        Toolbar toolbar = findViewById(R.id.edit_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("分享正文");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    // 填充数据
    private void setContent() {
        nickname = findViewById(R.id.share_detail_nickname);
        content = findViewById(R.id.share_detail_content);
        commit = findViewById(R.id.share_detail_commit);
        fav = findViewById(R.id.share_detail_fav);

        nickname.setText(userDynamic.get("nickname").getAsString());
        content.setText(userDynamic.get("share").getAsString());
        commit.setText(userDynamic.get("commit").getAsJsonArray().size() + "");
        fav.setText(userDynamic.get("fav").getAsString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_detail_layout);

        userDynamic = new JsonParser().parse(getIntent().getStringExtra("userDynamic")).getAsJsonObject();

        setBar();
        setContent();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
