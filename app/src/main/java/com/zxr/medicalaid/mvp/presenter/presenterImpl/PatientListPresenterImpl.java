package com.zxr.medicalaid.mvp.presenter.presenterImpl;

import android.util.Log;
import android.widget.Toast;

import com.zxr.medicalaid.App;
import com.zxr.medicalaid.mvp.entity.moudle.PatientInfo;
import com.zxr.medicalaid.mvp.model.ModelImpl.PatientListModelImpl;
import com.zxr.medicalaid.mvp.model.PatientListModel;
import com.zxr.medicalaid.mvp.presenter.PatientPresent;
import com.zxr.medicalaid.mvp.presenter.base.BasePresenterImpl;
import com.zxr.medicalaid.mvp.view.PatientListView;
import com.zxr.medicalaid.utils.others.CodeUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS-NB on 2017/7/13.
 */

public class PatientListPresenterImpl extends BasePresenterImpl<PatientListView> implements PatientPresent {
    PatientListModel model = new PatientListModelImpl();
    @Inject
    public PatientListPresenterImpl(){

    }
    @Override
    public void getPatient(String doctorId,String type,String status,int currentPage) {
        model.getPatient(doctorId, type, status, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PatientInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG",e.getMessage());
                    }

                    @Override
                    public void onNext(PatientInfo patientInfo) {
                        Log.e("TAG","请求列表成功"+patientInfo.getCode()+patientInfo.getMessage());
                        if(CodeUtil.codeCheck(patientInfo.getCode()).equals("OK"))
                        mView.showPatient(patientInfo);
                        else Toast.makeText(App.getBaseApplicationContext(),CodeUtil.codeCheck(patientInfo.getCode()),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
