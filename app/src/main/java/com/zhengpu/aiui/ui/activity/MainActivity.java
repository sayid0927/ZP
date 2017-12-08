package com.zhengpu.aiui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.zhengpu.aiui.ui.adapter.TalkApadtep;
import com.zhengpu.aiuilibrary.iflytekbean.BaseBean;
import com.zhengpu.aiuilibrary.iflytekbean.HelpBean;
import com.zhengpu.aiuilibrary.iflytekbean.UserChatBean;
import com.zhengpu.aiuilibrary.iflytekutils.IGetVoiceToWord;
import com.zhengpu.aiuilibrary.iflytekutils.VoiceToWords;
import com.zhengpu.aiuilibrary.service.SpeechRecognizerService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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


    private TalkApadtep mAdapter;
    private BaseBean data;
    private List<BaseBean> datas;
    private List<BaseBean> saveData;
    private VoiceToWords voiceToWords;
    private boolean isFist = true;
    private boolean isClickHelp = false;
    private UserChatBean userChatBean;


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

    @Override
    public void initView() {

//        if (!isAccessibilitySettingsOn(getApplicationContext())) {
//            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//        }


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

        Iterator<BaseBean> iter = datas.iterator();
        while (iter.hasNext()) {
            BaseBean dict = iter.next();
            if (dict.getHelpBean() != null) {
                iter.remove();
            }
        }

        userChatBean = new UserChatBean();
        data = new BaseBean();
        userChatBean.setText(result.getContext());
        data.setItemType(BaseBean.USER_CHAT);
        data.setUserChatBean(userChatBean);
        datas.add(data);
        datas.add(result);

        mAdapter.notifyDataSetChanged();
        rvSpeech.scrollToPosition(mAdapter.getItemCount() - 1);

    }

    @Override
    public void showLowVoice(String result) {
        Logger.e(result);
        voiceToWords.startRecognizer();
    }

    @Override
    public void appendResult(CharSequence sequence) {
//        Logger.e("说话内容" + sequence);
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


    @OnClick({R.id.iv_phone, R.id.iv_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_phone:
                break;
            case R.id.iv_help:

                saveData = datas;


                if (!isClickHelp) {

                    if (datas.size() != 0)
                        datas.clear();

                    if (llCentet.getVisibility() == View.VISIBLE)
                        llCentet.setVisibility(View.INVISIBLE);
                    if (rvSpeech.getVisibility() == View.GONE)
                        rvSpeech.setVisibility(View.VISIBLE);
                    if (RippleVoice_N.getVisibility() == View.GONE)
                        RippleVoice_N.setVisibility(View.VISIBLE);


                    BaseBean data = new BaseBean();
                    HelpBean helpBean = new HelpBean();
                    helpBean.setText("你可以这样对我说");
                    helpBean.setType(0);
                    data.setHelpBean(helpBean);
                    data.setItemType(BaseBean.HELP_CHAT);
                    datas.add(data);

                    BaseBean data1 = new BaseBean();
                    HelpBean helpBean1 = new HelpBean();
                    helpBean1.setText("......");
                    helpBean1.setType(0);
                    data1.setHelpBean(helpBean1);
                    data1.setItemType(BaseBean.HELP_CHAT);
                    datas.add(data1);

                    BaseBean data2 = new BaseBean();
                    HelpBean helpBean2 = new HelpBean();
                    helpBean2.setText("你是谁？");
                    helpBean2.setType(1);
                    data2.setHelpBean(helpBean2);
                    data2.setItemType(BaseBean.HELP_CHAT);
                    datas.add(data2);

                    BaseBean data3 = new BaseBean();
                    HelpBean helpBean3 = new HelpBean();
                    helpBean3.setText("给我讲个故事");
                    helpBean3.setType(1);
                    data3.setHelpBean(helpBean3);
                    data3.setItemType(BaseBean.HELP_CHAT);
                    datas.add(data3);

                    BaseBean data4 = new BaseBean();
                    HelpBean helpBean4 = new HelpBean();
                    helpBean4.setText("给我讲个笑话");
                    helpBean4.setType(1);
                    data4.setHelpBean(helpBean4);
                    data4.setItemType(BaseBean.HELP_CHAT);
                    datas.add(data4);

                    BaseBean data5 = new BaseBean();
                    HelpBean helpBean5 = new HelpBean();
                    helpBean5.setText("你会做什么？");
                    helpBean5.setType(1);
                    data5.setHelpBean(helpBean5);
                    data5.setItemType(BaseBean.HELP_CHAT);
                    datas.add(data5);

                    BaseBean data6 = new BaseBean();
                    HelpBean helpBean6 = new HelpBean();
                    helpBean6.setText("你多大了？");
                    helpBean6.setType(1);
                    data6.setHelpBean(helpBean6);
                    data6.setItemType(BaseBean.HELP_CHAT);
                    datas.add(data6);

                    mAdapter.notifyDataSetChanged();
                    isClickHelp = true;
                    break;

                } else {

                    Iterator<BaseBean> iter = datas.iterator();
                    while (iter.hasNext()) {
                        BaseBean dict = iter.next();
                        if (dict.getHelpBean() != null) {
                            iter.remove();
                        }
                    }

                    mAdapter.setNewData(saveData);
                    mAdapter.notifyDataSetChanged();
                    isClickHelp = false;
                    break;
                }
        }
    }
}
