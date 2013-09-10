package com.roiland.crm.sm.ui.view;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.FileUtil;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.PromissionController;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 登录页面
 * 
 * @author JKim
 */
public class LoginActivity extends Activity{
    private final static String   tag             = Log.getTag(LoginActivity.class); //log标志
    View.OnKeyListener            pwdEnterKey;
    View.OnKeyListener            ipEnterKey;
    View.OnKeyListener            outipEnterKey;
    private boolean               set_btn_checked = false;
    private boolean               set_in_lan      = true;
    //以下变量或常量用于版本更新
    private RoilandCRMApplication myApplication;
    private static final int      TIMEOUT         = 10 * 1000;                      // 超时
    private static String         down_url        = "";                              //下载地址
    private static final int      DOWN_OK         = 1;                               //下载成功
    private static final int      DOWN_ERROR      = 0;                               //下载失败
    private String                app_name;                                          //程序名
    private RemoteViews           contentView;
    private NotificationManager   notificationManager;
    private Notification          notification;                                      //通知
    private Intent                updateIntent;
    private PendingIntent         pendingIntent;
    private int                   notification_id = 0;
    public static int             localVersion    = 0;                           // 本地安装版本
    public static int             serverVersion   = 0;                           // 服务器版本
    public static String          downloadDir     = "CRM/";                         // 安装目录
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		//设置默认的登录画面
		unadvanced_login();
		
