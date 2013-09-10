package com.roiland.crm.sm;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.roiland.crm.sm.GlobalConstant.DeviceModel;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.controller.CRMManagerImpl;
import com.roiland.crm.sm.utils.EventReceiver;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.LogcatHelper;
import com.roiland.crm.sm.utils.PromissionController;


public class RoilandCRMApplication extends Application {
	private static final String tag = Log.getTag(RoilandCRMApplication.class);
	
	private DisplayMetrics metrics;
    private static CRMManager manager;
    public static EventReceiver connectionReceiver = null;
    public IntentFilter intentFilter;
    
    
	@Override
	public void onCreate() {
		super.onCreate();
		init();
		SharedPreferences loginInfo = getSharedPreferences("login_info", 0);
		setIPAdddress(loginInfo);
		networkRegister();
		//启动严重log信息保存机制。
		LogcatHelper.getInstance(getApplicationContext()).start(); 
		//加载权限配置信息
        PromissionController.getInstance().loadPromissionConfig(this);
	}
	
	private void init() {
		manager = new CRMManagerImpl(this);
		
	}
	
	/**
	 * 
	 * <pre>
	 * 获取设定的地址
	 * </pre>
	 *
	 * @param loginInfo 存储登陆信息
	 */
	public void setIPAdddress(SharedPreferences loginInfo) {
		if (loginInfo == null)
			return;
		GlobalConstant.SELECTED_ADDRESS_TYPE = loginInfo.getString("net_kind", "");
		if (GlobalConstant.SELECTED_ADDRESS_TYPE != null && GlobalConstant.SELECTED_ADDRESS_TYPE.equals("in")) {
			GlobalConstant.BASE_ADDRESS = loginInfo.getString("in_ip", "");
		} else {
			GlobalConstant.BASE_ADDRESS = loginInfo.getString("out_ip", "");
		}
	}
	
	public CRMManager getCRMManager() {
		if (manager == null) {
			manager = new CRMManagerImpl(this);
		}
		return manager;
	}
	
	/**
	 * 
	 * <pre>
	 * 注册network广播
	 * </pre>
	 *
	 */
	public void networkRegister() {
		connectionReceiver = new EventReceiver(this.getApplicationContext());
		intentFilter = new IntentFilter(); 
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); 
		registerReceiver(connectionReceiver, intentFilter);
		Log.d(tag, "networkRegister() == 注册network广播");
	}
	
	/**
	 * 
	 * <pre>
	 * 注消network广播
	 * </pre>
	 *
	 */
	public void onDestroy() {
		manager = null;
		metrics = null;
		//注消network广播
		if (connectionReceiver != null) {
			Log.d(tag, "unregisterReceiver == 注消network广播");
			unregisterReceiver(connectionReceiver);
			connectionReceiver = null;
		}
		//停止log保存机制
		LogcatHelper.getInstance(getApplicationContext()).stop(); 
		System.exit(0);
	}
	
	public synchronized DisplayMetrics getDisplayMetrics() {
		if (metrics == null) {
			metrics = new DisplayMetrics();
			WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
	        manager.getDefaultDisplay().getMetrics(metrics);
		}
		return metrics;
	}
	
	public int getWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public int getHeight() {
        return getDisplayMetrics().heightPixels;
    }
    
    public boolean noActionBar(){
    	return Build.VERSION.SDK_INT < DeviceModel.SDK_VERSION_11;
    }
}
