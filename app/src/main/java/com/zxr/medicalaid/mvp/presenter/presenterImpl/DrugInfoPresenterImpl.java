package com.zxr.medicalaid.mvp.presenter.presenterImpl;

import android.util.Log;

import com.zxr.medicalaid.mvp.entity.moudle.DrugInfo;
import com.zxr.medicalaid.mvp.model.DrugInfoModel;
import com.zxr.medicalaid.mvp.model.ModelImpl.DrugInfoModelImpl;
import com.zxr.medicalaid.mvp.presenter.DrugInfoPresenter;
import com.zxr.medicalaid.mvp.presenter.base.BasePresenterImpl;
import com.zxr.medicalaid.mvp.view.SearchView;
import com.zxr.medicalaid.net.FilterSubscriber;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS-NB on 2017/7/4.
 */

public class DrugInfoPresenterImpl extends BasePresenterImpl<SearchView> implements DrugInfoPresenter{
    DrugInfoModel model = new DrugInfoModelImpl();
    @Inject
    public DrugInfoPresenterImpl(){

    }
    @Override
    public void getDrugInfo(String drugName) {
        model.getDrugInfo(drugName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FilterSubscriber<DrugInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(DrugInfo drugInfoData) {
                        mView.showDrugInfo(drugInfoData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("TAG",e.getMessage());
                    }
                });
    }
}
