package com.zxr.medicalaid.mvp.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lazylibrary.util.ToastUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.zxr.medicalaid.DaoSession;
import com.zxr.medicalaid.MedicalList;
import com.zxr.medicalaid.MedicalListDao;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.mvp.entity.PrescriptionItem;
import com.zxr.medicalaid.mvp.entity.moudle.PrescriptionInfo;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.UpLoadPrescriptionPresenterImpl;
import com.zxr.medicalaid.mvp.ui.activities.base.BaseActivity;
import com.zxr.medicalaid.mvp.ui.adapters.PrescribeTableAdapter;
import com.zxr.medicalaid.mvp.view.UpLoadPrescriptionView;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.others.DialogUtils;
import com.zxr.medicalaid.widget.CircleImageView;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;


public class PrescribeActivity extends BaseActivity implements UpLoadPrescriptionView{

    /**
     * view
     */
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.patient_image)
    CircleImageView mPatientImage;
    @InjectView(R.id.patient_name)
    TextView mName;
    @InjectView(R.id.patient_sex)
    TextView mSex;
    @InjectView(R.id.patient_age)
    TextView mAge;
    @InjectView(R.id.time)
    TextView mRegisterTime;
    @InjectView(R.id.prescribe_table)
    EasyRecyclerView mTable;
    @InjectView(R.id.medicine_name_input)
    EditText mNameInput;
    @InjectView(R.id.medicine_weight_input)
    EditText mWeightInput;
    @Inject
    UpLoadPrescriptionPresenterImpl presenter;
    long linkId ;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECT_FAILED:
                    ToastUtils.showToast(PrescribeActivity.this, "连接失败，请重试");
                    //此处弹窗
                    EditText editText = (EditText) LayoutInflater.from(PrescribeActivity.this).inflate(R.layout.view_reset_ip,null);
                    new AlertDialog.Builder(PrescribeActivity.this)
                            .setTitle("提示")
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            String newIp = editText.getText().toString();
                                            editor.putString("ip",newIp);
                                            editor.commit();
                                            IP_ADD = newIp;
                                        }
                                    }
                            ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    break;
                case NO_THIS_MEDICINE:
//                    ToastUtils.showToast(PrescribeActivity.this, "暂时不支持 " + msg.obj.toString() + " 发送");
                    Toast.makeText(PrescribeActivity.this, "暂时不支持 " + msg.obj.toString() + " 发送", Toast.LENGTH_SHORT);
                    break;
                case CONNECT_SUCCESS:
                    ToastUtils.showToast(PrescribeActivity.this, "连接成功，正在发送");
                    break;
                case SEND_SUCCESS:
                    ToastUtils.showToast(PrescribeActivity.this, "已成功发送");
                    StringBuilder builder = new StringBuilder();
                    for(int i=0;i<listName.size();i++){
                        builder.append(listName.get(i)+"_"+listWeight.get(i)+",");
                    }
