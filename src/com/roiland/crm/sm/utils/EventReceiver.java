package com.roiland.crm.sm.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;


/* add by BonSa 20130317 */
public class EventReceiver extends BroadcastReceiver {
	private static final String tag = Log.getTag(EventReceiver.class);
	private static boolean isNetworkUnavailable = false;

	public EventReceiver() {
		super();
	}
	
	public EventReceiver(Context context) {
		isNetworkUnavailable = !NetworkUtils.isNetworkConnected(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			isNetworkUnavailable = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			Log.i(tag, "========================网络状态变更======isNetworkUnavailable==" + isNetworkUnavailable);
		}
	}

	public static boolean isNetworkUnavailable() {
		return isNetworkUnavailable;
	}

}
