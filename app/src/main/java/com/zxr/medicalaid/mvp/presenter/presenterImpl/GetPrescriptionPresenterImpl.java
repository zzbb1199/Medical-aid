package com.zxr.medicalaid.mvp.presenter.presenterImpl;

import com.zxr.medicalaid.mvp.entity.moudle.PrescriptionInfo;
import com.zxr.medicalaid.mvp.model.GetPrescriptionModel;
import com.zxr.medicalaid.mvp.model.ModelImpl.GetPrescriptionModelImpl;
import com.zxr.medicalaid.mvp.presenter.GetPrescriptionPresenter;
import com.zxr.medicalaid.mvp.presenter.base.BasePresenterImpl;
import com.zxr.medicalaid.mvp.view.GetPrescriptionView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS-NB on 2017/8/3.
 */

public class GetPrescriptionPresenterImpl extends BasePresenterImpl<GetPrescriptionView> implements GetPrescriptionPresenter {
    GetPrescriptionModel model = new GetPrescriptionModelImpl();
    @Inject
    public GetPrescriptionPresenterImpl(){

    }
    @Override
    public void getPrescription(long linkId) {
        model.getPrescription(linkId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PrescriptionInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(PrescriptionInfo info) {
                        mView.getSucceed(info);
                    }
                });
    }
}
