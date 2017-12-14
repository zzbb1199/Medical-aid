package com.zxr.medicalaid.mvp.ui.fragments;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.youth.banner.Banner;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.dagger.scope.ContextLife;
import com.zxr.medicalaid.mvp.ui.activities.CurrentPatientsActivity;
import com.zxr.medicalaid.mvp.ui.activities.QRActivity;
import com.zxr.medicalaid.mvp.ui.fragments.base.BaseFragment;
import com.zxr.medicalaid.utils.image.GildeImageLoader;
import com.zxr.medicalaid.utils.system.ToActivityUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 *
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
                ToActivityUtil.toNextActivity(mContext, QRActivity.class);
                break;
        
        }
    }

  
}
