package com.lichangxin.xiuchat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

public class SendDynamicActivity extends AppCompatActivity {
    private String URL;
    private EditText content;

    // 设置 Toolbar 和事件
    private void setBar() {
        Toolbar toolbar = findViewById(R.id.edit_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("发送动态");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_dynamic_layout);

        URL = ProperTies.getProperties().getProperty("URL");
        content = findViewById(R.id.send_edit_text);

        setBar();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_btn:
                HashMap<String, String> parm = new HashMap<>();

                parm.put("_id", new Global(getApplicationContext()).getId());
                parm.put("token", new Global(getApplicationContext()).getToken());
                parm.put("type", "0");
                parm.put("share", content.getText().toString());
                parm.put("story", "");

                if (!content.getText().toString().isEmpty()) {
                    NetRequest.postFormRequest(URL + "/api/createDynamic", parm, new NetRequest.DataCallBack() {
                        @Override
                        public void requestSuccess(String result) throws Exception {
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                            if (jsonObject.get("status").getAsInt() == 1) {
                                Toast.makeText(SendDynamicActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SendDynamicActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void requestFailure(Request request, IOException e) {
                            Toast.makeText(SendDynamicActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
