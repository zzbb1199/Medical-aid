/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.zxr.medicalaid.dagger.component;

import android.app.Activity;
import android.content.Context;

import com.zxr.medicalaid.dagger.module.ActivityModule;
import com.zxr.medicalaid.dagger.scope.ContextLife;
import com.zxr.medicalaid.dagger.scope.PerActivity;
import com.zxr.medicalaid.mvp.ui.activities.CurrentPatientsActivity;
import com.zxr.medicalaid.mvp.ui.activities.InquiryActivity;
import com.zxr.medicalaid.mvp.ui.activities.LoginActivity;
import com.zxr.medicalaid.mvp.ui.activities.PasswordEditActivity;
import com.zxr.medicalaid.mvp.ui.activities.PrescribeActivity;
import com.zxr.medicalaid.mvp.ui.activities.PrescribeRecordActivity;
import com.zxr.medicalaid.mvp.ui.activities.QRActivity;
import com.zxr.medicalaid.mvp.ui.activities.QbShowActivity;
import com.zxr.medicalaid.mvp.ui.activities.RegisterActivity;
import com.zxr.medicalaid.mvp.ui.activities.UserInfoEditActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(CurrentPatientsActivity currentPatientsActivity);
    void inject(RegisterActivity currentPatientsActivity);
    void inject(LoginActivity currentPatientsActivity);
    void inject(QRActivity currentPatientsActivity);
    void inject(QbShowActivity qbShowActivity);
    void inject(PasswordEditActivity passwordEditActivity);
    void inject(InquiryActivity passwordEditActivity);
    void inject(PrescribeRecordActivity passwordEditActivity);
    void inject(PrescribeActivity passwordEditActivity);
    void inject(UserInfoEditActivity userInfoEditActivity);

}
