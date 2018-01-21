package com.zxr.medicalaid.mvp.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 张兴锐 on 2017/12/16.
 */

public class HardLinkService extends Service {

    public String sendData;
    private String ip;
    private int port;
    private final int success = 1;
    private final int failed = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case success:
                    listener.onSuccess();
                    break;
                case failed:
                    listener.onError((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }


    private SendResultListener listener;

    public interface SendResultListener {
        void onSuccess();

        void onError(String msg);
    }

    public void setSendResultListener(SendResultListener listener) {
        this.listener = listener;
    }


    //进行发送
    Socket socket;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void send(String msg) {
        Log.i("Service", "send");
        sendData = msg;
        new SendTread().start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    CountDownLatch latch;

    class SendTread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                latch = new CountDownLatch(1);
                Log.i("Service", "连接开始");
                socket = new Socket(ip, port);
                Log.i("Service", "连接成功");
                OutputStream os = socket.getOutputStream();
                new AccpetThread().start();
                latch.await();
                StringBuffer buffer = new StringBuffer();
                buffer.append(sendData);
                Log.i("Service", sendData.toString());
                os.write((buffer.toString()).getBytes("UTF-8"));
                Log.i("Service", "发送完成");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Service", e.getMessage());
                handler.sendMessage(Message.obtain(handler, failed, e.getMessage()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    class AccpetThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                InputStream in = socket.getInputStream();
                DataInputStream input = new DataInputStream(in);
                Log.i("Service", "准备读取数据");
                latch.countDown();
                byte[] b = new byte[100];
                Pattern p = Pattern.compile("UnlockSuccess", Pattern.CASE_INSENSITIVE);
                Matcher matcher;
                while (true) {
                    Log.i("Service", "阻塞式接受");
                    int length = input.read(b);
                    String Msg = new String(b, 0, length,"UTF-8");
                    matcher = p.matcher(Msg);
                    Log.i("Service", "接受到信息：" + Msg);
                    if (matcher.find()) {
                        handler.sendEmptyMessage(success);
                        break;
                    }
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Service", "error");
            }
        }
    }

    public class MyBinder extends Binder {
        public HardLinkService getService() {
            return HardLinkService.this;
        }
    }
}
