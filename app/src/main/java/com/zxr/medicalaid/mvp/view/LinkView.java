package com.zxr.medicalaid.mvp.view;


import com.zxr.medicalaid.mvp.entity.moudle.LinkInfo;
import com.zxr.medicalaid.mvp.view.base.BaseView;

/**
 * Created by ASUS-NB on 2017/7/8.
 */

public interface LinkView extends BaseView {
    void linkSucceed(LinkInfo linkInfo);
}
