package com.zhengpu.aiui.ui.activity;

import android.Manifest;
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
import com.zhengpu.aiui.ui.fragment.LrcFragment;
import com.zhengpu.aiui.ui.view.HelpViewPager;
import com.zhengpu.aiuilibrary.base.AppController;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


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
    private KuGuoMuiscPlayThread kuGuoMuiscPlayThread;
    private String artist = "", song = "";

    private int duration;
    private String fileName;
    private String hash;
    private String singername;
    private String songname;
    private String songUrl;


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
        fragmentList.add(new LrcFragment());

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

        List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
        permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "定位", R.drawable.permission_ic_location));
        HiPermission.create(MainActivity.this)
                .permissions(permissonItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Logger.e("用户关闭权限申请");

                    }

                    @Override
                    public void onFinish() {
                        Logger.e("所有权限申请完成");
                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

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
            pointBean.setText(result.getMusicXBean().getText());
            data.setPointBean(pointBean);
            data.setItemType(BaseBean.POINT);

            switch (result.getMusicXBean().getSemantic().get(0).getIntent()) {
                case "INSTRUCTION": // 指令命令
                    if (result.getMusicXBean().getSemantic().get(0).getSlots().get(0).getName().equals("insType")) {
                        switch (result.getMusicXBean().getSemantic().get(0).getSlots().get(0).getValue()) {
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
                    for (int i = 0; i < result.getMusicXBean().getSemantic().get(0).getSlots().size(); i++) {
                        String name = result.getMusicXBean().getSemantic().get(0).getSlots().get(i).getName();
                        if (name.equals("artist")) {
                            artist = result.getMusicXBean().getSemantic().get(0).getSlots().get(i).getValue();
                        } else if (name.equals("song")) {
                            song = result.getMusicXBean().getSemantic().get(0).getSlots().get(i).getValue();
                        }
                    }
                    mPresenter.getSearchKugouSong(artist + song, "1", "20");

                    break;
            }
        } else if (service.equals("openQA")) {   //开放问答
            if (!kuGuoMuiscPlayThread.isPlay()) {
                datas.add(result);
                WordsToVoice.startSynthesizer(AppController.OPENQA, result.getOpenQABean().getAnswer().getText());
            }
        } else if (service.equals("joke")) {     //笑话的点播
            mPresenter.getTianJoke();
        } else if (service.equals("r4")) {
            if (!kuGuoMuiscPlayThread.isPlay()) {
                datas.add(result);
                WordsToVoice.startSynthesizer(AppController.R4, "不好意思，我好像没听懂");
            }
        } else if (service.equals("poetry")) {       //诗词查询和诗句对答。
            if (!kuGuoMuiscPlayThread.isPlay()) {
                datas.add(result);
                WordsToVoice.startSynthesizer(AppController.POETRY, result.getPoetryBean().getAnswer().getText());
            }
        } else if (service.equals("OPENAPPTEST.music_demo")) {

            if (result.getCustomMusicBean() != null && result.getCustomMusicBean().getSemantic().size() != 0 &&
                    result.getCustomMusicBean().getSemantic().get(0).getSlots().size() != 0) {

                PointBean pointBean = new PointBean();
                pointBean.setText(result.getCustomMusicBean().getText());
                data.setPointBean(pointBean);
                data.setItemType(BaseBean.POINT);
                for (int i = 0; i < result.getCustomMusicBean().getSemantic().get(0).getSlots().size(); i++) {
                    String name = result.getCustomMusicBean().getSemantic().get(0).getSlots().get(i).getName();
                    if (name.equals("artist")) {
                        artist = result.getCustomMusicBean().getSemantic().get(0).getSlots().get(i).getValue();
                    } else if (name.equals("song")) {
                        song = result.getCustomMusicBean().getSemantic().get(0).getSlots().get(i).getValue();
                    }
                }
                mPresenter.getSearchKugouSong(artist + song, "1", "20");
            }
        } else if (service.equals("datetime")) {
            if (!kuGuoMuiscPlayThread.isPlay()) {
                datas.add(result);
                WordsToVoice.startSynthesizer(AppController.DATETIME, result.getDatetimeBean().getAnswer().getText());
            }
        } else if (service.equals("calc")) {
            if (!kuGuoMuiscPlayThread.isPlay()) {
                datas.add(result);
                WordsToVoice.startSynthesizer(AppController.DATETIME, result.getCalcBean().getAnswer().getText());
            }
        } else if (service.equals("weather")) {
            if (!kuGuoMuiscPlayThread.isPlay()) {
                datas.add(result);

                StringBuilder stringBuffer = new StringBuilder();
                String humidity = result.getWeatherBean().getData().getResult().get(0).getHumidity();  //湿度
                String tempRange = result.getWeatherBean().getData().getResult().get(0).getTempRange();   // 温度范围
                String weather = result.getWeatherBean().getData().getResult().get(0).getWeather(); //天气情况
                String wind = result.getWeatherBean().getData().getResult().get(0).getWind();
                String prompt = result.getWeatherBean().getData().getResult().get(0).getExp().getCt().getPrompt();

                String airQuality = result.getWeatherBean().getData().getResult().get(0).getAirQuality();

                stringBuffer.append("空气质量为").append(airQuality)
                        .append("湿度为").append(humidity).append("温度范围为").append(tempRange)
                        .append("天气情况为").append(weather).append("穿衣指数为").append(prompt);

                WordsToVoice.startSynthesizer(AppController.WEATHER, stringBuffer.toString());

            }


        } else {
            datas.add(result);
        }
        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);
        if (kuGuoMuiscPlayThread.isPlay())
            voiceToWords.startRecognizer();
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

                mPresenter.getSearchKugouSong("在人间", "1", "20");
//                mPresenter.getTianJoke();
                break;
            case R.id.iv_phone:
                setCurrentVolume(1);
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
                break;
            case AppController.OPENAPPTEST_MUSIC_DEMO:
                voiceToWords.startRecognizer();
                kuGuoMuiscPlayThread.playUrl(songUrl);
                return;
        }
        voiceToWords.startRecognizer();
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
        WordsToVoice.startSynthesizer(AppController.NEWS, "为你推荐如下热门新闻");
    }

    //搜索酷狗音乐
    @Override
    public void getSearchKugouSongSuccess(KuGouSongBean kuGouSongBean) {
        List<KuGouSongBean.DataBean.InfoBean> infoBeanList = kuGouSongBean.getData().getInfo();
        boolean isgetKuguoSong = false;
        if (infoBeanList.size() != 0) {
            for (int i = 0; i < infoBeanList.size(); i++) {
                String FileExt = infoBeanList.get(i).getExtname();
                singername = infoBeanList.get(i).getSingername();
                songname = infoBeanList.get(i).getSongname();
                duration = infoBeanList.get(i).getDuration();
                fileName = infoBeanList.get(i).getFilename();
                hash = infoBeanList.get(i).getHash();

                if (artist != "" && song != "") {
                    if (FileExt.equals("mp3") && singername.equals(artist) && songname.contains(song)) {
                        mPresenter.getKugouSongInfo(infoBeanList.get(i).getHash());
                        isgetKuguoSong = true;
                        break;
                    }
                } else if (song != "") {
                    if (FileExt.equals("mp3") && songname.equals(song)) {
                        mPresenter.getKugouSongInfo(infoBeanList.get(i).getHash());
                        isgetKuguoSong = true;
                        break;
                    }
                }
            }
        }
        if (!isgetKuguoSong) {
            PointBean pointBean = new PointBean();
            pointBean.setText("不好意思没有找到" + artist + song + "的歌曲");
            data.setPointBean(pointBean);
            data.setItemType(BaseBean.POINT);
            datas.add(data);
            mAdapter.notifyDataSetChanged();
            rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);
            WordsToVoice.startSynthesizer(AppController.POINT, "不好意思没有找到" + artist + song + "的歌曲");
        }
    }

    //获取酷狗指定音乐播放地址
    @Override
    public void getKugouSongInfoSuccess(KuGouSongInfoResult kuGouSongInfoResult) {
        if (wordsToVoice.isTtsSpeaking())
            wordsToVoice.mTtsStop();
        songUrl = kuGouSongInfoResult.getUrl();
        mPresenter.downloadLyric(fileName, duration, hash);
        WordsToVoice.startSynthesizer(AppController.OPENAPPTEST_MUSIC_DEMO, "为你播放" + singername + "的" + songname);

    }


    //获取酷狗音乐歌词
    @Override
    public void downloadLyric(File file) {
        Logger.e(file.getAbsolutePath());
    }

    //获取笑话内容
    @Override
    public void getTianJokeSuccess(TianJokeBean jokeBean) {

        if (jokeBean != null && jokeBean.getNewslist().size() != 0 && jokeBean.getNewslist().get(0).getTitle() != null
                && jokeBean.getNewslist().get(0).getContent() != null) {
            data = new BaseBean();
            // 去除特殊字符串
            String content = jokeBean.getNewslist().get(0).getContent();
            content = content.replace("<", " ");
            content = content.replace("b", " ");
            content = content.replace("r", " ");
            content = content.replace("/", " ");
            content = content.replace(">", " ");
            jokeBean.getNewslist().get(0).setContent(content);

            data.setTianJokeBean(jokeBean);
            data.setItemType(BaseBean.JOKE);
            datas.add(data);
            mAdapter.notifyDataSetChanged();
            rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);

            if (wordsToVoice.isTtsSpeaking())
                wordsToVoice.mTtsStop();
            voiceToWords.mIatDestroy();

            WordsToVoice.startSynthesizer(AppController.JOKE,
                    "请欣赏笑话" + "       " + jokeBean.getNewslist().get(0).getTitle() + jokeBean.getNewslist().get(0).getContent());

        }
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
