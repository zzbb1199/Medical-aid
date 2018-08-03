package com.zxr.medicalaid.utils.others;

import com.zxr.medicalaid.common.Constants;
import com.zxr.medicalaid.mvp.model.sendtype.AndroidBroadcast;
import com.zxr.medicalaid.mvp.model.sendtype.AndroidNotification;

/**
 * Created by Raven on 2017/12/14.
 */

public class NotifyDoctorUtils {
    private static PushClient client = new PushClient();

    public static void notifyDoctor(String text) {
        AndroidBroadcast broadcast = null;
        try {
            broadcast = new AndroidBroadcast(Constants.APP_KEY, Constants.APP_MASTER_SECRET);
            broadcast.setTicker("Android broadcast ticker");
            broadcast.setTitle("您有一条新消息");
            broadcast.setText(text);
            broadcast.setPlaySound(true);
            broadcast.setPlayVibrate(true);
            broadcast.setPlayLights(true);
            broadcast.goAppAfterOpen();
            broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // TODO Set 'production_mode' to 'false' if it's a test device. 
            // For how to register a test device, please see the developer doc.
            broadcast.setProductionMode();
            // Set customized fields
//            broadcast.setExtraField("test", "helloworld");
            client.send(broadcast);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
