package com.ustcinfo.mobile.platform.ability.retrofit;


import com.alibaba.fastjson.JSONObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lixq on 2016/9/21.
 */
public interface  ApiStores {
//    String BASE_URL = "http://192.168.216.22:8080/eip-app-anhui/";
//    String BASE_URL  =  "http://192.168.80.71:8080/eip-app-web/";
    //生产环境
    String BASE_URL = "http://202.102.221.75:8231/eip-app-web/";


    @POST("static/index/api/getAnnouncementInfoList")
    @FormUrlEncoded
    Observable<JSONObject> getAnnouncements(@Field("data") String data);



    @POST("static/index/api/downloadAnnouncementImg")
    @FormUrlEncoded
    Observable<JSONObject> getAnnouncementImg(@Field("data") String data);


    @POST("static/index/api/getAnnouncementInfoById")
    @FormUrlEncoded
    Observable<JSONObject> getAnnouncementById(@Field("data") String data);

}