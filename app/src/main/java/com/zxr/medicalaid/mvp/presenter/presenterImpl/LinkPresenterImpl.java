package com.zxr.medicalaid.mvp.presenter.presenterImpl;

import android.util.Log;

import com.zxr.medicalaid.mvp.entity.moudle.LinkInfo;
import com.zxr.medicalaid.mvp.model.LinkModel;
import com.zxr.medicalaid.mvp.model.ModelImpl.LinkModelImpl;
import com.zxr.medicalaid.mvp.presenter.LinkPresenter;
import com.zxr.medicalaid.mvp.presenter.base.BasePresenterImpl;
import com.zxr.medicalaid.mvp.view.LinkView;
import com.zxr.medicalaid.utils.others.CodeUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS-NB on 2017/7/8.
 */

public class LinkPresenterImpl extends BasePresenterImpl<LinkView> implements LinkPresenter {
    LinkModel model = new LinkModelImpl();

    @Inject
    public LinkPresenterImpl() {
    }

    @Override
    public void linkDP(String doctorId, String patientId) {
        model.linkDP(doctorId, patientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LinkInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());
                    }

                    @Override
                    public void onNext(LinkInfo linkInfo) {
                        if (CodeUtil.codeCheck(linkInfo.getCode()).equals("OK")) {
                            mView.linkSucceed(linkInfo);
                        } else {
                            Log.e("TAG", CodeUtil.codeCheck(linkInfo.getCode()));
                        }
                    }
                });
    }
}
