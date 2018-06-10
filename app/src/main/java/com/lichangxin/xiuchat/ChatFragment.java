package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.BaseFragment;
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;
import com.lichangxin.xiuchat.utils.RecyclerAdapter;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

class ChatFragmentAdapter extends RecyclerAdapter {
    private JsonArray jsonArray;
    private JsonObject jsonObject;
    private TextView chatName;

    public ChatFragmentAdapter(int layout, int id, JsonArray jsonArray) {
        super(layout, id);

        this.jsonArray = jsonArray;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        jsonObject = jsonArray.get(position).getAsJsonObject();

        chatName = holder.itemView.findViewById(R.id.chat_nickname);

        chatName.setText(jsonObject.get("name").getAsString());
    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }
}

public class ChatFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat_fragment, container, false);

        isPrepared = true;
        loadData();

        return view;
    }

    @Override
    protected void loadData() {
        if (!isPrepared || !isVisble) {
            return;
        }

        recyclerView = view.findViewById(R.id.chat_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        URL = ProperTies.getProperties().getProperty("URL");

        HashMap parm = new HashMap();

        parm.put("_id", new Global(getContext()).getId());
        parm.put("token", new Global(getContext()).getToken());

        NetRequest.postFormRequest(URL + "/api/getMsgList", parm, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                if (jsonObject.get("status").getAsInt() == 1) {
                    recyclerView.setLayoutManager(layoutManager);

                    ChatFragmentAdapter chatFragmentAdapter = new ChatFragmentAdapter(R.layout.chat_fragment_item, 0, jsonObject.get("data").getAsJsonArray());

                    recyclerView.setAdapter(chatFragmentAdapter);
                } else {
                    Toast.makeText(getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
