package com.zhengpu.aiui.ui.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.skyfishjy.library.RippleBackground;
import com.zhengpu.aiui.R;
import com.zhengpu.aiui.base.BaseActivity;
import com.zhengpu.aiui.component.AppComponent;
import com.zhengpu.aiui.component.DaggerMainComponent;
import com.zhengpu.aiui.presenter.contract.MainContract;
import com.zhengpu.aiui.presenter.impl.MainActivityPresenter;
import com.zhengpu.aiui.thread.KuGuoMuiscPlayListener;
import com.zhengpu.aiui.thread.KuGuoMuiscPlayThread;
import com.zhengpu.aiui.ui.adapter.HelpFragmentAdapter;
import com.zhengpu.aiui.ui.adapter.TalkApadtep;
import com.zhengpu.aiui.ui.fragment.FragmentHelp_1;
import com.zhengpu.aiui.ui.fragment.FragmentHelp_Home_2;
import com.zhengpu.aiui.ui.view.HelpViewPager;
import com.zhengpu.aiuilibrary.base.AppController;
import com.zhengpu.aiuilibrary.iflytekaction.CalcAction;
import com.zhengpu.aiuilibrary.iflytekbean.BaseBean;
import com.zhengpu.aiuilibrary.iflytekbean.PointBean;
import com.zhengpu.aiuilibrary.iflytekbean.UserChatBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.KuGouSongBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.KuGouSongInfoResult;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.TianJokeBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.WXItemBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.ZhiHuNewsBean;
import com.zhengpu.aiuilibrary.iflytekutils.IGetVoiceToWord;
import com.zhengpu.aiuilibrary.iflytekutils.IGetWordToVoice;
import com.zhengpu.aiuilibrary.iflytekutils.VoiceToWords;
import com.zhengpu.aiuilibrary.iflytekutils.WordsToVoice;
import com.zhengpu.aiuilibrary.service.SpeechRecognizerService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements MainContract.View, IGetVoiceToWord, IGetWordToVoice, KuGuoMuiscPlayListener {

    @Inject
    MainActivityPresenter mPresenter;

    @BindView(R.id.llExit)
    LinearLayout llExit;

    @BindView(R.id.video_n)
    ImageView videoN;

    @BindView(R.id.ripple_voice)
    RippleBackground RippleVoice;

    @BindView(R.id.ripple_voice_n)
    RippleBackground RippleVoice_N;

    @BindView(R.id.voice)
    ImageView voice;
    @BindView(R.id.rv_speech)
    RecyclerView rvSpeech;

    @BindView(R.id.ll_centet)
    LinearLayout llCentet;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.iv_help)
    ImageView ivHelp;
    @BindView(R.id.viewpager)
    HelpViewPager viewpager;


    public static MainActivity mainActivity;

    private TalkApadtep mAdapter;
    private BaseBean data;
    private List<BaseBean> datas;
    private VoiceToWords voiceToWords;
    private WordsToVoice wordsToVoice;
    private boolean isFist = true;
    private boolean isClickHelp = false;
    private UserChatBean userChatBean;

    private List<Fragment> fragmentList;
    private HelpFragmentAdapter helpFragmentAdapter;


    private static final int NUM_OF_PAGE = 20;
    private int currentPage = 1;
    private String songnameValue;
    private KuGuoMuiscPlayThread kuGuoMuiscPlayThread;


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void attachView() {
        mPresenter.attachView(this);
    }

    @Override
    public void detachView() {
        mPresenter.detachView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initView() {

//        if (!isAccessibilitySettingsOn(getApplicationContext())) {
//            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//        }


        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentHelp_1());
        fragmentList.add(new FragmentHelp_Home_2());

        helpFragmentAdapter = new HelpFragmentAdapter(fragmentList, getSupportFragmentManager());
        viewpager.setAdapter(helpFragmentAdapter);
        datas = new ArrayList<>();

        mAdapter = new TalkApadtep(this, datas);
        rvSpeech.setLayoutManager(new LinearLayoutManager(this));
        rvSpeech.setAdapter(mAdapter);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

        kuGuoMuiscPlayThread = KuGuoMuiscPlayThread.getInstance(MainActivity.this);
        kuGuoMuiscPlayThread.setKuGuoMuiscPlayListener(this);

//        UmengUtil.onEvent(MainActivity.this, "MainActivity", null);
        startService(new Intent(MainActivity.this, SpeechRecognizerService.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wordsToVoice = WordsToVoice.getInstance(MainActivity.this);
                wordsToVoice.setiGetWordToVoice(MainActivity.this);
            }
        }, 1000);
        mainActivity = this;
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void getResult(String service, BaseBean result) {
//        Logger.e("service" + service + "说话内容" + result.toString());
        isFist = false;

        if (llCentet.getVisibility() == View.VISIBLE)
            llCentet.setVisibility(View.INVISIBLE);

        if (rvSpeech.getVisibility() == View.GONE)
            rvSpeech.setVisibility(View.VISIBLE);

        if (RippleVoice_N.getVisibility() == View.GONE)
            RippleVoice_N.setVisibility(View.VISIBLE);

        if (viewpager.getVisibility() == View.VISIBLE)
            viewpager.setVisibility(View.GONE);

        if (!kuGuoMuiscPlayThread.isPlay()) {   //如果正在播放歌曲不做回应
            userChatBean = new UserChatBean();
            data = new BaseBean();
            userChatBean.setText(result.getContext());
            data.setItemType(BaseBean.USER_CHAT);
            data.setUserChatBean(userChatBean);
            datas.add(data);
        }

        //语义场景判断
        if (service.equals("news")) {   //新闻播报
            PointBean pointBean = new PointBean();
            pointBean.setText("为你推荐如下热门新闻");
            data.setPointBean(pointBean);
            data.setItemType(BaseBean.POINT);
            mPresenter.getWXHot(NUM_OF_PAGE, currentPage);
        } else if (service.equals("musicX")) {  // 播放音乐

            PointBean pointBean = new PointBean();
            pointBean.setText(result.getCmdBean().getText());
            data.setPointBean(pointBean);
            data.setItemType(BaseBean.POINT);

            switch (result.getMusicXBean().getSemantic().get(0).getIntent()) {
                case "INSTRUCTION": // 指令命令
                    if (result.getMusicXBean().getSemantic().get(0).getSlots().get(0).getName().equals("insType")) {
                        switch (result.getCmdBean().getSemantic().get(0).getSlots().get(0).getValue()) {
                            case "volume_minus":
                                setCurrentVolume(0);  // 音量小
                                break;
                            case "volume_plus": // 音量大
                                setCurrentVolume(1);
                                break;
                            case "pause": // 暂停
                                setKuGuoMuiscPlayStart(0);
                                break;
                            case "replay": // 继续播放
                                setKuGuoMuiscPlayStart(1);
                                break;
                        }
                    }
                    break;

                case "PLAY": // 播放歌曲命令

                    break;
            }
        } else if (service.equals("openQA")) {
            if (!kuGuoMuiscPlayThread.isPlay()) {
                datas.add(result);
                WordsToVoice.startSynthesizer(AppController.OPENQA, result.getOpenQABean().getAnswer().getText());
                voiceToWords.startRecognizer();
            }
        } else if (service.equals("joke")) {

            mPresenter.getTianJoke();

        } else if (service.equals("r4")) {
            if (!kuGuoMuiscPlayThread.isPlay() && service.equals("r4")) {
                datas.add(result);
                WordsToVoice.startSynthesizer(AppController.R4, "不好意思，我好像没听懂");
                voiceToWords.startRecognizer();
            }
        } else {
            datas.add(result);
        }
        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);
