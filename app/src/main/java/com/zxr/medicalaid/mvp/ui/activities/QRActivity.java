package com.zxr.medicalaid.mvp.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.lazylibrary.util.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zxr.medicalaid.DaoSession;
import com.zxr.medicalaid.MedicalList;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.User;
import com.zxr.medicalaid.UserDao;
import com.zxr.medicalaid.common.Constants;
import com.zxr.medicalaid.mvp.entity.moudle.LinkInfo;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.LinkPresenterImpl;
import com.zxr.medicalaid.mvp.ui.activities.base.PermissionActivity;
import com.zxr.medicalaid.mvp.view.LinkView;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.db.IdUtil;
import com.zxr.medicalaid.utils.others.NotifyDoctorUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class QRActivity extends PermissionActivity implements LinkView {

    @Inject
    LinkPresenterImpl presenter;
    private boolean isFirstTime = true;
    DaoSession daoSession;


    String patientAction = "";

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
        presenter.injectView(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstTime) {
        } else {
            finish();
        }
        isFirstTime = false;

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getPermission();
        patientAction = getIntent().getStringExtra("patient_action");
        super.onCreate(savedInstanceState);
    }

    public void initViews() {
        SharedPreferences preferences = getSharedPreferences("isConnect", MODE_PRIVATE);
        String doctorId = preferences.getString("uId", "");
        if (patientAction.equals(Constants.CHA_KAN)) {
            //病人-已挂号
            Intent intent = new Intent(QRActivity.this, CurrentPatientsActivity.class);
            intent.putExtra("uId", doctorId);
            intent.putExtra(CurrentPatientsActivity.GET_FROM, CurrentPatientsActivity.PATIENT);
            intent.putExtra(CurrentPatientsActivity.GET_FROM, CurrentPatientsActivity.PATIENT);
            startActivity(intent);
            finish();
        } else {
            //病人-未挂号
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, 1);
            //          finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (patientAction.equals(Constants.GUA_HAO)) {
                        String doctorId = result;
                        Log.e(TAG, doctorId);
                        DaoSession daoSession = DbUtil.getDaosession();
                        UserDao userDao = daoSession.getUserDao();
                        List<User> list = userDao.queryBuilder().list();
                        Log.e(TAG, list.size() + "");
                        presenter.linkDP(doctorId, IdUtil.getIdString());
                    } else if (patientAction.equals(Constants.QU_YAO)) {
                        //发送什么样的指令
                        ToastUtils.showToast(this,"取药");
                        NotifyDoctorUtils.notifyDoctor("取药");
                    }

                });
            }
        }


    }


    @Override
    public int getLayout() {
        return R.layout.activity_qr;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void linkSucceed(LinkInfo linkInfo) {
        long linkId = linkInfo.getBody().getId();
        SharedPreferences sp = getSharedPreferences("linkIdForPat", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(linkInfo.getBody().getDoctor().getNickName(), linkId);
        Log.e(TAG, linkId + "");
        editor.apply();
        MedicalList list = new MedicalList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.setDate((dateFormat.format(new Date())));
        list.setName(linkInfo.getBody().getDoctor().getNickName());
        list.setPatient("doctor");
        daoSession.getMedicalListDao().insert(list);

        sp = getSharedPreferences("isConnect", MODE_PRIVATE);
        sp.edit().putString("isConnect", linkInfo.getBody().getDoctor().getIdString());
        Intent intent = new Intent(QRActivity.this, CurrentPatientsActivity.class);
        intent.putExtra("uId", linkInfo.getBody().getDoctor().getIdString());
        startActivity(intent);
    }


    void getPermission() {
        QRActivityPermissionsDispatcher.cameraNeedsWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void cameraNeeds() {
        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QRActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void cameraOnShow(final PermissionRequest request) {
        ToastUtils.showToast(this, "当前操作，需要授予相关权限");
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void cameraDenied() {
        ToastUtils.showToast(this, "您未授予权限，正在退出");
    }


    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void cameraDeniedForever() {
        ToastUtils.showToast(this, "您未授予权限，请手动授予");
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1);

    }
}
