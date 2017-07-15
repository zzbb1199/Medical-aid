package com.zxr.medicalaid.net;


import com.zxr.medicalaid.mvp.entity.Data;
import com.zxr.medicalaid.mvp.entity.moudle.CancleInfo;
import com.zxr.medicalaid.mvp.entity.moudle.DrugInfo;
import com.zxr.medicalaid.mvp.entity.moudle.LinkInfo;
import com.zxr.medicalaid.mvp.entity.moudle.PatientInfo;
import com.zxr.medicalaid.mvp.entity.moudle.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


/**
 * Created by 猿人 on 2017/5/24.
 */

public interface Api {

    @POST("drug/info")
    @FormUrlEncoded
    Observable<Data<DrugInfo>> getDrugInfo(@Field("chineseName")String drugName);

    @POST("user/signUp")
    @FormUrlEncoded
    Observable<Data<String>> signIn(@Field("nickName")String nickName,@Field("password")String password,@Field("phoneNumber")String phoneNumber,
                                    @Field("type")String type);
    @POST("user/signIn")
    @FormUrlEncoded
    Observable<Data<UserInfo>> logIn(@Field("phoneNumber")String phoneNumber, @Field("password")String password);

    @POST("link")
    @FormUrlEncoded
    Observable<LinkInfo> linkDP(@Field("doctorId")String doctorId,@Field("patientId")String patientId);

    //医生获取二维码
    @POST("link/QR.png")
    @FormUrlEncoded
    Observable<ResponseBody> getQBImg(@Field("idString") String idString);


    //医生获取连接的患者‘
    @POST("link/list")
    @FormUrlEncoded
    Observable<PatientInfo> getPatient(@Field("doctorId")String doctorId,@Field("currentPage") int currentPage);

    @POST("link/cancle")
    @FormUrlEncoded
    Observable<CancleInfo> cancleLink(@Field("doctorId")String doctorId,@Field("patientId")String patientId);

    //修改密码
    @POST("user/updatePassword")
    @FormUrlEncoded
    Observable<Data<String>> changPassword(@Field("idString") String idString,@Field("oldPassword") String oldPassword,
                                           @Field("newPassword") String newPassword);

}
