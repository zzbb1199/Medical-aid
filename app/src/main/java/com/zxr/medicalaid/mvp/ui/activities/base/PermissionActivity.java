package com.zxr.medicalaid.mvp.ui.activities.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zxr.medicalaid.App;
import com.zxr.medicalaid.dagger.component.ActivityComponent;
import com.zxr.medicalaid.dagger.component.DaggerActivityComponent;
import com.zxr.medicalaid.dagger.module.ActivityModule;
import com.zxr.medicalaid.utils.system.ActivityStack;

import butterknife.ButterKnife;

/**
 * Created by Raven on 2017/8/7.
 */

public abstract class PermissionActivity extends Activity {
    protected  String TAG;
    protected ActivityComponent mActivityComponent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();

        ActivityStack.getScreenManager().pushActivity(this);
        //初始化组件 注入器
        initActivityComponent();

        setContentView(getLayout());
        ButterKnife.inject(this);

        initInjector();
    }
    protected abstract int getLayout();
    
    protected abstract void initInjector();

    protected  void initActivityComponent(){
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getmApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }
}
