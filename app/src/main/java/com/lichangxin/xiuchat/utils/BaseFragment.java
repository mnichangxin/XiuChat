/*
 * Fragment 懒加载
 */

package com.lichangxin.xiuchat.utils;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    // 是否可见
    protected boolean isVisble;
    // 标志 Fragment 初始化完成
    public boolean isPrepared = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisble = true;
            onVisible();
        } else {
            isVisble = false;
            onInVisible();
        }
    }
    protected void onInVisible() {
    }
    protected void onVisible() {
        loadData();
    }
    protected abstract void loadData();
}