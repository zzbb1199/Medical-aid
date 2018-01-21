package com.zxr.medicalaid.mvp.entity.moudle;

import java.util.List;

/**
 * Created by ASUS-NB on 2017/8/3.
 */

public class PrescriptionInfo {

    /**
     * code : 201
     * message : CREATED#新建或修改资源成功
     * body : {"id":94,"doctor":{"idString":"a8403ef8f59b1d07d0148353d6a736bc5","nickName":"周家豪三号","phoneNumber":"15340504858","type":"doctor"},"patient":{"idString":"ce8c32cb5a5c7830036a1acd437b78af6","nickName":"周家豪二号","phoneNumber":"15340504859","type":"patient"},"status":"linking","time":"2017-08-02 10:47:51.0"}
     */

    private int code;
    private String message;
    /**
     * id : 94
     * doctor : {"idString":"a8403ef8f59b1d07d0148353d6a736bc5","nickName":"周家豪三号","phoneNumber":"15340504858","type":"doctor"}
     * patient : {"idString":"ce8c32cb5a5c7830036a1acd437b78af6","nickName":"周家豪二号","phoneNumber":"15340504859","type":"patient"}
     * status : linking
     * time : 2017-08-02 10:47:51.0
     */

    private BodyBean body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {

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



}
