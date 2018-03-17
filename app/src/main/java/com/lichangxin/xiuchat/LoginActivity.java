package com.lichangxin.xiuchat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import com.lichangxin.xiuchat.utils.DBOpenHelper;

public class LoginActivity extends AppCompatActivity {
    private TextView switchLogin;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private Boolean switchStatus = false;

    // 登录注册验证
    private void verify(String username, String password, String type) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Pattern.matches("[a-zA-Z_]+[0-9]*@(([a-zA-z0-9]-*)+\\\\.){1,3}[a-zA-z\\\\-]+", username) && Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", username)) {
            Toast.makeText(this, "用户名格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Pattern.matches("\\w{6,}", password)) {
            Toast.makeText(this, "密码至少6位长度", Toast.LENGTH_SHORT).show();
            return;
        }

        DBOpenHelper helper = new DBOpenHelper(LoginActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        Cursor cursor = db.query("user", null, null, null, null, null, null);

        switch (type) {
            case "login":
                Boolean loginSuccess = false;

                if (cursor.moveToFirst()) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (cursor.getString(1).equals(username) && cursor.getString(2).equals(password)) {
                            loginSuccess = true;
                            break;
                        }
                    }
                }
                if (!loginSuccess) {
                    Toast.makeText(this, "用户名或密码有误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case "register":
                Boolean regSuccess = true;

                if (cursor.moveToFirst()) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (cursor.getString(1).equals(username)) {
                            regSuccess = false;
                            break;
                        }
                    }
                }
                if (regSuccess) {
                    db.insert("user", null, contentValues);
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "该用户名已注册过", Toast.LENGTH_SHORT).show();
                }
                break;
        }

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
                    verify(username.getText().toString(), password.getText().toString(), "login");
                } else {
                    verify(username.getText().toString(), password.getText().toString(), "register");
                }
            }
        });
    }
}
