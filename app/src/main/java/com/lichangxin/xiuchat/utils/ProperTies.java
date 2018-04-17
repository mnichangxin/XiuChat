/*
 * 读取 APP 配置工具
 */
package com.lichangxin.xiuchat.utils;

import java.io.InputStream;
import java.util.Properties;

import okhttp3.internal.Util;

public class ProperTies {
    public static Properties getProperties() {
        Properties props = new Properties();

        try {
            InputStream in = Util.class.getResourceAsStream("/assets/appConfig");
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return props;
    }
}
