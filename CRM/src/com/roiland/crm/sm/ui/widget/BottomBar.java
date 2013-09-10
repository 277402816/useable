package com.roiland.crm.sm.ui.widget;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * BottomBar
 * </pre>
 *
 * @author liuyu
 * @version $Id: BottomBar.java, v 0.1 2013-7-2 上午9:03:22 liuyu Exp $
 */
public class BottomBar implements OnClickListener{
	private static final String tag = Log.getTag(BottomBar.class);
	
	public final static int SWIPE_MIN_DISTANCE = 70;

	public final static int TAB_INDEX_SALES_OPPO = 0;
	public final static int TAB_INDEX_PURCHASE_CAR = 1;
	public final static int TAB_INDEX_CONTACTER = 2;
	public final static int TAB_INDEX_DRIVETEST = 3;
	public final static int TAB_INDEX_SALESORDER = 4;
	public final static int TAB_INDEX_TRACEPLAN = 5;
	public final static int TAB_INDEX_ATTACHMENT = 6;
	
	public final static int SALES_OPPO_BOTTOM = R.id.sales_oppo_bottom;
	public final static int PURCHASE_CAR_BOTTOM = R.id.purchase_car_bottom;
	public final static int CONTACTER_BOTTOM = R.id.contactor_bottom;
	public final static int DRIVE_TEST_BOTTOM = R.id.drive_test_bottom;
	public final static int SALES_ORDER_BOTTOM = R.id.sales_order_bottom;
	public final static int TRACE_PLAN_BOTTOM = R.id.trace_plan_bottom;
	public final static int ATTACHMENT_BOTTOM = R.id.attachment_bottom;
	
	public final static int PREVIOUS_EDIT_TAB = 0;
	public final static int NEXT_EDIT_TAB = 1;
	private final static int INVALID_VALUE = -1;
	private final static int MAX_COUNT_PAGE = 5;
	private int currentTabIndex = -1;
	private int tabCount = 0;
	
	private Animation mSlideLeftIn;
	private Animation mSlideLeftOut;
	private Animation mSlideRightIn;
	private Animation mSlideRightOut;
	
	private View mBottombar;
	private BottomBarLayout mBottomBarTab;
	
	private View mContacter;
	private View mDriveTest;
	private View mSalesOrder;
	private View mAttachment;
	private View mTracePlan;
	private View mSalesOppo;
	private View mPurchaseCar;
	
	private View mContacterIcon;
	private View mDriveTestIcon;
	private View mSalesOrderIcon;
	private View mAttachmentIcon;
	private View mTracePlanIcon;
	private View mSalesOppoIcon;
	private View mPurchaseCarIcon;
	
	private CustomViewFlipper mBottomBarViewFlipper;

	private BottomBarPageIndicator mBottomBarPageIndicator;
	
//	private Activity mActivity;
	
	private BottomEventListener bottomEventListener;
	
	public BottomBar(Activity mActivity, View bottomBar) {
//		this.mActivity = mActivity;
		
		mSlideLeftIn = AnimationUtils.loadAnimation(mActivity, R.anim.slide_left_in);
		mSlideLeftOut = AnimationUtils.loadAnimation(mActivity, R.anim.slide_left_out);
		mSlideRightIn = AnimationUtils.loadAnimation(mActivity, R.anim.slide_right_in);
		mSlideRightOut = AnimationUtils.loadAnimation(mActivity, R.anim.slide_right_out);

		
		mBottombar = bottomBar;
		
		mBottomBarTab = (BottomBarLayout)mBottombar.findViewById(R.id.bottom_bar_tab);
		mBottomBarTab.setBottomBar(this);
		mBottomBarTab.setVisibility(View.VISIBLE);
		
		mBottomBarViewFlipper = (CustomViewFlipper) mBottomBarTab.findViewById(R.id.bottom_bar_tab_flipper);
		
		mBottomBarPageIndicator = (BottomBarPageIndicator) mBottomBarTab.findViewById(R.id.bottom_bar_tab_indicator);
		mBottomBarPageIndicator.setActivity(mActivity);
		mBottomBarPageIndicator.setBottomBar(this);
		mBottomBarPageIndicator.setAutoHide(false);
		mBottomBarPageIndicator.setCurrentItem(PREVIOUS_EDIT_TAB);
		mBottomBarPageIndicator.setItems(0);
		mBottomBarPageIndicator.show();
		
		
	}
	
