package com.roiland.crm.sm.ui.widget;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicBoolean;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.utils.Log;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * <pre>
 * 自定义进度框类
 * </pre>
 *
 * @author cjyy
 * @version $Id: ProgressDialog.java, v 0.1 2013-5-17 下午02:12:57 cjyy Exp $
 */
public class ProgressDialog extends Dialog {
	private static final String tag = "ProgressDialog";

	public static final int STYLE_SPINNER = 0;
	public static final int STYLE_HORIZONTAL = 1;
	private int mProgressStyle = STYLE_SPINNER;
	
	private ProgressBar mProgress;
	private TextView mMessageView;	
	
	//private TextView mProgressNumber;
	//private String mProgressNumberFormat;
	private TextView mProgressPercent;
	private TextView mTitle;
	private NumberFormat mProgressPercentFormat;

	private int mMax;
	private int mProgressVal;
	private int mSecondaryProgressVal;
	private int mIncrementBy;
	private int mIncrementSecondaryBy;
	private Drawable mProgressDrawable;
    private Drawable mIndeterminateDrawable;
    private CharSequence mMessage;
    private boolean mIndeterminate;
    private boolean mHasStarted;
    private Handler mViewUpdateHandler;
    
    public OnProgressCancelListener onProgressCancelListener = null;
    public AtomicBoolean isCancelButtonClicked = new AtomicBoolean(true);
    
    public interface OnProgressCancelListener {
    	void OnCancel();
    }

	public ProgressDialog(Context context, int theme) {
		super(context, R.style.load_dialog);
	}

	public static ProgressDialog show(Context context, CharSequence title,
			CharSequence message) {
		return show(context, title, message, false);
	}

	public static ProgressDialog show(Context context, CharSequence title,
			CharSequence message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}

