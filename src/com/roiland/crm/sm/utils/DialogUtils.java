package com.roiland.crm.sm.utils;


import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.view.BaseTask;
import com.roiland.crm.sm.ui.view.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/**
 * Code sample:
 * 登录失败提示
 * DialogUtils.alert(activity, "登录失败", “用户名或密码输入错误！�? null);
 * 
 * 
 * @author cjyy
 *
 */
public class DialogUtils {
    private static final String tag = Log.getTag(DialogUtils.class);
    
    private static AlertDialog alertDialog = null;
    private String flag = null;

    private DialogUtils() {
    }
    
    /**
     * 对话框是否已显示
     * @return
     */
    public static boolean isDialogShow(){
    	return null!=alertDialog?alertDialog.isShowing():false;
    }
    
    /**
     * 提示信息对话�?
     * @param activity	Activity
     * @param title		标题
     * @param message	信息
     * @param runnable	确认后的响应任务
     */
    public static void info(Activity activity, String title, String message, Runnable runnable) {
        String theTitle;
        if (TextUtils.isEmpty(title)) {
            theTitle = activity.getString(R.string.info);
        } else {
            theTitle = title;
        }
        showDialog(activity, android.R.drawable.ic_dialog_info, theTitle, message, runnable);
    }
    
    /**
     * 错误信息对话框
     * @param activity
     * @param title
     * @param message
     * @param runnable
     */
    public static void error(Activity activity, String title, String message, Runnable runnable) {
        String theTitle;
        if (TextUtils.isEmpty(title)) {
            theTitle = activity.getString(R.string.error);
        } else {
            theTitle = title;
        }
        showDialog(activity, android.R.drawable.ic_dialog_alert, theTitle, message, runnable);
    }

    /**
     * 警示信息对话框
     * @param activity
     * @param title
     * @param message
     * @param runnable
     * @return
     */
    public static AlertDialog warn(Activity activity, String title, String message, Runnable runnable) {
        String theTitle;
        if (TextUtils.isEmpty(title)) {
            theTitle = activity.getString(R.string.warn);
        } else {
            theTitle = title;
        }
        return showDialog(activity, android.R.drawable.ic_dialog_alert, theTitle, message, runnable);
    }
    
    
    public static AlertDialog warnExt(Activity activity, String title, String message, Runnable runnable) {
        String theTitle;
        if (TextUtils.isEmpty(title)) {
            theTitle = activity.getString(R.string.warn);
        } else {
            theTitle = title;
        }
        return showDialogExt(activity, android.R.drawable.ic_dialog_alert, theTitle, message, runnable);
    }
    
    public static AlertDialog alert(Activity activity, String title, String message, Runnable runnable) {
        String theTitle;
        if (TextUtils.isEmpty(title)) {
            theTitle = activity.getString(android.R.string.dialog_alert_title);
        } else {
            theTitle = title;
        }
        return showDialog(activity, android.R.drawable.ic_dialog_alert, theTitle, message, runnable);
    }
    
    public static AlertDialog showDialog(final Activity activity, Integer iconResId, String title, String message, final Runnable runnable) {
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (runnable != null) {
                    activity.runOnUiThread(runnable);
//                    ThreadPool.excuteShortTask(runnable);
                }
            }
        };
        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (Log.DEBUG.get()) {
                    Log.d(tag, "showDialog Dismiss!");
                }
                if(dialog!= null) {
                	dialog.dismiss();
                }
            }
        };

        removeDialog(activity);
        Builder builder = new Builder(activity);
        if (iconResId != null) {
            builder.setIcon(iconResId.intValue());
        } else {
            builder.setIcon(android.R.drawable.ic_dialog_info);
        }

        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }
        builder.setPositiveButton(R.string.ok, clickListener);

        alertDialog = builder.create();
        alertDialog.setOnDismissListener(dismissListener);

        alertDialog.show();
        return alertDialog;
    }
    public static AlertDialog showDialogExt(final Activity activity, Integer iconResId, String title, String message, final Runnable runnable) {
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (runnable != null) {
                    activity.runOnUiThread(runnable);
//                    ThreadPool.excuteShortTask(runnable);
                }
            }
        };
        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (Log.DEBUG.get()) {
                    Log.d(tag, "showDialogExt Dismiss!");
                }
                if(dialog!= null) {
                	dialog.dismiss();
                }
            }
        };
        removeDialog(activity);
        Builder builder = new Builder(activity);
        if (iconResId != null) {
            builder.setIcon(iconResId.intValue());
        } else {
            builder.setIcon(android.R.drawable.ic_dialog_info);
        }

        if (title != null) {
            builder.setTitle(title);
        }
