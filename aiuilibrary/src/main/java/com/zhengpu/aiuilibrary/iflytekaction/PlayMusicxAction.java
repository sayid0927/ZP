package com.zhengpu.aiuilibrary.iflytekaction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.zhengpu.aiuilibrary.base.AppController;
import com.zhengpu.aiuilibrary.iflytekutils.WordsToVoice;
import com.zhengpu.aiuilibrary.utils.PreferUtil;

import java.util.List;

/**
 * sayid ....
 * Created by wengmf on 2017/11/24.
 */

public class PlayMusicxAction {


    private Context context;
    private String songName;
    private String appName;
    private String text;

    public PlayMusicxAction(String songName, String appName, String text, Context context) {
        this.text = text;
        this.songName = songName;
        this.appName = appName;
        this.context = context;

        AppController.searchClickabl = false;
        AppController.playClickabl = false;
        AppController.appAction = false;
        AppController.Clickabl = false;
        AppController.goHome = false;

    }

    public void start() {

        if (appName != null && songName != null && text != null && context != null) {

            PreferUtil.getInstance().setPlayMusicName(songName);

            startAppName();
            AppController.appAction = true;
        }
    }

    private void startAppName() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> installAppList = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : installAppList) {
            String name = info.loadLabel(pm).toString();
            if (name.equalsIgnoreCase(appName)) {
                String pkgname = info.activityInfo.packageName;
                if ("com.android.contacts".equalsIgnoreCase(pkgname)) {
                    Uri uri = Uri.parse("content://contacts/people");
                    Intent i = new Intent("android.intent.action.VIEW", uri);
                    context.startActivity(i);
                    WordsToVoice.startSynthesizer(AppController.OPENAPPTEST_APP,text);
                } else {
                    intent = pm.getLaunchIntentForPackage(pkgname);
                    intent.addCategory("android.intent.category.LAUNCHER");
                    context.startActivity(intent);
                    WordsToVoice.startSynthesizer(AppController.OPENAPPTEST_APP,text);
                }
                return;
            }
        }
        WordsToVoice.startSynthesizer(AppController.OPENAPPTEST_APP,"没有找到酷狗音乐播放" + songName + "哦");
    }
}
