package com.lichangxin.xiuchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * 获取 SharedPreferences 中的持久化数据
 */

public class Global {
    private Context context;

    public Global(Context context) {
        this.context = context;
    }

    // 获取用户 _id
    public String getId() {
        SharedPreferences pf = context.getSharedPreferences("loginInfo", MODE_PRIVATE);

        return pf.getString("_id", null);
    }
    // 获取用户 token
    public String getToken() {
        SharedPreferences pf = context.getSharedPreferences("loginInfo", MODE_PRIVATE);

        return pf.getString("token", null);
    }
}
