package com.lichangxin.xiuchat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.CustomDialog;
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

public class ShareDetailActivity extends AppCompatActivity {
    private String dynamicId;
    private Boolean isCommit;
    private TextView nickname;
    private TextView content;
    private TextView commit;
    private TextView fav;

    private String URL;

    // 设置 Toolbar 和事件
    private void setBar() {
        Toolbar toolbar = findViewById(R.id.edit_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("分享正文");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    // 填充数据
    private void setContent() {
        nickname = findViewById(R.id.share_detail_nickname);
        content = findViewById(R.id.share_detail_content);
        commit = findViewById(R.id.share_detail_commit);
        fav = findViewById(R.id.share_detail_fav);

        HashMap<String, String> parm = new HashMap<>();

        parm.put("_id", dynamicId);

        NetRequest.getFormRequest(URL + "/api/getDynamicDetail", parm, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                if (jsonObject.get("status").getAsInt() == 0) {
                    Toast.makeText(ShareDetailActivity.this, "动态不存在", Toast.LENGTH_SHORT);
                } else if (jsonObject.get("status").getAsInt() == 1) {
                    JsonObject jsonData = jsonObject.get("data").getAsJsonObject();

                    JsonObject userInfo = jsonData.get("userInfo").getAsJsonObject(); // 发布者信息
                    JsonArray commiterInfo = jsonData.get("commiterInfo").getAsJsonArray(); // 评论者信息

                    nickname.setText(userInfo.get("nickname").getAsString());
                    content.setText(jsonData.get("share").getAsString());
                    commit.setText(commiterInfo.size() + "");
                    fav.setText(jsonData.get("fav").getAsInt() + "");
                } else {
                    Toast.makeText(ShareDetailActivity.this, "网络错误", Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(ShareDetailActivity.this, "网络错误", Toast.LENGTH_SHORT);
            }
        });
    }

    // 显示评论弹框
    private void showDialog() {
        final CustomDialog dialog = new CustomDialog(this);

        dialog.show();
        dialog.setHintText("评论这条动态");
        dialog.setLeftButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setRightButton("评论", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> parm = new HashMap<>();

                parm.put("_id", new Global(getApplicationContext()).getId());
                parm.put("token", new Global(getApplicationContext()).getToken());
                parm.put("dynamic_id", dynamicId);
                parm.put("content", dialog.getEdit());

                NetRequest.postFormRequest(URL + "/api/commitDynamic", parm, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                        if (jsonObject.get("status").getAsInt() == 1) {
                            Toast.makeText(ShareDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(ShareDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(ShareDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_detail_layout);

        URL = ProperTies.getProperties().getProperty("URL");

        dynamicId = getIntent().getStringExtra("dynamicId");
        isCommit = getIntent().getBooleanExtra("isCommit", true);

        setBar();
        setContent();

        if (isCommit) {
            showDialog();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
