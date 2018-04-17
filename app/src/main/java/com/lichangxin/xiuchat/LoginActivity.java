package com.lichangxin.xiuchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;

import okhttp3.Request;

public class LoginActivity extends AppCompatActivity {
    private TextView switchLogin;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private Boolean switchStatus = false;
    private String URL = ProperTies.getProperties().getProperty("URL");

    // 跳转到主界面
    private void activityToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 登录注册验证
    private void verify(final String username, final String password, String type) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(Pattern.matches("^[1][3,4,5,6,7,8,9][0-9]{9}$", username) || Pattern.matches("[a-zA-Z_]+[0-9]*@(([a-zA-z0-9]-*)+\\\\.){1,3}[a-zA-z\\\\-]+", username))) {
            Toast.makeText(this, "用户名格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Pattern.matches("\\w{6,}", password)) {
            Toast.makeText(this, "密码至少6位长度", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> parms = new HashMap<>();

        parms.put("username", username);
        parms.put("password", password);

        switch (type) {
            case "login":
                NetRequest.postFormRequest(URL + "/api/login", parms, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                        if (jsonObject.get("status").getAsString().equals("0")) {
                            Toast.makeText(LoginActivity.this, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.get("status").getAsString().equals("1")) {
                            SharedPreferences pf = getSharedPreferences("loginInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pf.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("token", jsonObject.get("data").getAsJsonObject().get("token").getAsString());
                            editor.commit();

                            activityToMain();
                        } else {
                            Toast.makeText(LoginActivity.this, "网络错误，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(LoginActivity.this, "系统错误，请重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
                break;
            case "register":
                NetRequest.postFormRequest(URL + "/api/register", parms, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                        Toast.makeText(LoginActivity.this, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(LoginActivity.this, "系统错误，请重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pf = getSharedPreferences("loginInfo", MODE_PRIVATE);

        if (pf.getString("username", null) != null && pf.getString("password", null) != null) {
            verify(pf.getString("username", null), pf.getString("password", null), "login");
        }

        setContentView(R.layout.login_layout);

        loginButton = findViewById(R.id.login_button);
        switchLogin = findViewById(R.id.switch_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        // 登录和注册切换的事件
        switchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switchStatus) {
                    loginButton.setText("注 册");
                    switchLogin.setText("< 返回登录");
                } else {
                    loginButton.setText("登 录");
                    switchLogin.setText("新用户？点击这里注册");
                }
                switchStatus = !switchStatus;
            }
        });

        // 登录或注册事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (!switchStatus) {
                verify(username.getText().toString(), password.getText().toString(), "login");
            } else {
                verify(username.getText().toString(), password.getText().toString(), "register");
            }
            }
        });
    }
}
