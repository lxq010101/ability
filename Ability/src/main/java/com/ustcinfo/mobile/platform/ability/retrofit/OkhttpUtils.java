package com.ustcinfo.mobile.platform.ability.retrofit;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.platform.Platform;

/**
 * Created by xueqili on 2017/12/25.
 */

public class OkhttpUtils {
    private static OkhttpUtils okhttpUtils;
    private static OkHttpClient okHttpClient;

    private OkhttpUtils(Context context) {

        okHttpClient = new OkHttpClient();
        try {
            setCertificates(context.getAssets().open("ustcinfo.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static OkhttpUtils getInstance(Context context) {
        if (okhttpUtils == null) {
            okhttpUtils = new OkhttpUtils(context);

        }
        return okhttpUtils;
    }

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            X509TrustManager trustManager = Platform.get().trustManager(sslContext.getSocketFactory());
            builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            builder.hostnameVerifier((s, sslSession) -> true);
            okHttpClient = builder.build();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void get(String url, Map<String, String> headers, Callback apiCallback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null && headers.size() != 0) {

            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                builder.header(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(apiCallback);
    }

    public void post(String url, Map<String, String> headers, Map<String, String> values, Callback apiCallback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null && headers.size() != 0) {

            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        builder.addHeader("content-type","application/json");
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (values != null && values.size() != 0) {

            Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        builder.post(formBodyBuilder.build());
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(apiCallback);
    }


    public void get(String url, Callback apiCallback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(apiCallback);
    }

    public void post(String url, Map<String, String> values, Callback apiCallback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        if (values != null && values.size() != 0) {

            Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        builder.addHeader("content-type","application/json");
        builder.post(formBodyBuilder.build());
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(apiCallback);

    }

}
