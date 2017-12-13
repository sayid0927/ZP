package com.zhengpu.aiui.thread;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.orhanobut.logger.Logger;
import com.zhengpu.aiuilibrary.iflytekutils.VoiceToWords;

import java.io.IOException;

/**
 * sayid ....
 * Created by wengmf on 2017/12/13.
 */

public class KuGuoMuiscPlayThread implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    static MediaPlayer mediaPlayer;
    private Context context;
    static KuGuoMuiscPlayThread kuGuoMuiscPlayThread;
    private   KuGuoMuiscPlayListener kuGuoMuiscPlayListener;


    public static synchronized KuGuoMuiscPlayThread getInstance(Context context) {
        if (kuGuoMuiscPlayThread == null)
            kuGuoMuiscPlayThread = new KuGuoMuiscPlayThread(context);
        return kuGuoMuiscPlayThread;
    }

    public KuGuoMuiscPlayThread(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    public  void setKuGuoMuiscPlayListener(KuGuoMuiscPlayListener kuGuoMuiscPlayListener){
        this.kuGuoMuiscPlayListener= kuGuoMuiscPlayListener;
    }

    public  void playUrl(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(url); // 设置数据源
                    mediaPlayer.prepare(); // prepare自动播放
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 停止
  public   void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if(kuGuoMuiscPlayListener!=null)
                kuGuoMuiscPlayListener.KuGuoMuiscPlayStop();

        }
    }

    // 暂停
  public   void pause() {
        mediaPlayer.pause();
        if(kuGuoMuiscPlayListener!=null)
            kuGuoMuiscPlayListener.KuGuoMuiscPlayPause();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(kuGuoMuiscPlayListener!=null)
            kuGuoMuiscPlayListener.KuGuoMuiscPlayStop();
    }
}
