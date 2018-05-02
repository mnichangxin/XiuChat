package com.lichangxin.xiuchat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

public class EditInfoActivity extends AppCompatActivity {
    private JsonObject userInfo;
    private EditText editNickname;
    private EditText editBirthday;
    private EditText editSex;
    private EditText editArea;
    private EditText editSignature;
    private String URL;

    // 设置 Toolbar 和事件
    private void setBar() {
        Toolbar toolbar =  findViewById(R.id.edit_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("编辑资料");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        Button saveButton = findViewById(R.id.edit_save);
        Button cancelButton = findViewById(R.id.edit_cancel);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String, String> parm = new HashMap<>();

                parm.put("_id", new Global(getApplicationContext()).getId());
                parm.put("token", new Global(getApplicationContext()).getToken());
                parm.put("nickname", editNickname.getText().toString());
                parm.put("birthday", editBirthday.getText().toString());
                parm.put("sex", editSex.getText().toString());
                parm.put("area", editArea.getText().toString());
                parm.put("signature", editSignature.getText().toString());

                NetRequest.postJsonRequest(URL + "/api/updateUserInfo", parm, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject =  parser.parse(result).getAsJsonObject();

                        if (jsonObject.get("status").getAsInt() == 1) {
                            JsonObject newUserInfo =  new JsonParser().parse(new Gson().toJson(parm)).getAsJsonObject();
                            newUserInfo.add("following_num", userInfo.get("following_num"));
                            newUserInfo.add("followed_num", userInfo.get("followed_num"));

                            userInfo = newUserInfo;

                            Toast.makeText(EditInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditInfoActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(EditInfoActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditInfoActivity.this, PersonalActivity.class);
                intent.putExtra("userInfo", userInfo.toString());
                startActivity(intent);
                finish();
            }
        });
    }

    // 填充 EditText
    private void setContent() {
        editNickname.setText(userInfo.get("nickname").getAsString());
        editBirthday.setText(userInfo.get("birthday").getAsString());
        editSex.setText(userInfo.get("sex").getAsString());
        editArea.setText(userInfo.get("area").getAsString());
        editSignature.setText(userInfo.get("signature").getAsString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info_layout);

        userInfo = new JsonParser().parse(getIntent().getStringExtra("userInfo")).getAsJsonObject();
        editNickname = findViewById(R.id.edit_nickname);
        editBirthday = findViewById(R.id.edit_birthday);
        editSex = findViewById(R.id.edit_sex);
        editArea = findViewById(R.id.edit_area);
        editSignature = findViewById(R.id.edit_signature);
        URL = ProperTies.getProperties().getProperty("URL");

        setBar();
        setContent();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EditInfoActivity.this, PersonalActivity.class);
                intent.putExtra("userInfo", userInfo.toString());
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
