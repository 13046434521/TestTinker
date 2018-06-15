package com.hjimi.logistics.testtinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.hjimi.logistics.testtinker.Log.ImiLog;
import com.hjimi.logistics.testtinker.util.TinkerManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Author:      A-mew
 * Date:        Created in 2018/6/14 10:28
 * Modified By:
 * Description:
 */
@DefaultLifeCycle(application = ".TestTinkerApplication",flags = ShareConstants.TINKER_ENABLE_ALL
)
public class TestTinkerLike extends DefaultApplicationLike {
    private static TestTinkerLike mTestTinkerLike;
    public TestTinkerLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
        mTestTinkerLike=this;
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        TinkerManager.fastInstallAll(this);
    }

    public static TestTinkerLike getmTestTinkerLike(){
        return mTestTinkerLike;
    }
}
