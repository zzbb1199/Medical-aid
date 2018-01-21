package com.zxr.medicalaid.mvp.model.ModelImpl;

import com.zxr.medicalaid.mvp.entity.moudle.PatientInfo;
import com.zxr.medicalaid.mvp.model.PatientListModel;
import com.zxr.medicalaid.mvp.model.base.BaseModelImpl;

import rx.Observable;

/**
 * Created by ASUS-NB on 2017/7/13.
 */

public class PatientListModelImpl extends BaseModelImpl implements PatientListModel{


    @Override
    public Observable<PatientInfo> getPatient(String doctorId, String type, String status, int currentPage) {
        return api.getPatient(doctorId, type, status, currentPage);
    }
}
