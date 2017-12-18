package com.zhengpu.aiuilibrary.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhengpu.aiuilibrary.appaction.AppActionListener;
import com.zhengpu.aiuilibrary.appaction.KuGou;
import com.zhengpu.aiuilibrary.appaction.Qiyi;
import com.zhengpu.aiuilibrary.base.AppController;

/**
 * sayid ....
 * Created by wengmf on 2017/11/29.
 */

public class MyAccessibilityService extends AccessibilityService implements AppActionListener {

    private static String TAG = "MyAccessibilityService类 ";


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        //接收事件,如触发了通知栏变化、界面变化等
        String nowPackageName = event.getPackageName().toString();
        Log.e(TAG, nowPackageName);
        AccessibilityNodeInfo rootNode = this.getRootInActiveWindow();
        switch (nowPackageName) {

            case "com.kugou.android":
                if (AppController.appAction) {
                    KuGou kuGou = new KuGou(this, this);
                    kuGou.start(rootNode);
                }
                break;

            case "com.qiyi.video":

                Qiyi youKu = new Qiyi(this, this);
                youKu.start(rootNode);

                break;


        }
    }

    @Override
    public void onInterrupt() {
        //服务中断，如授权关闭或者将服务杀死
        Log.i(TAG, "授权中断");
    }

    @Override
    public void goHome() {
        //点击  Home键健
        if (AppController.appAction)
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        AppController.playClickabl = false;
        AppController.searchClickabl = false;
        AppController.appAction = false;
        AppController.Clickabl = false;
        AppController.goHome = true;

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AppController.appAction = false;
        AppController.Clickabl = false;
        AppController.goHome = false;
        AppController.playClickabl = false;
        AppController.searchClickabl = false;
        Log.e(TAG, "AccessibilityService >>>>  授权成功");

    }

}
