package com.zhengpu.aiui.thread;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.zhengpu.aiuilibrary.iflytekutils.VoiceToWords;

import java.io.IOException;

/**
 * sayid ....
 * Created by wengmf on 2017/12/13.
 */

public class KuGuoMuiscPlayThread implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    static MediaPlayer mediaPlayer;
    private Context context;
    static KuGuoMuiscPlayThread kuGuoMuiscPlayThread;


    public static synchronized KuGuoMuiscPlayThread getInstance(Context context, String url) {
        if (kuGuoMuiscPlayThread == null)
            kuGuoMuiscPlayThread = new KuGuoMuiscPlayThread(context, url);
        return kuGuoMuiscPlayThread;
    }

    public KuGuoMuiscPlayThread(Context context, final String url) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                playUrl(url);
            }
        }).start();
    }

    static void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 停止
    static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 暂停
    static void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
