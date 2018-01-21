package com.zxr.medicalaid.mvp.presenter;

import com.zxr.medicalaid.mvp.presenter.base.BasePresenter;

/**
 * Created by ASUS-NB on 2017/8/3.
 */

public interface GetPrescriptionPresenter extends BasePresenter {
    void getPrescription(long linkId);
}
