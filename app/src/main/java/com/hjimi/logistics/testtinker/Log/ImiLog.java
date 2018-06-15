package com.hjimi.logistics.testtinker.Log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.tinker.lib.util.TinkerLog;


/**
 * Author:      A-mew
 * Date:        Created in 2018/6/14 13:48
 * Modified By:
 * Description:
 */

public class ImiLog implements TinkerLog.TinkerLogImp{
    private static final String TAG = "Tinker.ImiLog";

    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG   = 1;
    public static final int LEVEL_INFO    = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR   = 4;
    public static final int LEVEL_NONE    = 5;
    private static int level = LEVEL_VERBOSE;

    public static int getLevel(){
        return level;
    }

    public static void setLevel(int level){
        ImiLog.level=level;
    }

    @Override
    public void v(String tag, String msg, Object... obj) {
        if (level <= LEVEL_VERBOSE) {
            final String log = obj == null ? msg : String.format(msg, obj);
            Log.v(tag, log);
        }
    }

    @Override
    public void i(String tag, String msg, Object... obj) {
        if (level <= LEVEL_INFO) {
            final String log = obj == null ? msg : String.format(msg, obj);
            Log.i(tag, log);
        }
    }

    @Override
    public void w(String tag, String msg, Object... obj) {
        if (level <= LEVEL_WARNING) {
            final String log = obj == null ? msg : String.format(msg, obj);
            Log.w(tag, log);
        }
    }

    @Override
    public void d(String tag, String msg, Object... obj) {
        if (level <= LEVEL_DEBUG) {
            final String log = obj == null ? msg : String.format(msg, obj);
            Log.d(tag, log);
        }
    }

    @Override
    public void e(String tag, String msg, Object... obj) {
        if (level <= LEVEL_ERROR) {
            final String log = obj == null ? msg : String.format(msg, obj);
            Log.e(tag, log);
        }
    }

    @Override
    public void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {
        String log = obj == null ? format : String.format(format, obj);
        if (log == null) {
            log = "";
        }
        log = log + "  " + Log.getStackTraceString(tr);
        Log.e(tag, log);
    }
}