//        if (!kuGuoMuiscPlayThread.isPlay() && service.equals("r4")) {
//            CalcAction calcAction = new CalcAction(AppController.R4,"不好意思，我好像没听懂");
//            calcAction.start();
//            voiceToWords.startRecognizer();
//        }
    }

    @Override
    public void showLowVoice(String result) {
        Logger.e(result);
        voiceToWords.startRecognizer();
    }

    @Override
    public void SpeechOver() {
//        Logger.e("说话结束");
        if (RippleVoice.isRippleAnimationRunning())
            RippleVoice.stopRippleAnimation();
        if (RippleVoice_N.isRippleAnimationRunning())
            RippleVoice_N.stopRippleAnimation();
    }

    @Override
    public void SpeechStart() {
//        Logger.e("开始说话");
        if (isFist)
            RippleVoice.startRippleAnimation();
        else
            RippleVoice_N.startRippleAnimation();
    }

    @Override
    public void SpeechError(String error) {
        Logger.e(error);
        voiceToWords.startRecognizer();
    }

    @OnClick({R.id.iv_phone, R.id.iv_help, R.id.llExit, R.id.video_n})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.llExit:

                if (FragmentHelp_Home_2.fragmentHelpHome2.getvisibilityType() != 0) {
                    FragmentHelp_Home_2.fragmentHelpHome2.setvisibilityStatr();
                } else if (isClickHelp) {
                    isClickHelp = false;
                    if (viewpager.getVisibility() == View.VISIBLE)
                        viewpager.setVisibility(View.GONE);
                    if (datas.size() == 0) {
                        if (rvSpeech.getVisibility() == View.VISIBLE)
                            rvSpeech.setVisibility(View.GONE);
                        if (RippleVoice_N.getVisibility() == View.VISIBLE)
                            RippleVoice_N.setVisibility(View.GONE);
                        if (llCentet.getVisibility() == View.INVISIBLE)
                            llCentet.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        if (rvSpeech.getVisibility() == View.GONE)
                            rvSpeech.setVisibility(View.VISIBLE);
                    }
                }

                break;

            case R.id.video_n:

