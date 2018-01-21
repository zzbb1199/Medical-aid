package com.zxr.medicalaid.mvp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.lazylibrary.util.DensityUtil;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.zxr.medicalaid.DaoSession;
import com.zxr.medicalaid.MedicalList;
import com.zxr.medicalaid.MedicalListDao;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.mvp.entity.Person;
import com.zxr.medicalaid.mvp.ui.activities.base.BaseActivity;
import com.zxr.medicalaid.mvp.ui.adapters.InquiryContentAdapter;
import com.zxr.medicalaid.mvp.ui.adapters.InquiryHeaderAdapter;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.others.EasyRecyViewInitUtils;
import com.zxr.medicalaid.widget.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class InquiryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{


    /**
     * view
     */
    @InjectView(R.id.inquiry_easy_recycler_view)
    EasyRecyclerView mEasyRecyclerView;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    DaoSession daoSession;
    MedicalListDao medicalListDao;
    List<String> medicalName;
    List<String> medicaldome;
    List<String> patientName = new ArrayList<>();
    /**
     * 数据
     */
    private InquiryContentAdapter adapter;
    private String idType;

    List<String> linkIdList = new ArrayList<>();
    String linkIdArray[];
    @Override
    public void initInjector() {

    }

    @Override
    public void initViews() {
        //初始化ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        SharedPreferences sharedPreferences = getSharedPreferences("linkId",MODE_PRIVATE);
//        String str = sharedPreferences.getString("linkId","");
//        if(!str.equals("")){
//             linkIdArray=str.split(",");
//        }
//        for(int i=0;i<linkIdArray.length;i++){
//            linkIdList.add(linkIdArray[i]);
        //        }
        idType = getIntent().getStringExtra("type");
        if(idType.equals("doctor")){
            mToolbar.setTitle(R.string.inquiryRecord);
        }else {
            mToolbar.setTitle("就诊记录");
        }
        mToolbar.setTitleTextColor(Color.WHITE);
        //初始化adaper
        adapter = new InquiryContentAdapter(this);

        adapter.setOnItemClickListener((position) -> {
            //进行详细药方详细信息显示
            Intent intent = new Intent(InquiryActivity.this,PrescribeRecordActivity.class);
            intent.putExtra("patientName",patientName.get(position));
            intent.putExtra("idType",idType);
            startActivity(intent);
        });
        //初始化RecyclerView
        EasyRecyViewInitUtils.initEasyRecyclerView(mEasyRecyclerView);
        mEasyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, DensityUtil.dip2px(this, 0.5f), DensityUtil.dip2px(this, 8f), DensityUtil.dip2px(this, 8f));
        itemDecoration.setDrawLastItem(false);
//        mEasyRecyclerView.addItemDecoration(itemDecoration);
        mEasyRecyclerView.setAdapter(adapter);

        //加入header
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new InquiryHeaderAdapter(this, adapter));
        decoration.setIncludeHeader(true);
        mEasyRecyclerView.addItemDecoration(decoration);
        mEasyRecyclerView.setRefreshListener(this);
        mEasyRecyclerView.setRefreshing(true, true);
        
        adapter.addAll(datas);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_inquiry;
    }

    /**
     * 测试用的handler
     */
    private Handler handler = new Handler();
    private List<Person> datas = new ArrayList<>();

    @Override
    public void onRefresh() {
        
        //进行加载
        Log.e("TAG","onRefresh");
        daoSession = DbUtil.getDaosession();
        MedicalListDao medicalListDao = daoSession.getMedicalListDao();
        datas.clear();
        patientName.clear();
        adapter.clear();
        if(idType.equals("doctor")){
            Log.e("TAG","doctor");
            List<MedicalList> lists = medicalListDao.queryBuilder().where(MedicalListDao.Properties.Patient.eq("patient")).list();
            Log.e(TAG,lists.size()+"");
            for(int i=0;i<lists.size();i++){
                datas.add(new Person(lists.get(i).getName(),lists.get(i).getDate(),""));
                Log.e("TAG",datas.get(i).getName());
                patientName.add(lists.get(i).getName());
            }
        }else if(idType.equals("patient")){
            List<MedicalList> lists = medicalListDao.queryBuilder().where(MedicalListDao.Properties.Patient.eq("doctor")).list();
            for(int i=0;i<lists.size();i++){
                datas.add(new Person(lists.get(i).getName(),lists.get(i).getDate(),""));
                patientName.add(lists.get(i).getName());
            }
        }
//        List<MedicalList> lists = daoSession.getMedicalListDao().queryBuilder().list();
//        for(int i=0;i<lists.size();i++){
//            datas.add(new Person(lists.get(i).getName(),lists.get(i).getDate(),""));
//            patientName.add(lists.get(i).getName());
//        }

        adapter.addAll(datas);
        adapter.notifyDataSetChanged();
//        User user = daoSession.getUserDao().queryBuilder().where(UserDao.Properties.IsAlready.eq(1)).unique();
//        Link link =daoSession.getLinkDao().queryBuilder().where(LinkDao.Properties.Id.eq(user.getLinkId())).unique();
//        handler.postDelayed(() -> {
//            for(int i=0;i<lists.size();i++){
//                datas.add(new Person(lists.get(i).getPatient(),lists.get(i).getDate(),""));
//            }
//            adapter.addAll(datas);
//        }, 1000);


    }


//    @Override
//    public void showProgress() {
//
//    }
//
//    @Override
//    public void hideProgress() {
//
//    }
//
//    @Override
//    public void showMsg(String msg) {
//        Log.e(TAG,msg);
//    }
//
//    @Override
//    public void getSucceed(PrescriptionInfo info) {
//        List<DrugDose> drugDoses=info.getBody().getDrugs();
//        for(int i=0;i<drugDoses.size();i++){
//            medicaldome.add(drugDoses.get(i).getDose());
//            medicalName.add(drugDoses.get(i).getDrugName());
//        }
//    }
}
