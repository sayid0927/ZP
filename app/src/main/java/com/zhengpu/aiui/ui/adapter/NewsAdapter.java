package com.zhengpu.aiui.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhengpu.aiui.R;

import com.zhengpu.aiuilibrary.iflytekbean.NewsBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.ZhiHuNewsBean;

import java.util.List;

/**
 * sayid ....
 * Created by wengmf on 2017/12/7.
 */

public class NewsAdapter extends BaseQuickAdapter<ZhiHuNewsBean.TopStoriesBean, BaseViewHolder> {


    private Context context;

    public NewsAdapter(List<ZhiHuNewsBean.TopStoriesBean> data, Context context) {
        super(R.layout.item_news, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ZhiHuNewsBean.TopStoriesBean item) {

        ImageView iv = helper.getView(R.id.im_url);
        Glide.with(context).load(item.getImage()).into(iv);
        helper.setText(R.id.title,item.getTitle());

    }
}
