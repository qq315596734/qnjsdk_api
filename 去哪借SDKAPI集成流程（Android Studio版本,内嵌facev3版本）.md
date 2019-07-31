# 去哪借SDK集成流程（Android Studio版本）



## 接入前须知：

#### 如果您应用的包名（bundleID）没有绑定face++，则需要进行绑定。可自行申请认证face商户相关资质进行绑定，也可以联系我司产品绑定 （平台--公司--产品--bundleID：手机平台--公司名称--产品名称--您的包名）。

## 开始集成

### 1.导入sdk

在project目录下build.gradle文件中添加：

    allprojects {
        repositories {
            maven {
               url 'https://dl.bintray.com/xiaosaobo/maven'
            }
        }
    }

在项目的build.gradle文件中添加：

    android {
        defaultConfig {
            ndk {
                abiFilters 'armeabi-v7a'
            }
        }
    }
    //如果您的项目中没有使用过魔蝎sdk和face++ sdk 
    dependencies {
      // gradle >= 3.0
      implementation 'com.qckj.qnjsdk:qnjsdk-api-v3:1.0.0'
    
      // gradle < 3.0
      compile 'com.qckj.qnjsdk:qnjsdk-api-v3:1.0.0'
    }

### 2.配置build.gradle

    android {
        defaultConfig {
            javaCompileOptions {
                annotationProcessorOptions {
                    includeCompileClasspath = true    //加上这行即可
                }
            }
        }
    }


### 3. 配置工程

请在AndroidManifest.xml中添加：

    <meta-data
         android:name="QNJSDK_APPID"
         android:value="您的appid"/>
    <meta-data
         android:name="QNJSDK_APPKEY"
         android:value="您的appkey"/>
    
    <service
        android:name="com.moxie.client.accessible.AccessibleCrawlerService"
        android:accessibilityFlags="flagReportViewIds"
        android:enabled="true"
        android:exported="true"
        android:label="@string/accessibility_name"
        android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
            <intent-filter>
                <action android:name="android.accessibilityservice.AccessibilityService" />
            </intent-filter>
            <meta-data
                 android:name="android.accessibilityservice"
                 android:resource="@xml/accessible_crawler_config" />
    </service>


### 4. 打开SDK并接收回调

第一步：在 `Application.oncreate()` 里面调用 QNJSdk.init(this); 完成初始化

第二步：在需要启动sdk的地方，通过调用QNJSdk.start(); 方法启动qnjsdk;

示例如下：

    /*
     Context context           上下文
     String phone              用户手机号
     String uid                用户uid 该用户在app的唯一标识（很重要，切勿传错）
     String url                要跳转的url地址
     QnjsdkCallback callback   sdk启动成功失败的回调
    */
    QNJSdk.openWeb(MainActivity.this, phone, uuid, url, new QnjsdkCallback() {
        @Override
        public void onResult(QnjsdkResult result) {
            if (result.getCode() == 1000) {
                //sdk启动成功
            } else {
                //sdk启动失败
            }
        }
    });


回调中的返回码解释如下：

    客户端错误码：
    * 1000  去哪借sdk启动成功
    * 1001  去哪借sdk没有初始化
    * 1002  AppId 或 AppKey 不能为空
    * 1003  phone 或uuid 不能为空
    * 1004  网络错误
    * 1005  请传入weburl
    
    服务端错误码：
    *  8001 商户不合法
    *  8002 签名失败
    *  8005 版本号太低
    *  8007 稍后再试
    *  8008 获取tab数据失败
    
### 5. SDK提供的可选择调用的功能：
    
1，获取版本号 

    QNJSdk.getVersion();
    
2，修改sdk的主题色

    /*
     *  String color 例如：#ff0000
    */
    QNJSdk.setThemeColor(color);
    
3，设置退出sdk的回调
    
     QNJSdk.setBackCallback(new QnjsdkBackCallback() {
            @Override
            public void onBack() {
                Toast.makeText(MainActivity.this, "退出了sdk", Toast.LENGTH_LONG).show();
            }
     });
     
4，获取设备信息 返回json字符串

    QNJSdk.getDeviceInfo();
    
5，获取位置信息 

     public void getLocationData(View view) {
            QNJSdk.setLocationCallback(new LocationCallback() {
                @Override
                public void onLocation(SDKLocationBean sdkLocationBean) {
                    Log.e(TAG, "lng : " + sdkLocationBean.getLng() + "\nlat: " + sdkLocationBean.getLat());
                }
            });
        }
    

### ————————–-————接入完成——–-————————–-



## 在接入sdk时可能遇到的问题

1，如果您的项目中已经接入了魔蝎sdk或者face++ ,请在build.gradle文件中添加

```
//如果您的项目中使用过魔蝎sdk或face++ sdk 
dependencies {
  // gradle >= 3.0
    implementation （'com.qckj.qnjsdk:qnjsdk-api-v3:1.0.0'）{
    	//解决support包冲突
    	exclude group: "com.android.support"
        //如果您的项目中已经具有face++ sdk 添加
        exclude group: "com.qckj.qnjsdk", module : 'face-v3'
        //如果您的项目中已经具有魔蝎 sdk 添加
        exclude group: "com.qckj.qnjsdk", module : 'moxie'
    }

  // gradle < 3.0
    compile （'com.qckj.qnjsdk:qnjsdk-api-v3:1.0.0'）{
       	//解决support包冲突
    	exclude group: "com.android.support"
        //如果您的项目中已经具有face++ sdk 添加
        exclude group: "com.qckj.qnjsdk", module : 'face-v3'
        //如果您的项目中已经具有魔蝎 sdk 添加
        exclude group: "com.qckj.qnjsdk", module : 'moxie'
    }
}

```

并且在AndroidMenifest.xml文件中删除

```
<service
	android:name="com.moxie.client.accessible.AccessibleCrawlerService"
    android:accessibilityFlags="flagReportViewIds"
    android:enabled="true"
    android:exported="true"
    android:label="@string/accessibility_name"
    android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
    	<intent-filter>
    		<action android:name="android.accessibilityservice.AccessibilityService" />
        </intent-filter>
        <meta-data
             android:name="android.accessibilityservice"
             android:resource="@xml/accessible_crawler_config" />
</service>
```

如果使用了上述方法，还是会报错，请联系我司。