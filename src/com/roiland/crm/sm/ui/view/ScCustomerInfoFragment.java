package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客户管理客户信息Fragment
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScCustomerInfoFragment.java, v 0.1 2013-7-16 上午9:33:51 shuang.gao Exp $
 */
public class ScCustomerInfoFragment extends SherlockFragment {
    private final static String tag = Log.getTag(ScCustomerInfoFragment.class);
	private BasicInfoListAdapter customerInfoAdapter;
    private List<BasicInfoListAdapter.Info> customerInfoCaches;//客户信息用于显示更多后隐藏校验缓存
	private List<BasicInfoListAdapter.Info> customerInfo;
	private Customer customer;
	private boolean isError = false; //是否通过校验 false 通过
	private ScCustomerInfoActivity activity;
	private Boolean isDetail;  //是否详细标识
    String orderId = "";
    String orderStatus = "";
    String address = "";         
    String province = "";
    String city = "";
    String district = "";
	private LinearLayout customerInfoList;
	public CRMManager crmManager;
	private ImageButton MoreBtn;
	private ImageButton HideBtn;
	public boolean editFlag = false;
	private BottomBar bbar;
	public Boolean getDataState = false;
	public String[] addressInfoList;
	MenuItem item;
	
	BasicInfoListAdapter.Info idnumberInfo;
	BasicInfoListAdapter.Info birthdayInfo;
	BasicInfoListAdapter.Info idtypeInfo;
	BasicInfoListAdapter.Info bigCustInfo;
	BasicInfoListAdapter.Info bigCustSelectInfo;
	BasicInfoListAdapter.Info oldCustInfo;
	BasicInfoListAdapter.Info oldCustSelectInfo;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressInfoList = new String[] { getString(R.string.province), getString(R.string.city),
                                         getString(R.string.district) };
        if(customer != null){
        	province = customer.getProvince();
        	city = customer.getCity();
        }
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//初期加载控件UI
		View view = inflater.inflate(R.layout.sc_customer_info, null, true);
		isDetail = false;
		customerInfoList = (LinearLayout)(view.findViewById(R.id.customer_info_list));
		if (customerInfoAdapter == null)
			customerInfoAdapter = new BasicInfoListAdapter(this.getActivity());
		customerInfoAdapter.crmManager = this.crmManager;
		
