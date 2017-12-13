package com.zxr.medicalaid.mvp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;

import com.github.lazylibrary.util.ToastUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.dagger.scope.ContextLife;
import com.zxr.medicalaid.mvp.entity.Person;
import com.zxr.medicalaid.mvp.entity.moudle.PatientInfo;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.CanclePresenterImpl;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.PatientListPresenterImpl;
import com.zxr.medicalaid.mvp.ui.activities.base.RxBusSubscriberBaseActivity;
import com.zxr.medicalaid.mvp.ui.adapters.PatientListAdapter;
import com.zxr.medicalaid.mvp.view.CancleView;
import com.zxr.medicalaid.mvp.view.PatientListView;
import com.zxr.medicalaid.utils.db.IdUtil;
import com.zxr.medicalaid.utils.system.RxBus;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import butterknife.InjectView;


/**
 * 区分药师和病人
 */
public class CurrentPatientsActivity extends RxBusSubscriberBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, PatientListView, CancleView {

    @Inject
    PatientListPresenterImpl presenter;
    @Inject
    CanclePresenterImpl canclePresenter;
    @InjectView(R.id.mToolbar)
    Toolbar mToolbar;
    @InjectView(R.id.person_list)
    EasyRecyclerView mRecycler;
    public static final String GET_FROM = "get_from";
    private int type = 0;
    public static final int DOCTOR = 1;
    public static final int PATIENT = 2;
    MessageDigest md;
    /**
     * adapter
     */
    private PatientListAdapter adapter;
    private static final String LINKING = "linking";
    private static final String HISTORY = "history";
    /**
     *
     */
    String doctorId;
    /**
     * context
     */

    @Inject
    @ContextLife("Activity")
    Context mContext;


