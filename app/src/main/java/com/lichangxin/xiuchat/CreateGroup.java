package com.lichangxin.xiuchat;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class CreateGroup extends AppCompatActivity {
    private EditText groupName;
    private EditText groupInfo;
    private String URL;

    // 设置 Toolbar 和事件
    private void setBar() {
        Toolbar toolbar =  findViewById(R.id.create_group_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("创建群聊");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        Button createButton = findViewById(R.id.create_group_btn);
        Button cancelButton = findViewById(R.id.create_group_cancel);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupName.getText().toString().isEmpty()) {
                    return;
                }

                final HashMap<String, String> parm = new HashMap<>();

                parm.put("_id", new Global(getApplicationContext()).getId());
                parm.put("token", new Global(getApplicationContext()).getToken());
                parm.put("name", groupName.getText().toString());

                NetRequest.postJsonRequest(URL + "/api/createGroup", parm, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject =  parser.parse(result).getAsJsonObject();

                        if (jsonObject.get("status").getAsInt() == 1) {
                            Toast.makeText(CreateGroup.this, "创建成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CreateGroup.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CreateGroup.this, "创建失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(CreateGroup.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroup.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_layout);

        groupName = findViewById(R.id.create_group_name);
        groupInfo = findViewById(R.id.create_group_info);

        URL = ProperTies.getProperties().getProperty("URL");

        setBar();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CreateGroup.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
