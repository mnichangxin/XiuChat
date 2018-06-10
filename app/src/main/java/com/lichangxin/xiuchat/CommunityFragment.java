package com.lichangxin.xiuchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CommunityRecyclerAdapter communityRecyclerAdapter;
    private String URL;

    private void request() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetRequest.getFormRequest(URL + "/api/recommendedGroup", null, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                        if (jsonObject.get("status").getAsInt() == 0) {
                            Toast.makeText(getContext(), "暂无推荐", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.get("status").getAsInt() == 1) {
                            recyclerView.setLayoutManager(layoutManager);

                            communityRecyclerAdapter = new CommunityRecyclerAdapter(R.layout.community_fragment_item, R.id.group_item, jsonObject.get("data").getAsJsonArray());

                            communityRecyclerAdapter.setmOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    TextView textView = view.findViewById(R.id.community_nickname);
                                    String name = textView.getText().toString();

                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("name", name);

                                    startActivity(intent);
                                }
                            });

                            recyclerView.setAdapter(communityRecyclerAdapter);

                            communityRecyclerAdapter.notifyDataSetChanged();

                            swipeRefreshLayout.setRefreshing(false);
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
        }, 300);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment, container, false);

        isPrepared = true;
        loadData();

        return view;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    protected void loadData() {
        if (!isPrepared || !isVisble) {
            return;
        }

        swipeRefreshLayout = view.findViewById(R.id.community_swiperefreshlayout);
        recyclerView = view.findViewById(R.id.community_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        URL = ProperTies.getProperties().getProperty("URL");

        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request();
            }
        });

        request();
    }
}