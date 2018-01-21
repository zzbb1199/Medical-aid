package com.zxr.medicalaid.mvp.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxr.medicalaid.R;
import com.zxr.medicalaid.mvp.entity.moudle.DrugInfo;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.DrugInfoPresenterImpl;
import com.zxr.medicalaid.mvp.ui.fragments.base.BaseFragment;
import com.zxr.medicalaid.mvp.view.SearchView;
import com.zxr.medicalaid.widget.LoadDialog;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 猿人 on 2017/4/20.
 */

public class SearchFragment extends BaseFragment implements SearchView{

    @Inject
    DrugInfoPresenterImpl presenter;
    @InjectView(R.id.et_searchMedical)
    EditText etSearchMedical;
    @InjectView(R.id.iv_clear)
    ImageView ivClear;
    @InjectView(R.id.btn_search)
    Button btnSearch;
    @InjectView(R.id.medical_name)
    TextView medicalName;
    @InjectView(R.id.introduction_content)
    TextView introductionContent;
    @InjectView(R.id.growth_habit_content)
    TextView growthHabitContent;
    @InjectView(R.id.medicinal_value_content)
    TextView medicinalValueContent;
    @InjectView(R.id.character_content)
    TextView characterContent;


    //    @InjectView(R.id.input_medicine)
//    EditText mSearch;
//    @InjectView(R.id.search_confirm_bt)
//    ImageView mConfirmBt;
//    @InjectView(R.id.easy_recyclerview)
//    EasyRecyclerView mRecycler;
//
//    private TextWatcher watcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if(s.toString().length() == 0){
//                mConfirmBt.setVisibility(View.INVISIBLE);
//            }else{
//                mConfirmBt.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };
    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
        presenter.injectView(this);
    }

    @Override
    public void initViews() {
//        mSearch.addTextChangedListener(watcher);
//
//        mConfirmBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(presenter==null){
//                    Log.e("TAG","presenter是空的");
//                }else {
//                    presenter.getDrugInfo("中文");
//                }
//            }
//        });


//        WebSettings webSettings = mWebView.getSettings();
//        //支持JS
//        webSettings.setJavaScriptEnabled(true);
//        //支持屏幕缩放
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        //不显示webview缩放按钮
//        webSettings.setDisplayZoomControls(false);
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.loadUrl("http://baike.baidu.com/item/%E5%BD%93%E5%BD%92/760994");
        etSearchMedical.addTextChangedListener(new TextChange());
        btnSearch.setEnabled(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        presenter.getDrugInfo("当归");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
    private String[] medicalNameArray={"人参","何首乌","党参","冬虫夏草","当归","枸杞","甘草","相思子","薄荷","豆蔻","车前子"};
    @OnClick({R.id.iv_clear, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etSearchMedical.setText("");
                break;
            case R.id.btn_search:
                if(etSearchMedical.getText().toString().equals("")){
                    Toast.makeText(getContext(),"内容不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    boolean flag = false;
                    String medicalName = etSearchMedical.getText().toString().trim();
                    for(String name:medicalNameArray){
                        if(medicalName.equals(name)){
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        presenter.getDrugInfo(medicalName);
                        showProgress();
                    }else {
                        Toast.makeText(getContext(),"药材信息暂未收录",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void showProgress() {
        LoadDialog.show(getContext());
    }

    @Override
    public void hideProgress() {
        LoadDialog.dismiss(getContext());
    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showDrugInfo(DrugInfo drugInfo) {
        hideProgress();
        medicalName.setText(drugInfo.getChineseName());
        introductionContent.setText(drugInfo.getIntroduction());
        medicinalValueContent.setText(drugInfo.getMedicinalValue());
        growthHabitContent.setText(drugInfo.getGrowthHabit());
        characterContent.setText(drugInfo.getCharacter());
    }
    private class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {
            boolean feedback = etSearchMedical.getText().length() > 0;
            if (feedback) {
                ivClear.setVisibility(View.VISIBLE);
                btnSearch.setEnabled(true);
            } else {
                ivClear.setVisibility(View.GONE);
                btnSearch.setEnabled(false);
            }
        }
    }
}
