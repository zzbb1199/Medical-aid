package com.zxr.medicalaid.mvp.presenter.base;

import android.support.annotation.NonNull;

import com.zxr.medicalaid.mvp.view.base.BaseView;

/**
 * Created by 猿人 on 2017/4/9.
 */

/**
 *
 * @param <T> view
 */
public class BasePresenterImpl<T extends BaseView> implements BasePresenter {

    //声明为protected 供子类使用
    protected T mView;

    /**
     * 注入view
     * @param view
     */
    @Override
    public void injectView(@NonNull BaseView view) {
        mView = (T) view;
    }
}