//        if (message != null) {
//            builder.setMessage(message);
//        }
        builder.setPositiveButton(R.string.ok, clickListener);

        alertDialog = builder.create();
        TextView tv = new TextView(activity);
        tv.setSingleLine(false);
	   	tv.setText(message);
	   	tv.setTextSize(16);
	   	tv.setPadding(16, 9, 16, 9);
	   	tv.setTextColor(Color.WHITE);
	   	tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	   	alertDialog.setView(tv);
        alertDialog.setOnDismissListener(dismissListener);

        alertDialog.show();
        return alertDialog;
    }
    
    /**
     * 确认信息对话�?
     * @param activity
     * @param title
     * @param message
     * @param yesTask
     * @param noTask
     */
    public static void confirm(Activity activity, String title, String message, final Runnable yesTask, final Runnable noTask) {
        confirm(activity, null, title, message, yesTask, noTask, -1, -1);
    }
    
    //add 2 params for custom button text
    public static void confirm(final Activity activity, Integer iconResId, String title, String message, final Runnable yesTask, final Runnable noTask,final int but_yes_res,final int but_no_res) {
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if (Log.DEBUG.get()) {
                    Log.d(tag, "confirm ok!");
                }
                if (DialogInterface.BUTTON_POSITIVE == which) {
                    if (yesTask != null) {
                    	BaseTask<String> task = new BaseTask<String>(activity) {

							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								yesTask.run();
								return null;
							}
						};
						task.execute();
                    }
                } else {
                    if (noTask != null) {
                    	BaseTask<String> task = new BaseTask<String>(activity) {

							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								noTask.run();
								return null;
							}
						};
						task.execute();
                    }
                }
            }
        };
        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (Log.DEBUG.get()) {
                    Log.d(tag, "confirm Dismiss!");
                }
                if(dialog!= null) {
                	dialog.dismiss();
                }
            }
        };

        removeDialog(activity);
        
        Builder builder = new Builder(activity);
        if (iconResId != null) {
            builder.setIcon(iconResId.intValue());
        } else {
            builder.setIcon(android.R.drawable.ic_dialog_info);
        }

        if (title != null) {
            builder.setTitle(title);
        }
		
        if (message != null) {
            builder.setMessage(message);
        }
		
        if (but_yes_res > 0) {
        	builder.setPositiveButton(but_yes_res, clickListener);
        } else {
        	builder.setPositiveButton(R.string.yes, clickListener);
        }
        
		if (but_no_res > 0) {
			builder.setNegativeButton(but_no_res, clickListener);
		} else {
			builder.setNegativeButton(R.string.no, clickListener);
		}
        
        alertDialog = builder.create();
        alertDialog.setOnDismissListener(dismissListener);
        alertDialog.show();
    }
    public static void hasDriveTestConfirm(final Activity activity, Integer iconResId, String title, String message, final Runnable yesTask, final Runnable noTask,final int but_yes_res,final int but_no_res) {
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Log.DEBUG.get()) {
                    Log.d(tag, "confirm ok!");
                }
                if (DialogInterface.BUTTON_POSITIVE == which) {
                    if (yesTask != null) {
                        BaseTask<String> task = new BaseTask<String>(activity) {

                            @Override
                            protected String doInBackground(String... params) {
                                // TODO Auto-generated method stub
                                activity.runOnUiThread(yesTask);
                                return null;
                            }
                        };
                        task.execute();
                    }
                } else {
                    if (noTask != null) {
                        BaseTask<String> task = new BaseTask<String>(activity) {

                            @Override
                            protected String doInBackground(String... params) {
                                // TODO Auto-generated method stub
                                activity.runOnUiThread(noTask);
                                return null;
                            }
                        };
                        task.execute();
                    }
                }
            }
        };
        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (Log.DEBUG.get()) {
                    Log.d(tag, "confirm Dismiss!");
                }
                if(dialog!= null) {
                    dialog.dismiss();
                }
            }
        };

        removeDialog(activity);
        
        Builder builder = new Builder(activity);
        if (iconResId != null) {
            builder.setIcon(iconResId.intValue());
        } else {
            builder.setIcon(android.R.drawable.ic_dialog_info);
        }

        if (title != null) {
            builder.setTitle(title);
        }
        
        if (message != null) {
            builder.setMessage(message);
        }
        
        if (but_yes_res > 0) {
            builder.setPositiveButton(but_yes_res, clickListener);
        } else {
            builder.setPositiveButton(R.string.yes, clickListener);
        }
        
        if (but_no_res > 0) {
            builder.setNegativeButton(but_no_res, clickListener);
        } else {
            builder.setNegativeButton(R.string.no, clickListener);
        }
        
        alertDialog = builder.create();
        alertDialog.setOnDismissListener(dismissListener);
        alertDialog.show();
    }
    public static Dialog makeConfirmDialog(Activity activity, String message,
            DialogInterface.OnClickListener okListener,
            DialogInterface.OnClickListener cancelListener) {
    	removeDialog(activity);
        return new android.app.AlertDialog.Builder(activity)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(message)
            .setPositiveButton(R.string.ok, okListener)
            .setNegativeButton(R.string.cancel, cancelListener)
            .create();
    }

    public static Dialog makeContentMenuDialog(Activity activity, String title, String[] msgs, OnClickListener listener) {
    	removeDialog(activity);
    	alertDialog = new android.app.AlertDialog.Builder(activity) .setIcon(android.R.drawable.ic_dialog_info)
        .setTitle(title).setItems(msgs, listener)
        .create();

    	return alertDialog;
    }

    public static Dialog makeExceptionsDialog(final Activity activity, String title, String[] msgs, final Runnable runnable) {
    	removeDialog(activity);
    	String theTitle;
        if (TextUtils.isEmpty(title)) {
            theTitle = activity.getString(android.R.string.dialog_alert_title);
        } else {
            theTitle = title;
        }

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (runnable != null) {
                    activity.runOnUiThread(runnable);
                }
            }
        };
        OnDismissListener dismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (Log.DEBUG.get()) {
                    Log.d(tag, "makeExceptionsDialog Dismiss!");
                }
                if(dialog!= null) {
                	dialog.dismiss();
                }
            }
        };

        Builder builder = new android.app.AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert)
    	.setTitle(theTitle).setPositiveButton(R.string.ok, clickListener);
        if (msgs.length == 1) {
        	builder.setMessage(msgs[0]);
        } else {
        	builder.setItems(msgs, null);
        }
        
    	alertDialog = builder.create();
    	alertDialog.setOnDismissListener(dismissListener);
    	return alertDialog;
    }

    public static Dialog makeConfirmDialogExt(Activity activity, String message,
            DialogInterface.OnClickListener okListener,
            DialogInterface.OnClickListener cancelListener) {
    		removeDialog(activity);
    		alertDialog = new android.app.AlertDialog.Builder(activity)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(android.R.string.dialog_alert_title)
            //.setMessage(message)
            .setPositiveButton(R.string.ok, okListener)
            .setNegativeButton(R.string.cancel, cancelListener)
            .create();
	    	 TextView tv = new TextView(activity);
	    	 tv.setText(message);
	    	 tv.setTextSize(16);
	    	 tv.setPadding(16, 9, 16, 9);
	    	 tv.setTextColor(Color.WHITE);
	    	 tv.setSingleLine(false);
	    	 tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

	    	 alertDialog.setView(tv);

    	return alertDialog;
    }

    public static void removeDialog(final Activity activity) {
    	if (alertDialog != null && alertDialog.getContext().equals(activity)) {
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			alertDialog = null;
    	}
    }
    
    // Just use for notification, it's dangerous to clear all, please use the method above.
    public static void clearDialogs() {
    	if (alertDialog != null) {
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			alertDialog = null;
    	}
    }
    public static Builder WarningExit(final Activity activity, String strTitle,String message) {
    	 AlertDialog.Builder dialog = new Builder(activity);
    	 dialog.setMessage(message);
    	 dialog.setTitle(strTitle);
    	 dialog.setPositiveButton("确认", new OnClickListener() {
    	 
    	   @Override
    	   public void onClick(DialogInterface dialog, int which) {
    	    dialog.dismiss();
    	    ((RoilandCRMApplication) activity.getApplication()).onDestroy();
    	    activity.finish();
    	   }
    	  });
    	 dialog.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
    	return dialog;
    }
    public static AlertDialog.Builder selectedDialog(Activity activity){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        return dialog;
    }
    /**
     * 
     * <pre>
     * 当时间超过30分钟 自动跳转到登陆页
     * </pre>
     *
     * @param activity 页面activity 
     * @param responseException 异常
     */
    public static void overMinuteDo(final FragmentActivity activity, ResponseException responseException ){
        alert(activity, null, responseException.getMessage(), new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    
    }
}