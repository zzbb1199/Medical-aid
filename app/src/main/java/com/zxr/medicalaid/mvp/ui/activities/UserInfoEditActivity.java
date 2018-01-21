package com.zxr.medicalaid.mvp.ui.activities;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.lazylibrary.util.ToastUtils;
import com.zxr.medicalaid.R;
import com.zxr.medicalaid.mvp.presenter.presenterImpl.ChangUserNamePresenterImpl;
import com.zxr.medicalaid.mvp.ui.activities.base.BaseActivity;
import com.zxr.medicalaid.mvp.view.ChangUserNameView;
import com.zxr.medicalaid.net.ResponseCons;
import com.zxr.medicalaid.utils.db.DbUtil;
import com.zxr.medicalaid.utils.db.IdUtil;
import com.zxr.medicalaid.utils.encode.EncodeUtil;
import com.zxr.medicalaid.utils.system.ActivityStack;
import com.zxr.medicalaid.utils.system.ToActivityUtil;
import com.zxr.medicalaid.widget.CircleImageView;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by 猿人 on 2017/7/7.
 */

public class UserInfoEditActivity extends BaseActivity implements ChangUserNameView {
    private static final int REQUEST_CODE_GALLERY = 1;
    @InjectView(R.id.circleImageView)
    CircleImageView mUserImage;
    @InjectView(R.id.nick_name)
    EditText mUserName;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    private boolean hasChanged = false;

    @Inject
    ChangUserNamePresenterImpl mPresenter;

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
        mPresenter.injectView(this);
    }

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle(R.string.setting);
        mToolbar.setTitleTextColor(Color.WHITE);

        //最开始设置姓名编辑光标不现实
        mUserName.setCursorVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_userinfo_edit;
    }

    @OnClick({R.id.head_relative, R.id.name_relative, R.id.log_off, R.id.password_relative,R.id.medical_date_setting_relative, R.id.finish_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_relative:
                mUserName.setCursorVisible(false);
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHandlerResultCallback);
                break;
            case R.id.name_relative:
                mUserName.setCursorVisible(true);
                mUserName.requestFocus();
                break;
            case R.id.password_relative:
                mUserName.setCursorVisible(false);
                ToActivityUtil.toNextActivity(this, PasswordEditActivity.class);
                break;
            case R.id.finish_bt:
                mUserName.setCursorVisible(false);
                //进行头像和姓名修改等上传
                String changName = mUserName.getText().toString();
                String newName = EncodeUtil.doEncrypt(changName, ResponseCons.KEY_NAME);
                if (changName == null || changName.equals("")){
                    ToastUtils.showToast(this,"暂未进行修改呢");
                    return;
                }
                mPresenter.changUserName(IdUtil.getIdString(),newName);
                break;
            case R.id.medical_date_setting_relative:
                ToActivityUtil.toNextActivity(this,MedicalDateSettingActivity.class);
                break;
            case R.id.log_off:
                mUserName.setCursorVisible(false);
                DbUtil.getDaosession().getUserDao().deleteAll();
                DbUtil.getDaosession().getMedicalDateInfoDao().deleteAll();
                DbUtil.getDaosession().getDateDao().deleteAll();
                DbUtil.getDaosession().getMedicalListDao().deleteAll();
                ToActivityUtil.toNextActivity(this, LoginActivity.class);
                ActivityStack.getScreenManager().clearAllActivity();
                SharedPreferences preferences =getSharedPreferences("isConnect",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //同时进行一些数据清除，如数据库的清理
                break;
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            //进行图片上传与置换
            //置换
            hasChanged = true;
            ToastUtils.showToast(UserInfoEditActivity.this, "取得照片");
            String photoPath = resultList.get(0).getPhotoPath();
            mUserImage.setImageBitmap(BitmapFactory.decodeFile(photoPath));
            //上传
            //上传时记得压缩
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(UserInfoEditActivity.this, errorMsg);
        }
    };


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.showToast(this,msg);
    }
}
