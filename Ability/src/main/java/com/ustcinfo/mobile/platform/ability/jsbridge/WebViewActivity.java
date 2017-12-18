package com.ustcinfo.mobile.platform.ability.jsbridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.event.WebViewEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * web
 */

public class WebViewActivity extends Activity implements OnClickListener {
    private AudioManager mgr;
    private BridgeWebView webView;
    private String path;
    Button button;
    ValueCallback<Uri> mUploadMessage;
    private Context mContext;
    //    CircleProgressDialog progress;
    Handler handler = new Handler();
    public boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", 0);
        //web app需要隐藏actionbar
        if (type == 2)
            requestBaseWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);
        String url = getIntent().getStringExtra("url");
        mgr = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        mContext = this;
        //初始化loading框
        webView = (BridgeWebView) findViewById(R.id.web_view);
        webView.setDefaultHandler(new DefaultHandler());
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);
//		// WebView加载web资源
//		path = "file://" + getIntent().getStringExtra("path");
        //工单调度
        if (type == 2) {
            webView.loadUrl(getIntent().getStringExtra("webAppExtractPath") + url);
        } else {
//            url = new StringBuilder().append(url).append("?userId=").append(getIntent().getStringExtra("userId")).toString();
//            webView.loadUrl(url);
            webView.loadUrl("file:///android_asset/basic.html");
        }
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        JsMethodAdapter.register(webView);

    }


    WebChromeClient wvcc = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, android.webkit.JsResult result) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            result.confirm();
            return true;
        }

        ;

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {
            return true;
        }

        ;

        // For Android > 4.1.1
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String AcceptType, String capture) {
            this.openFileChooser(uploadMsg);
        }

        // For Android 3.0+
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String AcceptType) {
            this.openFileChooser(uploadMsg);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        JsMethodAdapter.getmInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (button.equals(v)) {
            webView.callHandler("functionInJs", "data from Java",
                    new CallBackFunction() {

                        @Override
                        public void onCallBack(String data) {
//							Log.i(TAG, "reponse data from js " + data);
                        }

                    });
        }
    }

    @Override
    public void onDestroy() {
//        if(progress.isShowing()){
//            progress.dismiss();
//        }
        super.onDestroy();
        JsMethodAdapter.unRegister();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

        } else {
            setResult(Activity.RESULT_OK);
            this.finish();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void requestBaseWindowFeature(int featureId) {
        requestWindowFeature(featureId);
    }


}
