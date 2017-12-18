package com.ustcinfo.mobile.platform.ability.fragment.common;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.sdsmdg.tastytoast.TastyToast;
import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.presenter.common.BasePresenter;
import com.ustcinfo.mobile.platform.ability.view.common.BaseView;
import com.ustcinfo.mobile.platform.ability.widgets.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 学祺 on 2017/10/17.
 */
@SuppressLint("NewApi")
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private CompositeSubscription mCompositeSubscription;

    protected P mvpPresenter;
    protected boolean isFirst;
    protected File tempFile;
    private static final int PHOTO_REQUEST_CUT = 2;
    private Handler hander =new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(getLayoutId(), container, false);

        mvpPresenter = createPresenter();
        initView(view, savedInstanceState);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!isFirst) {
                initRequest();
                isFirst = true;
            }
        }
    }


    public void onUnSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();//取消注册，以避免内存泄露
        }
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscription);
    }


    /**
     * 以渐变方式跳转Activity
     * android 5.0以后特有的
     *
     * @param clazz
     */
    public void startActivityWithTransitionAnimation(Class clazz) {

        Intent intent = new Intent(getActivity(), clazz);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
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
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
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
        Intent intent = new Intent(getActivity(), clazz);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                    (getActivity(), view, "transition_morph_view");
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
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation
                    (getActivity(), view, "transition_morph_view");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
        if(supportEvent()) {
            if(EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }

    /**
     * 吐司
     *
     * @param message
     */
    public void showToast(CharSequence message) {
        TastyToast.makeText(getContext(), message.toString(), TastyToast.LENGTH_LONG, TastyToast.INFO);
    }

    /**
     * 弹出加载框
     */
    @Override
    public void showProgressBar(String... msg) {
        if (msg.length != 0)
            LoadingDialog.show(getActivity(), msg[0]);
        else
            LoadingDialog.show(getActivity());
    }

    /**
     * 关闭加载框
     */
    @Override
    public void closeProgressBar() {
        LoadingDialog.dismiss();
    }


    @Override
    public void setTitle() {

    }

    @Override
    public void error(String message) {
        showToast(message);
    }

    @Override
    public void empty() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if(supportEvent()) {
            if(! EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }



    /**
     * 是否支持EventBus
     *
     * @return
     */
    protected boolean supportEvent() {
        return false;
    }

    /**
     * 网络错误页面
     */
    @Override
    public void netError() {
        closeProgressBar();
        showToast("网络连接异常");
    }

    protected abstract int getLayoutId();


    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void initRequest();

    protected abstract P createPresenter();

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

}