	/**
	 * 获取当前备选中Tab的索引
	 * @return
	 */
	public int getCurrentTabIndex() {
		return currentTabIndex;
	}
	
	public BottomBarPageIndicator getBottomBarPageIndicator() {
		return mBottomBarPageIndicator;
	}
	
	public CustomViewFlipper getCustomViewFlipper() {
		return mBottomBarViewFlipper;
	}
	
	public void setBottomEventListener(BottomEventListener bottomEventListener) {
		this.bottomEventListener = bottomEventListener;
	}
	
	
	@Override
	public void onClick(View v) {
		int clickTabIndex = INVALID_VALUE;
		
		switch (v.getId()) {
		case R.id.sales_oppo_bottom:
			clickTabIndex = TAB_INDEX_SALES_OPPO;
			break;
		case R.id.purchase_car_bottom:
			clickTabIndex = TAB_INDEX_PURCHASE_CAR;
			break;
		case R.id.contactor_bottom:
			clickTabIndex = TAB_INDEX_CONTACTER;
			break;
		case R.id.drive_test_bottom:
			clickTabIndex = TAB_INDEX_DRIVETEST;
			break;
		case R.id.sales_order_bottom:
			clickTabIndex = TAB_INDEX_SALESORDER;
			break;
		case R.id.trace_plan_bottom:
			clickTabIndex = TAB_INDEX_TRACEPLAN;
			break;
		case R.id.attachment_bottom:
			clickTabIndex = TAB_INDEX_ATTACHMENT;
			break;
		}
		Log.w(tag, "Current tabl ID = " + clickTabIndex);
		setCurrentTab(clickTabIndex);
	}

	public void moveEditTab(int item) {
		if (tabCount <= MAX_COUNT_PAGE)
			return;
		
		if (mBottomBarViewFlipper.getCurrentView() != mBottomBarViewFlipper.getChildAt(item)) {
			switch (item) {
			case PREVIOUS_EDIT_TAB:
				mBottomBarViewFlipper.setInAnimation(mSlideRightIn);
				mBottomBarViewFlipper.setOutAnimation(mSlideRightOut);
				mBottomBarViewFlipper.showPrevious();
				break;
			case NEXT_EDIT_TAB:
				mBottomBarViewFlipper.setInAnimation(mSlideLeftIn);
				mBottomBarViewFlipper.setOutAnimation(mSlideLeftOut);
				mBottomBarViewFlipper.showNext();
				break;

			default:
				break;
			}
			mBottomBarPageIndicator.setCurrentItem(item);
			mBottomBarPageIndicator.invalidate();
		}
	}
	
	public void setCurrentTab(int index) {
		if (currentTabIndex == index) {
			return;
		}
		if (mSalesOppoIcon != null)
			mSalesOppoIcon.setSelected(false);
		if (mPurchaseCarIcon != null)
			mPurchaseCarIcon.setSelected(false);
		if (mContacterIcon != null)
			mContacterIcon.setSelected(false);
		if (mDriveTestIcon != null)
			mDriveTestIcon.setSelected(false);
		if (mSalesOrderIcon != null)
			mSalesOrderIcon.setSelected(false);
		if (mTracePlanIcon != null)
			mTracePlanIcon.setSelected(false);
		if (mAttachmentIcon != null)
			mAttachmentIcon.setSelected(false);
		
		switch (index) {
		case TAB_INDEX_SALES_OPPO:
			mSalesOppoIcon.setSelected(true);
			break;
		case TAB_INDEX_PURCHASE_CAR:
			mPurchaseCarIcon.setSelected(true);
			break;
		case TAB_INDEX_CONTACTER:
			mContacterIcon.setSelected(true);
			break;
		case TAB_INDEX_DRIVETEST:
			mDriveTestIcon.setSelected(true);
			break;
		case TAB_INDEX_SALESORDER:
			mSalesOrderIcon.setSelected(true);
			break;
		case TAB_INDEX_TRACEPLAN:
			mTracePlanIcon.setSelected(true);
			break;
		case TAB_INDEX_ATTACHMENT:
			mAttachmentIcon.setSelected(true);
			break;
		}
		//Set current tab index
		currentTabIndex = index;
		//Call your specified function implementation.
		if (bottomEventListener != null) {
			bottomEventListener.bottomTabClick(currentTabIndex);
		}
	}
	
