package com.zxr.medicalaid.mvp.model.ModelImpl;

import com.zxr.medicalaid.mvp.entity.moudle.PrescriptionInfo;
import com.zxr.medicalaid.mvp.model.UpLoadPrescriptionModel;
import com.zxr.medicalaid.mvp.model.base.BaseModelImpl;

import rx.Observable;

/**
 * Created by ASUS-NB on 2017/8/3.
 */

public class UpLoadPrescriptionModelImpl extends BaseModelImpl implements UpLoadPrescriptionModel {
    @Override
    public Observable<PrescriptionInfo> upLoadPrescription(long linkId, String message) {
        return api.uploadPrescription(linkId,message);
    }
    

}
