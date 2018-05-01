package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* 个人页 ViewPager + Fragment */
public class PersonalFragment extends Fragment {
    private Bundle args;
    private int page;
    private View view;
    private JsonObject userInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = args.getInt("page");

        if (page == 1) {
            view = inflater.inflate(R.layout.personal_dynamic_fragment, null);
        } else if (page == 2) {
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