//                    long linkId= daoSession.getLinkDao().queryBuilder().where(LinkDao.Properties.Id.eq(
//                            daoSession.getUserDao().queryBuilder().where(UserDao.Properties.IsAlready.eq(1)).unique().getLinkId()
//                    )).unique().getUId();
//                    SharedPreferences sp = getSharedPreferences("linkId",MODE_PRIVATE);
//                    String[] str = sp.getString("linkId","").split(",");
//                    for(String str1 :str){
//                        Log.e(TAG,str1);
//                    }
//                    long linkId = Long.valueOf(str[str.length-1]);
                    Log.e(TAG,linkId+" ++"+builder.toString());
                    presenter.upLoadPrescription(linkId,builder.toString());
                    finish();
                    break;
                case EMPTY_MEDICINE:
                    ToastUtils.showToast(PrescribeActivity.this, "药方现在为空呢");
                    break;
                case REMOVE_ONE_ITEM:
                    adapter.remove(msg.arg1);
                    dbNameList.add(listName.get(msg.arg1));
                    dbWeightList.add(listWeight.get(msg.arg1));
                    listName.remove(msg.arg1);
                    listWeight.remove(msg.arg1);
                    break;
            }
        }
    };


    //写入数据流
    OutputStream os;
    //ip地址和端口(公网,私有地址不行)
    public static  String IP_ADD ;
    public static final int PORT = 5566;
    private final int CONNECT_FAILED = 0;
    private final int NO_THIS_MEDICINE = 1;
    private final int CONNECT_SUCCESS = 2;
    private final int SEND_SUCCESS = 3;
    private final int EMPTY_MEDICINE = 4;
    private final int REMOVE_ONE_ITEM = 5;
    private List<String> dbNameList = new ArrayList<>();
    private List<String> dbWeightList = new ArrayList<>();
    private List<String> listName = new ArrayList<>();
    private List<String> listWeight = new ArrayList<>();
    private String patientName;
    private String phoneNumber;
    DaoSession daoSession= DbUtil.getDaosession();
    MedicalListDao medicalListDao;
    private String patientId;
    private SharedPreferences spf =this.getPreferences(MODE_PRIVATE);
    private SharedPreferences.Editor editor = spf.edit();
    /**
     * 数据
     */
    private PrescribeTableAdapter adapter;

    private Map<String, String> medicineTable = new HashMap<>();

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
        presenter.injectView(this);
    }

    @Override
    public void initViews() {

        //设置toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.prescibe_text);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        patientName = getIntent().getStringExtra("name");
        phoneNumber = getIntent().getStringExtra("number");
        linkId = getIntent().getIntExtra("linkId",-1);
        int randomNum = (int) ((Math.random()*4+1.9)*10);
        mSex.setText(((randomNum/2)==1)?"男":"女");
        mAge.setText(randomNum+"");

        mName.setText(patientName);
        mRegisterTime.setText("电话号码：   " + phoneNumber);
        //设置recyclerView
        mTable.setEmptyView(R.layout.view_empty);
        mTable.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrescribeTableAdapter(this);
        adapter.setOnItemLongClickListener(
                position -> {
                    DialogUtils.showAlert(this, "提示", "确定删除当前条目?", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.remove(position);
                            listName.remove(position);
                            listWeight.remove(position);
                            dialog.dismiss();
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return false;
                }
        );
        mTable.setAdapter(adapter);
        //设置重量输入框的弹起类型
        mWeightInput.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        initData();

        IP_ADD = spf.getString("ip",null);
    }

    private void initData() {
        medicineTable.put("何首乌", "a");
        medicineTable.put("冬虫夏草", "b");
        medicineTable.put("人参", "c");
        medicineTable.put("当归", "d");
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.precribe_confirm, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    Thread sendTread = new SendMedicineThread();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        //add top-left icon click event deal
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.confirm_bt:
                //弹窗
                new AlertDialog.Builder(PrescribeActivity.this)
                        .setTitle("提醒")
                        .setMessage("您确定要进行提交吗?")
                        .setPositiveButton("确定",
                                (dialog, which) -> {
                                    //进行相关逻辑
                                    sendTread.start();
                                    dialog.dismiss();
                                }
                        )
                        .setNegativeButton("取消",
                                (dialog, which) ->
                                        dialog.dismiss()
                        )
                        .show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_prescribe;
    }


    @OnClick(R.id.add)
    public void onViewClicked() {
        /**
         * 注意考虑药名输入加入模糊联想
         */
        String name = mNameInput.getText().toString().trim();
        String weight = mWeightInput.getText().toString().trim();

        if (name.length() == 0 || weight.length() == 0) {
            ToastUtils.showToast(this, "您的输入不完整，请重新输入");
            return;
        }
        if (getDotNumber(weight) >= 2) {
            ToastUtils.showToast(this, "小数点过多");
            return;
        }
        if (weight.charAt(0) == '.') {
            weight = '0' + weight;
        }
        if (weight.charAt(weight.length() - 1) == '.') {
            weight = weight + '0';
        }
        //获取到药名和重量
        listName.add(name);
        listWeight.add(weight);
        adapter.add(new PrescriptionItem(name, weight));
        adapter.notifyDataSetChanged();
        mNameInput.setText("");
        mWeightInput.setText("");
    }

    private int getDotNumber(String str) {
        int x = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                x++;
            }
        }
        return x;
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void upLaodSucceed(PrescriptionInfo info) {
        SharedPreferences sp = getSharedPreferences("linkIdForDoc",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.e(TAG,linkId+"");
        editor.putLong(patientName,linkId);
        editor.commit();
        DaoSession daoSession = DbUtil.getDaosession();
        MedicalListDao listDao = daoSession.getMedicalListDao();
        MedicalList list = new MedicalList();
        list.setName(patientName);
        list.setPatient("patient");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.setDate(dateFormat.format(new Date()));
        listDao.insert(list);
    }


    class SendMedicineThread extends Thread {
        @Override
        public void run() {
            super.run();
            boolean flag = true;
            Socket socket;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(IP_ADD, PORT), 1500);
                os = socket.getOutputStream();
                handler.sendEmptyMessage(CONNECT_SUCCESS);
                StringBuffer buffer = new StringBuffer();
                long sleep_interval = 1000;
                int size = listName.size();
                if (size == 0) {
                    handler.sendEmptyMessage(EMPTY_MEDICINE);
                    return;
                }
                for (int i = size - 1; i >= 0; i--) {
                    String medicine = medicineTable.get(listName.get(i));
                    if (medicine == null) {
                        flag = false;
                        Message msg = new Message();
                        msg.what = NO_THIS_MEDICINE;
                        msg.obj = listName.get(i);
                        handler.sendMessage(msg);
                        continue;
                    }
                    String medicineInfo = medicineTable.get(listName.get(i)) + listWeight.get(i) + "g";
                    buffer.append(medicineInfo);
                    Log.e(TAG, buffer.toString());
                    os.write((buffer.toString()).getBytes("utf-8"));

                    buffer = new StringBuffer();
                    Message msg = new Message();
                    msg.what = REMOVE_ONE_ITEM;
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                    Thread.sleep(sleep_interval);
                }
                if (flag){

                    handler.sendEmptyMessage(SEND_SUCCESS);
                }
            } catch (Exception e) {
                Log.e(TAG, "连接超时");
                e.printStackTrace();
                handler.sendEmptyMessage(CONNECT_FAILED);
                sendTread = new SendMedicineThread();
            }
        }
    }
}
