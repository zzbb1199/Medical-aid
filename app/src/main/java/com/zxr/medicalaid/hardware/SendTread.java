package com.zxr.medicalaid.hardware;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author 张兴锐
 * @date 2017/12/13
 */

public class SendTread extends Thread {


    public String sendData;
    private SendResultListener listener;
    private String ip;
    private int port;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSendData(String sendData) {
        this.sendData = sendData;
    }

    public interface SendResultListener {
        void onSuccess();

        void onError(String msg);
    }

    public void setSendResultListener(SendResultListener listener) {
        this.listener = listener;
    }

    public SendTread() {
        super();
        this.port = 5566;
    }

    public SendTread(String data, String ip) {
        this.sendData = data;
        this.ip = ip;
        this.port = 5566;
    }


    @Override
    public void run() {
        super.run();
        //进行发送
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1500);
            OutputStream os = socket.getOutputStream();
            StringBuffer buffer = new StringBuffer();
            buffer.append(sendData);
            Log.i("Service", buffer.toString());
            os.write((buffer.toString()).getBytes("utf-8"));
            listener.onSuccess();
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }

    }

}
    