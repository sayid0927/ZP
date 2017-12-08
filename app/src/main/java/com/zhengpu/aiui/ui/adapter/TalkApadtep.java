package com.zhengpu.aiui.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhengpu.aiui.R;
import com.zhengpu.aiuilibrary.iflytekbean.BaseBean;
import com.zhengpu.aiuilibrary.iflytekbean.NewsBean;
import com.zhengpu.aiuilibrary.iflytekbean.WeatherBean;

import java.util.List;

/**
 * sayid ....
 * Created by wengmf on 2017/12/5.
 */

public class TalkApadtep extends BaseMultiItemQuickAdapter<BaseBean, BaseViewHolder> {

    private Context context;
    private NewsAdapter newsAdapter;


    public TalkApadtep(Context context, List data) {
        super(data);
        this.context = context;
        addItemType(BaseBean.USER_CHAT, R.layout.item_user_chat);
        addItemType(BaseBean.BAIKE, R.layout.item_baike_chat);
        addItemType(BaseBean.CALC, R.layout.item_calc_chat);
        addItemType(BaseBean.DATETIME, R.layout.item_calc_chat);
        addItemType(BaseBean.FLIGHT, R.layout.item_calc_chat);
        addItemType(BaseBean.JOKE, R.layout.item_calc_chat);
        addItemType(BaseBean.MUSICX, R.layout.item_calc_chat);
        addItemType(BaseBean.NEWS, R.layout.item_news_chat);
        addItemType(BaseBean.OPENAPPTEST_APP, R.layout.item_calc_chat);
        addItemType(BaseBean.OPENAPPTEST_SHIPING, R.layout.item_calc_chat);
        addItemType(BaseBean.OPENQA, R.layout.item_calc_chat);
        addItemType(BaseBean.POETRY, R.layout.item_calc_chat);
        addItemType(BaseBean.STORY, R.layout.item_calc_chat);
        addItemType(BaseBean.WEATHER, R.layout.item_weather_chat);
        addItemType(BaseBean.R4, R.layout.item_calc_chat);

    }

    @Override
    protected void convert(BaseViewHolder helper, BaseBean item) {

        switch (helper.getItemViewType()) {

            case BaseBean.USER_CHAT:
                helper.setText(R.id.chatlist_text_me, item.getUserChatBean().getText());
                break;
            case BaseBean.BAIKE:

                String value;
                if (item.getBaikeBean() != null && item.getBaikeBean().getData() != null &&
                        item.getBaikeBean().getData().getResult() != null && item.getBaikeBean().getData().getResult().get(0).getUrl() != null
                        ) {
                    ImageView iv = helper.getView(R.id.img);
                    Glide.with(context).load(item.getBaikeBean().getData().getResult().get(0).getUrl()).into(iv);
                }

                if (item.getBaikeBean() != null && item.getBaikeBean().getSemantic() != null &&
                        item.getBaikeBean().getSemantic().get(0).getSlotsBean() != null)

                    value = item.getBaikeBean().getSemantic().get(0).getSlotsBean().getValue();
                else
                    value = item.getBaikeBean().getText();

                helper.setText(R.id.tv_title, value);
                helper.setText(R.id.tv_context, item.getBaikeBean().getAnswer().getText());

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.baidu.com/s?wd=" + value + "&tn=SE_PSStatistics_p1d9m0nf"));
                context.startActivity(intent);

                break;

            case BaseBean.CALC:
                helper.setText(R.id.chatlist_text_other, item.getCalcBean().getAnswer().getText());
                break;

            case BaseBean.DATETIME:
                helper.setText(R.id.chatlist_text_other, item.getDatetimeBean().getAnswer().getText());
                break;

            case BaseBean.FLIGHT:
                helper.setText(R.id.chatlist_text_other, item.getFlightBean().getAnswer().getText());
                break;

            case BaseBean.JOKE:
                helper.setText(R.id.chatlist_text_other, item.getJokeBean().getAnswer().getText());
                break;
            case BaseBean.MUSICX:
                helper.setText(R.id.chatlist_text_other, item.getMusicXBean().getAnswer().getText());
                break;
            case BaseBean.NEWS:

                if (item.getNewsBean() != null && item.getNewsBean().getData() != null && item.getNewsBean().getData().getResult() != null) {
                    newsAdapter = new NewsAdapter(item.getNewsBean().getData().getResult(), context);
                    RecyclerView rvNews = helper.getView(R.id.rv_news);
                    rvNews.setAdapter(newsAdapter);
                }

                break;
            case BaseBean.OPENAPPTEST_APP:
                helper.setText(R.id.chatlist_text_other, item.getOpenAppBean().getText());
                break;
            case BaseBean.OPENAPPTEST_SHIPING:
                helper.setText(R.id.chatlist_text_other, item.getVideoBean().getText());
                break;
            case BaseBean.OPENQA:
                helper.setText(R.id.chatlist_text_other, item.getOpenQABean().getAnswer().getText());
                break;
            case BaseBean.POETRY:
                helper.setText(R.id.chatlist_text_other, item.getPoetryBean().getAnswer().getText());
                break;
            case BaseBean.STORY:
                helper.setText(R.id.chatlist_text_other, item.getStoryBean().getAnswer().getText());
                break;

            case BaseBean.WEATHER:

                if (item.getWeatherBean() != null && item.getWeatherBean().getData() != null &&
                        item.getWeatherBean().getData().getResult() != null && item.getWeatherBean().getData().getResult().size() != 0) {

                    WeatherBean.DataBean dataBean = item.getWeatherBean().getData();
                    String City = dataBean.getResult().get(0).getCity();
                    String Weather = dataBean.getResult().get(0).getWeather();
                    String airQuality = dataBean.getResult().get(0).getAirQuality();
                    String tempRange = dataBean.getResult().get(0).getTempRange();
                    String prompt = dataBean.getResult().get(0).getExp().getCt().getPrompt();

                    helper.setText(R.id.tv_city, City);
                    helper.setText(R.id.tv_weather, Weather);
                    helper.setText(R.id.tv_airQuality, airQuality);
                    helper.setText(R.id.tv_tempRange, tempRange);
                    helper.setText(R.id.tv_prompt, prompt);

                }

                break;

            case BaseBean.R4:
                helper.setText(R.id.chatlist_text_other, item.getR4Bean().getText());
                break;

        }
    }
}
