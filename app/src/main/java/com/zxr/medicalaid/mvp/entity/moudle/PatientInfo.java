package com.zxr.medicalaid.mvp.entity.moudle;

import java.util.List;

/**
 * Created by ASUS-NB on 2017/7/13.
 */

public class PatientInfo {

    /**
     * code : 200
     * message : OK#成功返回
     * body : {"page":{"totalNumber":1,"currentPage":1,"totalPage":1,"pageNumber":20,"dbIndex":0,"dbNumber":20},"list":[{"id":98,"doctor":null,"patient":{"idString":"ce8c32cb5a5c7830036a1acd437b78af6","nickName":"周家豪二号","phoneNumber":"15340504859","type":"patient"},"status":"linking","time":"2017-08-06 11:55:30.0"}]}
     */

    private int code;
    private String message;
    /**
     * page : {"totalNumber":1,"currentPage":1,"totalPage":1,"pageNumber":20,"dbIndex":0,"dbNumber":20}
     * list : [{"id":98,"doctor":null,"patient":{"idString":"ce8c32cb5a5c7830036a1acd437b78af6","nickName":"周家豪二号","phoneNumber":"15340504859","type":"patient"},"status":"linking","time":"2017-08-06 11:55:30.0"}]
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
        /**
         * totalNumber : 1
         * currentPage : 1
         * totalPage : 1
         * pageNumber : 20
         * dbIndex : 0
         * dbNumber : 20
         */

        private PageBean page;
        /**
         * id : 98
         * doctor : null
         * patient : {"idString":"ce8c32cb5a5c7830036a1acd437b78af6","nickName":"周家豪二号","phoneNumber":"15340504859","type":"patient"}
         * status : linking
         * time : 2017-08-06 11:55:30.0
         */

        private List<ListBean> list;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class PageBean {
            private int totalNumber;
            private int currentPage;
            private int totalPage;
            private int pageNumber;
            private int dbIndex;
            private int dbNumber;

            public int getTotalNumber() {
                return totalNumber;
            }

            public void setTotalNumber(int totalNumber) {
                this.totalNumber = totalNumber;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getTotalPage() {
                return totalPage;
            }

            public void setTotalPage(int totalPage) {
                this.totalPage = totalPage;
            }

            public int getPageNumber() {
                return pageNumber;
            }

            public void setPageNumber(int pageNumber) {
                this.pageNumber = pageNumber;
            }

            public int getDbIndex() {
                return dbIndex;
            }

            public void setDbIndex(int dbIndex) {
                this.dbIndex = dbIndex;
            }

            public int getDbNumber() {
                return dbNumber;
            }

            public void setDbNumber(int dbNumber) {
                this.dbNumber = dbNumber;
            }
        }

        public static class ListBean {
            private int id;
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
        }
    }
}
