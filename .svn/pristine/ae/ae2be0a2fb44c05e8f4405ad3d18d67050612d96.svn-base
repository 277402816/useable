package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.roiland.crm.sm.ui.adapter.SmCustomerInfoItemAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 客户管理客户信息Fragment
 * </pre>
 * @extends Fragment
 * @author zhaojiaqi
 * @version $Id: SmCustomerInfoFragment.java, v 0.1 2013-5-21 上午10:09:29 zhaojiaqi Exp $
 */
public class SmCustomerInfoFragment extends SherlockFragment {
    private static String tag = SmCustomerInfoFragment.class.getName();
	private SmCustomerInfoItemAdapter customerInfoAdapter;
	private List<BasicInfoListAdapter.Info> customerInfo;
	private Customer customer;
	private SmCustomerListFragment smCustomerListFragment;
	List<String[]> custmerInfo;
	//是否详细标识
	private Boolean isDetail = false;
	private LinearLayout customerInfoList;
	public CRMManager crmManager;
	private ImageButton MoreBtn;
	private ImageButton HideBtn;
    protected BaseActivity    activity;
	

   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        Log.i(tag, "-------------------------------onCreate----------------");
    }

    @Override
    public void onResume() {
        super.onResume();
        displayCustomerInfo(isDetail);
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    //初期加载控件UI
		View view = inflater.inflate(R.layout.sm_customer_info_view, null, true);
		isDetail = false;
		customerInfoList = (LinearLayout)(view.findViewById(R.id.customer_list));
		displayCustomerInfo(isDetail);
		if (customerInfoAdapter == null)
            customerInfoAdapter = new SmCustomerInfoItemAdapter(getActivity());
		for(int i=0;i<customerInfoAdapter.getCount();i++){
		    customerInfoList.addView(customerInfoAdapter.getView(i, null, null));
		}
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
			}
		});
		return view;
	}
	
	

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        MenuItem distriItem = menu.findItem(R.id.assign);
        distriItem.setVisible(true);
        Log.i(tag, "-------------creatMenu----------------");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.assign:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("customer",customer);
                //将传递的数据放入intent
                intent.putExtras(bundle);
                intent.setClass(getActivity(), SmCustomerOwenrListActivity.class);
                startActivityForResult(intent,0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
	 * 
	 * <pre>
	 * 添加数据 用于页面显示
	 * </pre>
	 *
	 * @param isDetail 是否点击更多
	 */
	public void displayCustomerInfo(Boolean isDetail) {
	    BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
	    List<String[]> custmerInfo = new ArrayList<String[]>();
		if (customerInfo == null){
			customerInfo = new ArrayList<BasicInfoListAdapter.Info>();
		
		}
		//如果没点击更多按钮
		if (!isDetail) {
		    custmerInfo.clear();
		    if(customerInfoList != null)
		        customerInfoList.removeAllViews();
			for (int i = 0; i < 11; i ++) {
	               custmerInfo.add(new String[2]);
	            }
	          custmerInfo.get(0)[0] = "客户名称";
	            custmerInfo.get(1)[0] = "客户移动电话";
	            custmerInfo.get(2)[0] = "客户其他电话";
	            custmerInfo.get(3)[0] = "客户来源"; 
	            custmerInfo.get(4)[0] = "客户类别";
	            custmerInfo.get(5)[0] = "信息来源";
	            custmerInfo.get(6)[0] = "客户性别";
	            custmerInfo.get(7)[0] = "证件类型";
	            custmerInfo.get(8)[0] = "证件号码";
//	            custmerInfo.get(9)[0] = "生日";
//	            custmerInfo.get(10)[0] = "客户地址";
//	            custmerInfo.get(11)[0] = "备注";
	            custmerInfo.get(9)[0] = "客户地址";
	            custmerInfo.get(10)[0] = "备注";
	            if (customer != null) {
	                custmerInfo.get(0)[1] = customer.getCustName();
	                custmerInfo.get(1)[1] = customer.getCustMobile();
	                custmerInfo.get(2)[1] = customer.getCustOtherPhone();
	                custmerInfo.get(3)[1] = customer.getCustFrom(); 
	                custmerInfo.get(4)[1] = customer.getCustType();
	                custmerInfo.get(5)[1] = customer.getInfoFrom();
	                custmerInfo.get(6)[1] = customer.getGender();
	                custmerInfo.get(7)[1] = customer.getIdType();
	                custmerInfo.get(8)[1] = customer.getIdNumber();
//	                custmerInfo.get(9)[1] = customer.getBirthday();
//	                custmerInfo.get(10)[1] = customer.getAddress();
//	                custmerInfo.get(11)[1] = customer.getCustComment();
	                custmerInfo.get(9)[1] = customer.getAddress();
                    custmerInfo.get(10)[1] = customer.getCustComment();
	            }
	            if(customerInfoAdapter != null)
	                customerInfoAdapter.clearData();
	            else 
	                customerInfoAdapter = new SmCustomerInfoItemAdapter(getActivity());
	            for(int i=0;i<=10; i++){
	                info.key = custmerInfo.get(i)[0];
	                info.value = custmerInfo.get(i)[1];
	                customerInfoAdapter.addItem(info.key,info.value,null);
	            }
	            customerInfoAdapter.notifyDataSetChanged();
	            
		} else {
		    custmerInfo.clear();
		    customerInfoList.removeAllViews();
	         for (int i = 0; i < 43; i ++) {
                 custmerInfo.add(new String[2]);
              }
            custmerInfo.get(0)[0] = "客户名称";
            custmerInfo.get(1)[0] = "客户移动电话";
            custmerInfo.get(2)[0] = "客户其他电话";
            custmerInfo.get(3)[0] = "客户来源"; 
            custmerInfo.get(4)[0] = "客户类别";
            custmerInfo.get(5)[0] = "信息来源";
            custmerInfo.get(6)[0] = "证件类型";
            custmerInfo.get(7)[0] = "证件号码";
            custmerInfo.get(8)[0] = "客户地址";
            custmerInfo.get(9)[0] = "客户性别";
            custmerInfo.get(10)[0] = "生日";
            custmerInfo.get(11)[0] = "省区";
            custmerInfo.get(12)[0] = "市县";
            custmerInfo.get(13)[0] = "行政区划";
            custmerInfo.get(14)[0] = "QQ号码";
            custmerInfo.get(15)[0] = "邮编";
            custmerInfo.get(16)[0] = "E-mail";
            custmerInfo.get(17)[0] = "方便联系时间";
            custmerInfo.get(18)[0] = "希望联系方式";
            custmerInfo.get(19)[0] = "传真";
            custmerInfo.get(20)[0] = "现用车";
            custmerInfo.get(21)[0] = "所处行业";
            custmerInfo.get(22)[0] = "职务";
            custmerInfo.get(23)[0] = "教育程度";
            custmerInfo.get(24)[0] = "现用车品牌";
            custmerInfo.get(25)[0] = "客户爱好1";
            custmerInfo.get(26)[0] = "客户爱好2";
            custmerInfo.get(27)[0] = "客户爱好3";
            custmerInfo.get(28)[0] = "现用车牌照号";
            custmerInfo.get(29)[0] = "企业性质";
            custmerInfo.get(30)[0] = "企业人数";
            custmerInfo.get(31)[0] = "注册资本";
            custmerInfo.get(32)[0] = "竞争车型";
            custmerInfo.get(33)[0] = "本店重购客户";
            custmerInfo.get(34)[0] = "网络重购客户";
            custmerInfo.get(35)[0] = "置换客户标识";
            custmerInfo.get(36)[0] = "老客户推荐标识";
            custmerInfo.get(37)[0] = "老客户";
            custmerInfo.get(38)[0] = "个贷客户标识";
            custmerInfo.get(39)[0] = "总部VIP客户";
            custmerInfo.get(40)[0] = "大客户";
            custmerInfo.get(41)[0] = "大客户选择";
            custmerInfo.get(42)[0] = "备注";
            
            if (customer != null) {
                custmerInfo.get(0)[1] = customer.getCustName();
                custmerInfo.get(1)[1] = customer.getCustMobile();
                custmerInfo.get(2)[1] = customer.getCustOtherPhone();
                custmerInfo.get(3)[1] = customer.getCustFrom();
                custmerInfo.get(4)[1] = customer.getCustType();
                custmerInfo.get(5)[1] = customer.getInfoFrom();
                custmerInfo.get(6)[1] = customer.getIdType();
                custmerInfo.get(7)[1] = customer.getIdNumber();
                custmerInfo.get(8)[1] = customer.getAddress();
                custmerInfo.get(9)[1] = customer.getGender();
                custmerInfo.get(10)[1] = DateFormatUtils.formatDate(customer.getBirthday());

                custmerInfo.get(11)[1] = customer.getProvince();
                custmerInfo.get(12)[1] = customer.getCity();
                custmerInfo.get(13)[1] = customer.getDistrict();
                custmerInfo.get(14)[1] = customer.getQq();
                
                custmerInfo.get(15)[1] = customer.getPostcode();
                custmerInfo.get(16)[1] = customer.getEmail();
                custmerInfo.get(17)[1] = customer.getConvContactTime();
                custmerInfo.get(18)[1] = customer.getExpectContactWay();
                custmerInfo.get(19)[1] = customer.getFax();
                custmerInfo.get(20)[1] = customer.getExistingCar();
                custmerInfo.get(21)[1] = customer.getIndustry();
                custmerInfo.get(22)[1] = customer.getPosition();
                custmerInfo.get(23)[1] = customer.getEducation();
                custmerInfo.get(24)[1] = customer.getExistingCarBrand();
                custmerInfo.get(25)[1] = customer.getCustInterest1();
                custmerInfo.get(26)[1] = customer.getCustInterest2();
                custmerInfo.get(27)[1] = customer.getCustInterest3();
                custmerInfo.get(28)[1] = customer.getExistLisenPlate();
                custmerInfo.get(29)[1] = customer.getEnterpType();
                custmerInfo.get(30)[1] = customer.getEnterpPeopleCount();
                custmerInfo.get(31)[1] = customer.getRegisteredCapital();
                custmerInfo.get(32)[1] = customer.getCompeCarModel();
                custmerInfo.get(33)[1] = String.valueOf(customer.getRebuyStoreCustTag());
                custmerInfo.get(34)[1] = String.valueOf(customer.getRebuyOnlineCustTag());
                custmerInfo.get(35)[1] = String.valueOf(customer.getChangeCustTag());
                custmerInfo.get(36)[1] = String.valueOf(customer.getRegularCustTag());
                custmerInfo.get(37)[1] = customer.getRegularCust();
                custmerInfo.get(38)[1] = String.valueOf(customer.getLoanCustTag());
                custmerInfo.get(39)[1] = String.valueOf(customer.getHeaderQuartCustTag());
                custmerInfo.get(40)[1] = String.valueOf(customer.getBigCustTag());
                custmerInfo.get(41)[1] = customer.getBigCusts();
                custmerInfo.get(42)[1] = customer.getCustComment();
            }
            customerInfoAdapter.clearData();
            for(int i=0;i<=42; i++){
                info.key = custmerInfo.get(i)[0];
                info.value = custmerInfo.get(i)[1];
                customerInfoAdapter.addItem(info.key,info.value,null);
            }
            customerInfoAdapter.notifyDataSetChanged();
           
		}
		 refreshList();
	}

	/**
	 * 
	 * <pre>
	 * 更新客户管理中的客户信息
	 * </pre>
	 * 调用此方法加载页面
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
		}
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
			this.customer = customer;
	}

	/**
	 * 
	 * <pre>
	 * 获得最新客户详细信息
	 * </pre>
	 *
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
			}else if (info.key.equals("客户性别") && info.pairKey != null){
				result.setGender(info.value);
				result.setGenderCode(info.pairKey);
			}else if (info.key.equals("生日"))
				result.setBirthday(info.value);
			else if (info.key.equals("省区") && info.pairKey != null){
					result.setProvinceCode(info.pairKey);
					result.setProvince(info.value);
			}else if (info.key.equals("市县") && info.pairKey != null){
					result.setCityCode(info.pairKey);
					result.setCity(info.value);
			}else if (info.key.equals("行政区划") && info.pairKey != null){
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
			}else if (info.key.equals("教育程度") && info.pairKey != null){
				result.setEducationCode(info.pairKey);
				result.setEducation(info.value);
			}else if (info.key.equals("现用车品牌") && info.pairKey != null){
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
			}else if (info.key.equals("现用车牌照号") && info.pairKey != null){
				result.setExistLisenPlate(info.value);
			}else if (info.key.equals("企业性质") && info.pairKey != null){
				result.setEnterpTypeCode(info.pairKey);
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
			else if (info.key.equals("老客户") && info.pairKey != null){
				result.setRegularCustCode(info.pairKey);
				result.setRegularCust(info.value);
			}else if (info.key.equals("个贷客户标识"))
				result.setLoanCustTag(info.value.equals("true"));
			else if (info.key.equals("总部VIP客户标识"))
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
}
