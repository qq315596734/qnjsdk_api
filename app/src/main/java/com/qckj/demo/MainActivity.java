package com.qckj.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qckj.qnjsdk.QNJSdk;
import com.qckj.qnjsdk.QnjsdkBackCallback;
import com.qckj.qnjsdk.QnjsdkCallback;
import com.qckj.qnjsdk.bean.QnjsdkResult;
import com.qckj.qnjsdk.upload.location.LocationCallback;
import com.qckj.qnjsdk.upload.location.SDKLocationBean;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText et_uuid;
    private EditText et_phone;
    private EditText et_color;
    private EditText et_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_uuid = findViewById(R.id.et_uuid);
        et_phone = findViewById(R.id.et_phone);
        et_color = findViewById(R.id.et_color);
        et_link = findViewById(R.id.et_link);
    }

    public void openWeb(View view) {
        String color = et_color.getText().toString();
        String uuid = et_uuid.getText().toString();
        String phone = et_phone.getText().toString();
        if (color != null && !color.isEmpty()) {
            QNJSdk.setThemeColor(color);
        }
        QNJSdk.setBackCallback(new QnjsdkBackCallback() {
            @Override
            public void onBack() {
                Toast.makeText(MainActivity.this, "我点击了返回键", Toast.LENGTH_LONG).show();
            }
        });

        String link = et_link.getText().toString();
        if (link.isEmpty()) {
            Toast.makeText(this, "请输入跳转地址", Toast.LENGTH_SHORT).show();
            return;
        }

        QNJSdk.openWeb(MainActivity.this, phone, uuid, link, new QnjsdkCallback() {
            @Override
            public void onResult(QnjsdkResult result) {
                Toast.makeText(MainActivity.this, "code :" + result.getCode() + "\nmsg: " + result.getMsg(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //获取设备信息  得到json数据
    public void getDeviceInfo(View view) {
        String data = QNJSdk.getDeviceInfo();
        Log.e(TAG, "data : " + data);
    }

    //获取位置信息  得到的数据如果为空 则不做处理
    public void getLocationData(View view) {
        QNJSdk.setLocationCallback(new LocationCallback() {
            @Override
            public void onLocation(SDKLocationBean sdkLocationBean) {
                Log.e(TAG, "lng : " + sdkLocationBean.getLng() + "\nlat: " + sdkLocationBean.getLat());
            }
        });
    }

}
