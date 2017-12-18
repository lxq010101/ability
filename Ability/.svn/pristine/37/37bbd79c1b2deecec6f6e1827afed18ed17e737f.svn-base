package com.ustcinfo.mobile.platform.ability.map;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ustcinfo.mobile.platform.ability.apicallback.AbilityCallback;
import com.ustcinfo.mobile.platform.ability.jsbridge.CallBackFunction;

/**
 * Created by xueqili on 2017/11/14.
 */

public class LocationHelper implements AMapLocationListener {


    private static  LocationHelper instance;

    private static Context context;

    private CallBackFunction callBackFunction;


    private AbilityCallback<JSONObject> callback;

    //声明mlocationClient对象
    public static  AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public static AMapLocationClientOption mLocationOption = null;


    public synchronized static LocationHelper getInstance(Context cx, AbilityCallback<JSONObject> callback) {
        context = cx;
        if (instance == null) {
            instance = new LocationHelper();
            instance.callback =  callback;
            initOptions();
        }

        return instance;
    }


    public synchronized static LocationHelper getInstance(Context cx ,CallBackFunction function) {
        context = cx;
        if (instance == null) {
            instance = new LocationHelper();
            instance.callBackFunction = function;
            initOptions();
        }

        return instance;
    }


    public synchronized static void destory() {
        mlocationClient =  null;
        mLocationOption = null;
        instance = null;
    }


    private LocationHelper() {
    }

    private static  void initOptions() {

        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(instance);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setNeedAddress(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }

    public void startLocation() {
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息

                JSONObject jsonObject =new JSONObject();
                JSONObject locationInfo =new JSONObject();
                locationInfo.put("latitude",amapLocation.getLatitude());
                locationInfo.put("longitude",amapLocation.getLongitude());
                locationInfo.put("address",amapLocation.getAddress());
                locationInfo.put("province",amapLocation.getProvince());
                locationInfo.put("city",amapLocation.getCity());
                locationInfo.put("district",amapLocation.getDistrict());
                locationInfo.put("street",amapLocation.getStreet());
                locationInfo.put("cityCode",amapLocation.getCityCode());
                locationInfo.put("adCode",amapLocation.getAdCode());
                jsonObject.put("data",locationInfo);
                if(callBackFunction!=null) {
                    callBackFunction.onCallBack(jsonObject.toJSONString());
                }else if(callback!=null){
                    callback.callbcak(jsonObject);
                }
                mlocationClient.stopLocation();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}


