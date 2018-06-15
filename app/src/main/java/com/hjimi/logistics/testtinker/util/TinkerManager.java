/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hjimi.logistics.testtinker.util;

import android.os.Environment;

import com.hjimi.logistics.testtinker.Log.ImiLog;
import com.hjimi.logistics.testtinker.service.ImiResultService;
import com.hjimi.logistics.testtinker.crash.UncaughtExceptionHandler;
import com.tencent.tinker.lib.library.TinkerLoadLibrary;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.app.ApplicationLike;

/**
 * Created by zhangshaowen on 16/7/3.
 */
public class TinkerManager {
    private static final String TAG = "Tinker.TinkerManager";

    private static ApplicationLike applicationLike;
    private static UncaughtExceptionHandler uncaughtExceptionHandler;
    private static boolean isInstalled = false;

    public static void setTinkerApplicationLike(ApplicationLike appLike) {
        applicationLike = appLike;
    }

    public static ApplicationLike getTinkerApplicationLike() {
        return applicationLike;
    }

    public static void initFastCrashProtect() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = new UncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
    }

    public static void setUpgradeRetryEnable(boolean enable) {
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(enable);
    }


    /**
     * all use default class, simply Tinker install method
     */
    public static void sampleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;

    }

    /**
     * you can specify all class you want.
     * sometimes, you can only install tinker in some process you want!
     *
     * @param appLike
     */
    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        //or you can just use DefaultLoadReporter
        LoadReporter loadReporter = new DefaultLoadReporter(appLike.getApplication());
        //or you can just use DefaultPatchReporter
        PatchReporter patchReporter = new DefaultPatchReporter(appLike.getApplication());
        //or you can just use DefaultPatchListener
        PatchListener patchListener = new DefaultPatchListener(appLike.getApplication());
        //you can set your own upgrade patch if you need
        AbstractPatch upgradePatchProcessor = new UpgradePatch();

        TinkerInstaller.install(appLike,
            loadReporter, patchReporter, patchListener,
            ImiResultService.class, upgradePatchProcessor);

        isInstalled = true;
    }

    //installNavitveLibraryABI(Hack)
    public static void installNavitveLibraryABI(ApplicationLike appLike){
        String CPU_ABI = android.os.Build.CPU_ABI;
        // 将tinker library中的 CPU_ABI架构的so 注册到系统的library path中。
        TinkerLoadLibrary.installNavitveLibraryABI(appLike.getApplication(), CPU_ABI);
    }

    public static void fastInstallAll(ApplicationLike appLike){
        if (appLike==null){
            TinkerLog.e(TAG,"Application is null");
        }
        applicationLike=appLike;
        // 设置全局异常捕获
        TinkerManager.initFastCrashProtect();
        //开启升级重试功能（在安装Tinker之前设置）
        TinkerManager.setUpgradeRetryEnable(true);
        //设置Tinker日志输出类
        TinkerInstaller.setLogIml(new ImiLog());

        TinkerManager.installTinker(appLike);

        //install patch
        TinkerInstaller.onReceiveUpgradePatch(appLike.getApplication(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");

        //install so
        TinkerManager.installNavitveLibraryABI(appLike);
    }
}
