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
package com.zhengpu.aiui.presenter.contract;


import com.zhengpu.aiui.base.BaseContract;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.KuGouSongBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.KuGouSongInfoResult;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.TianJokeBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.WXItemBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.ZhiHuNewsBean;


import java.io.File;
import java.util.List;

public interface MainContract {

    interface View extends BaseContract.BaseView {

        void getZhiHuNewsBeanSuccess(ZhiHuNewsBean data);

        void getErrror(String error);

        void getWXHotSuccess(WXItemBean newslistBean);

        void getSearchKugouSongSuccess(KuGouSongBean kuGouSongBean);

        void getKugouSongInfoSuccess(KuGouSongInfoResult kuGouSongInfoResult);
        void getTianJokeSuccess(TianJokeBean jokeBean);
        void downloadLyric (File file,byte[] bytes);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getZhiHuNewsBean();

        void getWXHot(int num, int page);

        void getSearchKugouSong(String keyword, String page, String pagesize);

        void getKugouSongInfo(String hash);

        void getTianJoke();

        void  downloadLyric(String keyword,int duration,String hash);

    }
}
