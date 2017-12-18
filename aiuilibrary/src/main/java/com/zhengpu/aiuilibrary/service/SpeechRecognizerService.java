package com.zhengpu.aiuilibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.orhanobut.logger.Logger;
import com.zhengpu.aiuilibrary.R;
import com.zhengpu.aiuilibrary.base.AppController;
import com.zhengpu.aiuilibrary.iflytekbean.BaseBean;
import com.zhengpu.aiuilibrary.iflytekutils.IGetVoiceToWord;
import com.zhengpu.aiuilibrary.iflytekutils.IGetWordToVoice;
import com.zhengpu.aiuilibrary.iflytekutils.IflytekWakeUp;
import com.zhengpu.aiuilibrary.iflytekutils.MyWakeuperListener;
import com.zhengpu.aiuilibrary.iflytekutils.VoiceToWords;
import com.zhengpu.aiuilibrary.iflytekutils.WakeUpListener;
import com.zhengpu.aiuilibrary.iflytekutils.WordsToVoice;
import com.zhengpu.aiuilibrary.utils.PreferUtil;


/**
 * ....
 * Created by wengmf on 2017/11/21.
 */

public class SpeechRecognizerService extends Service implements IGetVoiceToWord, WakeUpListener {

    private IflytekWakeUp iflytekWakeUp;
    private VoiceToWords voiceToWords;
    private WordsToVoice wordsToVoice;


    @Override
    public void onCreate() {
        super.onCreate();


        SpeechUtility.createUtility(this.getApplication(), SpeechConstant.APPID + "=5a127875");// 传递科大讯飞appid
        PreferUtil.getInstance().init(this);
        //初始化讯飞语音识别
        voiceToWords = VoiceToWords.getInstance(this);
        voiceToWords.setmIGetVoiceToWord(this);
        wordsToVoice = WordsToVoice.getInstance(this);
//        wordsToVoice.setiGetWordToVoice(this);
        iflytekWakeUp = new IflytekWakeUp(this, new MyWakeuperListener(this, this));

        wordsToVoice.startSynthesizer(AppController.LAUNCHER_TEXT,getResources().getString(R.string.launcher_text));

        iflytekWakeUp.startWakeuper();

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                while (true){
//                    iflytekWakeUp.startWakeuper();
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 语音转文本回调
     */
    @Override
    public void getResult(String service, BaseBean result) {
        Logger.e(result.toString());
    }

    /**
     * 说话声音太小回调
     */
    @Override
    public void showLowVoice(String result) {
//    wordsToVoice.startSynthesizer(result);
//        voiceToWords.startRecognizer();
    }


    /**
     * 结束说话回调
     */
    @Override
    public void SpeechOver() {

    }

    @Override
    public void SpeechStart() {

    }

    @Override
    public void SpeechError(String error) {

    }

    /**
     * 唤醒成功
     */
    @Override
    public void OnWakeUpSuccess() {
//        wordsToVoice.startSynthesizer("是的主人");
//        voiceToWords.startRecognizer();
    }

    /**
     * 唤醒失败
     */
    @Override
    public void OnWakeUpError() {

    }

}
