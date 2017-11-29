package com.zhengpu.aiuilibrary.appaction;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhengpu.aiuilibrary.base.AppController;
import com.zhengpu.aiuilibrary.utils.PreferUtil;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * sayid ....
 * Created by wengmf on 2017/11/28.
 */

public class KuGou {


    private Context context;
    private String songName;
    private AppActionListener appActionListener;

    public KuGou(Context context, AppActionListener appActionListener) {
        this.context = context;
        this.appActionListener = appActionListener;
//        this.songName= PreferUtil.getInstance().getPlayMusicName();
        this.songName = "一天一夜";
    }


    public void start(AccessibilityNodeInfo info) {
        if (info != null) {
            if (info.getChildCount() == 0) {
                if (!AppController.appAction && FindNodeInfosById(info, "com.kugou.android:id/y5")) {
                    AccessibilityNodeInfo parent = info;
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent = parent.getParent();
                    }
                } else if (!AppController.Clickabl && FindNodeInfosById(info, "com.kugou.android:id/abj")) {
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("scb", songName);
                    clipboardManager.setPrimaryClip(clipData);
                    info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    AppController.Clickabl = true;
                } else if (AppController.Clickabl && !AppController.searchClickabl && FindNodeInfosById(info, "com.kugou.android:id/chy")) {
                    AppController.searchClickabl = true;
                    AccessibilityNodeInfo parent = info;
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent = parent.getParent();
                    }
                } else if (AppController.searchClickabl && !AppController.playClickabl && FindNodeInfosById(info, "com.kugou.android:id/b2w")) {
                    AccessibilityNodeInfo parent = info;
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent = parent.getParent();
                    }
                    AppController.playClickabl = true;
                    if (!AppController.goHome) {
                        appActionListener.goHome();
                    }
                }
            } else {
                for (int i = 0; i < info.getChildCount(); i++) {
                    if (info.getChild(i) != null) {
                        start(info.getChild(i));
                    }
                }
            }
        }
    }

    //通过id查找
    public static boolean FindNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
