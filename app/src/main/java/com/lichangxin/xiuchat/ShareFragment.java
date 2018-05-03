package com.lichangxin.xiuchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;
import com.lichangxin.xiuchat.utils.RecyclerAdapter;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Request;

class ShareRecyclerAdapter extends RecyclerAdapter {
    private JsonArray userDynamic;
    private TextView nickname;
    private TextView content;
    private TextView commit;
    private TextView fav;

    public ShareRecyclerAdapter(int layout, JsonArray userDynamic) {
        super(layout);

        this.userDynamic = userDynamic;
    }
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        nickname = holder.itemView.findViewById(R.id.share_nickname);
        content = holder.itemView.findViewById(R.id.share_content);
        commit = holder.itemView.findViewById(R.id.share_commit);
        fav = holder.itemView.findViewById(R.id.share_fav);

        JsonObject jsonObject = userDynamic.get(position).getAsJsonObject();

        nickname.setText(jsonObject.get("nickname").getAsString());
        content.setText(jsonObject.get("share").getAsString());
        commit.setText(jsonObject.get("commit").getAsJsonArray().size() + "");
        fav.setText(jsonObject.get("fav").getAsString());
    }
    @Override
    public int getItemCount() {
        return userDynamic.size();
    }
}

public class ShareFragment extends Fragment {
    private String URL;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private void sendDynamic(View view) {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.float_action_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SendDynamicActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_fragment, container, false);

        URL = ProperTies.getProperties().getProperty("URL");

        recyclerView = view.findViewById(R.id.share_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        NetRequest.getFormRequest(URL + "/api/getAllUserDynamic", null, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                if (jsonObject.get("status").getAsInt() == 1) {
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new ShareRecyclerAdapter(R.layout.share_fragment_item, jsonObject.get("data").getAsJsonArray()));
                } else {
                    Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });

        sendDynamic(view);

        return view;
    }
}
