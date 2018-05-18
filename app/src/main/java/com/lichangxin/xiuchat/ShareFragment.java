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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lichangxin.xiuchat.utils.CustomDialog;
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;
import com.lichangxin.xiuchat.utils.RecyclerAdapter;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

class ShareRecyclerAdapter extends RecyclerAdapter {
    private JsonArray userDynamic;
    private TextView nickname;
    private TextView content;
    private TextView forward;
    private TextView commit;
    private TextView fav;
    private LinearLayout activeForward;
    private LinearLayout activeCommit;
    private LinearLayout activeFav;

    private String URL;
    private Context context;

    public ShareRecyclerAdapter(int layout, int id, JsonArray userDynamic, Context context) {
        super(layout, id);

        this.userDynamic = userDynamic;
        this.context = context;
        this.URL = ProperTies.getProperties().getProperty("URL");
    }
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {
        nickname = holder.itemView.findViewById(R.id.share_nickname);
        content = holder.itemView.findViewById(R.id.share_content);
        forward = holder.itemView.findViewById(R.id.share_forward);
        commit = holder.itemView.findViewById(R.id.share_commit);
        fav = holder.itemView.findViewById(R.id.share_fav);
        activeForward = holder.itemView.findViewById(R.id.forward_active);
        activeCommit = holder.itemView.findViewById(R.id.commit_active);
        activeFav = holder.itemView.findViewById(R.id.fav_active);

        final JsonObject jsonObject = userDynamic.get(position).getAsJsonObject();

        nickname.setText(jsonObject.get("nickname").getAsString());
        content.setText(jsonObject.get("share").getAsString());
        forward.setText(jsonObject.get("forward").getAsString());
        commit.setText(jsonObject.get("commit").getAsJsonArray().size() + "");
        fav.setText(jsonObject.get("fav").getAsString());

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShareDetailActivity.class);
                intent.putExtra("dynamicId", jsonObject.get("_id").getAsString());
                intent.putExtra("isCommit", false);
                context.startActivity(intent);
            }
        });
        activeForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomDialog customDialog = new CustomDialog(context);

                customDialog.show();
                customDialog.setEditVisibility(false);
                customDialog.setHintText("确定转发这条动态？");
                customDialog.setLeftButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                });
                customDialog.setRightButton("转发", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, String> parm = new HashMap<>();

                        parm.put("_id", new Global(context).getId());
                        parm.put("token", new Global(context).getToken());
                        parm.put("dynamic_id", jsonObject.get("_id").getAsString());

                        NetRequest.postFormRequest(URL + "/api/forwardDynamic", parm, new NetRequest.DataCallBack() {
                            @Override
                            public void requestSuccess(String result) throws Exception {
                                JsonParser jsonParser = new JsonParser();
                                JsonObject obj = jsonParser.parse(result).getAsJsonObject();

                                if (obj.get("status").getAsInt() == 1) {
                                    customDialog.dismiss();
                                    Toast.makeText(context, "转发成功", Toast.LENGTH_SHORT);
                                } else {
                                    Toast.makeText(context, "转发失败", Toast.LENGTH_SHORT);
                                }
                            }
                            @Override
                            public void requestFailure(Request request, IOException e) {
                                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
            }
        });
        activeCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShareDetailActivity.class);
                intent.putExtra("dynamicId", jsonObject.get("_id").getAsString());
                intent.putExtra("isCommit", true);

                context.startActivity(intent);
            }
        });
        activeFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ImageView iView = view.findViewById(R.id.fav_active_image);
                final TextView  tView = view.findViewById(R.id.share_fav);

                HashMap<String, String> parm = new HashMap<>();

                parm.put("_id", new Global(context).getId());
                parm.put("token", new Global(context).getToken());
                parm.put("dynamic_id", jsonObject.get("_id").getAsString());

                NetRequest.postFormRequest(URL + "/api/favDynamic", parm, new NetRequest.DataCallBack() {
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject obj = jsonParser.parse(result).getAsJsonObject();

                        if (obj.get("status").getAsInt() == 1) {
                            tView.setText((Integer.parseInt(tView.getText().toString()) + 1) + "");
                            iView.setImageResource(R.drawable.ic_fav_active);
                        } else if (obj.get("status").getAsInt() == 2) {
                            tView.setText((Integer.parseInt(tView.getText().toString()) - 1) + "");
                            iView.setImageResource(R.drawable.ic_fav);
                        } else {
                            Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return userDynamic.size();
    }
}

public class ShareFragment extends Fragment {
    private String URL;
    private Context context;
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
        context = getContext();

        recyclerView = view.findViewById(R.id.share_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());

        NetRequest.getFormRequest(URL + "/api/getAllUserDynamic", null, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                if (jsonObject.get("status").getAsInt() == 1) {
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new ShareRecyclerAdapter(R.layout.share_fragment_item, 0, jsonObject.get("data").getAsJsonArray(), context));
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
