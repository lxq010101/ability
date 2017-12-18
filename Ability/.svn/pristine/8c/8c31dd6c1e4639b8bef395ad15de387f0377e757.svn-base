package com.ustcinfo.mobile.platform.ability.activity.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.transition.ArcMotion;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.sdsmdg.tastytoast.TastyToast;
import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.presenter.common.BasePresenter;
import com.ustcinfo.mobile.platform.ability.utils.CustomChangeBounds;
import com.ustcinfo.mobile.platform.ability.view.common.BaseView;
import com.ustcinfo.mobile.platform.ability.widgets.LoadingDialog;
import com.ustcinfo.mobile.platform.ability.widgets.SwipeWindowHelper;

import org.greenrobot.eventbus.EventBus;



/**
 * Created by 学祺 on 2017/10/16.
 */

@SuppressLint("NewApi")
public abstract class BaseActivity<P extends BasePresenter> extends FragmentActivity implements SwipeWindowHelper.SlideBackManager, BaseView {
    private SwipeWindowHelper mSwipeBackHelper;

    protected P mvpPresenter;
    // private ToastUtil toastUtil;

    protected Activity mActivity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        mActivity = this ;
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        mvpPresenter = createPresenter();
        if (getActivityViewByView() != null) {
            setContentView(getActivityViewByView());
        } else {
            setContentView(getLayoutId());
        }
        setTitle();
        init();

    }

    /**
     * 获得布局id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化presenter
     *
     * @return
     */
    protected abstract P createPresenter();

    /**
     * 如果通过代码创建View，则使用此方法
     *
     * @return
     */
    protected View getActivityViewByView() {
        return null;
    }

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 以渐变方式跳转Activity
     * android 5.0以后特有的
     *
     * @param clazz
     */
    public void startActivityWithTransitionAnimation(Class clazz) {
        Intent intent = new Intent(this, clazz);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * 以渐变方式跳转Activity
     * android 5.0以后特有的
     *
     * @param clazz
     */
    public void startActivityWithTransitionAnimation(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * 以渐变方式跳转Activity
     * android 5.0以后特有的
     *
     * @param clazz
     * @param view
     */
    public void startActivityWithTransitionAnimation(Class clazz, View view) {
        Intent intent = new Intent(this, clazz);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                    (this, view, "transition_morph_view");

            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * 以渐变方式跳转Activity
     * android 5.0以后特有的
     *
     * @param clazz
     * @param view
     */
    public void startActivityWithTransitionAnimation(Class clazz, Bundle bundle, View view) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                    (this, view, "transition_morph_view");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }


    /**
     * 设置跳转回来的view
     */
    public void setContainer(View container) {
        if (Build.VERSION.SDK_INT >= 21) {
            //定义ArcMotion
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);

            //插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

            //实例化自定义的ChangeBounds
            CustomChangeBounds changeBounds = new CustomChangeBounds();

            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(container);

            //将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }
    }

    /**
     * 弹出加载框
     */
    @Override
    public void showProgressBar(String... msg) {
        if (msg.length != 0)
            LoadingDialog.show(this, msg[0]);
        else
            LoadingDialog.show(this);
    }

    /**
     * 关闭加载框
     */
    @Override
    public void closeProgressBar() {
        LoadingDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (supportEvent()) {
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        }
    }

    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    /**
     * 是否支持页面跳转动画
     *
     * @return
     */
    protected abstract boolean toggleOverridePendingTransition();



    /**
     * 是否支持EventBus
     *
     * @return
     */
    protected boolean supportEvent() {
        return false;
    }

    /**
     * 手势滑动返回
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mSwipeBackHelper == null) {
            mSwipeBackHelper = new SwipeWindowHelper(this);
        }
        return mSwipeBackHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public Activity getSlideActivity() {
        return this;
    }

    /**
     * 是否支持滑动返回
     * 如果Activity不需要滑动返回，则从写此方法，return false;
     *
     * @return
     */
    @Override
    public boolean supportSlideBack() {
        return false;
    }

    @Override
    public boolean canBeSlideBack() {
        return true;
    }

    @Override
    public void finish() {
        if(mSwipeBackHelper != null) {
            mSwipeBackHelper.finishSwipeImmediately();
            mSwipeBackHelper = null;
        }
        super.finish();
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    public void doBack(View view) {
        super.finish();
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    /**
     * 异常处理
     *
     * @param message
     */
    @Override
    public void error(String message) {
        showToast(message);
    }

    /**
     * 空页面处理
     */
    @Override
    public void empty() {
    }

    /**
     * 网络错误页面
     */
    @Override
    public void netError() {

    }

    /**
     * 吐司
     *
     * @param message
     */
    public void showToast(CharSequence message) {
        TastyToast.makeText(this, message.toString(), TastyToast.LENGTH_LONG, TastyToast.INFO);
    }

    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (supportEvent())
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        // DialogUtil.clear();
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view == null)
            return;
        if (view.getBackground() != null && view.getBackground() instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            view.setBackgroundResource(0);
            bd.setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

}
