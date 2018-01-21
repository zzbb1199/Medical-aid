package com.zxr.medicalaid.mvp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.github.lazylibrary.util.ToastUtils;
import com.youth.banner.Banner;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.UserDao;
import com.zxr.medicalaid.common.Constants;
import com.zxr.medicalaid.dagger.scope.ContextLife;
import com.zxr.medicalaid.mvp.ui.activities.CurrentPatientsActivity;
import com.zxr.medicalaid.mvp.ui.activities.QRActivity;
import com.zxr.medicalaid.mvp.ui.fragments.base.BaseFragment;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.image.GildeImageLoader;
import com.zxr.medicalaid.utils.system.ToActivityUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author 猿人
 * @date 2017/4/11
 */

public class SelectFragment extends BaseFragment {
    @InjectView(R.id.prescribe_bt)
    ImageView mPrescribeBt;
    @InjectView(R.id.treat_bt)
    ImageView mTreatBt;
    @InjectView(R.id.banner)
    Banner mBanner;

    @Inject
    @ContextLife("Activity")
    Context mContext;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initViews() {
        //广告轮播图
        mBanner.setImageLoader(new GildeImageLoader());
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.scroll_image_1);
        images.add(R.drawable.scroll_image_2);
        images.add(R.drawable.scroll_image_3);
        images.add(R.drawable.scroll_image_4);
        images.add(R.drawable.scroll_image_5);

        mBanner.setImages(images);
        mBanner.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select;
    }


    @OnClick({R.id.prescribe_bt, R.id.treat_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.prescribe_bt:
                ToActivityUtil.toNextActivity(mContext, CurrentPatientsActivity.class,
                        new String[]{CurrentPatientsActivity.GET_FROM}, new int[]{CurrentPatientsActivity.DOCTOR});
                break;
            case R.id.treat_bt:
                String type = DbUtil.getDaosession().getUserDao().queryBuilder()
                        .where(UserDao.Properties.IsAlready.eq(1)).unique().getType();
                if (type.equals("doctor")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage("您没有权限进行以下操作")
                            .setPositiveButton("确定",
                                    (dialog, what) -> {
                                        dialog.dismiss();
                                    })
                            .setCancelable(true)
                            .show();
                    return;
                } else {
                    Intent intent = new Intent(getContext(), QRActivity.class);
                    new AlertDialog.Builder(getContext())
                            .setTitle("选择类型")
                            .setItems(new String[]{"我要挂号", "我要取药", "查看当前排队情况"}, (dialog, which) -> {
                                if (which == 0) {
                                    intent.putExtra("patient_action", Constants.GUA_HAO);
                                    startActivity(intent);
                                } else if (which == 1) {
                                    intent.putExtra("patient_action", Constants.QU_YAO);
                                    startActivity(intent);
                                } else if (which == 2) {
                                    SharedPreferences preferences = getContext().getSharedPreferences("isConnect", MODE_PRIVATE);
                                    String doctorId = preferences.getString("uId", "");
                                    if (!"".equals(doctorId)) {
                                        //已经挂号了
                                        intent.putExtra("patient_action", Constants.CHA_KAN);
                                        startActivity(intent);
                                    } else {
                                        ToastUtils.showToast(getContext(), "先挂号才能查看哦");
                                        
                                    }
                                }
                            }).show();

                }

                break;

        }
    }


}
