package com.zxr.medicalaid.mvp.ui.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.lazylibrary.util.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.mvp.ui.activities.base.BaseActivity;
import com.zxr.medicalaid.mvp.ui.service.HardLinkService;
import com.zxr.medicalaid.utils.system.ToActivityUtil;


/**
 * @author Raven
 */
public class MachineVerfyActivity extends BaseActivity {

    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    String ip;
    Intent service;
    HardLinkService sendService;

    @Override
    public void initInjector() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initViews() {

        findViewById(R.id.intent_camera).setOnClickListener(v -> {
                    Intent qrReaderIntent = new Intent(this, CaptureActivity.class);
                    qrReaderIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(qrReaderIntent, 1);
                }
        );
        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("认证小车");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        spf = this.getPreferences(MODE_PRIVATE);
        editor = spf.edit();
        ip = spf.getString("ip", "");

        //启动Service
        service = new Intent(this, HardLinkService.class);
        startService(service);
        this.bindService(service, connection, Context.BIND_AUTO_CREATE);

        Intent qrReaderIntent = new Intent(this, CaptureActivity.class);
        qrReaderIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(qrReaderIntent, 1);
    }

    boolean isBind;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "绑定成功");
            isBind = true;
            sendService = ((HardLinkService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            Log.i(TAG, "绑定失败");
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_about_us;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.i(TAG, result);
                    if ("".equals(ip)) {
                        Log.i(TAG, "重新IP");
                        //此处弹窗
                        resetIPDialog();
                    } else {
                        sendService.setIp(ip);
                        sendService.setSendResultListener(listener);
                        sendService.send("e");
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private HardLinkService.SendResultListener listener = new HardLinkService.SendResultListener() {
        @Override
        public void onSuccess() {
            Log.i(TAG, "发送成功");
        }

        @Override
        public void onError(String msg) {
            Log.i(TAG, "msg");
        }
    };


    boolean isOpen = false;

    private void resetIPDialog() {
        isOpen = true;
        EditText editText = (EditText) LayoutInflater.from(this).inflate(R.layout.view_reset_ip, null);
        new AlertDialog.Builder(this)
                .setTitle("提示,当前硬件IP已更改，请重新输入")
                .setView(editText)
                .setPositiveButton("确定", (dialog, which) -> {
                            dialog.dismiss();
                            String newIp = editText.getText().toString();
                            if (!"".equals(newIp)) {
                                editor.putString("ip", newIp);
                                editor.commit();
                                ip = newIp;
                                ToActivityUtil.toNextActivity(this, CaptureActivity.class);
                            } else {
                                ToastUtils.showToast(this, "输入为空，无法连接");
                            }

                        }
                ).show();
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            isOpen = false;
            ToastUtils.showToast(this, "没填写IP哦，您也可以在按一次退出");
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(connection);
        }
        this.stopService(service);
    }
}