//            mPresenter.getSearchKugouSong("周杰伦七里香", "1", "20");
                mPresenter.getTianJoke();
                break;
            case R.id.iv_phone:

                break;

            case R.id.iv_help:

                if (!isClickHelp) {

                    if (llCentet.getVisibility() == View.VISIBLE)
                        llCentet.setVisibility(View.INVISIBLE);
                    if (rvSpeech.getVisibility() == View.VISIBLE)
                        rvSpeech.setVisibility(View.GONE);
                    if (RippleVoice_N.getVisibility() == View.GONE)
                        RippleVoice_N.setVisibility(View.VISIBLE);
                    if (viewpager.getVisibility() == View.GONE)
                        viewpager.setVisibility(View.VISIBLE);

                    isClickHelp = true;
                    break;
                } else {
                    isClickHelp = false;
                    if (viewpager.getVisibility() == View.VISIBLE)
                        viewpager.setVisibility(View.GONE);
                    if (datas.size() == 0) {
                        if (rvSpeech.getVisibility() == View.VISIBLE)
                            rvSpeech.setVisibility(View.GONE);
                        if (RippleVoice_N.getVisibility() == View.VISIBLE)
                            RippleVoice_N.setVisibility(View.GONE);
                        if (llCentet.getVisibility() == View.INVISIBLE)
                            llCentet.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        if (rvSpeech.getVisibility() == View.GONE)
                            rvSpeech.setVisibility(View.VISIBLE);
                        break;
                    }
                }
        }
    }

    public void setScanScroll(boolean isCanScroll) {
        viewpager.setScanScroll(isCanScroll);
    }

    @Override
    public void getZhiHuNewsBeanSuccess(ZhiHuNewsBean zhiHuNewsBean) {

        data = new BaseBean();
        data.setZhiHuNewsBean(zhiHuNewsBean);
        data.setItemType(BaseBean.NEWS);
        datas.add(data);
        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);

    }

    @Override
    public void getErrror(String error) {

    }

    /**
     * 语音播放结束
     */
    @Override
    public void SpeechEnd(String service) {
        switch (service) {
            case AppController.LAUNCHER_TEXT:
                voiceToWords = VoiceToWords.getInstance(MainActivity.this);
                voiceToWords.setmIGetVoiceToWord(MainActivity.this);
                voiceToWords.startRecognizer();
                break;
        }
    }

    /**
     * 语音播放失败
     */
    @Override
    public void SpeechError() {

    }

    //获取微信热门新闻
    @Override
    public void getWXHotSuccess(WXItemBean wxItemBeans) {
        data = new BaseBean();
        data.setWxItemBean(wxItemBeans);
        data.setItemType(BaseBean.NEWS);
        datas.add(data);
        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);
        voiceToWords.startRecognizer();
    }

    //搜索酷狗音乐
    @Override
    public void getSearchKugouSongSuccess(KuGouSongBean kuGouSongBean) {
        List<KuGouSongBean.DataBean.InfoBean> infoBeanList = kuGouSongBean.getData().getInfo();
        if (infoBeanList.size() != 0) {
            for (int i = 0; i < infoBeanList.size(); i++) {
                String FileExt = infoBeanList.get(i).getExtname();
                String singername = infoBeanList.get(i).getSingername();
                String songname = infoBeanList.get(i).getSongname();
                songnameValue = "七里香";
                if (FileExt.equals("mp3") && songname.equals(songnameValue)) {
                    mPresenter.getKugouSongInfo(infoBeanList.get(i).getHash());
                }
            }
        }
    }

    //获取酷狗指定音乐播放地址
    @Override
    public void getKugouSongInfoSuccess(KuGouSongInfoResult kuGouSongInfoResult) {
        if (wordsToVoice.isTtsSpeaking())
            wordsToVoice.mTtsStop();
        voiceToWords.mTtsStop();
        kuGuoMuiscPlayThread.playUrl(kuGouSongInfoResult.getUrl());
    }

    //获取笑话内容
    @Override
    public void getTianJokeSuccess(TianJokeBean jokeBean) {

        if (jokeBean != null && jokeBean.getNewslist().size() != 0 && jokeBean.getNewslist().get(0).getTitle() != null
                && jokeBean.getNewslist().get(0).getContent() != null) {
            data = new BaseBean();
            data.setTianJokeBean(jokeBean);
            data.setItemType(BaseBean.JOKE);
            datas.add(data);
            mAdapter.notifyDataSetChanged();
            rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);

            WordsToVoice.startSynthesizer(AppController.JOKE,
                    "请听笑话" + jokeBean.getNewslist().get(0).getTitle() + "    " + jokeBean.getNewslist().get(0).getContent());

        }
        voiceToWords.startRecognizer();

    }

    @Override
    public void KuGuoMuiscPlayPause() {
        voiceToWords.startRecognizer();
    }

    @Override
    public void KuGuoMuiscPlayStop() {
        voiceToWords.startRecognizer();
    }

    //控制系统音量大小
    private void setCurrentVolume(int Type) {
        //初始化音频管理器
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //获取系统最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取设备当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (Type == 0) {
            //减少音量
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);//调低声音
        } else {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);  //调高声音
        }
    }

    // 播放音乐暂停、继续和停止
    private void setKuGuoMuiscPlayStart(int Type) {
        if (Type == 0) {   //暂停
            if (kuGuoMuiscPlayThread.isPlay())
                kuGuoMuiscPlayThread.pause();
        } else {                 // 继续
            kuGuoMuiscPlayThread.start();
        }
    }
}
