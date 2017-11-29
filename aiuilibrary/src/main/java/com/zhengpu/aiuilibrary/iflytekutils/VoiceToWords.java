package com.zhengpu.aiuilibrary.iflytekutils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.orhanobut.logger.Logger;
import com.zhengpu.aiuilibrary.base.AppController;
import com.zhengpu.aiuilibrary.iflytekaction.CalcAction;
import com.zhengpu.aiuilibrary.iflytekaction.JokeAction;
import com.zhengpu.aiuilibrary.iflytekaction.OpenAppAction;
import com.zhengpu.aiuilibrary.iflytekaction.PlayMusicxAction;
import com.zhengpu.aiuilibrary.iflytekaction.PlayVideoAction;
import com.zhengpu.aiuilibrary.iflytekbean.CalcBean;
import com.zhengpu.aiuilibrary.iflytekbean.DatetimeBean;
import com.zhengpu.aiuilibrary.iflytekbean.JokeBean;
import com.zhengpu.aiuilibrary.iflytekbean.MusicXBean;
import com.zhengpu.aiuilibrary.iflytekbean.NewsBean;
import com.zhengpu.aiuilibrary.iflytekbean.OpenAppBean;
import com.zhengpu.aiuilibrary.iflytekbean.OpenQABean;
import com.zhengpu.aiuilibrary.iflytekbean.PoetryBean;
import com.zhengpu.aiuilibrary.iflytekbean.StoryBean;
import com.zhengpu.aiuilibrary.iflytekbean.VideoBean;
import com.zhengpu.aiuilibrary.iflytekbean.WeatherBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import static com.zhengpu.aiuilibrary.utils.DeviceUtils.isAppInstalled;


/**
 * sayid ....
 * Created by wengmf on 2017/11/22.
 */

public class VoiceToWords {

    // 语音听写对象
//    private SpeechRecognizer mIat;
    public static SpeechUnderstander mIat;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Context context;
    // 函数调用返回值,表示返回结果，失败或成功
    int ret = 0;
    //设置回调接口
    private IGetVoiceToWord mIGetVoiceToWord;
    private String operation;
    private static VoiceToWords voiceToWords;


    /**
     * 构造方法
     */
    public VoiceToWords(Context context) {
        this.context = context;
        mIat = SpeechUnderstander.createUnderstander(context, mInitListener);
        //设置参数
        setParams();
    }


    public static synchronized VoiceToWords getInstance(Context context) {
        if (voiceToWords == null)
            voiceToWords = new VoiceToWords(context);
        return voiceToWords;
    }

    public void setmIGetVoiceToWord(IGetVoiceToWord mIGetVoiceToWord) {
        this.mIGetVoiceToWord = mIGetVoiceToWord;
    }


