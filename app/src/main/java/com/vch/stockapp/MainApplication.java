package com.vch.stockapp;

import android.app.Application;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;

/**
 * MainApplication
 *
 * @author VC_H
 * @date 2019-10-25
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);

        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.AVOSCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        AVOSCloud.initialize(this, "apFvB6kTN4305B8UfkV7ccUc-gzGzoHsz", "AP0b3Ga1XRu0nyes4xJo5JjM", "https://apfvb6kt.lc-cn-n1-shared.com");
    }
}
