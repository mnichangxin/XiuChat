package com.lichangxin.xiuchat.utils;

/* Socket.io 初始化类 */

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class ChatApplication extends Application {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(ProperTies.getProperties().getProperty("URL"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}

