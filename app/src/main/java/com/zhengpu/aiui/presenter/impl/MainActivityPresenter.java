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
package com.zhengpu.aiui.presenter.impl;


import com.orhanobut.logger.Logger;
import com.zhengpu.aiui.api.Api;
import com.zhengpu.aiui.base.RxPresenter;

import com.zhengpu.aiui.presenter.contract.MainContract;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.KuGouSongBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.KuGouSongInfoResult;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.WXItemBean;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.ZhiHuNewsBean;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter<MainContract.View> {

    private Api bookApi;
    public static boolean isLastSyncUpdateed = false;

    @Inject
    public MainActivityPresenter(Api bookApi) {
        this.bookApi = bookApi;
    }


    @Override
    public void getZhiHuNewsBean() {

        Subscription rxSubscription = bookApi.getDailyNews().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuNewsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.getErrror(e.toString());
                        }
                    }

                    @Override
                    public void onNext(ZhiHuNewsBean data) {
                        if (data != null && mView != null) {
                            mView.getZhiHuNewsBeanSuccess(data);
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getWXHot(int num, int page) {


        Subscription rxSubscription = bookApi.getWXHot(num, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WXItemBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WXItemBean wxItemBeans) {
                        if (wxItemBeans != null && wxItemBeans.getCode() == 200 && mView != null) {

                            mView.getWXHotSuccess(wxItemBeans);
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getSearchKugouSong(String keyword, String page, String pagesize) {

        Subscription rxSubscription = bookApi.getSearchKugouSong(keyword, page, pagesize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KuGouSongBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(KuGouSongBean kuGouSongBean) {
                        if (kuGouSongBean != null && kuGouSongBean.getStatus() == 1 && mView != null) {
                            mView.getSearchKugouSongSuccess(kuGouSongBean);
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getKugouSongInfo(String hash) {
        Subscription rxSubscription = bookApi.getKugouSongInfo(hash).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KuGouSongInfoResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(KuGouSongInfoResult kuGouSongInfoResult) {
                        if (kuGouSongInfoResult != null  && mView != null) {
                            mView.getKugouSongInfoSuccess(kuGouSongInfoResult);
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
