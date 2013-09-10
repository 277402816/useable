package com.roiland.crm.sm.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class BottomBarLayout extends LinearLayout {

	private float mFirstX = 0;
//	private float mMoveX = 0;
//	private BottomBar mBottomBar;
	
	public BottomBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BottomBarLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			mFirstX = ev.getX();
		}
//		Comments for disable sliding 
//		if(ev.getAction()==MotionEvent.ACTION_MOVE){
//			mMoveX = ev.getX() - mFirstX;
//			if (Math.abs(mMoveX) > BottomBar.SWIPE_MIN_DISTANCE) {
//				mBottomBar.moveEditTab(mMoveX < 0 ? BottomBar.NEXT_EDIT_TAB : BottomBar.PREVIOUS_EDIT_TAB);
//			}
//		}

		return false;
	}

	public float getFirstX() {
		return mFirstX;
	}
	
	public void setBottomBar(BottomBar bar) {
//		this.mBottomBar = bar;
	}
}