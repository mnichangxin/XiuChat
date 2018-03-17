package com.lichangxin.xiuchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView switchLogin;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private Boolean switchStatus = false;

    // 登录验证
    private void verifyLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
        } else {
            // 查询数据库
        }
    }
    // 注册验证
    private void verifyRegister(String username, String password) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    verifyLogin(username.getText().toString(), password.getText().toString());
                } else {
                    verifyRegister(username.getText().toString(), password.getText().toString());
                }
            }
        });
    }
}
