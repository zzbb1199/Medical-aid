package com.zxr.medicalaid.mvp.model;

import com.zxr.medicalaid.mvp.entity.moudle.PatientInfo;

import rx.Observable;

/**
 *
 *
 */

public interface PatientListModel {
    Observable<PatientInfo> getPatient(String doctorId,String type,String status,int currentPage);
}
