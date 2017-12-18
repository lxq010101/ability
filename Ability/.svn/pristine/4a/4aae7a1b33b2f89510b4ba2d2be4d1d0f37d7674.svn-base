package com.ustcinfo.mobile.platform.ability.jsbridge;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.database.database.DataBaseManager;
import com.database.database.SqliteDatabase;
import com.database.interfaces.SqliteInitICallBack;
import com.sdsmdg.tastytoast.TastyToast;
import com.ustcinfo.mobile.platform.ability.R;
import com.ustcinfo.mobile.platform.ability.event.WebViewEvent;
import com.ustcinfo.mobile.platform.ability.map.LocationHelper;
import com.ustcinfo.mobile.platform.ability.qrcode.CaptureActivity;
import com.ustcinfo.mobile.platform.ability.utils.Constants;
import com.ustcinfo.mobile.platform.ability.utils.Path;
import com.ustcinfo.mobile.platform.ability.widgets.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import rx.Observable;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by lxq on 2017/10/26.
 */

public class JsMethodAdapter {

    private static JsMethodAdapter mInstance;
    private BridgeWebView webView;
    public static final String CODE = "CODE";
    private static final int REQUEST_TAKE_PHOTO = 0x111;
    private static final int REQUEST_PICK_PHOTO = 0x112;
    private static final int REQUEST_CODE = 0x113;
    private static final int ADDRESS_BOOK = 0x114;
    private CallBackFunction callBackFunction;
    private String photoPath;
    private JSONObject json;
    private SqliteDatabase database;

    private JsMethodAdapter(BridgeWebView webView) {
        this.webView = webView;
    }

    public static JsMethodAdapter getmInstance() {
        return mInstance;
    }

    public static void register(BridgeWebView webView) {
        if (mInstance == null) {
            mInstance = new JsMethodAdapter(webView);
        }
        mInstance.init();
    }

    public static void unRegister() {
        mInstance = null;
    }

    private void init() {
        registerGetUserInfos();
        registerTakePhotos();
        registerBarCode();
        registerPickPic();
        registerShowLoading();
        registerCancelLoading();
        registerTelephoneCall();
        registerGetLocationInfo();
        registerShortMessage();
        registerAddressBook();
        registerGetPhoneDeviceName();
        registerCreateTable();
        registerInsertInfo();
        registerSelectInfos();
        registerdeleteInfo();
        registerUpdateInfo();
        registerDropTable();
    }

