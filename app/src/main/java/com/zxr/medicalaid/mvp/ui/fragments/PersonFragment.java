package com.zxr.medicalaid.mvp.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lazylibrary.util.ToastUtils;
import com.zxr.medicalaid.DaoSession;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.User;
import com.zxr.medicalaid.UserDao;

import com.zxr.medicalaid.mvp.ui.activities.InquiryActivity;
import com.zxr.medicalaid.mvp.ui.activities.MachineVerfyActivity;
import com.zxr.medicalaid.mvp.ui.activities.QbShowActivity;
import com.zxr.medicalaid.mvp.ui.activities.UserInfoEditActivity;
import com.zxr.medicalaid.mvp.ui.fragments.base.RxBusFragment;
import com.zxr.medicalaid.net.ResponseCons;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.system.RxBus;
import com.zxr.medicalaid.utils.system.ToActivityUtil;
import com.zxr.medicalaid.widget.CircleImageView;
import com.zxr.medicalaid.widget.MaskableImageView;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 猿人 on 2017/4/20.
 */


public class PersonFragment extends RxBusFragment {


    private static final int REQUEST_CODE_GALLERY = 1;
    @InjectView(R.id.presribe_bt)
    MaskableImageView mPresribeBt;
    @InjectView(R.id.caution_bt)
    MaskableImageView mCautionBt;
    @InjectView(R.id.machine)
    MaskableImageView mAboutUsBt;
    @InjectView(R.id.treat_record_bt)
    MaskableImageView mTreatRecordBt;
    @InjectView(R.id.user_image)
    CircleImageView userImage;
    @InjectView(R.id.user_name)
    TextView userName;
    @InjectView(R.id.user_info_layout)
    ConstraintLayout userInfoLayout;

    DaoSession daoSession = DbUtil.getDaosession();
    UserDao userDao = daoSession.getUserDao();

    @Override
    public void initInjector() {

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

    MessageDigest md;

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
    public void initViews() {
        User user = userDao.queryBuilder().where(UserDao.Properties.IsAlready.eq(1)).unique();
        userName.setText(doEncode(user.getUName(), ResponseCons.KEY_NAME));
        //长按可编辑用户信息
        userInfoLayout.setOnLongClickListener(
                (v -> {
                    //进行弹窗显示
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.remind)
                            .setMessage(R.string.need_to_go_to_user_setting)
                            .setCancelable(true)
                            .setPositiveButton("确定",
                                    (dialog, what) -> {
                                        ToActivityUtil.toNextActivity(getContext(), UserInfoEditActivity.class);
                                        dialog.dismiss();
                                    })
                            .setNegativeButton("取消",
                                    (dialog, what) -> dialog.dismiss()
                            )
                            .show();
                    return false;
                })
        );

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personage;
    }


    @OnClick({R.id.presribe_bt, R.id.machine, R.id.caution_bt, R.id.treat_record_bt, R.id.generate_qb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.presribe_bt:
                User user = userDao.queryBuilder().where(UserDao.Properties.IsAlready.eq(1)).unique();
                if (user.getType().equals("doctor")) {
                    Intent intent2 = new Intent(getContext(), InquiryActivity.class);
                    intent2.putExtra("type", user.getType());
                    startActivity(intent2);
                } else {
                    Toast.makeText(getContext(), "只有医师才有权限哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.machine:
                //判断身份
                User user2 = userDao.queryBuilder().where(UserDao.Properties.IsAlready.eq(1)).unique();
                if (user2.getType().equals("patient")) {
                    ToastUtils.showToast(getContext(), "只有医生才能控制小车呢");
                } else {
                    ToActivityUtil.toNextActivity(getContext(), MachineVerfyActivity.class);
                }
                break;
            case R.id.caution_bt:
                //跳转到系统的闹钟
                Intent alarm = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                startActivity(alarm);
                break;
            case R.id.treat_record_bt:
                User user1 = userDao.queryBuilder().where(UserDao.Properties.IsAlready.eq(1)).unique();
                if (user1.getType().equals("patient")) {
                    Intent inten1 = new Intent(getContext(), InquiryActivity.class);
                    inten1.putExtra("type", user1.getType());
                    startActivity(inten1);
                } else {
                    Toast.makeText(getContext(), "只有患者才有权限哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.generate_qb:
                //可能有bug
                String type = DbUtil.getDaosession().getUserDao().loadAll().get(0).getType();
                if (type.equals("doctor")) {
                    String id = DbUtil.getDaosession().getUserDao().loadAll().get(0).getIdString();
                    Intent intent = new Intent(getContext(), QbShowActivity.class);
                    intent.putExtra("doctorId", id);
                    getContext().startActivity(intent);
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage("只有医生才能生成二维码哦")
                            .setPositiveButton("我知道了",
                                    (dialog, what) -> {
                                        dialog.dismiss();
                                    })
                            .setCancelable(true)
                            .show();
                }
                break;
                default:break;
        }
    }


    @Override
    public void initRxBus() {
        RxBus.getDefault().toObservable(String.class)
                .subscribe(
                        s -> {
                            if (s.equals("修改昵称成功")) {
                                String name = DbUtil.getDaosession().getUserDao().loadAll().get(0).getUName();
                                if (name == null) {
                                    return;
                                }
                                if (userName != null) {
                                    userName.setText(doEncode(name, ResponseCons.KEY_NAME));
                                }

                            }
                            Log.e(TAG, s);
                        }
                );
    }
}