		//登录按钮
		((ImageButton)this.findViewById(R.id.login_login_button)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = ((TextView)findViewById(R.id.login_username_text)).getText().toString();
				String password = ((TextView)findViewById(R.id.login_password_text)).getText().toString();
		        LoginTask task = new LoginTask(LoginActivity.this);
		        task.execute(username, password);
				return;
			}
		});
		
		//设置按钮
		((ImageButton)this.findViewById(R.id.login_login_set_button)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				set_btn_checked = !set_btn_checked;
				if (set_btn_checked)
					advanced_login();
				else
					unadvanced_login();
				return;
			}
		});
		
		//内网IP地址单选按钮
		this.findViewById(R.id.login_radio_inlan).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findViewById(R.id.login_radio_outlan).setSelected(false);
				findViewById(R.id.login_radio_inlan).setSelected(true);
				set_in_lan = true;
			}
		});
		
		//外网IP地址单选按钮
		this.findViewById(R.id.login_radio_outlan).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findViewById(R.id.login_radio_inlan).setSelected(false);
				findViewById(R.id.login_radio_outlan).setSelected(true);
				set_in_lan = false;
			}
		});
		
		//密码输入
		if (pwdEnterKey == null)
		{
			pwdEnterKey = new View.OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getAction() != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER)
						return false;
					
					if (set_btn_checked)
						return false;
					
					submitLoginInfo();
					
					return true;
				}
			};
		}
		
		//内网IP输入事件
		if (ipEnterKey == null)
		{
			ipEnterKey = new View.OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getAction() != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER)
						return false;
					
					if (!set_in_lan)
						return false;
					
					submitLoginInfo();
					
					return true;
				}
			};
		}
		
		//外网IP输入事件
		if (outipEnterKey == null)
		{
			outipEnterKey = new View.OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getAction() != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER)
						return false;
					submitLoginInfo();
					
					return true;
				}
			};
		}
		this.findViewById(R.id.login_password_text).setOnKeyListener(pwdEnterKey);
		this.findViewById(R.id.login_ip_text).setOnKeyListener(ipEnterKey);
		this.findViewById(R.id.login_out_ip_text).setOnKeyListener(outipEnterKey);

		resetLoginInfo();
	}

	/**
	 * 提交输入的用户名、密码，并进行登录任务.
	 */
	private void submitLoginInfo()
	{
		String username = ((TextView)findViewById(R.id.login_username_text)).getText().toString();
		String password = ((TextView)findViewById(R.id.login_password_text)).getText().toString();
		
		LoginTask task = new LoginTask(LoginActivity.this);
		task.execute(username, password);
	}
	
	/**
	 * 输入校验并保存内、外网地IP地址.
	 */
	private boolean saveLoginInfo()
	{
		String in_ip = ((TextView)findViewById(R.id.login_ip_text)).getText().toString();
		String out_ip = ((TextView)findViewById(R.id.login_out_ip_text)).getText().toString();
		String net_kind = findViewById(R.id.login_radio_inlan).isSelected() ? "in" : "out";
		
		if (net_kind.equals("in") && StringUtils.isEmpty(in_ip)) {
			DialogUtils.alert(this, getString(R.string.warn), getString(R.string.login_ip_name_hint), null);
			((TextView)findViewById(R.id.login_ip_text)).requestFocus();
			return false;
		}
		if(net_kind.equals("in") && !StringUtils.isEmpty(DataVerify.checkIPAddress(in_ip))){
		    Toast msg = Toast.makeText(getApplicationContext(), DataVerify.checkIPAddress(in_ip), Toast.LENGTH_SHORT);
		    msg.show();
		    return false;
		}
		if (net_kind.equals("out") && StringUtils.isEmpty(out_ip)) {
			DialogUtils.alert(this, getString(R.string.warn), getString(R.string.login_outip_name_hint), null);
			((TextView)findViewById(R.id.login_out_ip_text)).requestFocus();
			return false;
		}
	    if(net_kind.equals("out") && !StringUtils.isEmpty(DataVerify.checkIPAddress(out_ip))){
             Toast msg = Toast.makeText(getApplicationContext(), DataVerify.checkIPAddress(out_ip), Toast.LENGTH_SHORT);
             msg.show();
	         return false;
	    }

		SharedPreferences loginInfo = getSharedPreferences("login_info", 0);
		loginInfo.edit().putString("in_ip", in_ip).commit();
		loginInfo.edit().putString("out_ip", out_ip).commit();
		loginInfo.edit().putString("net_kind", net_kind).commit();
		
		RoilandCRMApplication app = (RoilandCRMApplication) getApplication();
		app.setIPAdddress(loginInfo);
		return true;
	}
	
	/**
	 * 重置登录信息
	 */
	private void resetLoginInfo()
	{
		SharedPreferences loginInfo = getSharedPreferences("login_info", 0);
		if (loginInfo == null)
			return;
        String in_ip = loginInfo.getString("in_ip", "");
        String out_ip = loginInfo.getString("out_ip", "");
        String net_kind = loginInfo.getString("net_kind", "");
        //第一次显示ip 输入
        if (StringUtils.isEmpty(in_ip) && StringUtils.isEmpty(out_ip) && StringUtils.isEmpty(net_kind)) {
        	set_btn_checked = true;
			advanced_login();
        }
        
        ((TextView)this.findViewById(R.id.login_ip_text)).setText(in_ip);
        ((TextView)this.findViewById(R.id.login_out_ip_text)).setText(out_ip);
        if (net_kind.equals("in")) {
			findViewById(R.id.login_radio_inlan).setSelected(true);
			findViewById(R.id.login_radio_outlan).setSelected(false);
			set_in_lan = true;
        } else {
			findViewById(R.id.login_radio_inlan).setSelected(false);
			findViewById(R.id.login_radio_outlan).setSelected(true);
			set_in_lan = false;
        }
	}
	
	/**
	 * 显示服务器地址输入框
	 */
	private void advanced_login()
	{
		this.findViewById(R.id.login_input_pad_big_image).setVisibility(View.VISIBLE);
		this.findViewById(R.id.login_ip_name).setVisibility(View.VISIBLE);
		this.findViewById(R.id.login_outip_name).setVisibility(View.VISIBLE);
		this.findViewById(R.id.login_ip_text).setVisibility(View.VISIBLE);
		this.findViewById(R.id.login_out_ip_text).setVisibility(View.VISIBLE);
		this.findViewById(R.id.login_radio_inlan).setVisibility(View.VISIBLE);
		this.findViewById(R.id.login_radio_outlan).setVisibility(View.VISIBLE);
		this.findViewById(R.id.login_input_pad_image).setVisibility(View.VISIBLE);
	}
	
	/**
	 * 隐藏服务器地址输入框
	 */
	private void unadvanced_login()
	{
		this.findViewById(R.id.login_input_pad_big_image).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.login_ip_name).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.login_outip_name).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.login_ip_text).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.login_out_ip_text).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.login_radio_inlan).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.login_radio_outlan).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.login_input_pad_image).setVisibility(View.VISIBLE);
	}
	
	/**
	 * 校验输入的用户名和密码
	 * @return boolean 校验是否通过
	 */
	public boolean loginVerify() {
		String username = ((TextView)findViewById(R.id.login_username_text)).getText().toString();
		String password = ((TextView)findViewById(R.id.login_password_text)).getText().toString();
		if (StringUtils.isEmpty(username)) {
			DialogUtils.alert(this, getString(R.string.warn), getString(R.string.username_warning), null);
			((TextView)findViewById(R.id.login_username_text)).requestFocus();
			return false;
		} else if (StringUtils.isEmpty(password)) {
			DialogUtils.alert(this, getString(R.string.warn), getString(R.string.password_warning), null);
			((TextView)findViewById(R.id.login_password_text)).requestFocus();
			return false;
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		((RoilandCRMApplication)getApplication()).onDestroy();
	}
	
	/**
	 * 登录异步任务务 
	 * @author Chunji Li
	 *
	 */
	private class LoginTask extends BaseTask<User> {
		public LoginTask(Activity activity) {
			super(activity);
		}
		
		@Override
		protected void onPreExecute() {
			responseException = null;
			if (loginVerify() && saveLoginInfo())
				super.onPreExecute();
			else
				super.onCancelled();
		}

		@Override
		protected User doInBackground(String... params) {
			User user = new User();
			try {

                myApplication = (RoilandCRMApplication) getApplication();
				user.setUserName(params[0].trim());
				user.setPassword(params[1]);
				//执行登录操作
				user = myApplication.getCRMManager().login(user);

                PackageInfo packageInfo = getApplicationContext()
                        .getPackageManager().getPackageInfo(getPackageName(), 0);
                
	            //设置当前使用者角色
	            PromissionController.setCurrentRole(user.getPosiName());
                if(!PromissionController.isCurrentRole(packageInfo.packageName)){
	                throw new ResponseException(StatusCodeConstant.NO_ROLE);
	            }
                //以下版本更新用
                app_name = getResources().getString(R.string.app_name);
                //获得本地版本
                localVersion = packageInfo.versionCode;
                //获得服务器版本
                String[] mServerVersion =myApplication.getCRMManager().getVersion();
                serverVersion = Integer.parseInt(mServerVersion[1]);
                down_url = mServerVersion[0];
			} catch (NameNotFoundException e) {
                Log.e(tag, "获取本地版本失败");
            } catch (ResponseException ex) {
				Log.e(tag, "Login failure", ex);
				responseException = ex;
			}
			return user;
		}
		
		@Override
		public void onPostExecute(User user) {
			super.onPostExecute(user);
			if (responseException != null || user == null) {
				Toast msg = Toast.makeText(getApplicationContext(), responseException.getMessage(), Toast.LENGTH_SHORT);
				msg.show();
			} else {
			    //本地版本小于服务器版本时
		        if (localVersion < serverVersion) {
		              checkVersion();
		        }else{
		              Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
		                startActivity(intent);
		                finish();
		        }
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 检查是否更新版本
	 * </pre>
	 *
	 */
    public void checkVersion() {
            // 发现新版本，提示用户更新
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("软件升级").setMessage("发现新版本,建议立即更新使用.")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 创建文件
                        FileUtil.createFile(app_name);
                        //首次创建通知栏
                        createNotification();
                        //开始线程下载
                        createThread();
//                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            alert.create().show();
    }

    /**
     * 
     * <pre>
     * 开线程下载新版本
     * </pre>
     *
     */
    public void createThread() {
       
         // 更新UI
        final Handler handler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWN_OK:
                        // 下载完成，点击安装
                        Uri uri = Uri.fromFile(FileUtil.updateFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        startActivity(intent);
                        pendingIntent = PendingIntent.getActivity(LoginActivity.this, 0,
                            intent, 0);

                        notification.setLatestEventInfo(LoginActivity.this, app_name,
                            "下载成功，点击安装", pendingIntent);

                        notificationManager.notify(notification_id, notification);
                        break;
                    case DOWN_ERROR:
                        notification.setLatestEventInfo(LoginActivity.this, app_name, "下载失败", pendingIntent);
                        break;

                    default:
                        stopService(updateIntent);
                        break;
                }

            }

        };

        final Message message = new Message();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long downloadSize = downloadUpdateFile(down_url, FileUtil.updateFile.toString());
                    if (downloadSize > 0) {
                        // 下载成功
                        message.what = DOWN_OK;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                        //下载失败
                    message.what = DOWN_ERROR;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
    /**
     * 
     * <pre>
     * 创建通知栏
     * </pre>
     *
     */
    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        // 这个参数是通知提示闪出来的值.
        notification.tickerText = "开始下载";
        // 在这里我们用自定的view来显示Notification
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        notification.contentView = contentView;
        updateIntent = new Intent(this, LoginActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

        notification.contentIntent = pendingIntent;

        notificationManager.notify(notification_id, notification);

    }

    /**
     * 
     * <pre>
     * 下载文件
     * </pre>
     *
     * @param down_url 下载地址
     * @param file 文件
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public long downloadUpdateFile(String down_url, String file) throws Exception {
        int down_step = 5;// 提示step
        int totalSize;// 文件总大小
        int downloadCount = 0;// 已经下载好的大小
        int updateCount = 0;// 已经上传的文件大小
        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }
        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
        byte buffer[] = new byte[1024];
        int readsize = 0;
        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
             //每次增张5%
            if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                // 改变通知栏
                notification.setLatestEventInfo(this, "正在下载...", updateCount + "%" + "",
                    pendingIntent);
                contentView.setTextViewText(R.id.notificationPercent, updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
                // 显示通知
                notificationManager.notify(notification_id, notification);

            }

        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return downloadCount;

    }

}
