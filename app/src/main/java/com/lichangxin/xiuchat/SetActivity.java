package com.lichangxin.xiuchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

public class SetActivity extends AppCompatActivity {
    private Button exitBtn;
    private String URL;

    private void setBar() {
        Toolbar toolbar =  findViewById(R.id.set_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("设置");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        exitBtn = findViewById(R.id.exit_login);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> parm = new HashMap<>();

                parm.put("_id", new Global(getApplicationContext()).getId());
                parm.put("token", new Global(getApplicationContext()).getToken());

                NetRequest.postFormRequest(URL + "/api/exitLogin", parm, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                        if (jsonObject.get("status").getAsInt() == 1) {
                            SharedPreferences pf = getSharedPreferences("loginInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pf.edit();
                            editor.remove("username");
                            editor.remove("password");
                            editor.commit();

                            Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SetActivity.this, "退出异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(SetActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_layout);

        URL = ProperTies.getProperties().getProperty("URL");

        setBar();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
