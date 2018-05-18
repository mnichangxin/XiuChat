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
import com.lichangxin.xiuchat.utils.Global;
import com.lichangxin.xiuchat.utils.NetRequest;
import com.lichangxin.xiuchat.utils.ProperTies;
import com.lichangxin.xiuchat.utils.RecyclerAdapter;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

class PersonalRecyclerAdapter extends RecyclerAdapter {
    private JsonArray userDynamic;
    private JsonObject userInfo;
    private TextView nickname;
    private TextView content;
    private TextView commit;
    private TextView fav;

    public PersonalRecyclerAdapter(int layout, int id, JsonArray userDynamic, JsonObject userInfo) {
        super(layout, id);

        this.userDynamic = userDynamic;
        this.userInfo = userInfo;
    }
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        nickname = holder.itemView.findViewById(R.id.share_nickname);
        content = holder.itemView.findViewById(R.id.share_content);
        commit = holder.itemView.findViewById(R.id.share_commit);
        fav = holder.itemView.findViewById(R.id.share_fav);

        JsonObject jsonObject = userDynamic.get(position).getAsJsonObject();

        nickname.setText(userInfo.get("nickname").getAsString());
        content.setText(jsonObject.get("share").getAsString());
        commit.setText(jsonObject.get("commit").getAsJsonArray().size() + "");
        fav.setText(jsonObject.get("fav").getAsString());
    }
    @Override
    public int getItemCount() {
        return userDynamic.size();
    }
}

/* 个人页 ViewPager + Fragment */
public class PersonalFragment extends Fragment {
    private Bundle args;
    private int page;
    private View view;
    private String URL;
    private JsonObject userInfo;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = args.getInt("page");
        URL = ProperTies.getProperties().getProperty("URL");

        if (page == 1) {
            // 获取个人动态
            view = inflater.inflate(R.layout.personal_dynamic_fragment, null);
            userInfo = new JsonParser().parse(args.getString("userInfo")).getAsJsonObject();

            recyclerView = view.findViewById(R.id.personal_dynamic_recyclerview);
            layoutManager = new LinearLayoutManager(getContext());

            HashMap<String, String> parm = new HashMap<>();

            parm.put("_id", new Global(getContext()).getId());

            NetRequest.getFormRequest(URL + "/api/getUserDynamic", parm, new NetRequest.DataCallBack() {
                @Override
                public void requestSuccess(String result) throws Exception {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();

                    if (jsonObject.get("status").getAsInt() == 1) {
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(new PersonalRecyclerAdapter(R.layout.share_fragment_item, 0, jsonObject.get("data").getAsJsonArray(), userInfo));
                    } else {
                        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void requestFailure(Request request, IOException e) {
                    Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (page == 2) {
            // 获取传过来的个人数据
            view = inflater.inflate(R.layout.personal_about_fragment, null);
            userInfo = new JsonParser().parse(args.getString("userInfo")).getAsJsonObject();

            TextView personal_birthday = view.findViewById(R.id.personal_birthday);
            TextView personal_sex = view.findViewById(R.id.personal_sex);
            TextView personal_area = view.findViewById(R.id.personal_area);
            TextView personal_signature = view.findViewById(R.id.personal_signature);

            personal_birthday.setText(userInfo.get("birthday").getAsString());
            personal_sex.setText(userInfo.get("sex").getAsString());
            personal_area.setText(userInfo.get("area").getAsString());
            personal_signature.setText(userInfo.get("signature").getAsString());
        }

        return view;
    }

    // 保存状态的静态类
    public static PersonalFragment newInstance(Bundle args) {
        PersonalFragment personalFragment = new PersonalFragment();
        personalFragment.setArguments(args);

        return personalFragment;
    }
}
