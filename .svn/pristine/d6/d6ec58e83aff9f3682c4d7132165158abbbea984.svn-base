package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter;
import com.roiland.crm.sm.ui.adapter.SmCarInfoListAdapter;
import com.roiland.crm.sm.ui.adapter.SmCustomInfoListAdapter;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 销售线索详细信息fragment
 * </pre>
 * @extends SherlockFragment
 * @author liuyu
 * @version $Id: SmOppoInfoFragment.java, v 0.1 2013-5-16 下午4:52:58 liuyu Exp $
 */
public class SmOppoInfoFragment extends SherlockFragment {
    private final static String tag = Log.getTag(SmOppoInfoFragment.class);
    
    
    public LinearLayout mCarInfo;
    public boolean bEdit = false;
    public boolean flag = false;
    public Project project = null;
	private ImageButton mCustomInfoMore;
	private ImageButton mCustomInfoHide;
	private ImageButton mCarInfoMore;
	private ImageButton mCarInfoHide;
	private LinearLayout mCustomInfo;
	private LinearLayout mCustominfoTitle;
	private ListView mNewFollowListView;
	private static ToggleButton newToggleBtn;
	private LinearLayout newlayout;
	private boolean custFlag = false;
	private boolean carFlag = false;
	private boolean isFromCustManager;//从客户管理进入
	SmCustomInfoListAdapter customInfoAdapter = null;
    SmCarInfoListAdapter carInfoAdapter = null;
    BasicInfoListAdapter.Info idnumberInfo;
    BasicInfoListAdapter.Info birthdayInfo;
    BasicInfoListAdapter.Info idtypeInfo;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 创建时调用此方法
	 * @param inflater 页面加载
	 * @param container 容器
	 * @param savedInstanceState
	 * @return 加载后的页面
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sm_activity_example, container, false);
		newToggleBtn = (ToggleButton)view.findViewById(R.id.toggleButton_new);
		mNewFollowListView = (ListView)view.findViewById(R.id.oppo_followinfo_list);
		newlayout = (LinearLayout)view.findViewById(R.id.oppo_followinfo_list_layout);
		mCustominfoTitle = (LinearLayout)view.findViewById(R.id.custominfo);
		mCarInfoHide = (ImageButton)view.findViewById(R.id.carinfo_hide_btn);
		mCarInfoMore = (ImageButton)view.findViewById(R.id.carinfo_more_btn);
		newlayout.setVisibility(View.GONE);
		newToggleBtn.setVisibility(View.GONE);
		Intent intent = getActivity().getIntent();
	    isFromCustManager = intent.getBooleanExtra("fromCustManager", false);
		setNewListHidden();
		mCustomInfo = (LinearLayout)view.findViewById(R.id.custom_info_list);
		mCarInfo = (LinearLayout)view.findViewById(R.id.car_info_list);
		mCarInfoMore = (ImageButton)view.findViewById(R.id.carinfo_more_btn);
	      if(isFromCustManager){
	            comeFromCustMamager();
	        }else{
	            mCustomInfoMore = (ImageButton)view.findViewById(R.id.custominfo_more_btn);
	            mCustomInfoHide = (ImageButton)view.findViewById(R.id.custominfo_hide_btn);
	            customInfoAdapter = new SmCustomInfoListAdapter(this.getActivity()); 
	            
	            //隐藏按钮非表示
	            mCustomInfoHide.setVisibility(View.GONE);
	            
	            //客户信息更多按钮监听事件
	            mCustomInfoMore.setOnClickListener(new OnClickListener() {
	                
	                @Override
	                public void onClick(View v) {
	                    custFlag = !custFlag;
	                    mCustomInfoHide.setVisibility(View.VISIBLE);
	                    mCustomInfoMore.setVisibility(View.GONE);
	                    displayProject(custFlag,carFlag);
	                    
	                }
	            });
	            
	            //客户信息隐藏按钮监听事件
	            mCustomInfoHide.setOnClickListener(new OnClickListener() {
	                
	                @Override
	                public void onClick(View v) {
	                    custFlag = !custFlag;
	                    mCustomInfoHide.setVisibility(View.GONE);
	                    mCustomInfoMore.setVisibility(View.VISIBLE);
	                    displayProject(custFlag,carFlag);
	                    
	                }
	            });
	        }
		carInfoAdapter = new SmCarInfoListAdapter(this.getActivity());
		
		// 隐藏按钮非表示
		mCarInfoHide.setVisibility(View.GONE);
		
		// 购车信息更多按钮的监听事件
		mCarInfoMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				carFlag = !carFlag;
				mCarInfoHide.setVisibility(View.VISIBLE);
				mCarInfoMore.setVisibility(View.GONE);
				displayProject(custFlag,carFlag);
			}
		});
		
		// 购车信息隐藏按钮的监听事件
		mCarInfoHide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				carFlag = !carFlag;
				mCarInfoHide.setVisibility(View.GONE);
				mCarInfoMore.setVisibility(View.VISIBLE);
				displayProject(custFlag,carFlag);
			}
		});
		displayProject(false, false);
		return view;
	}
	
	/**
	 * 
	 * <pre>
	 * 显示销售线索信息
	 * </pre>
	 *
	 * @param custFlag 客户信息是否显示更多
	 * @param carFlag 车辆信息是否显示更多
	 */
	public void displayProject(boolean custFlag,boolean carFlag){
	    if(!custFlag){
	        if(!isFromCustManager){
	            customInfoAdapter.clearData();
	            mCustomInfo.removeAllViews();
	        customInfoAdapter.addItem(getString(R.string.custName), project != null ? project.getCustomer().getCustName() : "");
	        customInfoAdapter.addItem(getString(R.string.custFrom), project != null ? project.getCustomer().getCustFrom() : "");
	        customInfoAdapter.addItem(getString(R.string.custType), project != null ? project.getCustomer().getCustType() : "");  
	        customInfoAdapter.addItem(getString(R.string.infoFrom), project != null ? project.getCustomer().getInfoFrom() : "");
	        customInfoAdapter.addItem(getString(R.string.custMobile), project != null ? project.getCustomer().getCustMobile() : "");
	        customInfoAdapter.addItem(getString(R.string.custOtherPhone), project != null ? project.getCustomer().getCustOtherPhone() : "");
	        customInfoAdapter.addItem(getString(R.string.comment), project != null ? project.getCustomer().getCustComment() : "");
	        }
	    }else{
            if(!isFromCustManager){
	        customInfoAdapter.clearData();
	        mCustomInfo.removeAllViews();
	        customInfoAdapter.addItem(getString(R.string.custName), project != null ? project.getCustomer().getCustName() : "");
	        customInfoAdapter.addItem(getString(R.string.custFrom), project != null ? project.getCustomer().getCustFrom() : "");
	        customInfoAdapter.addItem(getString(R.string.custType), project != null ? project.getCustomer().getCustType() : "");  
	        customInfoAdapter.addItem(getString(R.string.infoFrom), project != null ? project.getCustomer().getInfoFrom() : "");
	        customInfoAdapter.addItem(getString(R.string.custMobile), project != null ? project.getCustomer().getCustMobile() : "");
	        customInfoAdapter.addItem(getString(R.string.custOtherPhone), project != null ? project.getCustomer().getCustOtherPhone() : "");
	        customInfoAdapter.addItem(getString(R.string.gender), project != null ? project.getCustomer().getGender() : "");
	        customInfoAdapter.addItem(getString(R.string.birthday), project != null ? DateFormatUtils.formatDate(project.getCustomer().getBirthday()) : null);
	        customInfoAdapter.addItem(getString(R.string.idType), project != null ? project.getCustomer().getIdType() : "");
	        customInfoAdapter.addItem(getString(R.string.idNumber), project != null ? project.getCustomer().getIdNumber() : "");
	        customInfoAdapter.addItem(getString(R.string.province), project != null ? project.getCustomer().getProvince() : "");
	        customInfoAdapter.addItem(getString(R.string.city), project != null ? project.getCustomer().getCity() : "");
	        customInfoAdapter.addItem(getString(R.string.district), project != null ? project.getCustomer().getDistrict() : "");
	        customInfoAdapter.addItem(getString(R.string.qq), project != null ? project.getCustomer().getQq() : "");
	        customInfoAdapter.addItem(getString(R.string.address), project != null ? project.getCustomer().getAddress() : "");
	        customInfoAdapter.addItem(getString(R.string.postcode), project != null ? project.getCustomer().getPostcode() : "");
	        customInfoAdapter.addItem(getString(R.string.email), project != null ? project.getCustomer().getEmail() : "");
	        customInfoAdapter.addItem(getString(R.string.convContactTime), project != null ? project.getCustomer().getConvContactTime() : "");
	        customInfoAdapter.addItem(getString(R.string.expectContactWay), project != null ? project.getCustomer().getExpectContactWay() : "");
	        customInfoAdapter.addItem(getString(R.string.fax), project != null ? project.getCustomer().getFax() : "");
	        customInfoAdapter.addItem(getString(R.string.existingCar), project != null ? project.getCustomer().getExistingCar() : "");
	        customInfoAdapter.addItem(getString(R.string.industry), project != null ? project.getCustomer().getIndustry() : "");
	        customInfoAdapter.addItem(getString(R.string.position), project != null ? project.getCustomer().getPosition() : "");
	        customInfoAdapter.addItem(getString(R.string.education), project != null ? project.getCustomer().getEducation() : "");
	        customInfoAdapter.addItem(getString(R.string.existingcarbrand), project != null ? project.getCustomer().getExistingCarBrand() : "");
	        customInfoAdapter.addItem(getString(R.string.custInterest1), project != null ? project.getCustomer().getCustInterest1() : "");
	        customInfoAdapter.addItem(getString(R.string.custInterest2), project != null ? project.getCustomer().getCustInterest2() : "");
	        customInfoAdapter.addItem(getString(R.string.custInterest3), project != null ? project.getCustomer().getCustInterest3() : "");
	        customInfoAdapter.addItem(getString(R.string.existLisenPlate), project != null ? project.getCustomer().getExistLisenPlate() : "");
	        customInfoAdapter.addItem(getString(R.string.enterpType), project != null ? project.getCustomer().getEnterpType() : "");
	        customInfoAdapter.addItem(getString(R.string.enterpPeopleCount), project != null ? project.getCustomer().getEnterpPeopleCount() : "");
	        customInfoAdapter.addItem(getString(R.string.registeredCapital), project != null ? project.getCustomer().getRegisteredCapital() : "");
	        customInfoAdapter.addItem(getString(R.string.compeCarModel), project != null ? project.getCustomer().getCompeCarModel() : "");
	        customInfoAdapter.addItem(getString(R.string.rebuyStoreCustTag), project != null ? String.valueOf(project.getCustomer().getRebuyStoreCustTag()) : "false");
	        customInfoAdapter.addItem(getString(R.string.rebuyOnlineCustTag), project != null ? String.valueOf(project.getCustomer().getRebuyOnlineCustTag()) : "false");
	        customInfoAdapter.addItem(getString(R.string.changeCustTag), project != null ? String.valueOf(project.getCustomer().getChangeCustTag()) : "false");
	        customInfoAdapter.addItem(getString(R.string.regularCustTag), project != null ? String.valueOf(project.getCustomer().getRegularCustTag()) : "false");
	        customInfoAdapter.addItem(getString(R.string.regularCust), project != null ? project.getCustomer().getRegularCust() : "");
	        customInfoAdapter.addItem(getString(R.string.loanCustTag), project != null ? String.valueOf(project.getCustomer().getLoanCustTag()) : "false");
	        customInfoAdapter.addItem(getString(R.string.headerQuartCustTag), project != null ? String.valueOf(project.getCustomer().getHeaderQuartCustTag()): "false");
	        customInfoAdapter.addItem(getString(R.string.bigCustTag), project != null ? String.valueOf(project.getCustomer().getBigCustTag()) : "false");
	        customInfoAdapter.addItem(getString(R.string.bigCusts), project != null ? project.getCustomer().getBigCusts() : "");
	        customInfoAdapter.addItem(getString(R.string.comment), project != null ? project.getCustomer().getCustComment() : "");
            }
	    }
	    //客户管理下显示全部购车信息
	    if(isFromCustManager){
	        carFlag = true;
	        mCarInfoMore.setVisibility(View.GONE);
	        mCarInfoHide.setVisibility(View.GONE);
	    }
	    if(!carFlag){
	        carInfoAdapter.clearData();
	        mCarInfo.removeAllViews();
	        carInfoAdapter.addItem(getString(R.string.brand_1), project != null ? project.getPurchaseCarIntention().getBrand() : "", project != null ? project.getPurchaseCarIntention().getBrandCode() : null);
	        carInfoAdapter.addItem(getString(R.string.model), project != null ? project.getPurchaseCarIntention().getModel() : "", project != null ? project.getPurchaseCarIntention().getModelCode() : null);
	       //根据流程状态判断显示
	        if( project!=null && !project.getPurchaseCarIntention().isGiveupTag()  && getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus())){
	            carInfoAdapter.addItem(getString(R.string.finish_preorderDate), project != null ? DateFormatUtils.formatDate(project.getPurchaseCarIntention().getPreorderDate()) : DataVerify.systemDate());
	        } if(project!=null && project.getPurchaseCarIntention().isGiveupTag()){
	            carInfoAdapter.addItem(getString(R.string.lose_date), project != null ? DateFormatUtils.formatDate(project.getPurchaseCarIntention().getPreorderDate()) : null);
	        }else{
	            carInfoAdapter.addItem(getString(R.string.preorderDate), project != null ? DateFormatUtils.formatDate(project.getPurchaseCarIntention().getPreorderDate()) : null);
	        }
	        carInfoAdapter.addItem(getString(R.string.flowStatus), project != null ? project.getPurchaseCarIntention().getFlowStatus() : getString(R.string.flowStatus_2), project != null ? project.getPurchaseCarIntention().getFlowStatusCode() : null);
	        carInfoAdapter.addItem(""+getString(R.string.purchasecarintention_comment), project != null ? project.getPurchaseCarIntention().getProjectComment() : "");
	    }else{
	        carInfoAdapter.clearData();
            mCarInfo.removeAllViews();
	        carInfoAdapter.addItem(getString(R.string.brand_1), project != null ? project.getPurchaseCarIntention().getBrand() : "", project != null ? project.getPurchaseCarIntention().getBrandCode() : null);
	        carInfoAdapter.addItem(getString(R.string.model), project != null ? project.getPurchaseCarIntention().getModel() : "", project != null ? project.getPurchaseCarIntention().getModelCode() : null);
	        carInfoAdapter.addItem(getString(R.string.outsideColor), project != null ? project.getPurchaseCarIntention().getOutsideColor() : "", project != null ? project.getPurchaseCarIntention().getOutsideColorCode() : null);
	        carInfoAdapter.addItem(getString(R.string.insideColor), project != null ? project.getPurchaseCarIntention().getInsideColor() : "", project != null ? project.getPurchaseCarIntention().getInsideColorCode() : null);
	        if(!isFromCustManager){
	            carInfoAdapter.addItem(getString(R.string.insideColorCheck), project != null ? String.valueOf(project.getPurchaseCarIntention().isInsideColorCheck()) : "false");
	        }
	        carInfoAdapter.addItem(getString(R.string.carConfiguration), project != null ? project.getPurchaseCarIntention().getCarConfiguration() : "", project != null ? project.getPurchaseCarIntention().getCarConfigurationCode() : null);
	        carInfoAdapter.addItem(getString(R.string.salesQuote), project != null ? project.getPurchaseCarIntention().getSalesQuote() : "");
	        carInfoAdapter.addItem(getString(R.string.dealPriceInterval), project != null ? project.getPurchaseCarIntention().getDealPriceInterval() : "", project != null ? project.getPurchaseCarIntention().getDealPriceIntervalCode() : null);
	        carInfoAdapter.addItem(getString(R.string.payment), project != null ? project.getPurchaseCarIntention().getPayment() : "", project != null ? project.getPurchaseCarIntention().getPaymentCode() : null);
	        carInfoAdapter.addItem(getString(R.string.preorderCount), project != null ? project.getPurchaseCarIntention().getPreorderCount() : "1");
	        //根据流程状态判断显示
	        if(project!=null && getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus())){
	            carInfoAdapter.addItem(getString(R.string.finish_preorderDate), project != null ? DateFormatUtils.formatDate(project.getPurchaseCarIntention().getPreorderDate()) : DataVerify.systemDate());
	        }else if(project!=null && project.getPurchaseCarIntention().isGiveupTag()){
	            carInfoAdapter.addItem(getString(R.string.lose_date), project != null ? DateFormatUtils.formatDate(project.getPurchaseCarIntention().getPreorderDate()) : null);
	        }else{
	            carInfoAdapter.addItem(getString(R.string.preorderDate), project != null ? DateFormatUtils.formatDate(project.getPurchaseCarIntention().getPreorderDate()) : null);
	        }
	        carInfoAdapter.addItem(getString(R.string.flowStatus), project != null ? project.getPurchaseCarIntention().getFlowStatus() : getString(R.string.flowStatus_2), project != null ? project.getPurchaseCarIntention().getFlowStatusCode() : null);
	        carInfoAdapter.addItem(getString(R.string.dealPossibility), project != null ? project.getPurchaseCarIntention().getDealPossibility() : "0.05");
	        carInfoAdapter.addItem(getString(R.string.purchMotivation), project != null ? project.getPurchaseCarIntention().getPurchMotivation() : "", project != null ? project.getPurchaseCarIntention().getPurchMotivationCode() : null);
	        carInfoAdapter.addItem(getString(R.string.chassisNo), project != null ? project.getPurchaseCarIntention().getChassisNo() : "");
	        carInfoAdapter.addItem(getString(R.string.engineNo), project != null ? project.getPurchaseCarIntention().getEngineNo() : "");
	        carInfoAdapter.addItem(getString(R.string.licensePlate), project != null ? project.getPurchaseCarIntention().getLicensePlate() : "");
	       
	        carInfoAdapter.addItem(getString(R.string.licenseProp), project != null ? project.getPurchaseCarIntention().getLicenseProp() : "", project != null ? project.getPurchaseCarIntention().getLicensePropCode() : null);
	        carInfoAdapter.addItem(getString(R.string.pickupDate), project != null ? ( "0".equals(project.getPurchaseCarIntention().getPickupDate()) || project.getPurchaseCarIntention().getPickupDate()==null? "" :DateFormatUtils.formatDate(project.getPurchaseCarIntention().getPickupDate())): "");
	        carInfoAdapter.addItem(getString(R.string.preorderTag), project != null ? project.getPurchaseCarIntention().getPreorderTag() : "");
	        carInfoAdapter.addItem(getString(R.string.giveupTag), project != null ? String.valueOf(project.getPurchaseCarIntention().isGiveupTag()) : "false");
	        carInfoAdapter.addItem(getString(R.string.giveupReason), project != null ? project.getPurchaseCarIntention().getGiveupReason() : "");
	        carInfoAdapter.addItem(getString(R.string.invoiceTitle), project != null ? project.getPurchaseCarIntention().getInvoiceTitle() : "");
	        carInfoAdapter.addItem(""+getString(R.string.purchasecarintention_comment), project != null ? project.getPurchaseCarIntention().getProjectComment() : "");
	    }
        if(!isFromCustManager){
            for(int i =0;i<customInfoAdapter.getCount();i++){
                mCustomInfo.addView(customInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mCustomInfo.addView(dividerView);
            }
        }
	    for(int i =0;i<carInfoAdapter.getCount();i++){
	        mCarInfo.addView(carInfoAdapter.getView(i, null, null));
	        View dividerView = new View(getActivity());
            dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
            dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
            mCarInfo.addView(dividerView);
        }
	    
        
	}
	

	/**
	 * 
	 * <pre>
	 * 设置列表中每条item的分割线
	 * </pre>
	 *
	 */
	private void setNewListHidden()
	{
		ViewGroup.LayoutParams params = mNewFollowListView.getLayoutParams();
        params.height = 0;
        mNewFollowListView.setLayoutParams(params);
	}
	
	/**
	 * 
	 * <pre>
	 * 对project赋值
	 * </pre>
	 * 在执行页面跳转时候条用此方法
	 * @param p 销售线索
	 */
	public void setProject(Project p){
		project = p;
	}
	
	/**
	 * 
	 * <pre>
	 * 列表显示信息
	 * </pre>
	 *
	 * @param listView 列表
	 * @param listAdapter 列表的适配器
	 */
	public void setListViewHeightBasedOnChildren(ListView listView,BaseAdapter listAdapter) {

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight(); 
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
	}
	/**
	 * 
	 * <pre>
	 * 从客户管理进入只显示购车信息
	 * </pre>
	 *
	 */
	public void comeFromCustMamager(){
	    mCustominfoTitle.setVisibility(View.GONE);
	    mCustomInfo.setVisibility(View.GONE);
	}
}

