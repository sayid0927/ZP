package com.zhengpu.aiuilibrary.iflytekaction;


import com.zhengpu.aiuilibrary.iflytekutils.WordsToVoice;

/**
 * sayid ....
 * Created by wengmf on 2017/11/23.
 */

public class CalcAction {

    private String text;

    public CalcAction(String text) {
        this.text = text;
    }

    public void start() {
        WordsToVoice.startSynthesizer(text);
    }

}
