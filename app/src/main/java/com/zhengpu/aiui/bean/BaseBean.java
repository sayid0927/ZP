package com.zhengpu.aiui.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * sayid ....
 * Created by wengmf on 2017/12/11.
 */

public class BaseBean  implements MultiItemEntity {

    private int itemType;
    private  ZhiHuNewsBean zhiHuNewsBean;





    public ZhiHuNewsBean getZhiHuNewsBean() {
        return zhiHuNewsBean;
    }

    public void setZhiHuNewsBean(ZhiHuNewsBean zhiHuNewsBean) {
        this.zhiHuNewsBean = zhiHuNewsBean;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
