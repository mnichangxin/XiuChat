/*
 * Android SQLite 数据库初始化
 */
package com.lichangxin.xiuchat.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static String name = "xiuchat.db";
    private static int version = 1;

    // 登录表 SQL
    private String sql_login = "create table user(" +
            "id integer primary key autoincrement," +
            "username varchar(64)," +
            "password varchar(64)" +
            ");";

    public DBOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_login);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
}
