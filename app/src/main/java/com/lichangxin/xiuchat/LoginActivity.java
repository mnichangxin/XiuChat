package com.lichangxin.xiuchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        System.exit(0);
        return super.onKeyDown(keyCode, event);
    }
}