	public void addVisiableBottom(int buttomId) {
		switch (buttomId) {
		case R.id.sales_oppo_bottom:
			if (mSalesOppo == null) {
				mSalesOppo = mBottomBarTab.findViewById(R.id.sales_oppo_bottom);
				mSalesOppo.setOnClickListener(this);
			}
			mSalesOppo.setVisibility(View.VISIBLE);
			mSalesOppoIcon = mBottomBarTab.findViewById(R.id.sales_oppo_bottom_icon);
			break;
		case R.id.purchase_car_bottom:
			if (mPurchaseCar == null) {
				mPurchaseCar = mBottomBarTab.findViewById(R.id.purchase_car_bottom);
				mPurchaseCar.setOnClickListener(this);
			}
			mPurchaseCar.setVisibility(View.VISIBLE);
			mPurchaseCarIcon = mBottomBarTab.findViewById(R.id.purchase_car_bottom_icon);
			break;
		case R.id.contactor_bottom:
			if (mContacter == null) {
				mContacter = mBottomBarTab.findViewById(buttomId);
			}
			mContacter.setOnClickListener(this);
			mContacter.setVisibility(View.VISIBLE);
			mContacterIcon = mBottomBarTab.findViewById(R.id.contactor_bottom_icon);
			break;
		case R.id.drive_test_bottom:
			if (mDriveTest == null) {
				mDriveTest = mBottomBarTab.findViewById(R.id.drive_test_bottom);
			}
			mDriveTest.setOnClickListener(this);
			mDriveTest.setVisibility(View.VISIBLE);
			mDriveTestIcon = mBottomBarTab.findViewById(R.id.drive_test_bottom_icon);
			break;
		case R.id.sales_order_bottom:
			if (mSalesOrder == null) {
				mSalesOrder = mBottomBarTab.findViewById(R.id.sales_order_bottom);
				mSalesOrder.setOnClickListener(this);
			}
			mSalesOrder.setVisibility(View.VISIBLE);
			mSalesOrderIcon = mBottomBarTab.findViewById(R.id.sales_order_bottom_icon);
			break;
		case R.id.trace_plan_bottom:
			if (mTracePlan == null) {
				mTracePlan = mBottomBarTab.findViewById(R.id.trace_plan_bottom);
				mTracePlan.setOnClickListener(this);
			}
			mTracePlan.setVisibility(View.VISIBLE);
			mTracePlanIcon = mBottomBarTab.findViewById(R.id.trace_plan_bottom_icon);
			break;
		case R.id.attachment_bottom:
			if (mAttachment == null) {
				mAttachment = mBottomBarTab.findViewById(R.id.attachment_bottom);
				mAttachment.setOnClickListener(this);
			}
			mAttachment.setVisibility(View.VISIBLE);
			mAttachmentIcon = mBottomBarTab.findViewById(R.id.attachment_bottom_icon);
			break;
		default:
			break;
		}
		
		tabCount++;
	}
	
	public void showEditTab() {
		
		if (mBottombar.getVisibility() != View.VISIBLE) {
			mBottombar.startAnimation(mSlideRightIn);
			mBottombar.setVisibility(View.VISIBLE);
		}

		mBottomBarTab.setVisibility(View.VISIBLE);
		
	}
	
	public void setVisible(boolean isVisible) {
		if (mBottombar != null) {
			if (isVisible) {
				if (mBottombar.getVisibility() != View.VISIBLE) {
					mBottombar.setVisibility(View.VISIBLE);
				}
			} else {
				if (mBottombar.getVisibility() != View.GONE) {
					mBottombar.setVisibility(View.GONE);
				}
			}
		}
	}
	
	/**
	 * This interface use to implement the functions in your spec UI, 
	 * @author cjyy
	 *
	 */
	public interface BottomEventListener {
		void bottomTabClick(int tabIndex);
	}
}
