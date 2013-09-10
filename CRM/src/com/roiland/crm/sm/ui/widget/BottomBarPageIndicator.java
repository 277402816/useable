package com.roiland.crm.sm.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

@SuppressLint("DrawAllocation")
public class BottomBarPageIndicator extends ViewGroup implements AnimationListener {
	public static final int INDICATOR_TYPE_SLIDER_TOP = 2;
	public static final int INDICATOR_TYPE_SLIDER_BOTTOM = 3;
	public static final int INDICATOR_HEIGHT_PHONE = 55;
	public static final int INDICATOR_HEIGHT_PAD = 65;
	public static final int INDICATOR_X_PHONE = 20;
	public static final int INDICATOR_X_PAD = 40;
	public static final int INTERVAL_Y_PHONE = 5;
	public static final int INTERVAL_Y_PAD = 3;
	private View mIndicator;
//	private int mItems = 5;
	private int mCurrent = 0;
	private int mVisibleTime = 300;
	private Handler mHandler = new Handler();
//	private BottomBar mBottomBar;
	private int mIndicatorHeight = INDICATOR_HEIGHT_PHONE;
	private int mIndicatorX = INDICATOR_X_PHONE;
	private int mIntervalY = INTERVAL_Y_PHONE;
	
	public BottomBarPageIndicator(Context context) {
		super(context);
		initIndicator(context);
	}

	public BottomBarPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initIndicator(context);
	}

	public BottomBarPageIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initIndicator(context);
	}

	private void initIndicator(Context context) {
		mIndicator = new SliderIndicator(context);
		addView(mIndicator);
	}
	
	public void setBottomBar(BottomBar bottomBar) {
//		mBottomBar = bottomBar;
	}
	
	public void setActivity(Activity activity) {
	}
	
	public void setItems(int items) {
//		mItems = items;
//		((SliderIndicator) mIndicator).setTotalItems(mItems);
		mIndicator.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int realHeight = mIndicatorHeight;
		mIndicator.measure(getWidth(), realHeight);
		int realHeightMeasurespec = MeasureSpec.makeMeasureSpec(realHeight,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, realHeightMeasurespec);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mIndicator.measure(getWidth(), mIndicatorHeight);
		mIndicator.setLayoutParams(params);
		mIndicator.layout(0, 0, getWidth(), mIndicatorHeight);
	}

	public void indicate(float percent) {
		setVisibility(View.VISIBLE);
		mIndicator.invalidate();
		mHandler.removeCallbacks(mAutoHide);
		if (mVisibleTime > 0)
			mHandler.postDelayed(mAutoHide, mVisibleTime);
	}

	public void fullIndicate(int position) {
		setVisibility(View.VISIBLE);
		mHandler.removeCallbacks(mAutoHide);
		if (mVisibleTime > 0)
			mHandler.postDelayed(mAutoHide, mVisibleTime);
	}

	public void setType(int type) {
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				this.getLayoutParams());
		if (type == INDICATOR_TYPE_SLIDER_BOTTOM) {
			lp.gravity = Gravity.BOTTOM;
		} else {
			lp.gravity = Gravity.TOP;
		}
		setLayoutParams(lp);
		removeView(mIndicator);
		initIndicator(getContext());
	}

	public void setAutoHide(boolean autohide) {
		if (autohide) {
			mVisibleTime = 300;
			setVisibility(INVISIBLE);
		} else {
			mVisibleTime = -1;
			setVisibility(VISIBLE);
		}
	}

	private Runnable mAutoHide = new Runnable() {
		public void run() {
//			if (mAnimation == null) {
//				mAnimation = AnimationUtils.loadAnimation(getContext(),
//						R.anim.slide_fast);
//				mAnimation.setAnimationListener(BottomBarPageIndicator.this);
//			}
//			startAnimation(mAnimation);
		}
	};

	private class SliderIndicator extends View implements View.OnTouchListener{
		private Paint mPaint;
		private int mTotalItems = 5;
		private int displayRadius = 6;

		public SliderIndicator(Context context) {
			super(context);
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			setOnTouchListener(this);
		}

		@Override
		public void draw(Canvas canvas) {
			for (int i = 0; i < mTotalItems; i++) {
				if (i == mCurrent) {
					mPaint.setColor(Color.WHITE);
				} else {
					mPaint.setColor(Color.GRAY);
				}
				
				canvas.drawCircle(mIndicatorX, 
						getHeight() * i + getHeight() / mIntervalY * ( 1 - 2 * i), displayRadius, mPaint);
			}
		}

		@SuppressWarnings("unused")
		public void setTotalItems(int items) {
			mTotalItems = items;
		}
	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
//			if (event.getAction() == MotionEvent.ACTION_DOWN) {
//				if (event.getY() <= getHeight() / 2) {
//					if (mCurrent != BottomBar.PREVIOUS_EDIT_TAB) {
//						mBottomBar.moveEditTab(BottomBar.PREVIOUS_EDIT_TAB);
//					}
//				} else {
//					if (mCurrent != BottomBar.NEXT_EDIT_TAB) {
//						mBottomBar.moveEditTab(BottomBar.NEXT_EDIT_TAB);
//					}
//				}
//				
//				postInvalidate();
//			}
			
			return true;
		}
	}

	public void setCurrentItem(int current) {
		mCurrent = current;
	}
	
	public void onAnimationEnd(Animation animation) {
		setVisibility(View.INVISIBLE);
	}

	public void onAnimationRepeat(Animation animation) {
	}

	public void onAnimationStart(Animation animation) {
	}

	public void hide() {
		setVisibility(View.INVISIBLE);
	}

	public void show() {
		if (mVisibleTime < 0) {
			setVisibility(View.VISIBLE);
		}
	}
}