    private List<Person> lists = new ArrayList<>();
    private List<String> listId = new ArrayList<>();
    private List<String> listNumber = new ArrayList<>();
    private List<Integer> listId1 = new ArrayList<>();
    private List<String> patientName = new ArrayList<>();

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
        presenter.injectView(this);
        canclePresenter.injectView(this);
    }

    @Override
    public void initViews() {
        //toolbar

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle(R.string.current_patients_num);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        doctorId = getIntent().getStringExtra("uId");
        SharedPreferences preferences = getSharedPreferences("isConnect", MODE_PRIVATE);
        String uId = preferences.getString("uId", "");
        if (!uId.equals("")) {
            doctorId = uId;
        }
        Log.e(TAG,"收到");
        if (doctorId != null) {
            Log.e(TAG, doctorId);
            type = PATIENT;
            presenter.getPatient(doctorId,"doctor",LINKING,1);
        } else {
            presenter.getPatient(IdUtil.getIdString(),"doctor",LINKING,1);
        }


        //recyclerview
        //adapter设置
        adapter = new PatientListAdapter(this);
        //设置item的点击监听
        adapter.setOnItemClickListener(
                pos -> {
                    if (type == PATIENT) {
//                        Toast.makeText(this, "病人，可选择退出", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
//                        Toast.makeText(this, "医生，点击进行开药", Toast.LENGTH_SHORT).show();
//                        ToActivityUtil.toNextActivity(mContext, PrescribeActivity.class);
                        Intent intent = new Intent(CurrentPatientsActivity.this,PrescribeActivity.class);
                        intent.putExtra("name",lists.get(pos).getName());
                        intent.putExtra("number",listNumber.get(pos));
                        intent.putExtra("linkId",listId1.get(pos));
                        intent.putExtra("patientName",patientName.get(pos));
                        startActivity(intent);
                    }
                }
        );
        if (type == DOCTOR) {
            adapter.setOnItemLongClickListener(
                    position -> {
                        new AlertDialog.Builder(this)
                                .setTitle("提示")
                                .setCancelable(true)
                                .setMessage("确定要将他出当前队列?")
                                .setPositiveButton("确定",
                                        (dialog, what) -> {
                                            adapter.remove(position);
                                            canclePresenter.cancleLink(IdUtil.getIdString(), listId.get(position));
                                            dialog.dismiss();
                                        }
                                )
                                .setNegativeButton("取消",
                                        (dialog, what) ->
                                                dialog.dismiss()
                                )
                                .show();
                        return false;
                    }
            );
        }

        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(adapter);
        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshing(true, true);//打开自动刷新并且立即毁掉
        mRecycler.setRefreshingColorResources(R.color.red_600, R.color.yellow_600, R.color.blue_600, R.color.green_600);
        //设置动画
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_patient_list_item));
        set.setInterpolator(new DecelerateInterpolator());
        LayoutAnimationController lc = new LayoutAnimationController(set);
        lc.setInterpolator(new LinearInterpolator());
        lc.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lc.setDelay(0.4f);
        mRecycler.getRecyclerView().setLayoutAnimation(lc);

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (type == PATIENT) {
            getMenuInflater().inflate(R.menu.patient_quit_menu, menu);
        }
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        //add top-left icon click event deal
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.quit:
                if (doctorId == null) {
                    ToastUtils.showToast(this, "出现异常，请重试");
                    return super.onOptionsItemSelected(item);
                }
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("您确定要取消挂号吗?")
                        .setPositiveButton("确定",
                                (dialog, what) -> {
                                    canclePresenter.cancleLink(doctorId, IdUtil.getIdString());
                                    dialog.dismiss();
                                })
                        .setNegativeButton("取消",
                                (dialog, what) ->
                                        dialog.dismiss()
                        )
                        .setCancelable(true)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_current_patients;
    }

    @Override
    public void initRxBus() {
        RxBus.getDefault().toObservable(String.class)
                .subscribe(
                        str -> {
                            //网路断开
                            canclePresenter.cancleLink(IdUtil.getIdString(), str);
                            //更新UI
                            int index = listId.indexOf(str);
                            if (index >= 0) {
                                adapter.remove(index);
                            }
                        }
                );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        type = getIntent().getIntExtra(GET_FROM, 0);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onRefresh() {
        if (doctorId != null) {
            Log.e(TAG, doctorId);
            type = PATIENT;
            presenter.getPatient(doctorId,"doctor",LINKING,1);
        } else {
            presenter.getPatient(IdUtil.getIdString(),"doctor",LINKING,1);
        }
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.showToast(this, msg);
    }

    public String doEncode(String data, String keyString) {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //生成字节Key
        byte[] byteKey = md.digest(keyString.getBytes());
        //Key转换
        Key convertKey = new SecretKeySpec(byteKey, "AES");
        Key myKey = convertKey;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //解密
            cipher.init(Cipher.DECRYPT_MODE, myKey);
            byte[] decodeByte = cipher.doFinal(hex2byte(data));
            String decodeString = new String(decodeByte);
            Log.e("decodeString", new String(decodeString));
            return decodeString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final byte[] hex2byte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    @Override
    public void showPatient(PatientInfo patientInfo) {
        adapter.clear();
        adapter.notifyDataSetChanged();
        listId.clear();
        lists.clear();
        listNumber.clear();
        for (int i = 0; i < patientInfo.getBody().getList().size(); i++) {
            String name =patientInfo.getBody().getList().get(i).getPatient().getNickName();
            String phoneNumber= patientInfo.getBody().getList().get(i).getPatient().getPhoneNumber();
            Log.e(TAG,patientInfo.getBody().getList().get(i).getPatient().getNickName());
            Person person = new Person(name, phoneNumber, "120.77.87.78:8080/arti-sports/image//user15.png");

            lists.add(person);
            patientName.add(name);
            listId.add(patientInfo.getBody().getList().get(i).getPatient().getIdString());
            listId1.add(patientInfo.getBody().getList().get(i).getId());
            listNumber.add(phoneNumber);
        }
        adapter.addAll(lists);
        if (!listId.contains(IdUtil.getIdString())){
            SharedPreferences spf = getSharedPreferences("isConnect",MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.clear();
            editor.commit();
        }
    }

    @Override
    public void cancleLinkSucceed() {
        if (type == PATIENT) {
            SharedPreferences preferences = getSharedPreferences("isConnect", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            finish();
        } else {

        }
    }
}
