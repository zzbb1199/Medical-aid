package com.zxr.medicalaid.mvp.ui.activities;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.mvp.entity.PrescriptionItem;
import com.zxr.medicalaid.mvp.entity.moudle.DrugDose;
import com.zxr.medicalaid.mvp.entity.moudle.PrescriptionInfo;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.GetPrescriptionPresenterImpl;
import com.zxr.medicalaid.mvp.ui.activities.base.BaseActivity;
import com.zxr.medicalaid.mvp.ui.adapters.PrescribeTableAdapter;
import com.zxr.medicalaid.mvp.view.GetPrescriptionView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

public class PrescribeRecordActivity extends BaseActivity implements GetPrescriptionView{


    @InjectView(R.id.easy_recycler_view)
    EasyRecyclerView mRecyclerView;

    private PrescribeTableAdapter adapter;
    @Inject
    GetPrescriptionPresenterImpl presenter;
    private String idType;
    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
        presenter.injectView(this);
    }

    @Override
    public void initViews() {
        adapter = new PrescribeTableAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        idType = getIntent().getStringExtra("idType");
        String patientName= getIntent().getStringExtra("patientName");
        SharedPreferences sp;
        if(idType.equals("doctor")){
            sp = getSharedPreferences("linkIdForDoc",MODE_PRIVATE);
        }else {
            sp = getSharedPreferences("linkIdForPat",MODE_PRIVATE);
        }
        long linkId = sp.getLong(patientName,-1);
        Log.e(TAG,"linkId:"+linkId+ "   patientName:"+patientName);
        presenter.getPrescription(linkId);
        findViewById(android.R.id.content).setOnClickListener(
                v -> finish()
        );

    }
    @Override
    public int getLayout() {
        return R.layout.activity_prescribe_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {
            Log.e(TAG,msg);
    }
    List<PrescriptionItem> list = new ArrayList<>();
    @Override
    public void getSucceed(PrescriptionInfo info) {
        List<DrugDose> drugDoses;
        drugDoses = info.getBody().getDrugs();
        for(int i=0;i<drugDoses.size();i++) {
            Log.e(TAG, drugDoses.get(i).getDrugName() + " " + drugDoses.get(i).getDose());
            list.add(new PrescriptionItem(info.getBody().getDrugs().get(i).getDrugName(), info.getBody().getDrugs().get(i).getDose()));
        }
        adapter.addAll(list);
        mRecyclerView.setAdapter(adapter);

    }
}
