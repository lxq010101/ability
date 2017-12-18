package com.ustcinfo.mobile.platform.ability.utils;

import android.os.Handler;
import android.os.Looper;

import static android.os.SystemClock.sleep;

/**
 * Created by xueqili on 2017/11/13.
 */

public class ThreadUtils {
    final static Handler mHandle;

    static {
        mHandle = new Handler(Looper.getMainLooper());
    }

    /**
     * 运行在主线程
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            mHandle.post(runnable);
        }
    }

    /**
     * 运行在主线程
     *
     * @param runnable
     * @param delayMillis
     */
    public static void runOnUiThread(long delayMillis, Runnable runnable) {
        mHandle.postDelayed(runnable, delayMillis);
    }

    /**
     * 运行在子线程
     *
     * @param runnable
     */
    public static void runOnBackgroundThread(Runnable runnable) {
        if (isMainThread()) {
            runOnBackgroundThread(0, runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * 运行在子线程
     *
     * @param runnable
     * @param delayMillis
     */
    public static void runOnBackgroundThread(final long delayMillis, final Runnable runnable) {
        ThreadPools.cachedThread().execute(new Runnable() {
            @Override
            public void run() {
                if (delayMillis > 0) {
                    sleep(delayMillis);
                }
                runnable.run();
            }
        });
    }

    /**
     * 当前线程是主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        boolean state = false;
        // Thread.currentThread() == Looper.getMainLooper().getThread();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            state = true;
        }
        return state;
    }
}