    /**
     * 初始化监听器
     */
    private static InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Logger.e("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 开始语音听写
     */
    public void startRecognizer() {
        //清空听写结果
        mIatResults.clear();
        ret = mIat.startUnderstanding(mSpeechUnderstanderListener);
        if (ret != ErrorCode.SUCCESS)
            Logger.e("听写失败,错误码：" + ret);
    }


    /**
     * 为听写对象设置参数
     */
    private void setParams() {
        //默认不设置的语音参数
        //SpeechConstant.NET_TIMEOUT:网络连接超时时间 默认20秒
        //SpeechConstant.KEY_SPEECH_TIMEOUT:语音输入超时时间  默认60秒
        //mIat.setParameter(SpeechConstant.SAMPLE_RATE, "16000") 默认的识别采样率支持16000Hz和8000Hz
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        //设置语音输入超时时间
        mIat.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, "100000");
        mIat.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //获得当前系统的语言
        String language = context.getResources().getConfiguration().locale.getCountry();
        // 设置语言
        if (language.equals("US")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        }
        //应用领域
        //mIat.setParameter(SpeechConstant.DOMAIN, "iat");

        // 传入音频源。
        //录音机的录音方式，默认为MIC(MediaRecorder.AudioSource.MIC)
        // 设置音频来源为外部文件
        //mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
        // mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
        // mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");

        mIat.setParameter(SpeechConstant.VAD_ENABLE, "1");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "2000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/abcd/iat.wav");
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
    }

    public boolean isIatListening() {
        return mIat.isUnderstanding();
    }


    //停止录音，开始解析
    public static void mTtsStop() {
        // 退出时释放连接
        if (mIat != null && mIat.isUnderstanding()) {
            mIat.stopUnderstanding();
        }
    }

    public void mIatDestroy() {

        // 退出时释放连接
        if (mIat != null) {
            if (mIat.isUnderstanding()) {
                mIat.cancel();
            }
            mIat.destroy();
        }
    }


    /**
     * 语义理解回调。
     */
    private SpeechUnderstanderListener mSpeechUnderstanderListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                Logger.e(result.getResultString());
                String text = result.getResultString();
                JSONObject jsonObject2 = null;
                String service = null;
                int rc = 4;
                try {
                    jsonObject2 = new JSONObject(text);
                    service = jsonObject2.getString("service");
                    rc = jsonObject2.getInt("rc");
                    if (service != null && rc != 4) {
                        judgeService(service, text);
                    } else {
                        if (mIGetVoiceToWord != null) {
                            mIGetVoiceToWord.SpeechError("不好意思，我好像没听懂");
                        }
                    }
                } catch (JSONException e) {
                    if (mIGetVoiceToWord != null) {
                        mIGetVoiceToWord.SpeechError("不好意思，我好像没听懂");
                    }
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //showTip("当前正在说话，音量大小：" + volume);
            //Log.d(TAG, "返回音频数据：" + data.length);
            //Log.d("返回音频数据"+ data.length);
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //showTip("结束说话");

        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            Logger.e("讯飞结束说话");
            if (mIGetVoiceToWord != null) {
                mIGetVoiceToWord.SpeechOver();
            }
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            // showTip(error.getPlainDescription(true));
            if ("您好像没有说话哦.(错误码:10118)".equals(error.getPlainDescription(true))) {
                String lowVoiceTip = "";
                int i = new Random().nextInt(3);
                switch (i) {
                    case 0:
                        lowVoiceTip = "声音太小了，听不清呢";
                        break;
                    case 1:
                        lowVoiceTip = "有在说话吗，没听到啊";
                        break;
                    case 2:
                        lowVoiceTip = "您好像没有说话呢";
                        break;
                }
                if (mIGetVoiceToWord != null)
                    mIGetVoiceToWord.showLowVoice(lowVoiceTip);
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}

        }
    };


    //语义场景判断
    private void judgeService(String service, String text) {

        if (!AppController.service_flag) {//如果不在一项服务中才进行服务的判断

            switch (service) {
                case "calc":     //  数值计算问答
                    CalcBean calcBean = JsonParser.parseResultCalc(text);
                    operation = calcBean.getOperation();
                    switch (operation) {
                        case "ANSWER":
                            if (calcBean.getAnswer() != null) {
                                if (calcBean.getAnswer().getText() != null) {
                                    String str = calcBean.getAnswer().getText();
                                    CalcAction calcAction = new CalcAction(str);
                                    calcAction.start();
                                }
                            }
                            break;
                    }
                    break;
                case "datetime":    //  时间、日期的查询。
                    DatetimeBean datetimeBean = JsonParser.parseResultDatetimeBean(text);
                    operation = datetimeBean.getOperation();
                    switch (operation) {
                        case "ANSWER": {
                            if (datetimeBean.getAnswer() != null) {
                                if (datetimeBean.getAnswer().getText() != null) {
                                    String str = datetimeBean.getAnswer().getText();
                                    CalcAction calcAction = new CalcAction(str);
                                    calcAction.start();
                                }
                            }
                        }
                        break;
                    }
                    break;

                case "news":    //  新闻的搜索和点播

                    NewsBean newsBean = JsonParser.parseResultNewsBean(text);
                    if (newsBean != null && newsBean.getAnswer() != null) {

                        String str = newsBean.getAnswer().getText();
                        String content = newsBean.getData().getResult().get(0).getContent();
                        CalcAction calcAction = new CalcAction(str + content);
                        calcAction.start();

                    }
                    break;

                case "weather":    //  天气情况的查询。

                    WeatherBean weatherBean = JsonParser.parseResultWeatherBean(text);
                    if (weatherBean.getData().getResult().size() != 0) {

                        StringBuilder stringBuffer = new StringBuilder();
                        String humidity = weatherBean.getData().getResult().get(0).getHumidity();  //湿度
                        String tempRange = weatherBean.getData().getResult().get(0).getTempRange();   // 温度范围
                        String weather = weatherBean.getData().getResult().get(0).getWeather(); //天气情况
                        String wind = weatherBean.getData().getResult().get(0).getWind();
                        String prompt = weatherBean.getData().getResult().get(0).getExp().getCt().getPrompt();

                        String airQuality = weatherBean.getData().getResult().get(0).getAirQuality();

                        stringBuffer.append("空气质量为").append(airQuality)
                                .append("湿度为").append(humidity).append("温度范围为").append(tempRange)
                                .append("天气情况为").append(weather).append("风向以及风力情况为").append(wind)
                                .append("穿衣指数为").append(prompt);

                        CalcAction calcAction = new CalcAction(stringBuffer.toString());
                        calcAction.start();

                    } else {
                        if (weatherBean.getAnswer() != null) {
                            String str = weatherBean.getAnswer().getText();
                            CalcAction calcAction = new CalcAction(str);
                            calcAction.start();
                        }
                    }

                    break;

                case "OPENAPPTEST.APP":   //   打开App

                    OpenAppBean openAppBean = JsonParser.parseResultOpenAppBean(text);
                    if (openAppBean != null && openAppBean.getSemantic().size() != 0) {
                        if (openAppBean.getSemantic().get(0).getSlots().size() != 0) {
                            if (openAppBean.getSemantic().get(0).getSlots().get(0).getNormValue() != null) {
                                String appName = openAppBean.getSemantic().get(0).getSlots().get(0).getNormValue();
                                OpenAppAction openAppAction = new OpenAppAction(appName, context);
                                openAppAction.start();

                            }
                        }
                    }

                case "openQA":   //     开放问答

                    OpenQABean openQABean = JsonParser.parseResultOpenQABean(text);
                    if (openQABean != null && openQABean.getAnswer() != null) {
                        String str = openQABean.getAnswer().getText();
                        CalcAction calcAction = new CalcAction(str);
                        calcAction.start();
                    }

                    break;

                case "story":   //     故事的点播
                    StoryBean storyBean = JsonParser.parseResultStoryBean(text);
                    if (storyBean != null && storyBean.getAnswer() != null) {
                        String str = storyBean.getAnswer().getText();
                        CalcAction calcAction = new CalcAction(str);
                        calcAction.start();
                    }

                    break;


                case "joke":   //     笑话的点播
                    JokeBean jokeBean = JsonParser.parseResultJokeBean(text);
                    if (jokeBean != null && jokeBean.getData() != null) {
                        if (jokeBean.getData().getResult().size() != 0) {
                            if (jokeBean.getData().getResult().get(0).getTitle() != null && jokeBean.getData().getResult().get(0).getMp3Url() != null) {
                                String mp3Url = jokeBean.getData().getResult().get(0).getMp3Url();
                                JokeAction jokeAction = new JokeAction(mp3Url, context);
                                jokeAction.start();
                            }
                        }
                    }
                    break;

                case "poetry":   //     诗词查询和诗句对答。
                    PoetryBean poetryBean = JsonParser.parseResultPoetryBean(text);
                    if (poetryBean != null && poetryBean.getData() != null) {
                        if (poetryBean.getData().getResult().size() != 0) {
                            if (poetryBean.getData().getResult().get(0).getTitle() != null && poetryBean.getData().getResult().get(0).getShowContent() != null) {
                                String showContent = poetryBean.getData().getResult().get(0).getShowContent();
                                String title = poetryBean.getData().getResult().get(0).getTitle();
                                JokeAction jokeAction = new JokeAction(title + "  " + showContent, context);
                                jokeAction.start();
                            }
                        }
                    }
                    break;


                case "musicX":   //   音乐的搜索和播放

                    MusicXBean musicXBean = JsonParser.parseResultMusicXBean(text);
                    if (musicXBean.getSemantic().size() != 0) {
                        if (musicXBean.getSemantic().get(0).getSlots().size() != 0) {
                            if (musicXBean.getSemantic().get(0).getSlots().get(0).getValue() != null) {

                                String songName = musicXBean.getSemantic().get(0).getSlots().get(0).getValue();
                                String appName = "酷狗音乐";
                                String str = musicXBean.getAnswer().getText();

                                if (isAppInstalled(context, appName)) {
                                    PlayMusicxAction playMusicxAction = new PlayMusicxAction(songName, appName, str, context);
                                    playMusicxAction.start();
                                } else {
                                    Logger.e("没有安装酷狗音乐APP");
                                }
                            }
                        }
                    }
                    break;


                case "OPENAPPTEST.shiping":   //   视频的搜索和播放

                    VideoBean videoBean = JsonParser.parseResultVideoBean(text);
                    if (videoBean != null && videoBean.getSemantic().size() != 0) {
                        if (videoBean.getSemantic().get(0).getSlots().size() != 0) {
                            if (videoBean.getSemantic().get(0).getSlots().get(0).getValue() != null) {

                                String videoName = videoBean.getSemantic().get(0).getSlots().get(0).getValue();
                                String appName = "优酷";
                                if (isAppInstalled(context, appName)) {
                                    PlayVideoAction playMusicxAction = new PlayVideoAction(videoName, appName, context);
                                    playMusicxAction.start();
                                } else {
                                    Logger.e("没有安装优酷APP");
                                }
                            }
                        }
                    }
                    break;


                default:
                    WordsToVoice.startSynthesizer("不好意思，我好像没听懂。");
            }
        }
    }
}
