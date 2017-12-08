package com.zhengpu.aiuilibrary.iflytekbean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * sayid ....
 * Created by wengmf on 2017/12/5.
 */

public class BaseBean implements MultiItemEntity {


    public static final int BAIKE = 10001;
    public static final int CALC = 10002;
    public static final int DATETIME = 10003;
    public static final int FLIGHT = 10004;
    public static final int JOKE = 10005;
    public static final int MUSICX = 10006;
    public static final int NEWS = 10007;
    public static final int OPENAPPTEST_APP = 10008;
    public static final int OPENQA = 10009;
    public static final int POETRY = 10010;
    public static final int STORY = 10011;
    public static final int OPENAPPTEST_SHIPING = 10012;
    public static final int WEATHER = 10013;
    public static final int USER_CHAT = 10014;
    public static final int HELP_CHAT = 10015;


    private int itemType;
    private String context;

    private BaikeBean baikeBean;
    private CalcBean calcBean;
    private DatetimeBean datetimeBean;
    private FlightBean flightBean;
    private JokeBean jokeBean;
    private MusicXBean musicXBean;
    private NewsBean newsBean;
    private OpenAppBean openAppBean;
    private OpenQABean openQABean;
    private PoetryBean poetryBean;
    private StoryBean storyBean;
    private VideoBean videoBean;
    private WeatherBean weatherBean;
    private UserChatBean userChatBean;
    private HelpBean helpBean;

    public HelpBean getHelpBean() {
        return helpBean;
    }

    public void setHelpBean(HelpBean helpBean) {
        this.helpBean = helpBean;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public UserChatBean getUserChatBean() {
        return userChatBean;
    }

    public void setUserChatBean(UserChatBean userChatBean) {
        this.userChatBean = userChatBean;
    }

    public BaikeBean getBaikeBean() {
        return baikeBean;
    }

    public void setBaikeBean(BaikeBean baikeBean) {
        this.baikeBean = baikeBean;
    }

    public CalcBean getCalcBean() {
        return calcBean;
    }

    public void setCalcBean(CalcBean calcBean) {
        this.calcBean = calcBean;
    }

    public DatetimeBean getDatetimeBean() {
        return datetimeBean;
    }

    public void setDatetimeBean(DatetimeBean datetimeBean) {
        this.datetimeBean = datetimeBean;
    }

    public FlightBean getFlightBean() {
        return flightBean;
    }

    public void setFlightBean(FlightBean flightBean) {
        this.flightBean = flightBean;
    }

    public JokeBean getJokeBean() {
        return jokeBean;
    }

    public void setJokeBean(JokeBean jokeBean) {
        this.jokeBean = jokeBean;
    }

    public MusicXBean getMusicXBean() {
        return musicXBean;
    }

    public void setMusicXBean(MusicXBean musicXBean) {
        this.musicXBean = musicXBean;
    }

    public NewsBean getNewsBean() {
        return newsBean;
    }

    public void setNewsBean(NewsBean newsBean) {
        this.newsBean = newsBean;
    }

    public OpenAppBean getOpenAppBean() {
        return openAppBean;
    }

    public void setOpenAppBean(OpenAppBean openAppBean) {
        this.openAppBean = openAppBean;
    }

    public OpenQABean getOpenQABean() {
        return openQABean;
    }

    public void setOpenQABean(OpenQABean openQABean) {
        this.openQABean = openQABean;
    }

    public PoetryBean getPoetryBean() {
        return poetryBean;
    }

    public void setPoetryBean(PoetryBean poetryBean) {
        this.poetryBean = poetryBean;
    }

    public StoryBean getStoryBean() {
        return storyBean;
    }

    public void setStoryBean(StoryBean storyBean) {
        this.storyBean = storyBean;
    }

    public VideoBean getVideoBean() {
        return videoBean;
    }

    public void setVideoBean(VideoBean videoBean) {
        this.videoBean = videoBean;
    }

    public WeatherBean getWeatherBean() {
        return weatherBean;
    }

    public void setWeatherBean(WeatherBean weatherBean) {
        this.weatherBean = weatherBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

}
