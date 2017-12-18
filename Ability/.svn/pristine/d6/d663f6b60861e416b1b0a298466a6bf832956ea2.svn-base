package com.ustcinfo.mobile.platform.ability.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.ustcinfo.mobile.platform.ability.R;


/**
 * 普通弹出基类
 * Created by ChenRui on 2016/11/7 0007 14:51.
 */
public class AppDialog extends Dialog {

    public AppDialog(Context context) {
        this(context, R.style.AppDialog);
    }

    public AppDialog(Context context, int themeResId) {
        super(context, themeResId);
        if (themeResId == R.style.AppDialog)
            init();
    }

    protected void init() {
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.gravity = Gravity.LEFT | Gravity.RIGHT | Gravity.CENTER;
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(attr);
    }

}
