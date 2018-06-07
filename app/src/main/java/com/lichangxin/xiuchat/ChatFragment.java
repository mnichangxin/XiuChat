package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;
import com.lichangxin.xiuchat.utils.BaseFragment;
import com.lichangxin.xiuchat.utils.ChatApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    public static final int TYPE_ACTION = 3;

    private String text;
    private int type;

    public Msg(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return this.text;
    }
    public int getType() {
        return this.type;
    }
}

class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {
    private List<Msg> mMsgList;

    public ChatRecyclerAdapter(List<Msg> msgList) {
        this.mMsgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_fragment_message_item, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);

        if (msg.getType() == Msg.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.actionMsg.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getText());
        } else if (msg.getType() == Msg.TYPE_SENT) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.actionMsg.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getText());
        } else if (msg.getType() == Msg.TYPE_ACTION) {
            holder.actionMsg.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.actionMsg.setText(msg.getText());
        }
    }
    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        LinearLayout actionLayout;

        TextView leftMsg;
        TextView rightMsg;
        TextView actionMsg;

        public ViewHolder(View view) {
            super(view);

            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            actionLayout = view.findViewById(R.id.action_layout);

            leftMsg = view.findViewById(R.id.left_msg_text);
            rightMsg = view.findViewById(R.id.right_msg_text);
            actionMsg = view.findViewById(R.id.action_msg_text);
        }
    }
}

public class ChatFragment extends BaseFragment {
    private View view;
    private ImageButton sendButton;
    private EditText msgText;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatRecyclerAdapter chatRecyclerAdapter;
    private List<Msg> msgList = new ArrayList<>();
    private Socket mSocket;

    private Boolean isConnected = false;

//    private void initMsgs() {
//        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
//        Msg msg2 = new Msg("Hello. Who is that.", Msg.TYPE_SENT);
//        Msg msg3 = new Msg("This is Tom. Nice talking to you.", Msg.TYPE_RECEIVED);
//        Msg msg4 = new Msg("小李加入了群聊", Msg.TYPE_ACTION);
//        Msg msg5 = new Msg("Hello, I am xiaoli", Msg.TYPE_RECEIVED);
//
//        msgList.add(msg1);
//        msgList.add(msg2);
//        msgList.add(msg3);
//        msgList.add(msg4);
//        msgList.add(msg5);
//    }

    // 刷新消息
    private void refreshMessage() {
        chatRecyclerAdapter.notifyItemInserted(msgList.size() - 1);
        recyclerView.scrollToPosition(msgList.size() - 1);
    }

    // 监听连接
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        Log.i("ChatApplication", "Connected");

                        isConnected = true;

                        JsonObject jsonObject = new JsonObject();

                        jsonObject.addProperty("room", "room1");
                        jsonObject.addProperty("name", "小李");

                        mSocket.emit("join", jsonObject);
                    }
                }
            });
        }
    };

    // 监听断开
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("ChatApplication", "Diconnected");

                    isConnected = false;
                }
            });
        }
    };

    // 监听连接异常或超时
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("ChatApplication", "ConnectError");
                }
            });
        }
    };

    // 监听用户加入房间
    private Emitter.Listener onUserJoin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String msg = null;

                    try {
                        msg = data.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Msg message = new Msg(msg, 3);

                    msgList.add(message);

                    refreshMessage();
                }
            });
        }
    };

    // 监听用户离开房间
    private Emitter.Listener onUserLeave = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String msg = null;

                    try {
                        msg = data.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Msg message = new Msg(msg, 3);

                    msgList.add(message);

                    refreshMessage();
                }
            });
        }
    };

    // 监听用户发送消息
    private Emitter.Listener onUserMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String name = null;
                    String msg = null;

                    try {
                        name = data.getString("name");
                        msg = data.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Msg message = new Msg(msg, 0);

                    msgList.add(message);

                    refreshMessage();
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatApplication app = (ChatApplication) getActivity().getApplication();

        mSocket = app.getSocket();

        // 系统级监听事件
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        // 自定义事件
        mSocket.on("join", onUserJoin);
        mSocket.on("message", onUserMessage);

        // 开启连接
        mSocket.connect();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat_fragment, container, false);

        isPrepared = true;
        loadData();

        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("join", onUserJoin);
    }

    @Override
    protected void loadData() {
        if (!isPrepared || !isVisble) {
            return;
        }

//        initMsgs();

        sendButton = view.findViewById(R.id.message_send_btn);
        msgText = view.findViewById(R.id.message_input);

        recyclerView = view.findViewById(R.id.message_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        chatRecyclerAdapter = new ChatRecyclerAdapter(msgList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatRecyclerAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = msgText.getText().toString().trim();

                if (isConnected == false) {
                    return;
                }

                if (TextUtils.isEmpty(text)) {
                    msgText.requestFocus();

                    return;
                }

                Msg msg = new Msg(text, 1);
                msgList.add(msg);

                refreshMessage();

                msgText.setText("");

                JsonObject data = new JsonObject();

                data.addProperty("name", "小李");
                data.addProperty("msg", text);

                mSocket.emit("message", data);
            }
        });
    }
}
