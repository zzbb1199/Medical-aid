package com.zxr.medicalaid.mvp.presenter;

import com.zxr.medicalaid.mvp.presenter.base.BasePresenter;

/**
 * Created by ASUS-NB on 2017/7/13.
 */

public interface PatientPresent extends BasePresenter {
    void getPatient(String doctorId,String type,String status,int currentPage);
}
