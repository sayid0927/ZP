package com.zhengpu.aiuilibrary.iflytekutils;

/**
 * sayid ....
 * Created by wengmf on 2017/11/22.
 */

public interface IGetVoiceToWord {


    void getResult(String result);

    //声音太小10118错误
    void showLowVoice(String result);

    void appendResult(CharSequence sequence);

    void SpeechOver();

    void SpeechError(String error);

}
