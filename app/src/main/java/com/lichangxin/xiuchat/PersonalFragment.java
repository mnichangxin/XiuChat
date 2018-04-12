package com.lichangxin.xiuchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* 个人页 ViewPager + Fragment */
public class PersonalFragment extends Fragment {
    private Bundle args;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, null);
        return view;
    }

    // 保存状态的静态类
    public static PersonalFragment newInstance(Bundle args) {
        PersonalFragment personalFragment = new PersonalFragment();
        personalFragment.setArguments(args);

        return personalFragment;
    }
}
