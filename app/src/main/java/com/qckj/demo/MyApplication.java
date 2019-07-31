package com.qckj.demo;

import android.app.Application;
import android.content.Context;

import com.qckj.qnjsdk.QNJSdk;

/**
 * Created by hujincheng on 2018/7/4.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QNJSdk.init(this);
    }
}
