package com.zhengpu.aiuilibrary.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhengpu.aiuilibrary.utils.PreferUtil;

/**
 * sayid ....
 * Created by wengmf on 2017/12/1.
 */


public class PlayService extends Service {
    private MediaPlayer mediaPlayer;
    private String playStoryUrl;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


        playStoryUrl = PreferUtil.getInstance().getPlayStoryUrl();

        mediaPlayer = new MediaPlayer();


//        mediaPlayer.reset();
//        mediaPlayer.setDataSource(this,);
//        mediaPlayer.start();


    }
}
