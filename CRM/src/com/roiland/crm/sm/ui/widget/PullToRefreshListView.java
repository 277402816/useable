package com.roiland.crm.sm.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.adapter.SeparatedListAdapter;

/**
 * 
 * <pre>
 * 下拉刷新控件
 * </pre>
 *
 * @author Administrator
 * @version $Id: PullToRefreshListView.java, v 0.1 2013-8-6 上午10:31:31 Administrator Exp $
 */
public class PullToRefreshListView extends ListView {

	private static final int STATE_NORMAL = 0;
	private static final int STATE_PULL = 1;
	private static final int STATE_READY = 2;
	private static final int STATE_REFRESH = 3;

	private static final int HEADER_HEIGHT_DP = 58;

	private int mHeaderState = STATE_NORMAL;

	/** header view */
	private View mHeaderFrame = null;
	private View mHeaderView = null;
	private ImageView mArrowView = null;
	private ProgressBar mProgress = null;
	private TextView mTxtMessage = null;

	private float mY = 0;
	private float mHistoricalY = 0;
	private int mInitialHeight = 0;
	private boolean mFlag = false;
	private int mHeaderHeight = 0;
	private OnRefreshListener mListener = null;

	private boolean mIsArrowUp = false;

	private Animation mRotateAnimation;
	private FlingRunnable mRunnable;
	private int scaledTouchSlop;

	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mRunnable = new FlingRunnable();
		mRotateAnimation = new RotateAnimation(180.f, 0.f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setDuration(200);

		ViewConfiguration config = ViewConfiguration.get(context);
		scaledTouchSlop = config.getScaledTouchSlop();

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeaderFrame = inflater.inflate(R.layout.pull_to_refresh_list_header, this, false);
		mHeaderView = mHeaderFrame.findViewById(R.id.refresh_frame);
		mArrowView = (ImageView) mHeaderFrame.findViewById(R.id.pull_to_refresh_image);
		mProgress = (ProgressBar) mHeaderFrame.findViewById(R.id.pull_to_refresh_progress);
		mTxtMessage = (TextView) mHeaderFrame.findViewById(R.id.pull_to_refresh_text);
		addHeaderView(mHeaderFrame);

		mHeaderHeight = (int) (HEADER_HEIGHT_DP * context.getResources().getDisplayMetrics().density);
		setHeaderHeight(0);
	}

	public void setOnRefreshListener(final OnRefreshListener listener) {
		mListener = listener;
	}

	public void onRefreshComplete() {
		changeHeaderState(STATE_NORMAL);
		mRunnable.startFling(0, mHeaderHeight);
		invalidateViews();
	}

	@Override
	public boolean performItemClick(final View view, final int position, final long id) {
		if (position == 0 
				|| (getAdapter().getItemViewType(position) == SeparatedListAdapter.TYPE_LOADINGVIEW)) {
			// This is the refresh header element
			return true;
		} else {
			return super.performItemClick(view, position - 1, id);
		}
	}

	@Override
	public void setSelectionFromTop(int position, int y) {
		position ++;
		super.setSelectionFromTop(position, y);
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mHeaderState != STATE_REFRESH) {
					mY = mHistoricalY = ev.getY();
					if (mHeaderFrame.getLayoutParams() != null) {
						mInitialHeight = mHeaderFrame.getLayoutParams().height;
					}
				}
				break;
			default:
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			switch (mHeaderState) {
			case STATE_PULL:
			case STATE_NORMAL:
				if (getChildAt(0).getTop() == 0) {
					mRunnable.startFling(0, (int) (ev.getY() - mY) / 2 + mInitialHeight);
				}
				break;
			case STATE_READY:
				changeHeaderState(STATE_REFRESH);
				mRunnable.startFling(mHeaderHeight, (int) (ev.getY() - mY) / 2 + mInitialHeight);
				break;
			case STATE_REFRESH:
				mRunnable.startFling(mHeaderHeight, (int) (ev.getY() - mY) / 2 + mInitialHeight);
				break;
			default:
				break;
			}

