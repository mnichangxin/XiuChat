package com.lichangxin.xiuchat;

import android.os.Bundle;
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

import java.io.IOException;
import okhttp3.Request;

import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;
import com.lichangxin.xiuchat.utils.RecyclerAdapter;

class InterestRecyclerAdapter extends RecyclerAdapter {
    private JsonArray jsonArray;
    private TextView interestNickName;
    private TextView interestActivity;
    private TextView originNickName;
    private TextView originContent;

    public InterestRecyclerAdapter(int layout, int id, JsonArray jsonArray) {
        super(layout, id);

        this.jsonArray = jsonArray;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        interestNickName = holder.itemView.findViewById(R.id.interest_nickname);
        interestActivity = holder.itemView.findViewById(R.id.interest_activity);
        originNickName = holder.itemView.findViewById(R.id.origin_nickname);
        originContent = holder.itemView.findViewById(R.id.origin_content);

        JsonObject jsonObject = jsonArray.get(position).getAsJsonObject();
        JsonObject userInfo = jsonObject.get("userInfo").getAsJsonObject();
        JsonObject dynamicInfo = jsonObject.get("dynamicInfo").getAsJsonObject();
        int type = jsonObject.get("type").getAsInt();

        String activityType = "";

        switch (type) {
            case 0:
                activityType = "点赞";
                break;
            case 1:
                activityType = "评论";
                break;
            case 2:
                activityType = "转发";
                break;
        }

        interestNickName.setText(userInfo.get("nickname").getAsString());
        interestActivity.setText(activityType);
        originNickName.setText(dynamicInfo.get("origin_nickname").getAsString());
        originContent.setText(dynamicInfo.get("origin_content").getAsString());
    }
    @Override
    public int getItemCount() {
        return jsonArray.size();
    }
}

public class InterestFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.interest_fragment, container, false);

        recyclerView = view.findViewById(R.id.interest_recyclerview);

        layoutManager = new LinearLayoutManager(getContext());

        URL = ProperTies.getProperties().getProperty("URL");

        NetRequest.getFormRequest(URL + "/api/getAllUserActivity", null, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                if (jsonObject.get("status").getAsInt() == 1) {
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new InterestRecyclerAdapter(R.layout.interest_fragment_item, 0, jsonObject.get("data").getAsJsonArray()));
                } else {
                    Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
