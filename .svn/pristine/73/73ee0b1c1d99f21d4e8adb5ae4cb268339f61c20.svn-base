package com.roiland.crm.sm.utils;

import java.util.concurrent.atomic.AtomicBoolean;

import com.roiland.crm.sm.GlobalConstant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Chunji Li
 *
 */
public class Log {
    @SuppressWarnings("unused")
    private static final String tag = "Log";
    public static final AtomicBoolean DEBUG = new AtomicBoolean(true);

    private Log() {
    }

    public static void initDebugLevel(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean newValue = preferences.getBoolean(GlobalConstant.KEY_DEBUG_ENABLE, false);
        DEBUG.set(newValue);
    }

    public static int v(String tag, String msg) {
    	if (DEBUG.get()) {
    		return android.util.Log.v(tag, "" + msg);
    	}else {
            return 0;
        }
    }

    public static int v(String tag, String msg, Throwable tr) {
    	if (DEBUG.get()) {
    		return android.util.Log.v(tag, "" + msg, tr);
    	}else {
            return 0;
        }
    }

    public static int d(String tag, String msg) {
        if (DEBUG.get()) {
            return android.util.Log.d(tag, "" + msg);
        } else {
            return 0;
        }
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (DEBUG.get()) {
            return android.util.Log.d(tag, "" + msg, tr);
        } else {
            return 0;
        }
    }

    public static int format(String tag, String format, Object... args) {
        if (DEBUG.get()) {
            return android.util.Log.i(tag, String.format(format, args));
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg) {
    	if (DEBUG.get()) {
    		return android.util.Log.i(tag, "" + msg);
    	 } else {
             return 0;
         }
    }

    public static int i(String tag, String msg, Throwable tr) {
    	if (DEBUG.get()) {
    		return android.util.Log.i(tag, "" + msg, tr);
    	}else {
            return 0;
        }
    }

    public static int w(String tag, String msg) {
        return android.util.Log.w(tag, "" + msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return android.util.Log.w(tag, "" + msg, tr);
    }

    public static int e(String tag, String msg) {
        return android.util.Log.e(tag, "" + msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return android.util.Log.e(tag, "" + msg, tr);
    }

    public static String getStackTraceString(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    public static boolean isLoggable(String tag, int level) {
        return android.util.Log.isLoggable(tag, level);
    }

    public static int println(int priority, String tag, String msg) {
        return android.util.Log.println(priority, tag, "" + msg);
    }

    public static String getTag(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
