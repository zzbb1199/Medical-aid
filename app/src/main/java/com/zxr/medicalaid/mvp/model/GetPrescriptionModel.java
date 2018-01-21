package com.zxr.medicalaid.mvp.model;

import com.zxr.medicalaid.mvp.entity.moudle.PrescriptionInfo;

import rx.Observable;

/**
 * Created by ASUS-NB on 2017/8/3.
 */

public interface GetPrescriptionModel {
    Observable<PrescriptionInfo> getPrescription(long linkId);
}
