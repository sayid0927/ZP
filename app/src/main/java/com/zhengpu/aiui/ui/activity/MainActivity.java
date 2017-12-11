package com.zhengpu.aiui.ui.activity;

import android.content.Intent;
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
import com.zhengpu.aiui.bean.ZhiHuNewsBean;
import com.zhengpu.aiui.component.AppComponent;
import com.zhengpu.aiui.component.DaggerMainComponent;
import com.zhengpu.aiui.presenter.contract.MainContract;
import com.zhengpu.aiui.presenter.impl.MainActivityPresenter;
import com.zhengpu.aiui.ui.adapter.HelpFragmentAdapter;
import com.zhengpu.aiui.ui.adapter.TalkApadtep;
import com.zhengpu.aiui.ui.fragment.FragmentHelp_1;
import com.zhengpu.aiui.ui.fragment.FragmentHelp_Home_2;
import com.zhengpu.aiui.ui.view.HelpViewPager;
import com.zhengpu.aiuilibrary.iflytekbean.BaseBean;
import com.zhengpu.aiuilibrary.iflytekbean.UserChatBean;
import com.zhengpu.aiuilibrary.iflytekbean.WeatherBean;
import com.zhengpu.aiuilibrary.iflytekutils.IGetVoiceToWord;
import com.zhengpu.aiuilibrary.iflytekutils.JsonParser;
import com.zhengpu.aiuilibrary.iflytekutils.VoiceToWords;
import com.zhengpu.aiuilibrary.service.SpeechRecognizerService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements MainContract.View, IGetVoiceToWord {

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
    private boolean isFist = true;
    private boolean isClickHelp = false;
    private UserChatBean userChatBean;

    private List<Fragment> fragmentList;
    private HelpFragmentAdapter helpFragmentAdapter;

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

//        UmengUtil.onEvent(MainActivity.this, "MainActivity", null);
        startService(new Intent(MainActivity.this, SpeechRecognizerService.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                voiceToWords = VoiceToWords.getInstance(MainActivity.this);
                voiceToWords.setmIGetVoiceToWord(MainActivity.this);

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


        userChatBean = new UserChatBean();
        data = new BaseBean();
        userChatBean.setText(result.getContext());
        data.setItemType(BaseBean.USER_CHAT);
        data.setUserChatBean(userChatBean);
        datas.add(data);

        switch (service) {
            case "news":
                mPresenter.getZhiHuNewsBean();
                break;
        }

        if (!service.equals("news"))
            datas.add(result);

        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);

    }

    @Override
    public void showLowVoice(String result) {
        Logger.e(result);
        voiceToWords.startRecognizer();
    }


    /**
     * 听不懂状态
     */
    @Override
    public void ResultR4Start(String sequence) {


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

                isFist = false;
                if (llCentet.getVisibility() == View.VISIBLE)
                    llCentet.setVisibility(View.INVISIBLE);

                if (rvSpeech.getVisibility() == View.GONE)
                    rvSpeech.setVisibility(View.VISIBLE);

                if (RippleVoice_N.getVisibility() == View.GONE)
                    RippleVoice_N.setVisibility(View.VISIBLE);

                if (viewpager.getVisibility() == View.VISIBLE)
                    viewpager.setVisibility(View.GONE);


                String text = "{\n" +
                        "  \"data\": {\n" +
                        "    \"result\": [\n" +
                        "      {\n" +
                        "        \"airData\": 66,\n" +
                        "        \"airQuality\": \"良\",\n" +
                        "        \"city\": \"深圳\",\n" +
                        "        \"date\": \"2017-12-11\",\n" +
                        "        \"dateLong\": 1512921600,\n" +
                        "        \"exp\": {\n" +
                        "          \"ct\": {\n" +
                        "            \"expName\": \"穿衣指数\",\n" +
                        "            \"level\": \"较舒适\",\n" +
                        "            \"prompt\": \"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。\"\n" +
                        "          }\n" +
                        "        },\n" +
                        "        \"humidity\": \"35%\",\n" +
                        "        \"lastUpdateTime\": \"2017-12-11 11:00\",\n" +
                        "        \"pm25\": \"47\",\n" +
                        "        \"temp\": 19,\n" +
                        "        \"tempRange\": \"15℃ ~ 22℃\",\n" +
                        "        \"weather\": \"多云\",\n" +
                        "        \"weatherType\": 1,\n" +
                        "        \"wind\": \"无持续风向微风\",\n" +
                        "        \"windLevel\": 0\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"city\": \"深圳\",\n" +
                        "        \"date\": \"2017-12-12\",\n" +
                        "        \"dateLong\": 1513008000,\n" +
                        "        \"lastUpdateTime\": \"2017-12-11 11:00\",\n" +
                        "        \"tempRange\": \"14℃ ~ 18℃\",\n" +
                        "        \"weather\": \"阴\",\n" +
                        "        \"weatherType\": 2,\n" +
                        "        \"wind\": \"无持续风向微风\",\n" +
                        "        \"windLevel\": 0\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"city\": \"深圳\",\n" +
                        "        \"date\": \"2017-12-13\",\n" +
                        "        \"dateLong\": 1513094400,\n" +
                        "        \"lastUpdateTime\": \"2017-12-11 11:00\",\n" +
                        "        \"tempRange\": \"13℃ ~ 17℃\",\n" +
                        "        \"weather\": \"小雨\",\n" +
                        "        \"weatherType\": 7,\n" +
                        "        \"wind\": \"无持续风向微风\",\n" +
                        "        \"windLevel\": 0\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"city\": \"深圳\",\n" +
                        "        \"date\": \"2017-12-14\",\n" +
                        "        \"dateLong\": 1513180800,\n" +
                        "        \"lastUpdateTime\": \"2017-12-11 11:00\",\n" +
                        "        \"tempRange\": \"14℃ ~ 18℃\",\n" +
                        "        \"weather\": \"阴\",\n" +
                        "        \"weatherType\": 2,\n" +
                        "        \"wind\": \"无持续风向微风\",\n" +
                        "        \"windLevel\": 0\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"city\": \"深圳\",\n" +
                        "        \"date\": \"2017-12-15\",\n" +
                        "        \"dateLong\": 1513267200,\n" +
                        "        \"lastUpdateTime\": \"2017-12-11 11:00\",\n" +
                        "        \"tempRange\": \"15℃ ~ 19℃\",\n" +
                        "        \"weather\": \"阴转多云\",\n" +
                        "        \"weatherType\": 2,\n" +
                        "        \"wind\": \"无持续风向微风\",\n" +
                        "        \"windLevel\": 0\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"city\": \"深圳\",\n" +
                        "        \"date\": \"2017-12-16\",\n" +
                        "        \"dateLong\": 1513353600,\n" +
                        "        \"lastUpdateTime\": \"2017-12-11 11:00\",\n" +
                        "        \"tempRange\": \"11℃ ~ 20℃\",\n" +
                        "        \"weather\": \"多云\",\n" +
                        "        \"weatherType\": 1,\n" +
                        "        \"wind\": \"东北风3-4级\",\n" +
                        "        \"windLevel\": 1\n" +
                        "      },\n" +
                        "      {\n" +
                        "        \"city\": \"深圳\",\n" +
                        "        \"date\": \"2017-12-17\",\n" +
                        "        \"dateLong\": 1513440000,\n" +
                        "        \"lastUpdateTime\": \"2017-12-11 11:00\",\n" +
                        "        \"tempRange\": \"9℃ ~ 18℃\",\n" +
                        "        \"weather\": \"多云转阴\",\n" +
                        "        \"weatherType\": 1,\n" +
                        "        \"wind\": \"东北风3-4级\",\n" +
                        "        \"windLevel\": 1\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  \"rc\": 0,\n" +
                        "  \"semantic\": [\n" +
                        "    {\n" +
                        "      \"intent\": \"QUERY\",\n" +
                        "      \"slots\": [\n" +
                        "        {\n" +
                        "          \"name\": \"location.city\",\n" +
                        "          \"value\": \"深圳市\",\n" +
                        "          \"normValue\": \"深圳市\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"location.cityAddr\",\n" +
                        "          \"value\": \"深圳\",\n" +
                        "          \"normValue\": \"深圳\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"location.type\",\n" +
                        "          \"value\": \"LOC_BASIC\",\n" +
                        "          \"normValue\": \"LOC_BASIC\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"queryType\",\n" +
                        "          \"value\": \"内容\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"subfocus\",\n" +
                        "          \"value\": \"天气状态\"\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"service\": \"weather\",\n" +
                        "  \"state\": {\n" +
                        "    \"fg::weather::default::default\": {\n" +
                        "      \"state\": \"default\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"text\": \"深圳天气\",\n" +
                        "  \"uuid\": \"atn057bdfd6@ch2eca0d894d616f2601\",\n" +
                        "  \"used_state\": {\n" +
                        "    \"state_key\": \"fg::weather::default::default\",\n" +
                        "    \"state\": \"default\"\n" +
                        "  },\n" +
                        "  \"answer\": {\n" +
                        "    \"text\": \"\\\"深圳\\\"今天\\\"多云\\\"，\\\"15℃ ~ 22℃\\\"，\\\"无持续风向微风\\\"\"\n" +
                        "  },\n" +
                        "  \"dialog_stat\": \"DataValid\",\n" +
                        "  \"save_history\": true,\n" +
                        "  \"sid\": \"atn057bdfd6@ch2eca0d894d616f2601\"\n" +
                        "}";

                data = new BaseBean();
                BaseBean baseBean = new BaseBean();
                WeatherBean weatherBean = JsonParser.parseResultWeatherBean(text);
                if (weatherBean.getData().getResult().size() != 0) {
                    baseBean.setItemType(BaseBean.WEATHER);

                    baseBean.setContext(weatherBean.getText());
                    baseBean.setWeatherBean(weatherBean);
                    data.setItemType(BaseBean.WEATHER);
                    data.setWeatherBean(weatherBean);
                    datas.add(data);
                    mAdapter.notifyDataSetChanged();
                    rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);
                }

                break;
            case R.id.iv_phone:

                mPresenter.getZhiHuNewsBean();

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

        if (llCentet.getVisibility() == View.VISIBLE)
            llCentet.setVisibility(View.INVISIBLE);

        if (rvSpeech.getVisibility() == View.GONE)
            rvSpeech.setVisibility(View.VISIBLE);

        if (RippleVoice_N.getVisibility() == View.GONE)
            RippleVoice_N.setVisibility(View.VISIBLE);

        if (viewpager.getVisibility() == View.VISIBLE)
            viewpager.setVisibility(View.GONE);




        zhiHuNewsBean.setItemType(BaseBean.NEWS);
        datas.add(zhiHuNewsBean);
        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);

    }

    @Override
    public void getZhiHuNewsBeanErrror(String error) {
        Logger.d(error);

    }
}
