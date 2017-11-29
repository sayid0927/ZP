package com.zhengpu.aiui;

import android.app.Application;

import com.zhengpu.aiuilibrary.utils.PreferUtil;

/**
 * sayid ....
 * Created by wengmf on 2017/11/29.
 */

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferUtil.getInstance().init(this);
    }
}
