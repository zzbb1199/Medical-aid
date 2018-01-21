package com.zxr.medicalaid.mvp.entity.moudle;

import java.util.List;

/**
 * Created by ASUS-NB on 2017/8/2.
 */

public class Prescription {
    private List<DrugDose> drugs;
    private long link_id;

    public List<DrugDose> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<DrugDose> drugs) {
        this.drugs = drugs;
    }

    public long getLink_id() {
        return link_id;
    }

    public void setLink_id(long link_id) {
        this.link_id = link_id;
    }
}
