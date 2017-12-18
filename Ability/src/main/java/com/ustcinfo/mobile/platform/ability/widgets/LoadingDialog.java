package com.ustcinfo.mobile.platform.ability.widgets;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;


import com.ustcinfo.mobile.platform.ability.R;

import java.lang.ref.WeakReference;

/**
 * 加载中对话框快速创建
 * Created by ChenRui on 2016/11/7 0007 17:00.
 */
public final class LoadingDialog {

    // 使用弱引用
    private static WeakReference<AppLoadingDialog> sInstance;

    private static AppLoadingDialog build(Context context) {
        AppLoadingDialog dialog;// 同一个处理
        if (sInstance != null && sInstance.get() != null) {
            dialog = sInstance.get();

            if (!((ContextWrapper) sInstance.get().getContext()).getBaseContext().equals(context)) {
                sInstance.get().dismiss();
                sInstance.clear();
                sInstance = null;
                dialog = new AppLoadingDialog(context);
                sInstance = new WeakReference<>(dialog);
            }

        } else {
            dialog = new AppLoadingDialog(context);
            sInstance = new WeakReference<>(dialog);
        }
        return dialog;
    }

    public static AppLoadingDialog show(Context context, String msg) {
        AppLoadingDialog dialog = build(context);
        if (!TextUtils.isEmpty(msg)) {
            dialog.setMessage(msg);
        }
        dialog.show();
        dialog.loading();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }


    public static AppLoadingDialog show(Context context, String msg,boolean isCancel) {
        AppLoadingDialog dialog = build(context);
        if (!TextUtils.isEmpty(msg)) {
            dialog.setMessage(msg);
        }
        dialog.show();
        dialog.loading();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(isCancel);
        return dialog;
    }


    public static AppLoadingDialog show(Context context) {
        AppLoadingDialog dialog = show(context, context.getString(R.string.tv_load));
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

        }
        return dialog;
    }


    public static void dismiss() {
        if (sInstance != null && sInstance.get() != null) {
            sInstance.get().dismiss();
            sInstance.clear();
        }
        sInstance = null;
    }
}
