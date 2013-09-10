package com.roiland.crm.sm.ui.view;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.widget.ProgressDialog;
import com.roiland.crm.sm.utils.Log;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

/**
 * <pre>
 * 异步任务抽象类，后台线程执行任务，UI线程显示进度条，表示正在执行任务.
 * </pre>
 * 
 * @author JKim
 * @param <Result>
 */
public abstract class BaseTask<Result> extends AsyncTask<String, Integer, Result> {
	private String tag = Log.getTag(BaseTask.class);
	
	protected ResponseException responseException = null;
	protected ProgressDialog progress;
	protected String mMessage;
    protected boolean mCancelable = true; //可否取消标识.
    protected boolean mInterrupt = false; //中断标志
    protected Activity activity; //执行该任务的activity
    protected BaseActivity mactivity;
    protected boolean mShowDialog = true; // 显示dialog标志
    protected boolean displayLoadingDown = false;
    
    /**
     * 构造函数
     * @param activity
     */
    public BaseTask(Activity activity) {
    	this.activity = activity;
    }
    
    /**
     * 构造函数
     * @param activity  Activity 活动窗体
     * @param message   显示信息
     * @param cancelable    可否取消任务标识
     */
    public BaseTask(Activity activity, String message, boolean cancelable) {
    	this.activity = activity;
    	this.mMessage = message;
    	this.mCancelable = cancelable;
    }
    
    public BaseTask(Activity activity, String message, boolean cancelable, boolean show) {
    	this.activity = activity;
    	this.mMessage = message;
    	this.mCancelable = cancelable;
    	this.mShowDialog = show;
    }
    
    public BaseTask(Activity activity, String message, boolean cancelable, boolean show, boolean displayLoadingDown) {
    	this.activity = activity;
    	this.mMessage = message;
    	this.mCancelable = cancelable;
    	this.mShowDialog = show;
    	this.displayLoadingDown = displayLoadingDown;
    }
    
    /**
     * 异步任务执行前
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
	protected void onPreExecute() {
		progress = new ProgressDialog(activity, R.style.Theme_Dialog_Alert);
		progress.setMessage(mMessage);
		progress.setIndeterminate(true);
		progress.setCancelable(mCancelable);
		progress.setCanceledOnTouchOutside(false);
		if (mCancelable) {
			progress.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					BaseTask.this.cancel(true);
				}
			});
	    }
		if (mShowDialog)
		{
		    synchronized(progress) {
	            if (displayLoadingDown) {
	            } else {
	                progress.show();
	            }
		    }
		}
	}
    
    
    /**
     * 异步任务执行后
     * @param result
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
	@Override
	protected void onPostExecute(Result result) {
		try {
		    if(this.activity.isFinishing()){
	            progress.dismiss();
	            return;
	        }
			Log.i(tag, "onPostExecute  => " + result);
			synchronized(progress) {
	            if (displayLoadingDown) {
	            } else {
	                if (progress != null && progress.isShowing())
	                    progress.dismiss();
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取消异步任务
	 * 
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override  
	protected void onCancelled() {
		if (progress != null && progress.isShowing())
			progress.dismiss();
		cancel(true);
	}
	public boolean isDisplayLoadingDown() {
		return displayLoadingDown;
	}
	public void setDisplayLoadingDown(boolean displayLoadingDown) {
		this.displayLoadingDown = displayLoadingDown;
	}
}
