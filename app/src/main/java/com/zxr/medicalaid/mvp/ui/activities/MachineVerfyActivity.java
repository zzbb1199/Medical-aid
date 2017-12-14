package com.zxr.medicalaid.mvp.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.github.lazylibrary.util.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.hardware.SendTread;
import com.zxr.medicalaid.mvp.ui.activities.base.BaseActivity;
import com.zxr.medicalaid.utils.system.ToActivityUtil;


/**
 * @author 张兴锐
 */
public class MachineVerfyActivity extends BaseActivity {

    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    @Override
    public void initInjector() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initViews() {

        findViewById(R.id.intent_camera).setOnClickListener(v -> {
            ToActivityUtil.toNextActivity(this, CaptureActivity.class);
        });

        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("认证小车");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        Intent qrReaderIntent = new Intent(this, CaptureActivity.class);
        qrReaderIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(qrReaderIntent, 1);
        spf = this.getPreferences(MODE_PRIVATE);
        editor = spf.edit();


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
                    String ip = spf.getString("ip", "");
                    if ("".equals(ip)) {
                        //此处弹窗
                        resetIPDialog();
                    } else {
                        SendTread sendTread = new SendTread("e", ip);
                        sendTread.setSendResultListener(new SendTread.SendResultListener() {
                            @Override
                            public void onSuccess() {
                                Log.i(TAG, "success");
                                runOnUiThread(() -> {
                                    ToastUtils.showToast(MachineVerfyActivity.this, "绑定成功");
                                    finish();
                                });
                            }

                            @Override
                            public void onError(String msg) {
                                runOnUiThread(() -> {
                                    ToastUtils.showToast(MachineVerfyActivity.this, msg);
                                    resetIPDialog();
                                });
                            }
                        });
                        sendTread.start();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }


        }
    }


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
}