    /**
     * 获取手机型号（设备名称）
     */
    private void registerGetPhoneDeviceName(/*int i*/) {
        webView.registerHandler("getPhoneDeviceName", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(android.os.Build.MODEL);

            }
        });
    }

    /**
     * 通讯录
     */
    private void registerAddressBook() {
        webView.registerHandler("addressBook", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction = function;
                //进入系统通訊錄頁面
                Uri uri = ContactsContract.Contacts.CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                ((Activity) webView.getContext()).startActivityForResult(intent, ADDRESS_BOOK);

            }
        });
    }

    /**
     * 短信
     */
    private void registerShortMessage() {
        webView.registerHandler("shortMessage", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String targetNum = json.getString("telNum");
                String smsBody = json.getString("smsBody");
                //进入系统短信列表界面
                Uri smsToUri = Uri.parse("smsto:" + targetNum);
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                intent.putExtra("sms_body", smsBody);
                webView.getContext().startActivity(intent);
            }
        });
    }

    /**
     * getUserInfos 获取用户信息
     */
    private void registerGetUserInfos() {
        webView.registerHandler("getUserInfos", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                WebViewEvent event = new WebViewEvent();
                event.webView = webView;
                event.key = "getUserInfos";
                event.callBackFunction = function;
                EventBus.getDefault().post(event);
            }
        });
    }

    /**
     * 定位
     */
    private void registerGetLocationInfo() {
        webView.registerHandler("getLocationInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LocationHelper.getInstance(webView.getContext(), function).startLocation();
            }
        });
    }

    /**
     * 拍照
     */
    private void registerTakePhotos() {
        webView.registerHandler("takePhoto", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                callBackFunction = function;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG);
                if (!file.exists())
                    file.mkdirs();// 创建文件夹
                photoPath = file.getAbsolutePath() + "/" + json.getString("keyword") + "_" + System.currentTimeMillis() + ".jpg";
                Uri uri = Uri.fromFile(new File(photoPath));
                //为拍摄的图片指定一个存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                ((Activity) webView.getContext()).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });
    }

    /**
     * 从相册选照片
     */
    private void registerPickPic() {
        webView.registerHandler("pickPic", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                callBackFunction = function;
                File file = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG);
                if (!file.exists())
                    file.mkdirs();// 创建文件夹
                MultiImageSelector.create()
                        .showCamera(false)
                        .count(json.getIntValue("picNum"))
                        .start((Activity) webView.getContext(), REQUEST_PICK_PHOTO);
            }
        });
    }

    /**
     * 扫码界面
     */
    private void registerBarCode() {
        webView.registerHandler("scanGetCode", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                callBackFunction = function;
                Intent intent = new Intent(webView.getContext(), CaptureActivity.class);
                ((Activity) webView.getContext()).startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    /**
     * 加载框
     */
    private void registerShowLoading() {
        webView.registerHandler("showLoading", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                LoadingDialog.show(webView.getContext(), json.getString("showInfo"), json.getBoolean("isCancel"));
            }
        });
    }

    /**
     * 关闭加载框
     */
    private void registerCancelLoading() {
        webView.registerHandler("cancelLoading", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LoadingDialog.dismiss();
            }
        });
    }

    /**
     * 拨打手机号码
     * callFlag 1:直接跳至拨号页面
     * 2:直接跳至拨打页面
     */
    private void registerTelephoneCall() {
        webView.registerHandler("telephoneCall", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(webView.getContext(), "大吉大利11", Toast.LENGTH_LONG).show();
                json = JSONObject.parseObject(data).getJSONObject("params");
                if (json.getIntValue("callFlag") == 1) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + json.getString("telNum")));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    (webView.getContext()).startActivity(intent);
                } else if (json.getIntValue("callFlag") == 2) {
                    Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + json.getString("telNum")));
                    (webView.getContext()).startActivity(intentPhone);
                }
            }
        });
    }

    /**
     * 生成数据库表
     */
    private void registerCreateTable() {
        webView.registerHandler("createTableInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                JSONArray arrays = json.getJSONArray("column");
                database = DataBaseManager.getInstance().setDBName(Constants.DB_NAME)
                        .setDBVersion(Constants.DB_VERSION)
                        .setOnInitListener(new SqliteInitICallBack() {
                            @Override
                            public void onCreate(SQLiteDatabase db) {

                            }

                            @Override
                            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                                Log.e("tag","s");
                            }

                            @Override
                            public void onInitSuccess(SqliteDatabase db) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("create table if not exists ").append(tableName).append(" (");
                                for (int i = 0; i < arrays.size(); i++) {
                                    String type = "TEXT";
                                    JSONObject jsonObject = arrays.getJSONObject(i);
                                    switch (jsonObject.getString("type").toLowerCase()) {
                                        case "int":
                                            type = "INTEGER";
                                            break;
                                        case "string":
                                            type = "TEXT";
                                            break;
                                    }

                                    sb.append(" ").append(jsonObject.getString("name")).append(" ").append(type).append(" ").append(jsonObject.getBoolean("isId") ? "primary key " : ",").append(jsonObject.getBoolean("isAutoIncrement")?"AUTOINCREMENT ,":"");
                                    if (i == arrays.size() - 1) {
                                        sb.setLength(sb.length() - 1);
                                        sb.append(")");
                                    }

                                }

                                db.execSQL(sb.toString());
                            }

                            @Override
                            public void onInitFailed(String s) {
                                Log.e("tag","s");
                            }
                        }).initDataBase(webView.getContext()).getDatabase(webView.getContext());

            }
        });

    }

    /**
     * 往数据库插入数据
     */
    private void registerInsertInfo() {

        webView.registerHandler("insertInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                JSONArray arrays = json.getJSONArray("column");
                    StringBuilder sb = new StringBuilder();
                    sb.append("insert into ").append(tableName).append(" ( ");
                    for (int i = 0; i < arrays.size(); i++) {
                        JSONObject jsonObject = arrays.getJSONObject(i);
                        sb.append(jsonObject.getString("name")).append(",");
                        if (i == arrays.size() - 1) {
                            sb.setLength(sb.length() - 1);
                            sb.append(") values(");
                        }
                    }
                String type = "TEXT";
                for (int i = 0; i < arrays.size(); i++) {
                    JSONObject jsonObject = arrays.getJSONObject(i);
                    switch (jsonObject.getString("type").toLowerCase()) {
                        case "int":
                            sb.append("'").append(jsonObject.getIntValue("value")).append("' ,");
                            break;
                        case "string":
                            sb.append("'").append(jsonObject.getString("value")).append("' ,");
                            break;
                    }

                    if (i == arrays.size() - 1) {
                        sb.setLength(sb.length() - 1);
                        sb.append(")");
                    }
                }

                    getDatabase().execSQL(sb.toString());


            }
        });
    }


    /**
     * 查询数据库
     */
    private void registerSelectInfos() {

        webView.registerHandler("selectInfos", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                sb.append("select * from ").append(tableName);
                List<Map<String,String>> list =   getDatabase().find(sb.toString());
               String result =  JSONArray.toJSONString(list);
               function.onCallBack(result);


            }
        });
    }


    /**
     * 修改某条数据
     */
    private void registerUpdateInfo() {

        webView.registerHandler("updateInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                JSONArray arrays = json.getJSONArray("column");
                sb.append("update ").append(tableName).append(" set ");
                for (int i = 0; i < arrays.size(); i++) {
                    JSONObject jsonObject = arrays.getJSONObject(i);
                    sb.append(jsonObject.getString("name")).append("=");
                    switch (jsonObject.getString("type").toLowerCase()) {
                        case "int":
                            sb.append(jsonObject.getIntValue("value")).append(",");
                            break;
                        case "string":
                            sb.append("'").append(jsonObject.getString("value")).append("',");
                            break;
                    }
                    if (i == arrays.size() - 1) {
                        sb.setLength(sb.length() - 1);
                        sb.append(" where ");
                    }
                }
                JSONObject jsonObject = json.getJSONObject("condition");
                switch (jsonObject.getString("type").toLowerCase()) {
                    case "int":
                        sb.append(jsonObject.getString("name")).append("=").append(jsonObject.getIntValue("value"));
                        break;
                    case "string":
                        sb.append(jsonObject.getString("name")).append("='").append(jsonObject.getString("value")).append("'");
                        break;
                }
                getDatabase().execSQL(sb.toString());


            }
        });
    }



    /**
     * 删除某条数据
     */
    private void registerdeleteInfo() {

        webView.registerHandler("deleteInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                sb.append("delete from ").append(tableName).append(" where ");
                JSONObject jsonObject = json.getJSONObject("condition");
                switch (jsonObject.getString("type").toLowerCase()) {
                    case "int":
                        sb.append(jsonObject.getString("name")).append(" = ").append(jsonObject.getIntValue("value"));
                        break;
                    case "string":
                        sb.append(jsonObject.getString("name")).append(" = ").append(jsonObject.getString("value"));
                        break;
                }

                getDatabase().execSQL(sb.toString());


            }
        });
    }


    /**
     * 删除表
     */
    private void registerDropTable() {

        webView.registerHandler("dropTable", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                json = JSONObject.parseObject(data).getJSONObject("params");
                String tableName = json.getString("tableName");
                StringBuilder sb = new StringBuilder();
                sb.append("drop table if exists ").append(tableName);

                getDatabase().execSQL(sb.toString());


            }
        });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case ADDRESS_BOOK:
                    if (data == null)
                        return;
                    Uri uri = data.getData();
                    String[] contacts = getPhoneContacts(uri);
                    Toast.makeText(webView.getContext(), "姓名:" + contacts[0] + ", " + "手机号:" + contacts[1], Toast.LENGTH_LONG).show();
                    JSONObject contactsJson = new JSONObject();
                    contactsJson.put("phoneNum", contacts[1]);
                    contactsJson.put("name", contacts[0]);
                    mInstance.callBackFunction.onCallBack(contactsJson.toJSONString());
                    break;
                case REQUEST_TAKE_PHOTO:
                    Luban.with(webView.getContext())
                            .load(new File(photoPath))   // 传人要压缩的图片列表
                            .ignoreBy(100)               // 忽略不压缩图片的大小
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
                                            callBackFunction.onCallBack(jsonObject.toJSONString());
                                        }

                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    TastyToast.makeText(webView.getContext(), webView.getContext().getString(R.string.photo_exception), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            })
                            .launch();    //启动压缩
                    break;
                case REQUEST_PICK_PHOTO:
                    ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    List<File> files = new ArrayList<>();
                    Luban.with(webView.getContext())
                            .load(list)        // 传人要压缩的图片列表
                            .ignoreBy(100)    // 忽略不压缩图片的大小
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
                                            File dest = new File(Environment.getExternalStorageDirectory() + "/" + Path.PATH_DOWNLOAD_IMG, json.getString("keyword") + "_" + System.currentTimeMillis() + ".jpg");
                                            f.renameTo(dest);
                                            JSONObject photoInfo = new JSONObject();
                                            photoInfo.put("photoName", dest.getName());
                                            photoInfo.put("photoPath", dest.getAbsoluteFile());
                                            photoInfos.add(photoInfo);

                                        });
                                        jsonObject.put("data", photoInfos);
                                        callBackFunction.onCallBack(jsonObject.toJSONString());
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    TastyToast.makeText(webView.getContext(), webView.getContext().getString(R.string.photo_exception), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            })
                            .launch();    //启动压缩

                    break;

                case REQUEST_CODE:
                    JSONObject json = new JSONObject();
                    json.put("data", data.getStringExtra(CODE));
                    mInstance.callBackFunction.onCallBack(json.toJSONString());
                    break;


            }
    }


    public SqliteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            initDatabase();
        }
        return database;
    }


    private boolean initDatabase() {
        database = DataBaseManager.getInstance().setDBName(Constants.DB_NAME)
                .setDBVersion(Constants.DB_VERSION)
                .setOnInitListener(new SqliteInitICallBack() {

                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
                    }

                    @Override
                    public void onInitSuccess(SqliteDatabase db) {
                    }

                    @Override
                    public void onInitFailed(String msg) {
                    }

                    @Override
                    public void onCreate(SQLiteDatabase db) {
                        // 创建 应用市场列表

                    }
                }).initDataBase(webView.getContext()).getDatabase(webView.getContext());

        if (database == null) {
            return false;
        }
        return true;
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = webView.getContext().getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}
