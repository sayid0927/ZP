/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhengpu.aiui.api;


import com.zhengpu.aiui.base.Constant;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.KuGouSongBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.WXItemBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.ZhiHuNewsBean;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


public interface ApiService {


    /**
     *   知乎新闻
     */
    @Headers({"url_name:zhihu"})
    @GET("api/4/news/latest")
    Observable<ZhiHuNewsBean> getDailyNews();


    /**
     * 微信精选列表
     */
    @Headers({"url_name:wechat"})
    @GET("wxnew")
    Observable<WXItemBean> getWXHot(@Query("key") String key, @Query("num") int num, @Query("page") int page);


    /**
     * 搜索酷狗音乐
     */
    @Headers({"url_name:kugou"})
    @GET("api/v3/search/song")
    Observable<KuGouSongBean> getSearchKugouSong(@Query("format") String format, @Query("keyword") String keyword,
                                                 @Query("page") String page, @Query("pagesize") String pagesize);






}