package com.zxr.medicalaid.mvp.model.ModelImpl;

import com.zxr.medicalaid.mvp.entity.moudle.PrescriptionInfo;
import com.zxr.medicalaid.mvp.model.GetPrescriptionModel;
import com.zxr.medicalaid.mvp.model.base.BaseModelImpl;

import rx.Observable;

/**
 * Created by ASUS-NB on 2017/8/3.
 */

public class GetPrescriptionModelImpl extends BaseModelImpl implements GetPrescriptionModel {
    @Override
    public Observable<PrescriptionInfo> getPrescription(long linkId) {
        return api.getPrescription(linkId);
    }
}
