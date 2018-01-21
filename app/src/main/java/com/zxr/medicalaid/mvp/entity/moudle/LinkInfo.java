package com.zxr.medicalaid.mvp.entity.moudle;

/**
 * Created by ASUS-NB on 2017/7/8.
 */

public class LinkInfo {

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
        private int id;
        /**
         * idString : a8403ef8f59b1d07d0148353d6a736bc5
         * nickName : 周家豪三号
         * phoneNumber : 15340504858
         * type : doctor
         */

        private DoctorBean doctor;
        /**
         * idString : ce8c32cb5a5c7830036a1acd437b78af6
         * nickName : 周家豪二号
         * phoneNumber : 15340504859
         * type : patient
         */

        private PatientBean patient;
        private String status;
        private String time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public DoctorBean getDoctor() {
            return doctor;
        }

        public void setDoctor(DoctorBean doctor) {
            this.doctor = doctor;
        }

        public PatientBean getPatient() {
            return patient;
        }

        public void setPatient(PatientBean patient) {
            this.patient = patient;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public static class DoctorBean {
            private String idString;
            private String nickName;
            private String phoneNumber;
            private String type;

            public String getIdString() {
                return idString;
            }

            public void setIdString(String idString) {
                this.idString = idString;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class PatientBean {
            private String idString;
            private String nickName;
            private String phoneNumber;
            private String type;

            public String getIdString() {
                return idString;
            }

            public void setIdString(String idString) {
                this.idString = idString;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
