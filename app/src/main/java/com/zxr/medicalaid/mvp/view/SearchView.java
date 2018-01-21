package com.zxr.medicalaid.mvp.view;

import com.zxr.medicalaid.mvp.entity.moudle.DrugInfo;
import com.zxr.medicalaid.mvp.view.base.BaseView;

/**
 * Created by ASUS-NB on 2017/7/4.
 */

public interface SearchView extends BaseView{
    void showDrugInfo(DrugInfo drugInfo);
}
