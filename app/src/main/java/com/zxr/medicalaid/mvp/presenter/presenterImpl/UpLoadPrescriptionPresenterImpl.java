package com.zxr.medicalaid.mvp.presenter.presenterImpl;

import android.util.Log;

import com.zxr.medicalaid.mvp.entity.moudle.PrescriptionInfo;
import com.zxr.medicalaid.mvp.model.ModelImpl.UpLoadPrescriptionModelImpl;
import com.zxr.medicalaid.mvp.model.UpLoadPrescriptionModel;
import com.zxr.medicalaid.mvp.presenter.UpLoadPrescriptionPresenter;
import com.zxr.medicalaid.mvp.presenter.base.BasePresenterImpl;
import com.zxr.medicalaid.mvp.view.UpLoadPrescriptionView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS-NB on 2017/8/3.
 */

public class UpLoadPrescriptionPresenterImpl extends BasePresenterImpl<UpLoadPrescriptionView> implements UpLoadPrescriptionPresenter{
    UpLoadPrescriptionModel model = new UpLoadPrescriptionModelImpl();
    @Inject
    public UpLoadPrescriptionPresenterImpl(){

    }
    @Override
    public void upLoadPrescription(long linkId, String message) {
        model.upLoadPrescription(linkId, message).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PrescriptionInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i("upLoadError",e.getMessage());
                        mView.showMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(PrescriptionInfo info) {
                        mView.upLaodSucceed(info);
                    }
                });
    }
}
