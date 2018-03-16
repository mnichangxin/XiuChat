package com.lichangxin.xiuchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pf = getSharedPreferences("view_page", MODE_PRIVATE);
        Boolean isOpen = pf.getBoolean("isOpen", false);

        if (!isOpen) {
            setContentView(R.layout.open_layout);

            Button button = (Button) findViewById(R.id.open_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OpenActivity.this, ViewSlideActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(OpenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