		MoreBtn = (ImageButton)view.findViewById(R.id.customer_info_more_button);
		HideBtn = (ImageButton)view.findViewById(R.id.customer_info_hide_button);
		view.findViewById(R.id.customer_info_hide_button).setVisibility(View.GONE);
		//更多按钮的点击监听事件
		((ImageButton)(MoreBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				isDetail = !isDetail;
				customer = getUpdatedCustomer();
				displayCustomerInfo(isDetail);
				MoreBtn.setVisibility(View.GONE);
				HideBtn.setVisibility(View.VISIBLE);
				customerInfoAdapter.setEditable(customerInfoAdapter.getEditable());
			}
		});
		//隐藏按钮的点击监听事件
		((ImageButton)(HideBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				isDetail = !isDetail;
				customer = getUpdatedCustomer();
				displayCustomerInfo(isDetail);
				MoreBtn.setVisibility(View.VISIBLE);
				HideBtn.setVisibility(View.GONE);
				customerInfoAdapter.setEditable(customerInfoAdapter.getEditable());
			}
		});
		//载入客户详细信息
		displayCustomerInfo(isDetail);
		return view;
	}
	
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
	 * 
	 * <pre>
	 * 向Adapter添加数据
	 * </pre>
	 *
	 * @param isDetail 是否点击更多
	 */
	
	public void displayCustomerInfo(Boolean isDetail) {
		if (!getDataState)
			return;
		if (customerInfo == null){
			customerInfo = new ArrayList<BasicInfoListAdapter.Info>();
		
		}
		BasicInfoListAdapter.Info temp;
		//如果没点击更多按钮
		if (!isDetail) {
			customerInfo.clear();
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custName), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getCustName() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custMobile), BaseInfoRowViewItem.MOBILETEXT_TYPE, null, (customer != null ? customer.getCustMobile() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custOtherPhone), BaseInfoRowViewItem.PSTNTEXT_TYPE, null, (customer != null ? customer.getCustOtherPhone() : null),false,InputType.TYPE_CLASS_NUMBER));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCustFrom() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custType), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCustType() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.infoFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getInfoFrom() : null),true));
//            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.gender), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"客户性别1", "客户性别2", "客户性别3"}, (customer != null ? customer.getGender() : null),false));
            customerInfo.add(idtypeInfo = new BasicInfoListAdapter.Info(getString(R.string.idType), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"证件类型1", "证件类型2", "证件类型3"}, (customer != null ? customer.getIdType() : null),false));
            customerInfo.add(idnumberInfo = new BasicInfoListAdapter.Info(getString(R.string.idNumber), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getIdNumber() : null),false));
			idnumberInfo.textChangeListener = new BasicInfoListAdapter.Info.TextChangeListener() {
				//身份证号同生日联动
				@Override
				public void textChanged(String key, String value) {
					
					try {
						if (!idtypeInfo.value.equals("身份证")) return;						
						customer.setBirthday(DateFormatUtils.getBirthFromId(value));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.address), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getAddress() : null),false));
            customerInfo.add(temp = new BasicInfoListAdapter.Info(getString(R.string.dormancy), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? String.valueOf(customer.getDormancy()) : null),false));
          //购车客户不能休眠
            if(customer != null && customer.getCustStatus() !=null && customer.getCustStatus().equals(getString(R.string.purchasecar_status)))
            temp.editable = false;
            //如果点击休眠。
            temp.dataChangeListener = new BasicInfoListAdapter.Info.DataChangeListener() {
                @Override
                public void chagedData(String key, String value) {
                    
                    if (customer == null) return;
                    
                    if ("true".equals(value)) {
                  	  //取消提示是否放弃销售线索 直接放弃
                  	  customer.setAbandonProject(true);
                    }
                    else {
                        customer.setAbandonProject(customer.isAbandonProject());
                    }
                }
            };
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.comment), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getCustComment() : null),false));
		} else {
			customerInfo.clear();
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custName), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getCustName() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custMobile), BaseInfoRowViewItem.MOBILETEXT_TYPE, null, (customer != null ? customer.getCustMobile() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custOtherPhone), BaseInfoRowViewItem.PSTNTEXT_TYPE, null, (customer != null ? customer.getCustOtherPhone() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCustFrom() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custType), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCustType() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.infoFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getInfoFrom() : null),true));
            customerInfo.add(idtypeInfo = new BasicInfoListAdapter.Info(getString(R.string.idType), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"证件类型1", "证件类型2", "证件类型3"}, (customer != null ? customer.getIdType() : null),false));
            customerInfo.add(idnumberInfo = new BasicInfoListAdapter.Info(getString(R.string.idNumber), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getIdNumber() : null),false));			
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.address), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getAddress() : null),false));
              customerInfo.add(temp = new BasicInfoListAdapter.Info(getString(R.string.dormancy), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? String.valueOf(customer.getDormancy()) : null),false));
              //购车客户不能休眠
              if(customer != null && customer.getCustStatus() !=null && customer.getCustStatus().equals(getString(R.string.purchasecar_status)))
              temp.editable = false;
              //如果点击休眠。
              temp.dataChangeListener = new BasicInfoListAdapter.Info.DataChangeListener() {
                  @Override
                  public void chagedData(String key, String value) {
                      
                      if (customer == null) return;
                      
                      if ("true".equals(value)) {
                    	  //取消提示是否放弃销售线索 直接放弃
                    	  customer.setAbandonProject(true);
                      }
                      else {
                            customer.setAbandonProject(customer.isAbandonProject());
                      }
                  }
              };
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.gender), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"客户性别1", "客户性别2", "客户性别3"}, (customer != null ? customer.getGender() : null),false));
            customerInfo.add(birthdayInfo = new BasicInfoListAdapter.Info(getString(R.string.birthday), BaseInfoRowViewItem.DATE_TYPE, null, (customer != null ? customer.getBirthday() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.province), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"省区1", "省区2", "省区3"}, (customer != null ? customer.getProvince() : null),false));
            if(customer != null && StringUtils.isEmpty(customer.getCity())){
                
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.city), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"市县1", "市县2", "市县3"}, (customer != null ? customer.getCity() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.district), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"行政区划1", "行政区划2", "行政区划3"}, (customer != null ? customer.getDistrict() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.qq), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getQq() : null),false,InputType.TYPE_CLASS_NUMBER));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.postcode), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getPostcode() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.email), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getEmail() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.convContactTime), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getConvContactTime() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.expectContactWay), BaseInfoRowViewItem.SELECTION_TYPE,null, (customer != null ? customer.getExpectContactWay() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.fax), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getFax() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.existingCar), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getExistingCar() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.industry), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getIndustry() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.position), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getPosition() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.education), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getEducation() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.existingcarbrand), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getExistingCarBrand() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custInterest1), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCustInterest1() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custInterest2), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCustInterest2() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custInterest3), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCustInterest3() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.existLisenPlate), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getExistLisenPlate() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.enterpType), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getEnterpType() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.enterpPeopleCount), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getEnterpPeopleCount() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.registeredCapital), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getRegisteredCapital() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.compeCarModel), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getCompeCarModel() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.rebuyStoreCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? (customer.getRebuyStoreCustTag() ? "true" : "false") : null),false,false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.rebuyOnlineCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? (customer.getRebuyOnlineCustTag() ? "true" : "false") : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.changeCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? (customer.getChangeCustTag() ? "true" : "false") : null),false));
            customerInfo.add(oldCustInfo = new BasicInfoListAdapter.Info(getString(R.string.regularCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? (customer.getRegularCustTag() ? "true" : "false") : null),false));
            //判断老客户选择状态
            if ("true".equals(oldCustInfo.value)) {
                customerInfo.add(oldCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.regularCust), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getRegularCust() : null),true));
            } else {
                customerInfo.add(oldCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.regularCust), BaseInfoRowViewItem.SELECTION_TYPE, null, (customer != null ? customer.getRegularCust() : null),false, false));
            }
            
            oldCustInfo.dataChangeListener = new BasicInfoListAdapter.Info.DataChangeListener() {
                //老客户推荐同老客户选择联动
                @Override
                public void chagedData(String key, String value) {
                    
                    if ("true".equals(value)) {
                        oldCustSelectInfo.required = true;
                        oldCustSelectInfo.editable = true;
                    } else {
                        oldCustSelectInfo.required = false;
                        oldCustSelectInfo.editable = false;
                        oldCustSelectInfo.pairKey = null;
                        oldCustSelectInfo.value = null;
                    }
                    customerInfoAdapter.getView(38, null, null);
                }
            };
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.loanCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? (customer.getLoanCustTag() ? "true" : "false") : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.headerQuartCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? (customer.getHeaderQuartCustTag() ? "true" : "false") : null),false));
            customerInfo.add(bigCustInfo = new BasicInfoListAdapter.Info(getString(R.string.bigCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (customer != null ? (customer.getBigCustTag() ? "true" : "false") : null),false));
            //判断大客户选择状态
            if ("true".equals(bigCustInfo.value)) {
                customerInfo.add(bigCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.bigCusts), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"大客户选择1", "大客户选择2", "大客户选择3"}, (customer != null ? customer.getBigCusts() : null),true));
            } else {
                customerInfo.add(bigCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.bigCusts), BaseInfoRowViewItem.SELECTION_TYPE, new String[] {"大客户选择1", "大客户选择2", "大客户选择3"}, (customer != null ? customer.getBigCusts() : null),false, false));
            }
            bigCustInfo.dataChangeListener = new BasicInfoListAdapter.Info.DataChangeListener() {
                //大客户推荐同大客户选择联动
                @Override
                public void chagedData(String key, String value) {
                    
                    if ("true".equals(value)) {
                        bigCustSelectInfo.required = true;
                        bigCustSelectInfo.editable = true;
                    } else {
                        bigCustSelectInfo.required = false;
                        bigCustSelectInfo.editable = false;
                        bigCustSelectInfo.pairKey = null;
                        bigCustSelectInfo.value = null;
                    }
                    customerInfoAdapter.getView(42, null, null);
                }
            };
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.comment), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (customer != null ? customer.getCustComment() : null),false));
            customerInfoCaches = new ArrayList<BasicInfoListAdapter.Info>();
            customerInfoCaches.addAll(customerInfo);
        }
        customerInfoAdapter.setContentList(customerInfo);
        customerInfoAdapter.notifyDataSetChanged();
		refreshList();
	}
	/**
	 * 更新客户管理中的客户信息
	 */
	@SuppressWarnings("deprecation")
	public void refreshList() {
		if (customerInfoList != null && customerInfoAdapter != null) {
			customerInfoList.removeAllViews();
			for (int i = 0; i < customerInfoAdapter.getCount(); i ++) {
				customerInfoList.addView(customerInfoAdapter.getView(i, null, null));
				View dividerView = new View(getActivity());
				dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
				dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
				customerInfoList.addView(dividerView);
			}
			if(customer != null && isDetail){
                BaseInfoRowViewItem item = (BaseInfoRowViewItem)customerInfoList.findViewWithTag(getString(R.string.city));
                item.setParentKey(customer.getProvinceCode());
            }
            if(customer != null && isDetail){
                BaseInfoRowViewItem item = (BaseInfoRowViewItem)customerInfoList.findViewWithTag(getString(R.string.district));
                item.setParentKey(customer.getProvinceCode());
                item.setParentKey2(customer.getCityCode());
            }
		}
	}
	/**
     * 
     * @param menu
     * @param inflater
     * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        item = menu.findItem(R.id.edit);
        item.setVisible(true);  
       
    }
    
    
    
    /**
     * 
     * @param item
     * @return
     * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(tag, ""+item.getItemId());
        switch(item.getItemId()){
            case R.id.edit:
                Log.i(tag, ""+item.getItemId());
                bbar.setVisible(false);
                if(editFlag){
                    activity = (ScCustomerInfoActivity) this.getActivity();
                    activity.saveCustomer();
                    if(!isError){
                        //通过校验 保存成功
                        item.setIcon(R.drawable.edit_btn_selector);
                        editFlag = false;
                    }
                }else{
                    item.setIcon(R.drawable.save_selector);
                    editFlag = true;
                    if (!this.getCustomerInfoAdapter().getEditable()) {
                        this.getCustomerInfoAdapter().setEditable(editFlag);
                    }
                }
                
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setError(boolean flag) {
        
        this.isError = flag;
        
    }
    
    public void setItemIcon(boolean editFlag){
        if(editFlag){
            item.setIcon(R.drawable.save_selector);
        }else{
            item.setIcon(R.drawable.edit_btn_selector);
        }
    }
    
    public void dataModify(String key, String value, String pairKey) {
        Log.i(tag, "-----"+key+"-----"+value+"-----"+pairKey+"-----");
        BaseInfoRowViewItem item;

        /**省区市县行政区划联动**/
        //省区
        if(getString(R.string.province).equals(key)){ 
        	province = value;
        	city = "";
        	district = "";
        	addAddress(province, city, district);
            for(int i=1;i<addressInfoList.length;i++){
                item = (BaseInfoRowViewItem)customerInfoList.findViewWithTag(addressInfoList[i]);
                item.setValue("");
                item.setParentKey(pairKey);
                customer.setCityCode("");
                customer.setDistrictCode("");
                for(BasicInfoListAdapter.Info info : customerInfo){
                    if(info.key.equals(addressInfoList[i])){
                        info.value = "";
                        info.pairKey = "";
                        break;
                    }
                }
            }
            
        }
        
        //市县
        if(getString(R.string.city).equals(key)){
        	city = value;
        	district = "";
        	addAddress(province, city, district);
            item = (BaseInfoRowViewItem)customerInfoList.findViewWithTag(getString(R.string.district));
            item.setValue("");
            item.setParentKey2(pairKey);
            customer.setDistrictCode("");
            for(BasicInfoListAdapter.Info info : customerInfo){
                if(info.key.equals(getString(R.string.district))){
                    info.value = "";
                    info.pairKey = "";
                }
            }
        }
        //行政区划
        if(getString(R.string.district).equals(key)){
        	district = value;
        	addAddress(province, city, district);
        }
        if(getString(R.string.dormancy).equals(key)){
            if("true".equals(value)){
                customer.setCustStatus(getString(R.string.dormancy));
            }else{
                customer.setCustStatus(getString(R.string.latency));
            }
        }
        
        /**生日联动**/
        if(customerInfoAdapter.getEditable()){
          //先选证件类型再输入证件号码
            if(getString(R.string.idNumber).equals(key)){
                item = (BaseInfoRowViewItem) customerInfoList.findViewWithTag(getString(R.string.idType));
                if(getString(R.string.id_card).equals(item.txtValue.getText().toString())){
                    BaseInfoRowViewItem itemBirthday = (BaseInfoRowViewItem) customerInfoList.findViewWithTag(getString(R.string.birthday));
                    if(DataVerify.personIdValid(value)){
                    	if(itemBirthday == null){
                    		customer.setBirthday(DateFormatUtils.getBirthFromId(value));
                    	}else{
                    		itemBirthday.setValue(DateFormatUtils.getBirthFromId(value));
                            customer.setBirthday(DateFormatUtils.getBirthFromId(value));
                            for(BasicInfoListAdapter.Info info : customerInfo){
                                if(info.key.equals(getString(R.string.birthday))){
                                    info.value = DateFormatUtils.getBirthFromId(value);
                                    break;
                                }
                            }
                    	}
                    }
                }
            }
        }
       
        
        //先输入证件号码再选证件类型
        if(getString(R.string.idType).equals(key)){
            item = (BaseInfoRowViewItem) customerInfoList.findViewWithTag(getString(R.string.idType));
            if(getString(R.string.id_card).equals(item.txtValue.getText().toString())){
                BaseInfoRowViewItem itemBirthday = (BaseInfoRowViewItem) customerInfoList.findViewWithTag(getString(R.string.birthday));
                BaseInfoRowViewItem itemIdNumber = (BaseInfoRowViewItem) customerInfoList.findViewWithTag(getString(R.string.idNumber));
                if(DataVerify.personIdValid(itemIdNumber.txtValue.getText().toString())){
                    if(DataVerify.personIdValid(itemIdNumber.txtValue.getText().toString())){
                    	if(itemBirthday == null){
                    		customer.setBirthday(DateFormatUtils.getBirthFromId(value));
                    	}else{
                    		itemBirthday.setValue(DateFormatUtils.getBirthFromId(value));
                            customer.setBirthday(DateFormatUtils.getBirthFromId(value));
                            for(BasicInfoListAdapter.Info info : customerInfo){
                                if(info.key.equals(getString(R.string.birthday))){
                                    info.value = DateFormatUtils.getBirthFromId(value);
                                    break;
                                }
                            }
                    	}
                    }
                }
            }
        }
    }
    /**
     * 
     * <pre>
     * 联动客户地址
     * </pre>
     *
     */
    public void addAddress(String province,String city,String district){
        BaseInfoRowViewItem item = null;
        item = (BaseInfoRowViewItem)customerInfoList.findViewWithTag(getResources().getString(R.string.address));
        address = province + city +district;
        for(BasicInfoListAdapter.Info info : customerInfo){
        	item.setValue(address);
            if(info.key.equals(getResources().getString(R.string.address))){
                item.setValue(address);
                info.value = address;
                break;
            }
        }
    }   
    
    public BasicInfoListAdapter getCustomerInfoAdapter() {
		return customerInfoAdapter;
	}

	public void setCustomerInfoAdapter(BasicInfoListAdapter customerInfoAdapter) {
		this.customerInfoAdapter = customerInfoAdapter;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		if (getDataState)
			this.customer = customer;
	}

	public Boolean getIsDetail() {
		return isDetail;
	}

	public void setIsDetail(Boolean isDetail) {
		this.isDetail = isDetail;
	}
	/**
	 * 获得最新客户详细信息
	 * @return result 客户信息
	 */
	public Customer getUpdatedCustomer()
	{
		Customer result = new Customer();
		result = customer;
		
		for (BasicInfoListAdapter.Info info : customerInfo)
		{
			if (info.key.equals("客户名称"))
				result.setCustName(info.value);
			else if (info.key.equals("客户移动电话"))
				result.setCustMobile(info.value);
			else if (info.key.equals("客户其他电话"))
				result.setCustOtherPhone(info.value);
			else if (info.key.equals("客户来源") && info.pairKey != null){
				result.setCustFromCode(info.pairKey);
				result.setCustFrom(info.value);
			}else if (info.key.equals("客户类别") && info.pairKey != null){
				result.setCustTypeCode(info.pairKey);
				result.setCustType(info.value);
			}else if (info.key.equals("信息来源") && info.pairKey != null){
				result.setInfoFromCode(info.pairKey);
				result.setInfoFrom(info.value);
			}else if (info.key.equals("证件类型") && info.pairKey != null){
				result.setIdTypeCode(info.pairKey);
				result.setIdType(info.value);
			}else if (info.key.equals("证件号码"))
				result.setIdNumber(info.value);
			else if (info.key.equals("客户地址"))
				result.setAddress(info.value);
			else if (info.key.equals("休眠")){
				result.setDormancy(info.value.equals("true"));
			}else if (info.key.equals("性别") && info.pairKey != null){
				result.setGender(info.value);
				result.setGenderCode(info.pairKey);
			}else if (info.key.equals("生日"))
				result.setBirthday(info.value);
			else if (info.key.equals("省区")){
			    if(!StringUtils.isEmpty(info.pairKey))
					result.setProvinceCode(info.pairKey);
			    result.setProvince(info.value);
			}else if (info.key.equals("市县")){
			    if(!StringUtils.isEmpty(info.pairKey))
					result.setCityCode(info.pairKey);
			    result.setCity(info.value);
			}else if (info.key.equals("行政区划")){
			    if(!StringUtils.isEmpty(info.pairKey))
					result.setDistrictCode(info.pairKey);
			    result.setDistrict(info.value);
			}else if (info.key.equals("QQ号码"))
				result.setQq(info.value);
			else if (info.key.equals("邮编"))
				result.setPostcode(info.value);
			else if (info.key.equals("E-mail"))
				result.setEmail(info.value);
			else if (info.key.equals("方便联系时间") && info.pairKey != null){
				result.setConvContactTimeCode(info.pairKey);
				result.setConvContactTime(info.value);
			}else if (info.key.equals("希望联系方式") && info.pairKey != null){
				result.setExpectContactWayCode(info.pairKey);
				result.setExpectContactWay(info.value);
			}else if (info.key.equals("传真"))
				result.setFax(info.value);
			else if (info.key.equals("现用车") && info.pairKey != null){
				result.setExistingCarCode(info.pairKey);
				result.setExistingCar(info.value);
			}else if (info.key.equals("所处行业") && info.pairKey != null){
				result.setIndustryCode(info.pairKey);
				result.setIndustry(info.value);
			}else if (info.key.equals("职务") && info.pairKey != null){
				result.setPositionCode(info.pairKey);
				result.setPosition(info.value);
			}else if (info.key.equals("教育程度") && info.pairKey != null){
				result.setEducationCode(info.pairKey);
				result.setEducation(info.value);
			}else if (info.key.equals("现用车品牌")){
				result.setExistingCarBrand(info.value);
			}else if (info.key.equals("客户爱好1") && info.pairKey != null){
				result.setCustInterestCode1(info.pairKey);
				result.setCustInterest1(info.value);
			}else if (info.key.equals("客户爱好2") && info.pairKey != null){
				result.setCustInterestCode2(info.pairKey);
				result.setCustInterest2(info.value);
			}else if (info.key.equals("客户爱好3") && info.pairKey != null){
				result.setCustInterestCode3(info.pairKey);
				result.setCustInterest3(info.value);
			}else if (info.key.equals("现用车牌照号")){
				result.setExistLisenPlate(info.value);
			}else if (info.key.equals("企业性质") && info.pairKey != null){
				result.setEnterpTypeCode(info.pairKey);
				result.setEnterpType(info.value);
			}else if (info.key.equals("企业人数") && info.pairKey != null){
				result.setEnterpPeopleCountCode(info.pairKey);
				result.setEnterpPeopleCount(info.value);
			}else if (info.key.equals("注册资本") && info.pairKey != null){
				result.setRegisteredCapitalCode(info.pairKey);
				result.setRegisteredCapital(info.value);
			}else if (info.key.equals("竞争车型") && info.pairKey != null){
				result.setCompeCarModelCode(info.pairKey);
				result.setCompeCarModel(info.value);
			}else if (info.key.equals("本店重购客户"))
				result.setRebuyStoreCustTag(info.value.equals("true"));
			else if (info.key.equals("网络重购客户"))
				result.setRebuyOnlineCustTag(info.value.equals("true"));
			else if (info.key.equals("置换客户标识"))
				result.setChangeCustTag(info.value.equals("true"));
			else if (info.key.equals("老客户推荐标识"))
				result.setRegularCustTag(info.value.equals("true"));
			else if (info.key.equals("老客户选择") && info.pairKey != null){
				result.setRegularCustCode(info.pairKey);
				result.setRegularCust(info.value);
			}else if (info.key.equals("个贷客户标识"))
				result.setLoanCustTag(info.value.equals("true"));
			else if (info.key.equals("总部VIP客户"))
				result.setHeaderQuartCustTag(info.value.equals("true"));
			else if (info.key.equals("大客户"))
				result.setBigCustTag(info.value.equals("true"));
			else if (info.key.equals("大客户选择") && info.pairKey != null){
				result.setBigCustsCode(info.pairKey);
				result.setBigCusts(info.value);
			}else if (info.key.equals("备注"))
				result.setCustComment(info.value);
		}
		
		return result;
	}
    /**
     * 
     * <pre>
     * 客户信息校验
     * </pre>
     *
     * @return errString 提示信息
     */
    public String customInfoValidation() {
        int count;//循环次数
        String errString1 = null;//隐藏出现错误的提示信息
        String errString2 = null;//更多出现错误的提示信息
        String errString = null;//错误提示信息
        String temp = customerInfo.get(1).value;//移动电话
        String tempt = customerInfo.get(2).value;//其他电话
        String strTemp = null;//老客户
        String strTemp2 = null;//大客户
        if(customerInfoCaches != null){
            count = customerInfoCaches.size();
        }else{
            count = customerInfo.size();
        }
        for (int i = 0; i < count; i++) {
            if(i < customerInfo.size()){
                BasicInfoListAdapter.Info info = customerInfo.get(i);
                String item = info.key;
                if(customerInfoCaches != null){
                    //此处校验老客户和大客户选择用
                    BasicInfoListAdapter.Info infoCaches = customerInfoCaches.get(i);
                    String itemCaches = infoCaches.key;
                    if (itemCaches.equals(getString(R.string.regularCustTag))) {
                        strTemp = infoCaches.value;
                    }
                    if (itemCaches.equals(getString(R.string.bigCustTag))) {
                        strTemp2 = infoCaches.value;
                    }
                }
                errString1 = DataVerify.infoValidation(item, info.value, orderId,
                    orderStatus, strTemp, strTemp2, getActivity(), false, false);
                if (item.equals(getString(R.string.custOtherPhone))) {
                    if (StringUtils.isEmpty(temp) && StringUtils.isEmpty(tempt)) {
                        errString1 = getString(R.string.dataverify_phonenumber_musthave);
                    }
                }
            }else{
                BasicInfoListAdapter.Info infoCaches = customerInfoCaches.get(i);
                String itemCaches = infoCaches.key;
                if (itemCaches.equals(getString(R.string.regularCustTag))) {
                    strTemp = infoCaches.value;
                }
                if (itemCaches.equals(getString(R.string.bigCustTag))) {
                    strTemp2 = infoCaches.value;
                }
                errString2 = DataVerify.infoValidation(itemCaches, infoCaches.value, orderId,
                    orderStatus, strTemp, strTemp2, getActivity(), false, false);
            }
            if (!StringUtils.isEmpty(errString1)) {
                errString = errString1;
                errString1 = "";
                break;
            }else if(!StringUtils.isEmpty(errString2)){
                errString = errString2;
                errString2 = "";
                break;
            }
        }
        orderId = "";
        orderStatus = "";
        return errString;
    }
    public void setBbar(BottomBar bbar) {
        this.bbar = bbar;
    }
    
}
