package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.BaseFragment;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;
import com.lichangxin.xiuchat.utils.RecyclerAdapter;

import java.io.IOException;

import okhttp3.Request;

class CommunityRecyclerAdapter extends RecyclerAdapter {
    private JsonArray jsonArray;
    private JsonObject jsonObject;
    private TextView communityName;

    public CommunityRecyclerAdapter(int layout, int id, JsonArray jsonArray) {
        super(layout, id);

        this.jsonArray = jsonArray;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        jsonObject = jsonArray.get(position).getAsJsonObject();

        communityName = holder.itemView.findViewById(R.id.community_nickname);

        communityName.setText(jsonObject.get("group_name").getAsString());
    }
    @Override
    public int getItemCount() {
        return jsonArray.size();
    }
}

public class CommunityFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment, container, false);

        isPrepared = true;
        loadData();

        return view;
    }
    @Override
    protected void loadData() {
        if (!isPrepared || !isVisble) {
            return;
        }

        recyclerView = view.findViewById(R.id.community_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        URL = ProperTies.getProperties().getProperty("URL");

        NetRequest.getFormRequest(URL + "/api/recommendedGroup", null, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                if (jsonObject.get("status").getAsInt() == 0) {
                    Toast.makeText(getContext(), "暂无推荐", Toast.LENGTH_SHORT).show();
                } else if (jsonObject.get("status").getAsInt() == 1) {
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new CommunityRecyclerAdapter(R.layout.community_fragment_item, 0, jsonObject.get("data").getAsJsonArray()));
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