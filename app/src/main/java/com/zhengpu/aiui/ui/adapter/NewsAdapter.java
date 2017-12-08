package com.zhengpu.aiui.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhengpu.aiui.R;
import com.zhengpu.aiuilibrary.iflytekbean.NewsBean;

import java.util.List;

/**
 * sayid ....
 * Created by wengmf on 2017/12/7.
 */

public class NewsAdapter extends BaseQuickAdapter<NewsBean.DataBean.ResultBean, BaseViewHolder> {


    private Context context;

    public NewsAdapter(List<NewsBean.DataBean.ResultBean> data, Context context) {
        super(R.layout.item_news, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean.DataBean.ResultBean item) {

        ImageView iv = helper.getView(R.id.im_url);
        Glide.with(context).load(item.getUrl()).into(iv);

    }
}