			mFlag = false;
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent ev) {
		if (mRunnable.isRunning()) {
			return true;
		}
		if (mListener == null) {
			return super.dispatchTouchEvent(ev);
		}
		if (mHeaderState != STATE_REFRESH &&
			ev.getAction() == MotionEvent.ACTION_MOVE &&
			getFirstVisiblePosition() == 0 &&
			!mRunnable.isRunning()) {

			float direction = ev.getY() - mHistoricalY;
			int height = (int) (ev.getY() - mY) / 2 + mInitialHeight;
			if (height < 0) {
				height = 0;
			}

			if (direction != 0) {
				float deltaY = Math.abs(mY - ev.getY());
				if (deltaY > scaledTouchSlop) {
					if (direction > 0) { // Scrolling downward
						// Refresh bar is extended if top pixel of the first item is visible
						if (getChildAt(0) != null && getChildAt(0).getTop() == 0) {
	
							// Extends refresh bar
							setHeaderHeight(height);
	
							// Stop list scroll to prevent the list from overscrolling
							ev.setAction(MotionEvent.ACTION_CANCEL);
							mFlag = false;
						}
					} else { // Scrolling upward
						// Refresh bar is shortened if top pixel of the first item is visible
						if (getChildAt(0) != null && getChildAt(0).getTop() == 0) {
							setHeaderHeight(height);
	
							// If scroll reaches top of the list, list scroll is enabled
							if (getChildAt(1) != null && getChildAt(1).getTop() <= 1 && !mFlag) {
								ev.setAction(MotionEvent.ACTION_DOWN);
								mFlag = true;
							}
						}
					}
				}
			}

			mHistoricalY = ev.getY();
		}

		try {
			return super.dispatchTouchEvent(ev);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int getLastVisiblePosition() {
		return super.getLastVisiblePosition() - 1;
	}

	private void setHeaderHeight(final int height) {
		if (height <= 1) {
			mHeaderView.setVisibility(GONE);
		} else {
			mHeaderView.setVisibility(VISIBLE);
		}

		// Extends refresh bar
		LayoutParams lp = (LayoutParams) mHeaderFrame.getLayoutParams();
		if (lp == null) {
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}
		lp.height = height;
		mHeaderFrame.setLayoutParams(lp);

		// Refresh bar shows up from bottom to top
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
		if (params == null) {
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}
		params.topMargin = -mHeaderHeight + height;
		mHeaderView.setLayoutParams(params);

		// 如果滚动到触发线，开始刷新
		if (mHeaderState != STATE_REFRESH) {
			if (height == 0) {
				changeHeaderState(STATE_NORMAL);
			} else if (height > mHeaderHeight) {
				changeHeaderState(STATE_READY);
			} else if (height < mHeaderHeight) {
				changeHeaderState(STATE_PULL);
			} else {
			}
		}
	}

	private void rotateArrow() {
		Drawable drawable = mArrowView.getDrawable();
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.save();
		canvas.rotate(180.0f, canvas.getWidth() / 2.0f, canvas.getHeight() / 2.0f);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		canvas.restore();
		mArrowView.setImageBitmap(bitmap);
		mIsArrowUp = !mIsArrowUp;
	}

	private void changeHeaderState(int state) {
		if (mHeaderState != state) {
			mHeaderState = state;

			switch (mHeaderState) {
			case STATE_NORMAL:
				mProgress.setVisibility(GONE);
				mArrowView.setVisibility(VISIBLE);
				mTxtMessage.setText("");
				break;
			case STATE_PULL:
				if (mIsArrowUp) {
					mArrowView.startAnimation(mRotateAnimation);
					rotateArrow();
				}
				mTxtMessage.setText(R.string.pull_down_to_refresh);
				break;
			case STATE_READY:
				if (!mIsArrowUp) {
					mArrowView.startAnimation(mRotateAnimation);
					rotateArrow();
				}
				mTxtMessage.setText(R.string.release_to_update);
				break;
			case STATE_REFRESH:
				mArrowView.setVisibility(GONE);
				mProgress.setVisibility(VISIBLE);
				mTxtMessage.setText(R.string.is_being_updated);
				if (mListener != null) {
					mListener.onRefresh(this);
				}
				break;
			default:
				break;
			}
		}
	}

	private class FlingRunnable implements Runnable {
		private int mValue;
		private int mLimit;
		private boolean mIsRunning;

		public FlingRunnable() {
			mValue = 0;
			mLimit = 0;
			mIsRunning = false;
		}

		public synchronized void startFling(int limit, int startValue) {
			removeCallbacks(this);
			mIsRunning = true;
			mLimit = limit;
			mValue = startValue;
			post(this);
		}

		public boolean isRunning() {
			return mIsRunning;
		}

		@Override
		public void run() {
			if (mValue >= mLimit) {
				setHeaderHeight(mValue);
				int displacement = (mValue - mLimit) / 10;
				if (displacement == 0) {
					mValue --;
					post(this);
				} else {
					mValue -= displacement;
					post(this);
				}
			} else {
				mIsRunning = false;
			}
		}
	}


	public static interface OnRefreshListener {
		public void onRefresh(View view);
	}

}
