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
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.WXItemBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.ZhiHuNewsBean;
import com.zhengpu.aiuilibrary.iflytekutils.IGetVoiceToWord;
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


    private static final int NUM_OF_PAGE = 20;
    private int currentPage = 1;

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

                mPresenter.getWXHot(NUM_OF_PAGE, currentPage);

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
    public void getZhiHuNewsBeanErrror(String error) {
        Logger.d(error);

    }

    @Override
    public void getWXHotSuccess(WXItemBean wxItemBeans) {


        isFist = false;


        if (llCentet.getVisibility() == View.VISIBLE)
            llCentet.setVisibility(View.INVISIBLE);

        if (rvSpeech.getVisibility() == View.GONE)
            rvSpeech.setVisibility(View.VISIBLE);

        if (RippleVoice_N.getVisibility() == View.GONE)
            RippleVoice_N.setVisibility(View.VISIBLE);

        if (viewpager.getVisibility() == View.VISIBLE)
            viewpager.setVisibility(View.GONE);


        data = new BaseBean();
        data.setWxItemBean(wxItemBeans);
        data.setItemType(BaseBean.NEWS);
        data.setWxItemBean(wxItemBeans);
        datas.add(data);
        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);
    }
}
