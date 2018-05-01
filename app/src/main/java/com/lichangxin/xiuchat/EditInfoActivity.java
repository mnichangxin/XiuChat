package com.lichangxin.xiuchat;

import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EditInfoActivity extends AppCompatActivity {
    private JsonObject userInfo;

    // 设置 Toolbar
    private void setBar() {
        Toolbar toolbar =  findViewById(R.id.edit_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("编辑资料");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    // 设置内容
    private void setContent() {
        EditText editNickname = findViewById(R.id.edit_nickname);
        EditText editBirthday = findViewById(R.id.edit_birthday);
        EditText editSex = findViewById(R.id.edit_sex);
        EditText editArea = findViewById(R.id.edit_area);
        EditText editSignature = findViewById(R.id.edit_signature);

        editNickname.setText(userInfo.get("nickname").getAsString());
        editNickname.setText(userInfo.get("nickname").getAsString());
        editNickname.setText(userInfo.get("nickname").getAsString());
        editNickname.setText(userInfo.get("nickname").getAsString());
        editNickname.setText(userInfo.get("nickname").getAsString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info_layout);

        userInfo = new JsonParser().parse(getIntent().getStringExtra("userInfo")).getAsJsonObject();

        setBar();
    }
}
