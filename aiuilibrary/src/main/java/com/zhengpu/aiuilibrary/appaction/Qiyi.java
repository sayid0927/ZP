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
 * Created by wengmf on 2017/11/29.
 */

public class Qiyi {

    private Context context;
    private String videoName;
    private AppActionListener appActionListener;


    public Qiyi(Context context, AppActionListener appActionListener) {
        this.context = context;
        this.appActionListener = appActionListener;
        this.videoName= PreferUtil.getInstance().getPlayVideoName();

    }

    public void start(AccessibilityNodeInfo info) {
        if (info != null) {
            if (info.getChildCount() == 0) {
                if (FindNodeInfosById(info, "com.qiyi.video:id/txt_left")) {
                     // 模拟点击搜索button
                    AccessibilityNodeInfo parent = info;
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent = parent.getParent();
                    }
                } else if ( AppController.SearchKeyword && FindNodeInfosById(info, "com.qiyi.video:id/phoneSearchKeyword")) {
                    // 模拟输入歌曲名
                    AppController.SearchKeyword= false;
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("scb", videoName);
                    clipboardManager.setPrimaryClip(clipData);
                    info.performAction(AccessibilityNodeInfo.ACTION_PASTE);

                } else if (info.getText() != null  && "搜索".equals(info.getText().toString()) && FindNodeInfosById(info, "com.qiyi.video:id/txt_action")) {
                    // 模拟点击 搜索歌曲 button
                    AppController.SearchKeyword= true;
                    AppController.AiQiPlayClickabl =false;
                    AccessibilityNodeInfo parent = info;
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent = parent.getParent();
                    }
                    if (appActionListener != null)
                        appActionListener.goHome();
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
