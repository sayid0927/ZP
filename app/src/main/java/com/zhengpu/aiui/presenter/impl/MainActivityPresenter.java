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


import com.zhengpu.aiui.api.Api;
import com.zhengpu.aiui.base.RxPresenter;

import com.zhengpu.aiui.presenter.contract.MainContract;
import com.zhengpu.aiuilibrary.iflytekbean.otherbean.ZhiHuNewsBean;

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
                        if (mView != null ) {
                            mView.getZhiHuNewsBeanErrror(e.toString());
                        }
                    }
                    @Override
                    public void onNext(ZhiHuNewsBean data) {
                        if (data != null && mView != null ) {
                            mView.getZhiHuNewsBeanSuccess(data);
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }



}
