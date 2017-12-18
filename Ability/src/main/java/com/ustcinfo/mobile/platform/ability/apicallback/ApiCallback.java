package com.ustcinfo.mobile.platform.ability.apicallback;



/**
 * 李学祺
 * @param <T>
 */
public interface ApiCallback<T> {

    void onSuccess(T model);

    void onFailure(int code, String msg);

    void netError();

    void onCompleted();


}
