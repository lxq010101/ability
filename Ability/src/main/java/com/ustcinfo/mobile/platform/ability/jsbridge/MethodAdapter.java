package com.ustcinfo.mobile.platform.ability.jsbridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.apicallback.AbilityCallback;
import com.ustcinfo.mobile.platform.ability.map.LocationHelper;
import com.ustcinfo.mobile.platform.ability.qrcode.CaptureActivity;
import com.ustcinfo.mobile.platform.ability.utils.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import rx.Observable;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by xueqili on 2017/11/14.
 */

public class MethodAdapter {

    private final String TAG = "MethodAdapter";
    public static final String CODE = "CODE";

    private static final int REQUEST_TAKE_PHOTO = 0x111;
    private static final int REQUEST_PICK_PHOTO = 0x112;
    private static final int REQUEST_CODE = 0x113;

    private static String photoPath;
    private static MethodAdapter mInstance;
    private Context context;
    private AbilityCallback callback;

    private MethodAdapter(Context context) {
        this.context = context;
    }

    public static MethodAdapter getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MethodAdapter(context);
        }
        return mInstance;
    }


    public static void destory() {
        mInstance = null;
    }

    /**
     * 定位
     */
    private void getLocationInfo(AbilityCallback callback) {

        LocationHelper.getInstance(context, callback).startLocation();

    }

    /**
     * 拍照
     */
    private void takePhotos(AbilityCallback callback) {
        this.callback = callback;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG);
        if (!file.exists())
            file.mkdirs();// 创建文件夹
        photoPath = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        Uri uri = Uri.fromFile(new File(photoPath));
        //为拍摄的图片指定一个存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ((Activity) context).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 从相册选照片
     */
    private void registerPickPic(int picNum, AbilityCallback callback) {
        this.callback = callback;
        File file = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG);
        if (!file.exists())
            file.mkdirs();// 创建文件夹
        MultiImageSelector.create()
                .showCamera(false)
                .count(picNum)
                .start((Activity) context, REQUEST_PICK_PHOTO);
    }


    /**
     * 扫码界面
     */
    private void registerBarCode(AbilityCallback callback) {
        this.callback = callback;
        Intent intent = new Intent(context, CaptureActivity.class);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }


    /**
     * 拨打手机号码
     * callFlag 1:直接跳至拨号页面
     * 2:直接跳至拨打页面
     */
    private void telephoneCall(String telNum, int callFlag) {
        {
            if (callFlag == 1) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telNum));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if (callFlag == 2) {
                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
                context.startActivity(intentPhone);
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:


                    Luban.with(context)
                            .load(new File(photoPath))                                   // 传人要压缩的图片列表
                            .ignoreBy(100)                                  // 忽略不压缩图片的大小
                            .setTargetDir(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG)                        // 设置压缩后文件存储位置
                            .setCompressListener(new OnCompressListener() { //设置回调
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    File f = new File(photoPath);
                                    if (f.delete()) {
                                        if (file.renameTo(new File(photoPath))) {
                                            JSONObject jsonObject = new JSONObject();
                                            JSONObject photoInfo = new JSONObject();
                                            photoInfo.put("photoName", new File(photoPath).getName());
                                            photoInfo.put("photoPath", new File(photoPath).getAbsoluteFile());
                                            jsonObject.put("data", photoInfo);
                                            callback.callbcak(jsonObject.toJSONString());
                                        }

                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    TastyToast.makeText(context, context.getString(R.string.photo_exception), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            }).

                            launch();    //启动压缩
                    break;
                case REQUEST_PICK_PHOTO:
                    ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    List<File> files = new ArrayList<>();
                    Luban.with(context)
                            .load(list)                             // 传人要压缩的图片列表
                            .ignoreBy(100)                                  // 忽略不压缩图片的大小
                            .setTargetDir(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG)                        // 设置压缩后文件存储位置
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(File file) {
                                    files.add(file);

                                    if (files.size() == list.size()) {
                                        JSONObject jsonObject = new JSONObject();
                                        JSONArray photoInfos = new JSONArray();
                                        Observable.from(files).subscribe(f -> {
                                            File dest = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG, +System.currentTimeMillis() + ".jpg");
                                            f.renameTo(dest);
                                            JSONObject photoInfo = new JSONObject();
                                            photoInfo.put("photoName", dest.getName());
                                            photoInfo.put("photoPath", dest.getAbsoluteFile());
                                            photoInfos.add(photoInfo);

                                        });
                                        jsonObject.put("data", photoInfos);
                                        callback.callbcak(jsonObject.toJSONString());
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    TastyToast.makeText(context, context.getString(R.string.photo_exception), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            })
                            .

                                    launch();    //启动压缩

                    break;

                case REQUEST_CODE:
                    JSONObject json = new JSONObject();
                    json.put("data", data.getStringExtra(CODE));
                    callback.callbcak(json.toJSONString());
                    break;


            }
    }


}
