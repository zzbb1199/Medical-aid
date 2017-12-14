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
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.github.lazylibrary.util.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zxr.medicalaid.DaoSession;
import com.zxr.medicalaid.DateDao;
import com.zxr.medicalaid.MedicalList;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.User;
import com.zxr.medicalaid.UserDao;
import com.zxr.medicalaid.mvp.entity.moudle.LinkInfo;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.LinkPresenterImpl;
import com.zxr.medicalaid.mvp.ui.activities.base.PermissionActivity;
import com.zxr.medicalaid.mvp.view.LinkView;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.db.IdUtil;
import com.zxr.medicalaid.utils.encode.EncodeUtil;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.Observer;

@RuntimePermissions
public class QRActivity extends PermissionActivity implements LinkView {
    public final static int SCANNING_REQUEST_CODE = 1;
    @Inject
    LinkPresenterImpl presenter;
    private boolean isFirstTime = true;
    DaoSession daoSession;
    UserDao userDao;
    DateDao dateDao;

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
        super.onCreate(savedInstanceState);
    }

    public void initViews() {
        daoSession = DbUtil.getDaosession();
        userDao = daoSession.getUserDao();
        User user = userDao.queryBuilder().where(UserDao.Properties.IsAlready.eq(1)).unique();
        String type = user.getType();
        if (type.equals("doctor")) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("您没有权限进行以下操作")
                    .setPositiveButton("确定",
                            (dialog, what) -> {
                                dialog.dismiss();
                                finish();
                            })
                    .setCancelable(true)
                    .show();
        } else {
            SharedPreferences preferences = getSharedPreferences("isConnect", MODE_PRIVATE);
            String doctorId = preferences.getString("uId", "");
            if (!doctorId.equals("")) {
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
    }


    Observable<String> observable;

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
                    String doctorId = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e(TAG, doctorId);
                    DaoSession daoSession = DbUtil.getDaosession();
                    UserDao userDao = daoSession.getUserDao();
                    List<User> list = userDao.queryBuilder().list();
                    Log.e(TAG, list.size() + "");
                    observable = Observable.create(subscriber -> subscriber.onNext(doctorId));
                    presenter.linkDP(doctorId, IdUtil.getIdString());
                });
            }
        }


    }

    MessageDigest md;

    public String doEncrypt(String data, String keyString) {
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
            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, myKey);
            byte[] encode = cipher.doFinal(data.getBytes());
            String encodeString = EncodeUtil.byte2hex(encode);
            return encodeString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        //        Log.e(TAG,"alreadyConnect");
        //        observable.subscribe(observer);
        ////        dateDao = daoSession.getDateDao();
        //        Date date = new Date();
        //        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        //        date.setDate(simpleDateFormat.format(new java.util.Date()));
        ////        dateDao.insert(date);
        //        finish();
    }

    @Override
    public void linkSucceed(LinkInfo linkInfo) {
        observable.subscribe(observer);
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
    }

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            SharedPreferences preferences = getSharedPreferences("isConnect", MODE_PRIVATE);
            Log.e(TAG, "观察者已经收到数据了" + s);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("uId", s);
            editor.commit();
            Intent intent = new Intent(QRActivity.this, CurrentPatientsActivity.class);
            //            ToActivityUtil.toNextActivity(QRActivity.this, CurrentPatientsActivity.class,
            //                    new String[]{CurrentPatientsActivity.GET_FROM,"uId"}, new int[]{CurrentPatientsActivity.PATIENT,s});
            intent.putExtra("uId", s);
            startActivity(intent);
        }
    };


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
