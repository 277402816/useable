package com.roiland.crm.sm.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ViewFlipper;
/*
 * fix Android bug
FATAL EXCEPTION: main
 java.lang.IllegalArgumentException: Receiver not registered: android.widget.ViewFlipper$1@405111a0
     at android.app.LoadedApk.forgetReceiverDispatcher(LoadedApk.java:610)
     at android.app.ContextImpl.unregisterReceiver(ContextImpl.java:821)
     at android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:331)
     at android.widget.ViewFlipper.onDetachedFromWindow(ViewFlipper.java:104)
     at android.view.View.dispatchDetachedFromWindow(View.java:6173)
     at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1164)
     at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1162)
     at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1162)
     at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1162)
     at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1162)
     at android.view.ViewGroup.dispatchDetachedFromWindow(ViewGroup.java:1162)
     at android.view.ViewRoot.dispatchDetachedFromWindow(ViewRoot.java:1746)
     at android.view.ViewRoot.doDie(ViewRoot.java:2757)
     at android.view.ViewRoot.die(ViewRoot.java:2727)
     at android.view.WindowManagerImpl.removeViewImmediate(WindowManagerImpl.java:218)
     at android.view.Window$LocalWindowManager.removeViewImmediate(Window.java:436)
     at android.app.ActivityThread.handleDestroyActivity(ActivityThread.java:2701)
     at android.app.ActivityThread.handleRelaunchActivity(ActivityThread.java:2806)
     at android.app.ActivityThread.access$1600(ActivityThread.java:117)
     at android.app.ActivityThread$H.handleMessage(ActivityThread.java:935)
     at android.os.Handler.dispatchMessage(Handler.java:99)
     at android.os.Looper.loop(Looper.java:130)
     at android.app.ActivityThread.main(ActivityThread.java:3683)
     at java.lang.reflect.Method.invokeNative(Native Method)
     at java.lang.reflect.Method.invoke(Method.java:507)
     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:839)
     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:597)
     at dalvik.system.NativeStart.main(Native Method)
     Force finishing activity com.wdc.wdfiles/.ui.activity.WelcomeActivity
 */
public class CustomViewFlipper extends ViewFlipper{

	private static final String tag = CustomViewFlipper.class.getName();

	public CustomViewFlipper(Context context) {
		super(context);
	}

	public CustomViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDetachedFromWindow() {
		try {
			super.onDetachedFromWindow();
		}
		catch (IllegalArgumentException e) {
			Log.w(tag, "Call stopFlipping() in order to kick off updateRunning()");
			try {
				stopFlipping();
			} catch (Exception e1) {
				//omitted
			}
		}
	}
}
