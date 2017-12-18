package com.ustcinfo.mobile.platform.ability.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 解决多个recycleview嵌套滑动卡顿问题
 * lxq
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = false;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}