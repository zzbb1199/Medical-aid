package com.zxr.medicalaid.mvp.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zxr.medicalaid.MedicalDateInfo;
import com.zxr.medicalaid.MedicalDateInfoDao;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.mvp.ui.activities.base.BaseActivity;
import com.zxr.medicalaid.mvp.ui.adapters.MedicalDateSettingAdapter;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.others.EasyRecyViewInitUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by Raven on 2017/8/7.
 */

public class MedicalDateSettingActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.easy_recycler_view)
    EasyRecyclerView mRecyclerView;
    /**
     *
     */
    //第一次进入，先进行药品的固化
    MedicalDateInfoDao dao = DbUtil.getDaosession().getMedicalDateInfoDao();

    /**
     * 数据
     */
    private MedicalDateSettingAdapter adapter;

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("药柜信息");
        mToolbar.setTitleTextColor(Color.WHITE);

        //初始化adapter
        adapter = new MedicalDateSettingAdapter(this);
        //设置recyclerview
        EasyRecyViewInitUtils.initEasyRecyclerView(mRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        initDatas();
        initListners();
    }

    private void initListners() {
        adapter.setOnItemClickListener(
                position -> {
                    //选择生产日期还是保质期
                    new AlertDialog.Builder(this)
                            .setItems(new String[]{"生产日期", "保质期"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (which == 0) {
                                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                                        calendar.setTimeInMillis(System.currentTimeMillis());
                                        calendar.set(java.util.Calendar.YEAR, 1970);
                                        calendar.set(java.util.Calendar.MONTH, 0);
                                        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
                                        TimePickerDialog timePickerDialog = new TimePickerDialog.Builder()
                                                .setCallBack(new OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(millseconds);
                                                        adapter.getAllData().get(position).setProductDate(date);
                                                        adapter.notifyDataSetChanged();
                                                        //刷新数据库
                                                        MedicalDateInfo medicalDateInfo = dao.loadByRowId(position);
                                                        medicalDateInfo = adapter.getAllData().get(position);
                                                        dao.update(medicalDateInfo);
                                                    }
                                                })
                                                .setCancelStringId("取消")
                                                .setSureStringId("确定")
                                                .setTitleStringId("生产日期")
                                                .setCyclic(false)
                                                .setMinMillseconds(calendar.getTimeInMillis())
                                                .setMaxMillseconds(System.currentTimeMillis())
                                                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                                                .setType(Type.YEAR_MONTH_DAY)
                                                .setWheelItemTextNormalColor(getResources().getColor(R.color.secondary_text))
                                                .setWheelItemTextSelectorColor(getResources().getColor(R.color.primary_text))
                                                .setWheelItemTextSize(16)
                                                .build();
                                        timePickerDialog.show(getSupportFragmentManager(), null);
                                    } else if (which == 1) {
                                        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
                                        calendar1.setTimeInMillis(System.currentTimeMillis());
                                        calendar1.set(Calendar.YEAR, 1);
                                        calendar1.set(Calendar.MONTH, 0);
                                        java.util.Calendar calendar2 = java.util.Calendar.getInstance();
                                        calendar2.setTimeInMillis(System.currentTimeMillis());
                                        calendar2.set(Calendar.YEAR, 29);
                                        calendar2.set(Calendar.MONTH, 11);
                                        TimePickerDialog timePickerDialog = new TimePickerDialog.Builder()
                                                .setCallBack(new OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                                        String date = new SimpleDateFormat("yy年MM月").format(millseconds);
                                                        int year = Integer.parseInt(date.substring(0, 2));
                                                        String month = date.substring(3, 5);
                                                        if (month.equals("12")) {
                                                            year += 1;
                                                            adapter.getAllData().get(position).setShelfLife(year + "年");
                                                        } else {
                                                            adapter.getAllData().get(position).setShelfLife(date);
                                                        }

                                                        adapter.notifyDataSetChanged();
                                                        //刷新数据库
                                                        MedicalDateInfo medicalDateInfo = dao.loadByRowId(position);
                                                        medicalDateInfo = adapter.getAllData().get(position);
                                                        dao.update(medicalDateInfo);
                                                    }
                                                })
                                                .setCancelStringId("取消")
                                                .setSureStringId("确定")
                                                .setTitleStringId("保质期")
                                                .setCyclic(false)
                                                .setMinMillseconds(calendar1.getTimeInMillis())
                                                .setMaxMillseconds(calendar2.getTimeInMillis())
                                                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                                                .setType(Type.YEAR_MONTH)
                                                .setWheelItemTextNormalColor(getResources().getColor(R.color.secondary_text))
                                                .setWheelItemTextSelectorColor(getResources().getColor(R.color.primary_text))
                                                .setWheelItemTextSize(16)
                                                .build();
                                        timePickerDialog.show(getSupportFragmentManager(), null);
                                    }
                                }
                            }).show();
                }
        );
    }


    private void initDatas() {
        List<MedicalDateInfo> list = dao.loadAll();
        if (list == null || DbUtil.getDaosession().getMedicalDateInfoDao().loadAll().size() == 0) {
            //还未进行固化
            MedicalDateInfo m1 = new MedicalDateInfo(null,1, "何首乌", "2015-01-02", "2年");
            MedicalDateInfo m2 = new MedicalDateInfo(null,2, "冬虫夏草", "2015-01-02", "2年");
            MedicalDateInfo m3 = new MedicalDateInfo(null,3, "人参", "2015-01-02", "2年");
            MedicalDateInfo m4 = new MedicalDateInfo(null, 4, "当归", "2015-01-02", "2年");
            dao.insert(m1);
            dao.insert(m2);
            dao.insert(m3);
            dao.insert(m4);

            list = new ArrayList<>();
            list.add(m1);
            list.add(m2);
            list.add(m3);
            list.add(m4);
        }
        adapter.addAll(list);


    }

    @Override
    public int getLayout() {
        return R.layout.activity_medical_date_setting;
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
}
