package com.ustcinfo.mobile.platform.ability.application;

import android.app.Application;

import com.ustcinfo.mobile.platform.ability.utils.ActivityLifecycleHelper;

/**
 * Created by 学祺 on 2017/10/18.
 */

public class AbilityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }
}