	public static ProgressDialog show(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable, null);
	}

	public static ProgressDialog show(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		ProgressDialog dialog = new ProgressDialog(context, R.style.load_dialog);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.show();
		return dialog;
	}

	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		/*
		 * Use a separate handler to update the text views as they must be
		 * updated on the same thread that created them.
		 */
		if (mProgressStyle == STYLE_HORIZONTAL) {
			mViewUpdateHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					/* Update the number and percent */
					int progress = mProgress.getProgress();
					int max = mProgress.getMax();
					double percent = (double) progress / (double) max;
				
					SpannableString tmp = new SpannableString(
							mProgressPercentFormat.format(percent));
					tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
							0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					mProgressPercent.setText(tmp);
				}
			};

			View view = inflater.inflate(R.layout.alert_dialog_progress, null);
			 mProgress = (ProgressBar) view.findViewById(R.id.progress);
	         
			 mTitle = (TextView) view.findViewById(R.id.progress_title);
	         mProgressPercent = (TextView) view.findViewById(R.id.progress_percent);
	         mProgressPercentFormat = NumberFormat.getPercentInstance();
	         mProgressPercentFormat.setMaximumFractionDigits(0);
	         mProgress.getLayoutParams().width = getContext().getResources().getDimensionPixelSize(R.dimen.progress_bar_length);;
			setContentView(view);
		} else {
			View view = inflater.inflate(R.layout.progress_dialog, null);
			mProgress = (ProgressBar) view.findViewById(R.id.progress);
			mMessageView = (TextView) view.findViewById(R.id.message);
			setContentView(view);
		}
		if (mMax > 0) {
			setMax(mMax);
		}
		if (mProgressVal > 0) {
			setProgress(mProgressVal);
		}
		if (mSecondaryProgressVal > 0) {
			setSecondaryProgress(mSecondaryProgressVal);
		}
		if (mIncrementBy > 0) {
			incrementProgressBy(mIncrementBy);
		}
		if (mIncrementSecondaryBy > 0) {
			incrementSecondaryProgressBy(mIncrementSecondaryBy);
		}
		if (mProgressDrawable != null) {
			setProgressDrawable(mProgressDrawable);
		}
		if (mIndeterminateDrawable != null) {
			setIndeterminateDrawable(mIndeterminateDrawable);
		}
		if (mMessage != null) {
			setMessage(mMessage);
		}
		setIndeterminate(mIndeterminate);
		onProgressChanged();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		mHasStarted = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHasStarted = false;
	}
	
	@Override
	public void dismiss() {
		if(isCancelButtonClicked.get()){			
			if (onProgressCancelListener != null) {
				onProgressCancelListener.OnCancel();	
			}
		} 
		super.dismiss();
	}

	public void setProgress(int value) {
		if (mHasStarted) {
			mProgress.setProgress(value);
			onProgressChanged();
		} else {
			mProgressVal = value;
		}
	}
	
	public void setProgressAndStopAuto(int value) {
		stopAutoPregress();
		setProgress(value);
	}

	public void setSecondaryProgress(int secondaryProgress) {
		if (mProgress != null) {
			mProgress.setSecondaryProgress(secondaryProgress);
			onProgressChanged();
		} else {
			mSecondaryProgressVal = secondaryProgress;
		}
	}

	public int getProgress() {
		if (mProgress != null) {
			return mProgress.getProgress();
		}
		return mProgressVal;
	}

	public int getSecondaryProgress() {
		if (mProgress != null) {
			return mProgress.getSecondaryProgress();
		}
		return mSecondaryProgressVal;
	}

	public int getMax() {
		if (mProgress != null) {
			return mProgress.getMax();
		}
		return mMax;
	}

	public void setMax(int max) {
		if (mProgress != null) {
			mProgress.setMax(max);
			onProgressChanged();
		} else {
			mMax = max;
		}
	}

	public void incrementProgressBy(int diff) {
		if (mProgress != null) {
			mProgress.incrementProgressBy(diff);
			onProgressChanged();
		} else {
			mIncrementBy += diff;
		}
	}

	public void incrementSecondaryProgressBy(int diff) {
		if (mProgress != null) {
			mProgress.incrementSecondaryProgressBy(diff);
			onProgressChanged();
		} else {
			mIncrementSecondaryBy += diff;
		}
	}

	public void setProgressDrawable(Drawable d) {
		if (mProgress != null) {
			mProgress.setProgressDrawable(d);
		} else {
			mProgressDrawable = d;
		}
	}

	public void setIndeterminateDrawable(Drawable d) {
		if (mProgress != null) {
			mProgress.setIndeterminateDrawable(d);
		} else {
			mIndeterminateDrawable = d;
		}
	}

	public void setIndeterminate(boolean indeterminate) {
		if (mProgress != null) {
			mProgress.setIndeterminate(indeterminate);
		} else {
			mIndeterminate = indeterminate;
		}
	}

	public boolean isIndeterminate() {
		if (mProgress != null) {
			return mProgress.isIndeterminate();
		}
		return mIndeterminate;
	}

	@Override
	public void setTitle(CharSequence title) {
		if (mProgress != null) {
			if (mProgressStyle == STYLE_HORIZONTAL) {
				mTitle.setText(title);
			} else {
				mMessageView.setText(title);
			}
		}
	}

	@Override
	public void setTitle(int titleId) {
		super.setTitle(null);
	}

	public void setMessage(CharSequence message) {
		if (mProgress != null) {
			if (mProgressStyle == STYLE_HORIZONTAL) {
				mProgressPercent.setText(message);
			} else {
				mMessageView.setText(message);
			}
		} else {
			mMessage = message;
		}
	}

	public void setProgressStyle(int style) {
		mProgressStyle = style;
	}

	private void onProgressChanged() {
		if (mProgressStyle == STYLE_HORIZONTAL) {
			mViewUpdateHandler.sendEmptyMessage(0);
		}
	}

	private int autoProgress;
	private static final int AUTO_PROGRESS_UPDATE = 0;
	private static final int AUTO_PROGRESS_END = 1;
	private Handler handler = new Handler() {
		@Override 
        public void handleMessage(Message msg) {
			int value = msg.arg2;
			
			switch (msg.arg1) {
			case AUTO_PROGRESS_UPDATE:
				setProgress(value);
				break;
			case AUTO_PROGRESS_END:
				setProgress(value);
				ProgressDialog.this.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	private Thread task;
	public synchronized void showAutoPregressRange(final int start, final int end, final int add, final long interval) {
		Runnable work = new Runnable() {
			@Override
			public void run() {
				try {
					for (autoProgress = start + add; !Thread.currentThread().isInterrupted() && autoProgress <= end; autoProgress += add) {
						try {
							Thread.sleep(interval);
						} catch (InterruptedException e) {
							break;
						}
						if(isCancelButtonClicked.get()){
							showAutoPregressRangeDesc(autoProgress,start,1,1000L);
							break;
						}else{
							
							Message msg = new Message();
							msg.arg2 = AUTO_PROGRESS_UPDATE;
							msg.arg2 = autoProgress;
							handler.sendMessage(msg);
						}
					}
				} catch (Exception e) {
					Log.e(tag, "showAutoPregressRange Thread", e);
				}
			}
		};

		if (task != null) {
			task.interrupt();
			task = null;
		}

		if (task == null) {
			task = new Thread(work);
			task.start();
		}
	}
	
	public synchronized void showAutoPregressRangeDesc(final int start, final int end, final int add, final long interval) {
		Runnable work = new Runnable() {
			@Override
			public void run() {
				try {
					for (autoProgress = start - add; !Thread.currentThread().isInterrupted() && autoProgress >= end; autoProgress -= add) {
						try {
							Thread.sleep(interval);
						} catch (InterruptedException e) {
							break;
						}

						Message msg = new Message();
						msg.arg2 = AUTO_PROGRESS_UPDATE;
						msg.arg2 = autoProgress;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Log.e(tag, "showAutoPregressRangeDesc Thread", e);
				}
			}
		};

		if (task != null) {
			task.interrupt();
			task = null;
		}

		if (task == null) {
			task = new Thread(work);
			task.start();
		}
	}
	

	public synchronized void stopAutoPregress() {
		if (task != null) {
			task.interrupt();
			task = null;
		}
	}
	
	public void autoProgressAndDismiss(int times, long delay) {
		stopAutoPregress();

		int start = autoProgress > 0? autoProgress: getProgress();
		int end = getMax();
		int add = (end - start) / times;
		
		if (add > 0) {
			long duration = delay / times;
			Log.d(tag, "start=" + start + "; end=" + end + "; add=" + add + "; duration=" + duration);
			for (int i = start + add; i <= end; i += add) {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					break;
				}
				
				setProgress(i);
			}
		}

		dismiss();
	}

	@Override
	public boolean onSearchRequested() {
		return false;
	}
}
