package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
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
 * 销售线索详细信息fragment
 * </pre>
 * @extends SherlockFragment
 * @author liuyu
 * @version $Id: SmOppoInfoFragment.java, v 0.1 2013-7-1 下午4:52:58 liuyu Exp $
 */
public class ScOppoInfoFragment extends SherlockFragment {
    private final static String             tag               = Log
                                                                  .getTag(ScOppoInfoFragment.class);

    private BasicInfoListAdapter            customerInfoAdapter;
    private BasicInfoListAdapter            carInfoAdapter;
    private BasicInfoListAdapter            followPlanAdapter;
    private List<BasicInfoListAdapter.Info> customerInfo;
    private List<BasicInfoListAdapter.Info> customerInfoCaches;                                  //客户信息用于显示更多后隐藏校验缓存
    private List<BasicInfoListAdapter.Info> carInfoCaches;                                       //购车信息用于显示更多后隐藏校验缓存
    private List<BasicInfoListAdapter.Info> carInfo;
    private List<BasicInfoListAdapter.Info> followPlanInfo;
    BasicInfoListAdapter.Info               idnumberInfo;
    BasicInfoListAdapter.Info               birthdayInfo;
    BasicInfoListAdapter.Info               idtypeInfo;
    BasicInfoListAdapter.Info               bigCustInfo;
    BasicInfoListAdapter.Info               bigCustSelectInfo;
    BasicInfoListAdapter.Info               oldCustInfo;
    BasicInfoListAdapter.Info               oldCustSelectInfo;
    BasicInfoListAdapter.Info               custNameInfo;                                           //新建销售线索客户名称
    public LinearLayout                     mCarInfo;                                               //购车信息
    private LinearLayout                    mCustomInfo;                                            //客户信息
    private LinearLayout                    mCustominfoTitle;                                       //客户信息标题
    private LinearLayout                    mFollowInfo;                                            //新建跟踪计划信息
    private LinearLayout                    newlayout;                                              //新建跟踪计划标题
    public boolean                          bEdit             = false;
    public boolean                          flag              = false;
    public boolean                          addFlag           = false;
    public Project                          project           = null;
    private Customer                        customer;
    private TracePlan                       tracePlan;
    private Project                         mHasProjectActive = null;                              //用于判断是否有活动订单
    private CRMManager                      crmManager;
    private ImageButton                     mCustomInfoMore;
    private ImageButton                     mCustomInfoHide;
    private ImageButton                     mCarInfoMore;
    private ImageButton                     mCarInfoHide;
    private TextView                        cust_title;
    private static ToggleButton             newToggleBtn;
    private boolean                         custFlag          = false;                              //客户更多信息标志
    private boolean                         carFlag           = false;                              //购车信息更多标志
    private boolean                         custOrderCome     = false;                              //从客户订单进入
    private boolean                         isFromCustManager;                                      //从客户管理进入
    private boolean                         mCustFlowCome     = false;                              // true 从跟踪计划进入
    private boolean                         editFlag          = false;
    private boolean                         FlowStatusFlg;
    private boolean                         isGiveUpChecked   = false;
    private boolean                         isSuccess         = true;                              //编辑添加成功标志
    private boolean                         isOldCust         = false;                             //老客户信息带入
    private boolean                         oldCustCollect    = false;							   //老客户选择带入
    private boolean                         newToggleBtnClick = false;                             // true 显示新建跟踪计划
    private boolean                         isSubmitNewCar    = false;                             //ture 递交新车
    private boolean                         isGetBirthFromId  = false;                             //true 生日根据身份证联动
    private boolean                         isFirstSubmitNewCar = false;                           //true 初次进入不是递交新车
    private boolean                         noDataModify      = false;
    private String                          changeValue;
    private String                          newPairKey;                      
    private String                          address           = "";                                 //客户地址
    private String                          province          = "";                                 //省区
    private String                          city              = "";                                 //市县
    private String                          district          = "";                                 //行政区划
    private BottomBar                       bottomBar;
    String                                  orderId           = "";
    String                                  orderStatus       = "";
    int                                     contacterPage     = -1;
    int                                     followInfoPage    = -1;

    private String[]                        carInfoStrList1;
    private String[]                        followinfokeylist;
    public String[]                         infokeylist1;
    public String[]                         infokeylist2;
    public String[]                         flowStatus;
    MenuItem                                item;                                                //菜单按钮
    public String[]                         addressInfoList;
    BaseInfoRowViewItem                     cityItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

        Log.i(tag, "-------------------------------onCreate----------------");

        carInfoStrList1 = new String[] { getString(R.string.brand_1), getString(R.string.model),
                getString(R.string.outsideColor), getString(R.string.insideColor),
                getString(R.string.carConfiguration), getString(R.string.salesQuote),
                getString(R.string.preorderCount), getString(R.string.preorderDate),
                getString(R.string.flowStatus), getString(R.string.dealPossibility) };
        followinfokeylist = new String[] {
                getString(R.string.activitytype), getString(R.string.executetime),
                getString(R.string.executestatus), getString(R.string.activitycontent),
                getString(R.string.contactresult), getString(R.string.custfeedback)};
        infokeylist1 = new String[] { getString(R.string.custName), getString(R.string.custMobile),
                getString(R.string.custOtherPhone), getString(R.string.activitytype),
                getString(R.string.executetime), getString(R.string.executestatus),
                getString(R.string.activitycontent), getString(R.string.contactresult),
                getString(R.string.custfeedback), getString(R.string.leadercomment) };
        infokeylist2 = new String[] {getString(R.string.model),getString(R.string.outsideColor),
                                     getString(R.string.insideColor),getString(R.string.cust_chassis),
                                     getString(R.string.engineNo),getString(R.string.birthday),
                                     getString(R.string.city),getString(R.string.address),
                                     getString(R.string.existingCar),getString(R.string.idType)
                                     ,getString(R.string.idNumber),getString(R.string.province),};
        flowStatus = new String[] { getString(R.string.flowStatus_2),
                getString(R.string.flowStatus_5), getString(R.string.flowStatus_6),
                getString(R.string.flowStatus_3), getString(R.string.flowStatus_4),
                getString(R.string.flowStatus_1), getString(R.string.flowStatus_7) };
        addressInfoList = new String[] { getString(R.string.province), getString(R.string.city),
                getString(R.string.district) };
        if(project != null){
        	province = project.getCustomer().getProvince();
        	city = project.getCustomer().getCity();
        }
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sc_activity_example, container, false);
        
        this.crmManager = ((RoilandCRMApplication) getActivity().getApplication()).getCRMManager();
        cust_title = (TextView)view.findViewById(R.id.cust_title);
        newToggleBtn = (ToggleButton) view.findViewById(R.id.toggleButton_new);
        mFollowInfo = (LinearLayout) view.findViewById(R.id.oppo_followinfo_list);
        newlayout = (LinearLayout) view.findViewById(R.id.oppo_followinfo_list_layout);
        mCustominfoTitle = (LinearLayout) view.findViewById(R.id.custominfo);
        mCarInfoHide = (ImageButton) view.findViewById(R.id.carinfo_hide_btn);
        mCarInfoMore = (ImageButton) view.findViewById(R.id.carinfo_more_btn);
        newlayout.setVisibility(View.GONE);
        newToggleBtn.setVisibility(View.GONE);
        Intent intent = getActivity().getIntent();
        isFromCustManager = intent.getBooleanExtra("fromCustManager", false);
        mCustFlowCome = intent.getBooleanExtra("CustFlowCome", false);
        custOrderCome = intent.getBooleanExtra("CustOrderCome", false);
        addFlag = intent.getBooleanExtra("addFlag", false);
        orderId = intent.getStringExtra("orderId");
        orderStatus = intent.getStringExtra("orderStatus");
        setNewListHidden();
        mCustomInfo = (LinearLayout) view.findViewById(R.id.custom_info_list);
        mCarInfo = (LinearLayout) view.findViewById(R.id.car_info_list);
        if (customerInfoAdapter == null)
            customerInfoAdapter = new BasicInfoListAdapter(this.getActivity());
            customerInfoAdapter.crmManager = this.crmManager;
        if (carInfoAdapter == null) {
            carInfoAdapter = new BasicInfoListAdapter(this.getActivity());
            carInfoAdapter.crmManager = this.crmManager;
        }
        //为跟踪计划开关按钮设置监听事件
        newToggleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //根据状态设置相应控件状态
                if (isChecked) {
                    newToggleBtnClick = true;
                    newlayout.setVisibility(View.VISIBLE);
                    setNewListdisplay();
                    newToggleBtn.setBackgroundResource(R.drawable.toggle_sliding2);
                } else {
                    newToggleBtnClick = false;
                    newlayout.setVisibility(View.GONE);
                    setNewListHidden();
                    newToggleBtn.setBackgroundResource(R.drawable.toggle_sliding1);
                }
            }
        });
        mCarInfoMore = (ImageButton) view.findViewById(R.id.carinfo_more_btn);
        if (isFromCustManager) {
            comeFromCustMamager();
            displayCarInfo(false);
            
            
        } else {
            mCustomInfoMore = (ImageButton) view.findViewById(R.id.custominfo_more_btn);
            mCustomInfoHide = (ImageButton) view.findViewById(R.id.custominfo_hide_btn);
            //隐藏按钮非表示
            mCustomInfoHide.setVisibility(View.GONE);

            //客户信息更多按钮监听事件
            mCustomInfoMore.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                	if(isOldCust)
                		isOldCust = false;
                    custFlag = !custFlag;
                    mCustomInfoHide.setVisibility(View.VISIBLE);
                    mCustomInfoMore.setVisibility(View.GONE);
                    displayCustomerInfo(custFlag);
                    customerInfoAdapter.setEditable(customerInfoAdapter.getEditable());
                }
            });

            //客户信息隐藏按钮监听事件
            mCustomInfoHide.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    custFlag = !custFlag;
                    mCustomInfoHide.setVisibility(View.GONE);
                    mCustomInfoMore.setVisibility(View.VISIBLE);
                    displayCustomerInfo(custFlag);
                    customerInfoAdapter.setEditable(customerInfoAdapter.getEditable());
                }
            });
            displayCustomerInfo(false);
            displayCarInfo(false);
        }
        // 隐藏按钮非表示
        mCarInfoHide.setVisibility(View.GONE);

        // 购车信息更多按钮的监听事件
        mCarInfoMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                carFlag = !carFlag;
                mCarInfoHide.setVisibility(View.VISIBLE);
                mCarInfoMore.setVisibility(View.GONE);
                displayCarInfo(carFlag);
                carInfoAdapter.setEditable(carInfoAdapter.getEditable());
            }
        });

        // 购车信息隐藏按钮的监听事件
        mCarInfoHide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                carFlag = !carFlag;
                mCarInfoHide.setVisibility(View.GONE);
                mCarInfoMore.setVisibility(View.VISIBLE);
                displayCarInfo(carFlag);
                carInfoAdapter.setEditable(carInfoAdapter.getEditable());
            }
        });
        //点击添加销售线索
        if (addFlag) {
            if (followPlanAdapter == null) {
                followPlanAdapter = new BasicInfoListAdapter(this.getActivity());
                followPlanAdapter.crmManager = this.crmManager;
            }
            displayfollowPlanInfo();
            setNewListdisplay();
            followPlanAdapter.setEditable(true);
            carInfoAdapter.setEditable(true);
            customerInfoAdapter.setEditable(true);
            bottomBar.setVisible(false);
            newlayout.setVisibility(View.VISIBLE);
            newToggleBtn.setVisibility(View.VISIBLE);
            mFollowInfo.setVisibility(View.VISIBLE);
            newToggleBtnClick = true;
        }
        //
        cust_title.setFocusable(true);
        cust_title.setFocusableInTouchMode(true);
        cust_title.requestFocus();
        return view;
    }
    
    
    

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * 
     * <pre>
     * 显示销售线索信息
     * </pre>
     *
     * @param isDetail 客户信息是否显示更多
     */
    public void displayCustomerInfo(boolean isDetail) {
        if (customerInfo == null){
            customerInfo = new ArrayList<BasicInfoListAdapter.Info>();
        
        }
        if(project == null){
            project = new Project();
        }
        if(!isOldCust()){
            project.setCustomer(getUpdatedCustomer());
        }
        
        //如果没点击更多按钮
        if (!isDetail) {
            customerInfo.clear();
            if(addFlag){
                customerInfo.add(custNameInfo = new BasicInfoListAdapter.Info(getString(R.string.custName), BaseInfoRowViewItem.RIGHT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustName() : null),true,true));              
            }else{
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custName), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustName() : null),true));
            }

            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustFrom() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custType), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustType() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.infoFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getInfoFrom() : null),true));
            if(addFlag){
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.collectFrom),BaseInfoRowViewItem.SELECTION_TYPE,null,(project != null && project.getCustomer() != null ? project.getCustomer().getCollectFrom() : null),true));
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custMobile), BaseInfoRowViewItem.MOBILETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustMobile() : null),false,InputType.TYPE_CLASS_NUMBER));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custOtherPhone), BaseInfoRowViewItem.PSTNTEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustOtherPhone() : null),false,InputType.TYPE_CLASS_PHONE));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.comment), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustComment() : null),false));
        } else {
            customerInfo.clear();       
            if(addFlag){
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custName), BaseInfoRowViewItem.RIGHT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustName() : null),true));
            }else{
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custName), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustName() : null),true));
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustFrom() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custType), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustType() : null),true));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.infoFrom), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getInfoFrom() : null),true));
            if(addFlag){
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.collectFrom),BaseInfoRowViewItem.SELECTION_TYPE,null,(project != null && project.getCustomer() != null ? project.getCustomer().getCollectFrom() : null),true));
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custMobile), BaseInfoRowViewItem.MOBILETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustMobile() : null),false,InputType.TYPE_CLASS_NUMBER));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custOtherPhone), BaseInfoRowViewItem.PSTNTEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustOtherPhone() : null),false,InputType.TYPE_CLASS_PHONE));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.gender), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getGender() : null),false));
            if(isSubmitNewCar() || isSubmitNewCar){
                customerInfo.add(birthdayInfo = new BasicInfoListAdapter.Info(getString(R.string.birthday), BaseInfoRowViewItem.DATE_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getBirthday() : null),true));
                customerInfo.add(idtypeInfo = new BasicInfoListAdapter.Info(getString(R.string.idType), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getIdType() : null),true));
                customerInfo.add(idnumberInfo = new BasicInfoListAdapter.Info(getString(R.string.idNumber), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getIdNumber() : null),true));
            }else{
                customerInfo.add(birthdayInfo = new BasicInfoListAdapter.Info(getString(R.string.birthday), BaseInfoRowViewItem.DATE_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getBirthday() : null),false));
                customerInfo.add(idtypeInfo = new BasicInfoListAdapter.Info(getString(R.string.idType), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getIdType() : null),false));
                customerInfo.add(idnumberInfo = new BasicInfoListAdapter.Info(getString(R.string.idNumber), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getIdNumber() : null),false));
            }
            if(addFlag && !oldCustCollect){
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.province), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getProvince() : null),false));
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.city), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCity() : null),false,false));  
            }else{
                if(isSubmitNewCar() || isSubmitNewCar){
                    //递交新车
                    customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.province), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getProvince() : null),true));
                    if(StringUtils.isEmpty(project.getCustomer().getProvince())){
                        customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.city), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCity() : null),true,false));
                    }else{
                        customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.city), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCity() : null),true));
                    }
                }else{
                    //非递交新车
                    customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.province), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getProvince() : null),false));    
                if(StringUtils.isEmpty(project.getCustomer().getProvince())){
                    customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.city), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCity() : null),false,false));
                }else{
                    customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.city), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCity() : null),false));
                }
                }
            }
            if(addFlag && !oldCustCollect || StringUtils.isEmpty(project.getCustomer().getCity())){
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.district), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getDistrict() : null),false,false));
            }else{
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.district), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getDistrict() : null),false));
            }
            
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.qq), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getQq() : null),false,InputType.TYPE_CLASS_NUMBER));
            if(isSubmitNewCar() || isSubmitNewCar){
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.address), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getAddress() : null),true));
            }else{
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.address), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getAddress() : null),false));
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.postcode), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getPostcode() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.email), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getEmail() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.convContactTime), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getConvContactTime() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.expectContactWay), BaseInfoRowViewItem.SELECTION_TYPE,null, (project != null && project.getCustomer() != null ? project.getCustomer().getExpectContactWay() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.fax), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getFax() : null),false));
            if(isSubmitNewCar() || isSubmitNewCar){
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.existingCar), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getExistingCar() : null),true));
            }else{
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.existingCar), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getExistingCar() : null),false));
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.industry), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getIndustry() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.position), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getPosition() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.education), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getEducation() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.existingcarbrand), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getExistingCarBrand() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custInterest1), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustInterest1() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custInterest2), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustInterest2() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.custInterest3), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustInterest3() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.existLisenPlate), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getExistLisenPlate() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.enterpType), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getEnterpType() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.enterpPeopleCount), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getEnterpPeopleCount() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.registeredCapital), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getRegisteredCapital() : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.compeCarModel), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCompeCarModel() : null),false));
          if(addFlag){
            if(isOldCust){
                project.getCustomer().setRebuyStoreCustTag(true);
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.rebuyStoreCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getRebuyStoreCustTag() != null ? "true" : null),false,false));
            }else{
                customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.rebuyStoreCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getRebuyStoreCustTag() != null ? "false" : null),false,false));
            }
          }else{
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.rebuyStoreCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getRebuyStoreCustTag() != null ? (project.getCustomer().getRebuyStoreCustTag() ? "true" : "false") : null),false,false));
          }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.rebuyOnlineCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getRebuyOnlineCustTag() != null ? (project.getCustomer().getRebuyOnlineCustTag() ? "true" : "false") : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.changeCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getChangeCustTag() != null? (project.getCustomer().getChangeCustTag() ? "true" : "false") : null),false));
            customerInfo.add(oldCustInfo = new BasicInfoListAdapter.Info(getString(R.string.regularCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getRegularCustTag() != null ? (project.getCustomer().getRegularCustTag() ? "true" : "false") : "false"),false));
            //判断老客户选择状态
            if ("true".equals(oldCustInfo.value)) {
                customerInfo.add(oldCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.regularCust), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getRegularCust() : null),true));
            } else {
                customerInfo.add(oldCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.regularCust), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getRegularCust() : null),false, false));
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.loanCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getLoanCustTag() != null ? (project.getCustomer().getLoanCustTag() ? "true" : "false") : null),false));
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.headerQuartCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getHeaderQuartCustTag() != null ? (project.getCustomer().getHeaderQuartCustTag() ? "true" : "false") : null),false));
            customerInfo.add(bigCustInfo = new BasicInfoListAdapter.Info(getString(R.string.bigCustTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (project != null && project.getCustomer() != null && project.getCustomer().getBigCustTag() != null ? (project.getCustomer().getBigCustTag() ? "true" : "false") : "false"),false));
            //判断大客户选择状态
            if ("true".equals(bigCustInfo.value)) {
                customerInfo.add(bigCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.bigCusts), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getBigCusts() : null),true));
            } else {
                customerInfo.add(bigCustSelectInfo = new BasicInfoListAdapter.Info(getString(R.string.bigCusts), BaseInfoRowViewItem.SELECTION_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getBigCusts() : null),false, false));
            }
            customerInfo.add(new BasicInfoListAdapter.Info(getString(R.string.comment), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (project != null && project.getCustomer() != null ? project.getCustomer().getCustComment() : null),false));
            customerInfoCaches = new ArrayList<BasicInfoListAdapter.Info>();
            customerInfoCaches.addAll(customerInfo);
        }
        customerInfoAdapter.setContentList(customerInfo);
        customerInfoAdapter.notifyDataSetChanged();
        if(addFlag){
            customerInfoAdapter.setEditable(true);
        }
        refreshCustList();
    }

    /**
     * 
     * <pre>
     * 刷新客户信息
     * </pre>
     *
     */
    public void refreshCustList() {
        if (mCustomInfo != null && customerInfoAdapter != null) {
            mCustomInfo.removeAllViews();
            for (int i = 0; i < customerInfoAdapter.getCount(); i++) {
                mCustomInfo.addView(customerInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mCustomInfo.addView(dividerView);
            }
            if(project != null && project.getCustomer() != null && !StringUtils.isEmpty(project.getCustomer().getProvince()) && custFlag){
                BaseInfoRowViewItem item = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(addressInfoList[1]);
                if(item != null)
                item.setParentKey(project.getCustomer().getProvinceCode());
            }
            if(project != null && project.getCustomer() != null && !StringUtils.isEmpty(project.getCustomer().getCity()) && custFlag){
                BaseInfoRowViewItem item = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(addressInfoList[2]);
                if(item != null)
                item.setParentKey(project.getCustomer().getProvinceCode());
                item.setParentKey2(project.getCustomer().getCityCode());
            }
        }
    }
    /**
     * 
     * <pre>
     * 判断流程状态是否为递交新车
     * </pre>
     *
     * @return 递交新车返回true
     */
    public boolean isSubmitNewCar(){
        if(project.getPurchaseCarIntention().getFlowStatus() != null && 
                getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus())){
            return true;
        }
        return false;
    }
    
    /**
     * 
     * <pre>
     * 判断是否有活动订单
     * </pre>
     *
     * @return
     */
    
    public boolean hsaActivityOrder(){
        if(project.getPurchaseCarIntention().isHasActiveOrder())
            return true;
        else
            return false;
    }
    /**
     * 
     * <pre>
     * 显示购车信息
     * </pre>
     *
     * @param isDetail 是否显示更多
     */
    public void displayCarInfo(boolean isDetail) {
        
        if (carInfo == null){
            carInfo = new ArrayList<BasicInfoListAdapter.Info>();
        }
        if(project == null){
            project = new Project();
        }
        project.setPurchaseCarIntention(getUpdatedPurchaseCar());
        if (!isDetail) {
            //如果没点击更多按钮
            carInfo.clear();
            if(isSubmitNewCar() || hsaActivityOrder()){
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.brand_1), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getBrand() : null),true,false));
            }else{
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.brand_1), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getBrand() : null),true));
            }
            if(addFlag){
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),false,false));
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.outsideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getOutsideColor() : null),false,false));
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getInsideColor() : null),false,false));
            }else{
                
                if(isSubmitNewCar() || hsaActivityOrder()){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),true,false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.outsideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getOutsideColor() : null),true,false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getInsideColor() : null),true));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.outsideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getOutsideColor() : null),false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getInsideColor() : null),false));
                }
            }
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColorCheck), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? String
                .valueOf(project.getPurchaseCarIntention().isInsideColorCheck()) : "false"),false));
            if(project != null && project.getPurchaseCarIntention() != null){
                if(!StringUtils.isEmpty(project.getPurchaseCarIntention().getModel())){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.carConfiguration), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                        .getPurchaseCarIntention().getCarConfiguration() : null),false));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.carConfiguration), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                        .getPurchaseCarIntention().getCarConfiguration() : null),false,false));
                }
            }
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.salesQuote), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getSalesQuote() : null),false,InputType.TYPE_CLASS_NUMBER));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderCount), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPreorderCount() : "1"),false,InputType.TYPE_CLASS_NUMBER));
            if(project != null && project.getPurchaseCarIntention() != null){
                if(project.getPurchaseCarIntention().getFlowStatus() != null && 
                        getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus())){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.finish_preorderDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true,false));
                }else if(project.getPurchaseCarIntention().isGiveupTag() !=null && project.getPurchaseCarIntention().isGiveupTag()){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.lose_date), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true,false));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true));
                }
            }
            if(project != null && project.getPurchaseCarIntention() != null){
                if(project.getPurchaseCarIntention().isGiveupTag()){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.flowStatus), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getFlowStatus() : getString(R.string.flowStatus_2)),false,false));
                }else if(getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus()) && !isFirstSubmitNewCar){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.flowStatus), BaseInfoRowViewItem.SELECTION_TYPE, null, project.getPurchaseCarIntention()
                        .getFlowStatus(),false,false));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.flowStatus), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getFlowStatus() : getString(R.string.flowStatus_2)),false));
                }
            }
        } else {
            carInfo.clear();
            if(isSubmitNewCar() || hsaActivityOrder()){
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.brand_1), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getBrand() : null),true,false));
            }else{
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.brand_1), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getBrand() : null),true));
            }
            if(addFlag){
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),false,false));
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.outsideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getOutsideColor() : null),false,false));
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getInsideColor() : null),false,false));
            }else{
                if(isSubmitNewCar() || hsaActivityOrder()){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),true,false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.outsideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getOutsideColor() : null),true,false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getInsideColor() : null),true));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.outsideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getOutsideColor() : null),false));
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getInsideColor() : null),false));
                }
            }
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColorCheck), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? String
                .valueOf(project.getPurchaseCarIntention().isInsideColorCheck()) : "false"),false));
            if(project != null && project.getPurchaseCarIntention() != null){
                if(!StringUtils.isEmpty(project.getPurchaseCarIntention().getModel())){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.carConfiguration), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                        .getPurchaseCarIntention().getCarConfiguration() : null),false));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.carConfiguration), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                        .getPurchaseCarIntention().getCarConfiguration() : null),false,false));
                }
            }
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.salesQuote), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getSalesQuote() : null),false,InputType.TYPE_CLASS_NUMBER));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.dealPriceInterval), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                .getPurchaseCarIntention().getDealPriceInterval() : null),false));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.payment), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPayment() : null),false));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderCount), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPreorderCount() : "1"),false,InputType.TYPE_CLASS_NUMBER));
            if(project != null && project.getPurchaseCarIntention() != null){
                if(project.getPurchaseCarIntention().getFlowStatus() != null && 
                        getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus())){
                    if(isSubmitNewCar){
                        carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.finish_preorderDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                                && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true,true));
                    }else{
                        carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.finish_preorderDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                                && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true,false));
                    }
                }else if(project.getPurchaseCarIntention().isGiveupTag() !=null && project.getPurchaseCarIntention().isGiveupTag()){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.lose_date), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true,false));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true));
                }
            }
            if(project != null && project.getPurchaseCarIntention() != null){
                if(project.getPurchaseCarIntention().isGiveupTag()){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.flowStatus), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getFlowStatus() : getString(R.string.flowStatus_2)),false,false));
                }else if(getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus()) && !isFirstSubmitNewCar){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.flowStatus), BaseInfoRowViewItem.SELECTION_TYPE, null, project.getPurchaseCarIntention()
                        .getFlowStatus(),false,false));
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.flowStatus), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getFlowStatus() : getString(R.string.flowStatus_2)),false));
                }
            }
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.dealPossibility), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getDealPossibility() : "0.05"),false,false));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.purchMotivation), BaseInfoRowViewItem.SELECTION_TYPE,null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getPurchMotivation() : null),false));
            if(isSubmitNewCar()){
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.chassisNo), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getChassisNo() : null),true));
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.engineNo), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getEngineNo() : null),true));
                }else{
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.chassisNo), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                            .getChassisNo() : null),false));
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.engineNo), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                            .getEngineNo() : null),false));
                }
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.licensePlate), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getLicensePlate() : null),false));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.licenseProp), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getLicenseProp() : null),false));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.pickupDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? ("0"
                    .equals(project.getPurchaseCarIntention().getPickupDate())
                    || project.getPurchaseCarIntention().getPickupDate() == null || project.getPurchaseCarIntention().getPickupDate() == "null" ? null: String.valueOf((StringUtils.getDateTrimNullLong(project.getPurchaseCarIntention().getPickupDate())))) : null),false));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPreorderTag() : null),false));
            //新建时不能放弃销售机会
            if(addFlag || (project != null && project.getPurchaseCarIntention() != null && getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus()))){
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? String
                    .valueOf(project.getPurchaseCarIntention().isGiveupTag()) : "false"),false,false));
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupReason), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                            .getGiveupReason() : null),false,false));   
            }else{
                carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? String
                    .valueOf(project.getPurchaseCarIntention().isGiveupTag()) : "false"),false));
                if(project != null && project.getPurchaseCarIntention() != null && project.getPurchaseCarIntention().isGiveupTag()){
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupReason), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getGiveupReason() : null),true)); 
                }else{
                    carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupReason), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getGiveupReason() : null),false,false)); 
                }
               
            }
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.invoiceTitle), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getInvoiceTitle() : null),false));
            carInfo.add(new BasicInfoListAdapter.Info(getString(R.string.comment), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                .getPurchaseCarIntention().getProjectComment() : null),false));
            carInfoCaches = new ArrayList<BasicInfoListAdapter.Info>();
            carInfoCaches.addAll(carInfo);
    }
        carInfoAdapter.setContentList(carInfo);
        carInfoAdapter.notifyDataSetChanged();
        refreshCarList();
    
}
    /**
     * 
     * <pre>
     * 刷新购车信息
     * </pre>
     *
     */
    private void refreshCarList() {
        if (mCarInfo != null && carInfoAdapter != null) {
            mCarInfo.removeAllViews();
            for (int i = 0; i < carInfoAdapter.getCount(); i++) {
                mCarInfo.addView(carInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mCarInfo.addView(dividerView);
            }
            
            if(project != null && project.getPurchaseCarIntention() != null && !StringUtils.isEmpty(project.getPurchaseCarIntention().getBrand())){
                for(int i=1;i<5;i++){
                    BaseInfoRowViewItem item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(carInfoStrList1[i]);
                    if(item != null)
                        item.setParentKey(project.getPurchaseCarIntention().getBrandCode());
                    
                }
            }
            
            if(project != null && project.getPurchaseCarIntention() != null && !StringUtils.isEmpty(project.getPurchaseCarIntention().getModel())){
                BaseInfoRowViewItem item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(carInfoStrList1[4]);
                if(item != null)
                    item.setParentKey2(project.getPurchaseCarIntention().getModelCode());
            }
        }
    }
    /**
     * 
     * <pre>
     * 跟踪计划信息部分 加载页面信息
     * </pre>
     *
     */
    public void displayfollowPlanInfo() {
        if (followPlanInfo == null){
            followPlanInfo = new ArrayList<BasicInfoListAdapter.Info>();
        }
        followPlanInfo.clear();
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[0],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getActivityType() != null ? tracePlan.getActivityType()
                : ""), true, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[1],
            BaseInfoRowViewItem.DATE_TYPE, null, (tracePlan != null ? (String.valueOf(tracePlan
                .getExecuteTime() != 0 ? tracePlan.getExecuteTime() : DateFormatUtils.getSystemDate())) : String.valueOf(DateFormatUtils
                .getSystemDate())), true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[2],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getExecuteStatus() != null ? tracePlan
                .getExecuteStatus() : getResources().getString(R.string.executestatus_1)), true,
            false));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[3],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (tracePlan != null && tracePlan.getActivityContent() != null ? tracePlan
                .getActivityContent() : ""), true, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[4],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getContactResult() != null ? tracePlan
                .getContactResult().trim() : ""), false, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[5],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (tracePlan != null && tracePlan.getCustFeedback() != null ? tracePlan.getCustFeedback()
                : ""), false, true));

        followPlanAdapter.setContentList(followPlanInfo);
        followPlanAdapter.notifyDataSetChanged();
        refreshFollowInfoList();
    }
    
    /**
     * 
     * <pre>
     * 更新跟踪计划方法，把页面上的数据更新到原始list中
     * </pre>
     *
     */
    private void refreshFollowInfoList() {
        if (mFollowInfo != null && followPlanAdapter != null) {
            mFollowInfo.removeAllViews();
            for (int i = 0; i < followPlanAdapter.getCount(); i++) {
                mFollowInfo.addView(followPlanAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mFollowInfo.addView(dividerView);
            }
        }
    }
    
    /**
     * 
     * <pre>
     * 显示新建跟踪计划
     * </pre>
     *
     */
    private void setNewListdisplay() {
        ViewGroup.LayoutParams params = mFollowInfo.getLayoutParams();
        params.height = params.MATCH_PARENT;
        mFollowInfo.setLayoutParams(params);
    }
    /**
     * 
     * <pre>
     * 隐藏新建跟踪计划
     * </pre>
     *
     */
    private void setNewListHidden() {
        ViewGroup.LayoutParams params = mFollowInfo.getLayoutParams();
        params.height = 0;
        mFollowInfo.setLayoutParams(params);
    }

    /**
     * 
     * <pre>
     * 对project赋值
     * </pre>
     * 在执行页面跳转时候条用此方法
     * @param p 销售线索
     */
    public void setProject(Project p) {
        project = p;
    }

    /**
    * 
    * <pre>
    * 获取是否显示编辑按钮flag
    * </pre>
    * 在跟踪计划或者客户订单跳转时调用
    * @param flag
    */
    public void setEditFlg(boolean flag) {
        custOrderCome = flag;
    }


    /**
     * 
     * <pre>
     * 从客户管理进入只显示购车信息
     * </pre>
     *
     */
    public void comeFromCustMamager() {
        mCustominfoTitle.setVisibility(View.GONE);
        mCustomInfo.setVisibility(View.GONE);
    }

    /**
     * 创建菜单
     * @param menu
     * @param inflater
     * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        if (addFlag) {
            item = menu.findItem(R.id.save);//编辑状态下按钮显示保存
        } else {
            item = menu.findItem(R.id.edit);//显示状态下按钮显示编辑
        }
        //跟踪计划进入不能编辑
        if (mCustFlowCome || custOrderCome) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
    }

    /**
     * 菜单点击事件
     * @param item 菜单项
     * @return
     * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(tag, "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.edit:
                Log.i(tag, "" + item.getItemId());
                if (editFlag) {//编辑
                    editFlag = false;
                    editButtonClick(item,editFlag);
                } else {//显示
                    editFlag = true;
                    editButtonClick(item,editFlag);
                    item.setIcon(R.drawable.save_selector);
                }
                noDataModify = true;
                refreshCustList();//刷新客户信息
                refreshCarList();//刷新购车信息
                break;
            case R.id.save:
                //新建保存前判断是否为已建立客户 并执行保存操作
                //客户信息保存前校验
                noDataModify = true;
                String customMessage = customInfoValidation(); 
                if(!StringUtils.isEmpty(customMessage)){
                    noDataModify = true;
                    item.setIcon(R.drawable.save_selector);
                    editFlag = false;
                    isSuccess = false;
                    Toast.makeText(getActivity(), customMessage, Toast.LENGTH_SHORT).show();
                    return false;
                } 
                //购车信息保存前校验
                String carMessage = carInfoValidation();
                if(!StringUtils.isEmpty(carMessage)){
                    noDataModify = true;
                    item.setIcon(R.drawable.save_selector);
                    editFlag = false;
                    isSuccess = false;
                    Toast.makeText(getActivity(), carMessage, Toast.LENGTH_SHORT).show();
                    return false;
                }
                
                //跟踪计划保存前校验
                if(newToggleBtnClick){
                    String followMessage = followInfoValidation();
                    item.setIcon(R.drawable.save_selector);
                    editFlag = false;
                    isSuccess = false;
                    if(!StringUtils.isEmpty(followMessage)){
                        noDataModify = true;
                        Toast.makeText(getActivity(), followMessage, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if(!editFlag && !isSuccess){
                    noDataModify = true;
                    customerInfoAdapter.setEditable(true);
                    carInfoAdapter.setEditable(true);
                    followPlanAdapter.setEditable(true);
                    editFlag = true;
                    item.setIcon(R.drawable.save_selector);
                }
                confirmProjectExistenceAndSave();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 
     * <pre>
     * 编辑菜单点击事件
     * </pre>
     *
     * @param item 菜单按钮
     */
    public void editButtonClick(MenuItem item,boolean b) {
        //如果是编辑状态
        if (b) {
            item.setIcon(R.drawable.oppoinfo_save_btn);
            carInfoAdapter.setEditable(true);
            customerInfoAdapter.setEditable(true);
            bottomBar.setVisible(false);
        } else {
                //未进行试驾操作，提示是否进行递交新车
                if(!project.getPurchaseCarIntention().isHasActiveDrive() && isSubmitNewCar){
                    DialogUtils.hasDriveTestConfirm(getActivity(), null, getString(R.string.prompts), getString(R.string.prompts_drivetest_message),
                        //点击了yes
                        new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                            
                        }, new Runnable() {
                            
                            @Override
                            public void run() {
                            	editFlag = true;
                                return;
                            }
                        }, -1, -1);  
                }else{
                    checkMust();
                }
                
        }
    }

    /**
     * 
     * <pre>
     * 新建销售线索，调用API
     * </pre>
     *
     */
    public void saveNewProjectInfo() {
        new BaseTask<Boolean>(this.getActivity()) {
            @Override
            protected Boolean doInBackground(String... arg0) {
                Boolean b = false;
                try {
                    CRMManager manager = ((RoilandCRMApplication) getActivity().getApplication())
                        .getCRMManager();
                    Customer c = getUpdatedCustomer();
                    //如果客户信息不是选择出来的，那么Customer ID就为空，标识创建销售线索同时，添加客户信息。
                    if (StringUtils.isEmpty(c.getCustomerID()))
                        c.setUpdateCustInfo(false); //false:添加新客户信息， true:更新现有客户信息
                    else {
                        c.setUpdateCustInfo(true); //true:更新现有客户信息， false:添加新客户信息
                    }

                    PurchaseCarIntention p = getUpdatedPurchaseCar();
                    TracePlan t = null;
                    if (newToggleBtn.isChecked()) {
                        t = getUpdatedTracePlan();
                        t.setCustomer(c);
                        //获取跟踪计划中默认执行状态对应的code.
                        if (StringUtils.isEmpty(t.getExecuteStatusCode())) {
                            List<Dictionary> dicArray = manager
                                .getDictionariesByType("dicExecuteStatus");
                            for (int i = 0; i < dicArray.size(); i++) {
                                if (getResources().getString(R.string.executestatus_1).equals(dicArray.get(i).getDicValue())) {
                                    t.setExecuteStatus(dicArray.get(i).getDicValue());
                                    t.setExecuteStatusCode(dicArray.get(i).getDicKey());
                                    break;
                                }
                            }
                        }
                    }

                    project = new Project();
                    project.setCustomer(c);
                    project.setPurchaseCarIntention(p);

                    //获取销售线索中默认流程状态对应的code.
                    if (StringUtils.isEmpty(p.getFlowStatusCode())) {
                        List<Dictionary> dicArray = manager.getDictionariesByType("dicFlowStatus");
                        for (int i = 0; i < dicArray.size(); i++) {
                            if (dicArray.get(i).getDicValue().equals(p.getFlowStatus())) {
                                p.setFlowStatus(dicArray.get(i).getDicValue());
                                p.setFlowStatusCode(dicArray.get(i).getDicKey());
                                break;
                            }
                        }
                    }

                    b = manager.createProject(project, t);
                } catch (ResponseException e) {
                    responseException = e;
                    Log.e(tag, "create project failure.", e);
                }

                return b;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                Log.i(tag, "saveNewProjectInfo: " + String.valueOf(result));
                if (responseException != null) {
                    noDataModify = true;
                    item.setIcon(R.drawable.save_selector);
                    Toast.makeText(getActivity(), responseException.getMessage(),
                        Toast.LENGTH_SHORT).show();
                    //更新未成功 编辑状态
                    carInfoAdapter.setEditable(true);
                    customerInfoAdapter.setEditable(true);
                    isSuccess = false;
                    return;
                }else if(result.booleanValue()) {
                    noDataModify = false;
                    DialogUtils.alert(activity, getString(R.string.oppotunity_info), getString(R.string.opt_success),
                        new Runnable() {
                            @Override
                            public void run() {
                                newToggleBtnClick = false;
                                if(newToggleBtn.isChecked()){
                                    project.getCustomer().setHasUnexePlan("true");
                                }
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("newprojectinfo", project);
                                intent.putExtras(bundle);
                                getActivity().setResult(10, intent);
                                getActivity().finish();
                            }
                        });
                } else {
                    noDataModify = true;
                    bottomBar.setVisible(false);
                    carInfoAdapter.setEditable(true);
                    customerInfoAdapter.setEditable(true);
                    if(newToggleBtn.isChecked()){
                        followPlanAdapter.setEditable(true);
                    }
                }

            }
        }.execute();
    }

    /**
     * 
     * <pre>
     * 编辑更新销售线索，调用API
     * </pre>
     *
     */
    public void saveProjectInfo() {

        new BaseTask<Boolean>(this.getActivity()) {
            @Override
            protected Boolean doInBackground(String... arg0) {
                Boolean b = false;
                Customer c = getUpdatedCustomer(); 
                if (!StringUtils.isEmpty(c.getCustomerID()))
                        c.setUpdateCustInfo(true); //true:更新现有客户信息， false:添加新客户信息
                project.setCustomer(c);
                if (!StringUtils.isEmpty(project.getCustomer().getCustomerID()))
                        project.getCustomer().setUpdateCustInfo(true); //true:更新现有客户信息， false:添加新客户信息
                PurchaseCarIntention p = getUpdatedPurchaseCar();
                project.setPurchaseCarIntention(p);
                try {
                    b = ((RoilandCRMApplication) getActivity().getApplication()).getCRMManager().updateProjectInfo(project);
                } catch (ResponseException e) {
                    responseException = e;
                }

                return b;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (responseException != null) {
                    Toast.makeText(getActivity(), responseException.getMessage(),
                        Toast.LENGTH_SHORT).show();
                    noDataModify = true;
                    //更新未成功 编辑状态
                    carInfoAdapter.setEditable(true);
                    customerInfoAdapter.setEditable(true);
                    editFlag = true;
                    isSuccess = false;
                    return;
                } else if (result.booleanValue()) {
                    //更新成功 显示状态
                    newToggleBtnClick = false;
                    bottomBar.setVisible(true);
                    carInfoAdapter.setEditable(false);
                    customerInfoAdapter.setEditable(false);
                    item.setIcon(R.drawable.edit_btn_selector);
                    DialogUtils.alert(getActivity(), getString(R.string.oppotunity_info), getString(R.string.opt_success), new Runnable() {
                        
                        @Override
                        public void run() {
                        	isFirstSubmitNewCar = false;
                        	isSubmitNewCar = false;
                        	displayCustomerInfo(custFlag);
                        	displayCarInfo(carFlag);
                        }
                    });
                    //提示是否同步联系人姓名和客户名称
//                    DialogUtils.alert(activity, getString(R.string.oppotunity_info),
//                        getString(R.string.opt_success), new Runnable() {
//
//                            @Override
//                            public void run() {
//                                DialogUtils.confirm(activity, getString(R.string.prompts), getString(R.string.issynchro_prompts),
//                                    //点击了yes
//                                    new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (crmManager == null)
//                                                crmManager = ((RoilandCRMApplication) ((Activity) activity)
//                                                        .getApplication()).getCRMManager();
//                                                Customer customer = getUpdatedCustomer();
//                                                customer.setProjectID(project.getCustomer()
//                                                .getProjectID());
//                                            customer.setCustomerID(project.getCustomer()
//                                                .getCustomerID());
//                                            try {
//                                                crmManager.syncContacter(project.getCustomer()
//                                                    .getProjectID(), customer);
//                                            } catch (ResponseException e) {
//                                                e.printStackTrace();
//                                            }
//                                            //用Intent将获得数据传到Activity中
//                                            ScOppoInfoActivity oppoActivity = (ScOppoInfoActivity) getActivity();
//                                            Intent intent = new Intent();
//                                            Bundle bundle = new Bundle();
//                                            bundle.putParcelable("newprojectinfo", project);
//                                            intent.putExtra("IsNeedUpdateList", oppoActivity.isNeedUpdateList);
//                                            intent.putExtras(bundle);
//                                            getActivity().setResult(11, intent);
//                                            getActivity().finish();
//                                        }
//                                        
//                                    }, 
//                                    //点击了no
//                                    new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            //用Intent将获得数据传到Activity中
//                                            ScOppoInfoActivity oppoActivity = (ScOppoInfoActivity) getActivity();
//                                            Intent intent = new Intent();
//                                            Bundle bundle = new Bundle();
//                                            bundle.putParcelable("newprojectinfo", project);
//                                            intent.putExtra("IsNeedUpdateList", oppoActivity.isNeedUpdateList);
//                                            intent.putExtras(bundle);
//                                            getActivity().setResult(11, intent);
//                                            getActivity().finish();
//                                        }
//                                        
//                                    });
//                            }
//                        });

                }
            }
        }.execute();
    }

    /**
     * 
     * <pre>
     * 异步线程处理,调用API检查客户是否已经存在
     * </pre>
     *
     */
    public void confirmProjectExistenceAndSave() {
        new BaseTask<String>(getActivity()) {

            @Override
            protected String doInBackground(String... params) {

                CRMManager manager = ((RoilandCRMApplication) ((Activity) activity)
                    .getApplication()).getCRMManager();
                try {
                    Customer customer = getUpdatedCustomer();
                    if (customer != null && !StringUtils.isEmpty(customer.getCustomerID())) {
                        return null;
                    }
                    Map<String, String> map = manager.isExistProject(customer.getCustMobile(),
                        customer.getCustOtherPhone());
                    String isExisting = map.get("isExisting");
                    if (isExisting != null && isExisting.equals("true")) {
                        String userName = map.get("userName");
                        String customerStatus = map.get("custStatus");
                        return getString(R.string.set_up_warning_1) + userName
                               + getString(R.string.set_up_warning_2) + customerStatus;
                    }
                } catch (ResponseException e) {
                    responseException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                if (responseException != null) {
                    noDataModify = true;
                    Toast.makeText(getActivity(), responseException.getMessage(),
                        Toast.LENGTH_SHORT).show();
                }
                if (StringUtils.isEmpty(result)) {
                    saveNewProjectInfo();
                    customerInfoAdapter.setEditable(false);
                    carInfoAdapter.setEditable(false);
                    followPlanAdapter.setEditable(false);
                    item.setIcon(R.drawable.edit_btn_selector);
                    editFlag = false;
                    isSuccess = false;
                } else {
                    noDataModify = true;
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    isSuccess = false;
                    customerInfoAdapter.setEditable(true);
                    carInfoAdapter.setEditable(true);
                    followPlanAdapter.setEditable(true);
                }
            }
        }.execute();
    }

    /**
     * 
     * <pre>
     * 获得销售线索中的客户信息
     * </pre>
     *
     * @return 客户信息
     */
    public Customer getCustomer() {
        if (project != null && project.getCustomer() != null) {
            return project.getCustomer();
        }
        return new Customer();
    }
    
    /**
     * 
     * <pre>
     * 递交新车后 生日 市县 地址 现用车 车型 车身颜色 内饰 底盘号 发动机号 证件类型 证件号码 不能为空校验
     * </pre>
     *
     * @return 其中任何一项为空 就返回true
     */
    private String isSubmitNewCarValidation(){
        if(isSubmitNewCar() || isSubmitNewCar){
            project.setCustomer(getUpdatedCustomer());
            project.setPurchaseCarIntention(getUpdatedPurchaseCar());
            //递交新车 生日 市县 地址 现用车 车型 车身颜色 内饰 底盘号 发动机号 证件类型 证件号码 不能为空
            if(StringUtils.isEmpty(project.getCustomer().getBirthday())){
            	return getString(R.string.birthday_not_empty);
            }else if(StringUtils.isEmpty(project.getCustomer().getProvince())){
            	return getString(R.string.province_not_empty);
            }else if(StringUtils.isEmpty(project.getCustomer().getCity())){
            	return getString(R.string.city_not_empty);
            }else if(StringUtils.isEmpty(project.getCustomer().getAddress())){
            	return getString(R.string.address_not_empty);
            }else if(StringUtils.isEmpty(project.getCustomer().getExistingCar())){
            	return getString(R.string.exist_car_not_empty);
            }else if(StringUtils.isEmpty(project.getCustomer().getIdType())){
            	return getString(R.string.id_type_not_empty);
            }else if(StringUtils.isEmpty(project.getCustomer().getIdNumber())){
            	return getString(R.string.id_number_not_empty);
            }else if(StringUtils.isEmpty(project.getPurchaseCarIntention().getModel())){
            	return getString(R.string.model_not_empty);
            }else if(StringUtils.isEmpty(project.getPurchaseCarIntention().getInsideColor())){
            	return getString(R.string.inside_not_empty);
            }else if(StringUtils.isEmpty(project.getPurchaseCarIntention().getOutsideColor())){
            	return getString(R.string.outside_not_empty);
            }else if(StringUtils.isEmpty(project.getPurchaseCarIntention().getChassisNo())){
            	return getString(R.string.chassis_no_not_empty);
            }else if(StringUtils.isEmpty(project.getPurchaseCarIntention().getEngineNo())){
            	return getString(R.string.engine_no_not_empty);
            }
        }
		return "";
    }
    /**
     * 
     * <pre>
     * 客户信息校验
     * </pre>
     *
     * @return errString 提示信息
     */
    private String customInfoValidation() {
        int count;//循环次数
        String errString1 = null;//隐藏出现错误的提示信息
        String errString2 = null;//更多出现错误的提示信息
        String errString = null;//错误提示信息

        String strTemp = null;//老客户选择
        String strTemp2 = null;//大客户
        if(customerInfoCaches != null){
            count = customerInfoCaches.size();
        }else{
            count = customerInfo.size();
        }
        for (int i = 0; i < count; i++) {
            if(customerInfoCaches == null){
                String temp = null; //移动电话
            	String tempt = null; //其他电话
            	if(addFlag){
            		temp = customerInfo.get(5).value;
            		tempt = customerInfo.get(6).value;
            	}else{
            		temp = customerInfo.get(4).value;
            		tempt = customerInfo.get(5).value;
            	}
                BasicInfoListAdapter.Info info = customerInfo.get(i);
                String item = info.key;
                errString1 = DataVerify.infoValidation(item, info.value, orderId,
                    orderStatus, strTemp, strTemp2, getActivity(), false, false, false);
                if (item.equals(getString(R.string.custOtherPhone))) {
                    if (StringUtils.isEmpty(temp) && StringUtils.isEmpty(tempt)) {
                        errString1 = getString(R.string.dataverify_phonenumber_musthave);
                    }
                }
            }else{
                BasicInfoListAdapter.Info infoCaches = customerInfoCaches.get(i);
                String itemCaches = infoCaches.key;
                String temp = null; //移动电话
            	String tempt = null; //其他电话
                if(addFlag){
            		temp = customerInfo.get(5).value;
            		tempt = customerInfo.get(6).value;
            	}else{
            		temp = customerInfo.get(4).value;
            		tempt = customerInfo.get(5).value;
            	}
                if (itemCaches.equals(getString(R.string.custOtherPhone))) {
                    if (StringUtils.isEmpty(temp) && StringUtils.isEmpty(tempt)) {
                        errString1 = getString(R.string.dataverify_phonenumber_musthave);
                        }
                    }
                if (itemCaches.equals(getString(R.string.regularCustTag))) {
                    strTemp = infoCaches.value;
                }
                if (itemCaches.equals(getString(R.string.bigCustTag))) {
                    strTemp2 = infoCaches.value;
                }
                errString2 = DataVerify.infoValidation(itemCaches, infoCaches.value, orderId,
                    orderStatus, strTemp, strTemp2, getActivity(), false, false,false);
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

    /**
     * 
     * <pre>
     * 购车信息校验
     * </pre>
     *
     * @return errString 提示信息
     */
    private String carInfoValidation() {
        String errString = null;//错误信息提示
        String errString1 = null;//隐藏出现错误的提示信息
        String errString2 = null;//产生订单时  品牌，车型，车身颜色，内饰，配置不可为空
        String errString3 = null;//更多出现错误的提示信息
        String strTemp = null;//放弃销售机会
        int count;//循环次数
        boolean isGiveUpChecked = getIsGiveUpChecked();
        boolean hasProjectActive = false;//用于判断是否有活动订单
        if(mHasProjectActive != null){
            hasProjectActive = true;
        }else{
            hasProjectActive = false;
        }
        if(carInfoCaches != null){
            count = carInfoCaches.size();
        }else{
            count = carInfo.size();
        }
        for (int i = 0; i < count; i++) {
            if(carInfoCaches != null){
                BasicInfoListAdapter.Info infoCaches = carInfoCaches.get(i);
                String item = infoCaches.key;   
                String valueCache = infoCaches.value;
                //Long型日期转成“yyyy-MM-dd”型
                if (item.contains(getString(R.string.preorderDate)) || item.contains(getString(R.string.finish_preorderDate)) || item.contains(getString(R.string.pickupDate))) {
                    valueCache = DateFormatUtils.formatDate(infoCaches.value);
                }
                if (item.contains(getString(R.string.giveupTag))) {
                    strTemp = infoCaches.value;
                }  
                errString3 = DataVerify.infoValidation(item, valueCache, null, null,
                    strTemp, null, getActivity(), isGiveUpChecked, false,hasProjectActive);
                if (getString(R.string.orderStatus_finish).equals(errString3)) {
                    //如果签订订单，进行以下校验
                    for (int k = 0; k < carInfoCaches.size(); k++) {
                        String items = carInfoCaches.get(k).key;
                        String values = carInfoCaches.get(k).value;
                        orderStatus = getString(R.string.orderStatus_finish);
                        errString2 = DataVerify.infoValidationCars(items, values, orderStatus);
                        if (!StringUtils.isEmpty(errString2)
                            && errString3.equals(getString(R.string.orderStatus_finish))) {
                            break;
                        }
                    }
                } 
            }else{
                BasicInfoListAdapter.Info info = carInfo.get(i);
                String item = info.key;
                String value = info.value;
                //Long型日期转成“yyyy-MM-dd”型
                if (item.contains(getString(R.string.preorderDate)) || item.contains(getString(R.string.finish_preorderDate)) || item.contains(getString(R.string.pickupDate))) {
                    value = DateFormatUtils.formatDate(info.value);
                }  
                errString1 = DataVerify.infoValidation(item, value, null, null,
                    strTemp, null, getActivity(), isGiveUpChecked, false,hasProjectActive);
                if (getString(R.string.orderStatus_finish).equals(errString1)) {
                    //如果签订订单，进行以下校验
                        PurchaseCarIntention purchaseCarIntention = getUpdatedPurchaseCar();
                        orderStatus = getString(R.string.orderStatus_finish);
                        errString2 = DataVerify.infoValidationCars(getResources().getString(R.string.model), purchaseCarIntention.getModel(), orderStatus);
                        if (!StringUtils.isEmpty(errString2)) {
                            errString = errString2;
                            break;
                        }
                        errString2 = DataVerify.infoValidationCars(getResources().getString(R.string.outsideColor), purchaseCarIntention.getOutsideColor(), orderStatus);
                        if (!StringUtils.isEmpty(errString2)) {
                            errString = errString2;
                            break;
                        }
                        errString2 = DataVerify.infoValidationCars(getResources().getString(R.string.insideColor), purchaseCarIntention.getInsideColor(), orderStatus);
                        if (!StringUtils.isEmpty(errString2)) {
                            errString = errString2;
                            break;
                        }
                        errString2 = DataVerify.infoValidationCars(getResources().getString(R.string.carConfiguration), purchaseCarIntention.getCarConfiguration(), orderStatus);
                        if (!StringUtils.isEmpty(errString2)) {
                            errString = errString2;
                            break;
                        }
                } 
            }
            if (!StringUtils.isEmpty(errString2)) {
                errString = errString2;
                break;
            }else if(!StringUtils.isEmpty(errString1) && !getString(R.string.orderStatus_finish).equals(errString1)){
                errString = errString1;
                errString1 = "";
                break;
            }else if(!StringUtils.isEmpty(errString3) && !errString3.equals(getString(R.string.orderStatus_finish))){
                errString = errString3;
                errString3 = "";
                break;
            } else if (!StringUtils.isEmpty(errString)
                       && !getString(R.string.orderStatus_finish).equals(errString)) {
                break;
            }
        }
        if(StringUtils.isEmpty(errString)){
        	errString = isSubmitNewCarValidation();
        }
        return errString;
    }

    /**
     * 
     * <pre>
     * 跟踪计划校验
     * </pre>
     *
     * @return errString 提示信息
     */
    private String followInfoValidation() {
        String errString = null;
        if(followPlanInfo != null){
            for (int i = 0; i < followPlanInfo.size(); i++) {
            String item = followPlanInfo.get(i).key;
            errString = DataVerify.infoValidation(item, followPlanInfo.get(i).value, null, null,
                null, null, getActivity(), false, false,false);
            if (!StringUtils.isEmpty(errString)) {
                break;
            }
        }
        }
        return errString;
    }

    public Customer getCustomerInfo() {
        return customer;
    }

    public void checkFlowStatus(boolean flag) {
        this.FlowStatusFlg = flag;
    }

    public boolean getFlowStatusFlg() {
        return FlowStatusFlg;
    }

    public void changeValueClear() {
        if (!StringUtils.isEmpty(changeValue)) {
            changeValue = "";
        }
    }
    
    

    public Boolean getIsGiveUpChecked() {
        return isGiveUpChecked;
    }

    public int getContacterPage() {
        return contacterPage;
    }

    public void setContacterPage(int contacterPage) {
        this.contacterPage = contacterPage;
    }

    public void sendBottomBar(BottomBar mBottomBar) {
        this.bottomBar = mBottomBar;
    }

    public void displayCustomer(Customer customer) {
        if (project == null) {
            project = new Project();
        }
        project.setCustomer(customer);
        displayCustomerInfo(custFlag);
    }
    
    public boolean isOldCust() {
        return isOldCust;
    }

    public void setOldCust(boolean isOldCust) {
        this.isOldCust = isOldCust;
    }
    
    public void setOldeCustCollect(boolean oldCust){
    	this.oldCustCollect = oldCust;
    }

    /**
     * 获得最新客户详细信息
     * @return result 客户信息
     */
    public Customer getUpdatedCustomer(){
        Customer result;
        result = getCustomer();
        
        for (BasicInfoListAdapter.Info info : customerInfo){
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
            }else if(info.key.equals(getString(R.string.collectFrom)) && info.pairKey != null){
                result.setCollectFromCode(info.pairKey);
                result.setCollectFrom(info.value);
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
                if(!(StringUtils.isEmpty(info.value) && isGetBirthFromId)){
                    result.setBirthday(info.value);
                }else{
                    continue;
                }
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
                result.setPosition(info.value);
                result.setPositionCode(info.pairKey);
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
                result.setEnterpType(info.value);
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
                result.setRebuyStoreCustTag(("true").equals(info.value));
            else if (info.key.equals("网络重购客户"))
                result.setRebuyOnlineCustTag(("true").equals(info.value));
            else if (info.key.equals("置换客户标识"))
                result.setChangeCustTag(("true").equals(info.value));
            else if (info.key.equals("老客户推荐标识"))
                result.setRegularCustTag(("true").equals(info.value));
            else if (info.key.equals("老客户选择") && info.pairKey != null){
                result.setRegularCustCode(info.pairKey);
                result.setRegularCust(info.value);
            }else if (info.key.equals("个贷客户标识"))
                result.setLoanCustTag(("true").equals(info.value));
            else if (info.key.equals("总部VIP客户"))
                result.setHeaderQuartCustTag(("true").equals(info.value));
            else if (info.key.equals("大客户"))
                result.setBigCustTag(("true").equals(info.value));
            else if (info.key.equals("大客户选择") && info.pairKey != null){
                result.setBigCustsCode(info.pairKey);
                result.setBigCusts(info.value);
            }else if (info.key.equals("备注"))
                result.setCustComment(info.value);
        }
        return result;
    }
    /**
     * 获得最新客户购车信息
     * @return result 客户购车信息
     */
    public PurchaseCarIntention getUpdatedPurchaseCar(){
        PurchaseCarIntention result = new PurchaseCarIntention();
        if(project != null && project.getPurchaseCarIntention() != null){
            result = project.getPurchaseCarIntention();
        }
        for (BasicInfoListAdapter.Info info : carInfo){
            if (info.key.equals(getString(R.string.brand_1)) && info.pairKey != null){
                result.setBrand(info.value);
                result.setBrandCode(info.pairKey);   
            }else if (info.key.equals(getString(R.string.model)) && info.pairKey != null){
                result.setModel(info.value);
                result.setModelCode(info.pairKey);
            }else if (info.key.equals(getString(R.string.outsideColor)) && info.pairKey != null){
                result.setOutsideColor(info.value);
                result.setOutsideColorCode(info.pairKey);
            }else if (info.key.equals(getString(R.string.insideColor)) && info.pairKey != null){
                result.setInsideColorCode(info.pairKey);
                result.setInsideColor(info.value);
            }else if (info.key.equals(getString(R.string.insideColorCheck))){
                result.setInsideColorCheck(Boolean.parseBoolean(info.value));
            }else if (info.key.equals(getString(R.string.carConfiguration)) && info.pairKey != null){
                result.setCarConfigurationCode(info.pairKey);
                result.setCarConfiguration(info.value);
            }else if (info.key.equals(getString(R.string.salesQuote))){
                result.setSalesQuote((info.value));
            }else if(info.key.equals(getString(R.string.dealPriceInterval)) && info.pairKey != null){
                result.setDealPriceInterval(info.value);
                result.setDealPriceIntervalCode(info.pairKey);
            }else if(info.key.equals(getString(R.string.payment)) && info.pairKey != null){
                result.setPayment(info.value);
                result.setPaymentCode(info.pairKey);
            }else if (info.key.equals(getString(R.string.preorderCount))){
                result.setPreorderCount(info.value);
            }else if (info.key.equals(getString(R.string.preorderDate)) || info.key.equals(getString(R.string.finish_preorderDate))){
                if(!StringUtils.isEmpty(info.value)){
                    if(TextUtils.isDigitsOnly(info.value)){
                        result.setPreorderDate(StringUtils.getDateTrimNullLong(info.value));
                    }else{
                        result.setPreorderDate(DateFormatUtils.parseDateToLong(info.value));
                    } 
                }
            }else if (info.key.equals(getString(R.string.flowStatus)) && info.pairKey != null){
                result.setFlowStatus(info.value);
                result.setFlowStatusCode(info.pairKey);
            }else if (info.key.equals(getString(R.string.dealPossibility))){
                result.setDealPossibility(info.value);
            }else if (info.key.equals(getString(R.string.purchMotivation)) && info.pairKey != null){
                result.setPurchMotivation(info.value);
                result.setPurchMotivationCode(info.pairKey);
            }else if (info.key.equals(getString(R.string.chassisNo))){
                    result.setChassisNo(info.value);
            }else if (info.key.equals(getString(R.string.engineNo))){
                    result.setEngineNo(info.value);
            }else if (info.key.equals(getString(R.string.licensePlate))){
                    result.setLicensePlate(info.value);
            }else if (info.key.equals(getString(R.string.licenseProp)) && info.pairKey != null){
                result.setLicenseProp(info.value);
                result.setLicensePropCode(info.pairKey);
            }else if (info.key.equals(getString(R.string.pickupDate)))
                result.setPickupDate(info.value);
            else if (info.key.equals(getString(R.string.preorderTag)))
                result.setPreorderTag(info.value);
            else if (info.key.equals(getString(R.string.giveupTag))){
                result.setGiveupTag(("true").equals(info.value));
            }else if (info.key.equals(getString(R.string.giveupReason)) && info.pairKey != null){
                result.setGiveupReasonCode(info.pairKey);
                result.setGiveupReason(info.value);
            }else if (info.key.equals(getString(R.string.invoiceTitle)))
                result.setInvoiceTitle(info.value);
            else if (info.key.equals(getString(R.string.comment))){
                result.setProjectComment(info.value);
            }
        }

        return result;
    }
        /**
         * 
         * <pre>
         * 获得最新跟踪计划详细信息
         * </pre>
         *
         * @return result 跟踪计划信息
         */
        public TracePlan getUpdatedTracePlan() {
            TracePlan result = new TracePlan();
            if(tracePlan != null){
                result = tracePlan;
            }

            for (BasicInfoListAdapter.Info info : followPlanInfo) {
                if (info.key.equals(infokeylist1[3]) && info.pairKey != null) {
                    result.setActivityTypeCode(info.pairKey);
                    result.setActivityType(info.value);
                } else if (info.key.equals(infokeylist1[4])) {
                    result.setExecuteTime(StringUtils.getDateTrimNullLong(info.value));
                } else if (info.key.equals(infokeylist1[5]) && info.pairKey != null) {
                    result.setExecuteStatusCode(info.pairKey);
                    result.setExecuteStatus(info.value);
                } else if (info.key.equals(infokeylist1[6])) {
                    result.setActivityContent(info.value);
                } else if (info.key.equals(infokeylist1[7])) {
                    result.setContactResultCode(info.pairKey);
                    result.setContactResult(info.value);
                } else if (info.key.equals(infokeylist1[8])) {
                    result.setCustFeedback(info.value);
                } else if (info.key.equals(infokeylist1[9])) {
                    result.setLeaderComment(info.value);
                }
            }
            return result;
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
            item = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getResources().getString(R.string.address));
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
        /**
         * 
         * <pre>
         * 监听页面数据改变
         * </pre>
         *
         * @param key 标签
         * @param value 数据
         * @param pairKey code值  可以为空
         */
        
        public void dataModify(String key,String value,String pairKey){
            Log.i(tag, "-----"+key+"-----"+value+"-----"+pairKey+"-----");
            BaseInfoRowViewItem item = null;

            /**省区市县行政区划联动**/
            //省区
            if(getString(R.string.province).equals(key)){
            	province = value;
            	city = "";
            	district = "";
            	addAddress(province,city,district);
                for(int i=1;i<addressInfoList.length;i++){
                    item = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(addressInfoList[i]);
                    if(i == 1){
                        item.btnDown.setEnabled(true);
                        item.txtValue.setEnabled(true);
                    }else{
                        item.btnDown.setEnabled(false);
                        item.txtValue.setEnabled(false);
                    }
                    item.setValue("");
                    item.setParentKey(pairKey);
                    item.setParentKey2("");
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
            	addAddress(province,city,district);
                item = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.district));
                item.btnDown.setEnabled(true);
                item.txtValue.setEnabled(true);
                item.setValue("");
                item.setParentKey2(pairKey);
                for(BasicInfoListAdapter.Info info : customerInfo){
                    if(info.key.equals(getString(R.string.district))){
                        info.value = "";
                        info.pairKey = "";
                        break;
                    }
                }
            }
            //行政区划
            if(getString(R.string.district).equals(key)){
            	district = value;
            	addAddress(province,city,district);
            }
            /**生日联动**/
            if(customerInfoAdapter.getEditable()){
              //先选证件类型再输入证件号码
                if(getString(R.string.idNumber).equals(key)){
                    if(noDataModify){
                        noDataModify = false;
                        return ;
                    }
                    item = (BaseInfoRowViewItem) mCustomInfo.findViewWithTag(getString(R.string.idType));
                    if(getString(R.string.id_card).equals(item.txtValue.getText().toString())){
                        BaseInfoRowViewItem itemBirthday = (BaseInfoRowViewItem) mCustomInfo.findViewWithTag(getString(R.string.birthday));
                        if(DataVerify.personIdValid(value)){
                            itemBirthday.setValue(DateFormatUtils.getBirthFromId(value));
                            project.getCustomer().setBirthday(DateFormatUtils.getBirthFromId(value));
                            isGetBirthFromId = true;
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
           
            
            //先输入证件号码再选证件类型
            if(getString(R.string.idType).equals(key)){
                item = (BaseInfoRowViewItem) mCustomInfo.findViewWithTag(getString(R.string.idType));
                if(getString(R.string.id_card).equals(item.txtValue.getText().toString())){
                    BaseInfoRowViewItem itemBirthday = (BaseInfoRowViewItem) mCustomInfo.findViewWithTag(getString(R.string.birthday));
                    BaseInfoRowViewItem itemIdNumber = (BaseInfoRowViewItem) mCustomInfo.findViewWithTag(getString(R.string.idNumber));
                    if(DataVerify.personIdValid(itemIdNumber.txtValue.getText().toString())){
                        itemBirthday.setValue(DateFormatUtils.getBirthFromId(itemIdNumber.txtValue.getText().toString()));
                        project.getCustomer().setBirthday(DateFormatUtils.getBirthFromId(itemIdNumber.txtValue.getText().toString()));
                        isGetBirthFromId = true;
                        for(BasicInfoListAdapter.Info info : customerInfo){
                            if(info.key.equals(getString(R.string.birthday))){
                                info.value = DateFormatUtils.getBirthFromId(itemIdNumber.txtValue.getText().toString());
                                break;
                            }
                        }
                    }
                }
            }
            
            /** 品牌，车型，内饰颜色，外室颜色，配置联动 **/
            //品牌
            if(getString(R.string.brand_1).equals(key)){
                for(int i=1;i<5;i++){
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(carInfoStrList1[i]);
                    if(item != null){
                        if(i == 4){
                            item.btnDown.setEnabled(false);
                            item.txtValue.setEnabled(false);
                        }
                        if(i != 4){
                            item.btnDown.setEnabled(true);
                            item.txtValue.setEnabled(true);
                        }
                        item.setValue("");
                        item.setParentKey(pairKey);
                        item.setParentKey2(null);
                        if(project != null && project.getPurchaseCarIntention() != null){
                            project.getPurchaseCarIntention().setModel("");
                            project.getPurchaseCarIntention().setModelCode("");
                            project.getPurchaseCarIntention().setOutsideColor("");
                            project.getPurchaseCarIntention().setOutsideColorCode("");
                            project.getPurchaseCarIntention().setInsideColor("");
                            project.getPurchaseCarIntention().setInsideColorCode("");
                            project.getPurchaseCarIntention().setCarConfiguration("");
                            project.getPurchaseCarIntention().setCarConfigurationCode("");
                        }
                        for(BasicInfoListAdapter.Info info : carInfo){
                            for(int j=1;j<5;j++){
                                if(info.key.equals(carInfoStrList1[j])){
                                    info.value = "";
                                    info.pairKey = "";
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            //车型
            if(getString(R.string.model).equals(key)){
                item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(carInfoStrList1[4]);
                if(item != null){
                    item.setValue("");
                    item.btnDown.setEnabled(true);
                    item.txtValue.setEnabled(true);
                    item.setParentKey2(pairKey);
                    project.getPurchaseCarIntention().setCarConfiguration("");
                    project.getPurchaseCarIntention().setCarConfigurationCode("");
                    for(BasicInfoListAdapter.Info info : carInfo){
                        if(info.key.equals(carInfoStrList1[4])){
                            info.value = "";
                            info.pairKey = "";
                        }
                    }
                }
            }
            
            //监听流程状态改变
            if(getString(R.string.flowStatus).equals(key) ){
                //操作未成功时的流程状态
                final String mFlowStatus = project.getPurchaseCarIntention().getFlowStatus();
                final String mFlowStatusCode = project.getPurchaseCarIntention().getFlowStatusCode();
                final String mPreorderDate = String.valueOf(project.getPurchaseCarIntention().getPreorderDate() != 0 ? project.getPurchaseCarIntention().getPreorderDate() : null);
                if(project != null)
                project.setPurchaseCarIntention(getUpdatedPurchaseCar());
                //流程状态为签订单
                if(getString(R.string.flowStatus_4).equals(value)){
                    if(StringUtils.isEmpty(project.getPurchaseCarIntention().getBrand()) ||
                            StringUtils.isEmpty(project.getPurchaseCarIntention().getModel()) ||
                            StringUtils.isEmpty(project.getPurchaseCarIntention().getOutsideColor()) ||
                            StringUtils.isEmpty(project.getPurchaseCarIntention().getInsideColor()) ||
                            StringUtils.isEmpty(project.getPurchaseCarIntention().getCarConfiguration()) ||
                            project == null){
                        Toast.makeText(getActivity(), getString(R.string.dataverify_hasorder), Toast.LENGTH_LONG).show();
                        item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.flowStatus));
                        item.setValue(mFlowStatus);
                        project.getPurchaseCarIntention().setFlowStatus(mFlowStatus);
                        project.getPurchaseCarIntention().setFlowStatusCode(mFlowStatusCode);
                        for(BasicInfoListAdapter.Info info : carInfo){
                            if(info.key.equals(key)){
                                info.value = mFlowStatus;
                                info.pairKey = mFlowStatusCode;
                                break;
                            }
                        }
                        return ;
                    }
                }
                
                //流程状态为递交新车
                if(getString(R.string.flowStatus_1).equals(value)){
                	//递交新车判断订单中的车辆信息和购车意向中的信息是否相符
                	isFlowStatus(item,mFlowStatus,mFlowStatusCode);
                	if(isSubmitNewCar){
                		checkOrderInfo();
                	}else{
                	    return;
                	}
                }else{
                    isSubmitNewCar = false;
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.finish_preorderDate));
                    if(item == null){
                        item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.preorderDate));
                    }
                    if(item != null){
                        item.setKey(getString(R.string.preorderDate));
                        //当流程状态不是递交新车时 可以编辑预购日期
                        item.setEditable(true);
                        item.setValue(mPreorderDate); 
                    }
                    for(BasicInfoListAdapter.Info info : carInfo){
                        if(getString(R.string.preorderDate).equals(info.key)){
                            info.setValue(mPreorderDate);
                        }}
                    for(int i=0;i<12;i++){
                            item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(infokeylist2[i]);
                            if(item != null){
                                item.setBlackColor();
                            }
                        
                    }
                }
                item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.dealPossibility));
                newPairKey = pairKey;
                getDicDealPossibility(item, true);
            }

            //放弃销售机会相应操作
            if(getString(R.string.giveupTag).equals(key)){
                if("true".equals(value)){
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.giveupReason));
                    if(item != null){
                        for(BasicInfoListAdapter.Info info : carInfo){
                            if(getString(R.string.giveupReason).equals(info.key)){
                                info.required = true;
                            }
                        }
                        item.btnDown.setEnabled(true);
                        item.txtValue.setEnabled(true);
                        item.setRequired(true);
                        item.setRedColor();
                    }
                   
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.preorderDate));
                    if(item != null){
                        item.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                        item.setKey(getString(R.string.lose_date));
                        item.btnDown.setEnabled(false);
                        item.txtValue.setEnabled(false);
                        for(BasicInfoListAdapter.Info info : carInfo){
                            if(getString(R.string.preorderDate).equals(info.key)){
                                info.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                                info.setKey(getString(R.string.lose_date));
                            }
                        }  
                    }
                    
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.flowStatus));
                    item.btnDown.setEnabled(false);
                    item.txtValue.setEnabled(false);
                    DialogUtils.confirm(getActivity(), "", getString(R.string.dormancy_warning), 
                        new Runnable() {
                            
                            @Override
                            public void run() {
                                if (project == null) {
                                    project = new Project();
                                    project.setCustomer(new Customer());
                                    project.setPurchaseCarIntention(new PurchaseCarIntention());
                                }
                                project.getCustomer().setDormancy(true);
                            }
                        }, null);
                }else{
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.giveupReason));
                    item.btnDown.setEnabled(false);
                    item.txtValue.setEnabled(false);
                    item.setValue("");
                    item.setBlackColor();
                    for(BasicInfoListAdapter.Info info : carInfo){
                        if(info.key.equals(getString(R.string.giveupReason))){
                            info.value = "";
                            info.required = false;
                        }
                    }
                    
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.preorderDate)); 
                    if(item == null){
                        item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.finish_preorderDate));
                    }
                    if(item == null){
                        item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.lose_date));
                    }
                    item.btnDown.setEnabled(true);
                    item.txtValue.setEnabled(true);
                    item.setKey(getString(R.string.preorderDate));
                    for(BasicInfoListAdapter.Info info : carInfo){
                    	if(info.key.equals(getString(R.string.lose_date))){
                    		info.key = getString(R.string.preorderDate);
                    	}
                    }
                    
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.flowStatus));
                    item.btnDown.setEnabled(true);
                    item.txtValue.setEnabled(true);
                }
                
            }
            
            //老客户推荐
            if(key.equals(getString(R.string.regularCustTag))){
                item = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.regularCust));
                if("true".equals(value)){
                    item.setRedColor();
                    item.btnDown.setEnabled(true);
                    item.txtValue.setEnabled(true);
                    for(BasicInfoListAdapter.Info info : customerInfo){
                        if(info.key.equals(getString(R.string.regularCust))){
                            info.required = true;
                            break;
                        }
                    }
                }else{
                    item.setBlackColor();
                    item.btnDown.setEnabled(false);
                    item.txtValue.setEnabled(false);
                    item.setValue("");
                    for(BasicInfoListAdapter.Info info : customerInfo){
                        if(info.key.equals(getString(R.string.regularCust))){
                            info.value = "";
                            info.pairKey = "";
                            info.required = false;
                            break;
                        }
                    }
                }
            }
            
            //大客户选择
            if(key.equals(getString(R.string.bigCustTag))){
                item = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.bigCusts));
                if("true".equals(value)){
                    item.setRedColor();
                    item.btnDown.setEnabled(true);
                    item.txtValue.setEnabled(true);
                    for(BasicInfoListAdapter.Info info : customerInfo){
                        if(info.key.equals(getString(R.string.bigCusts))){
                            info.required = true;
                            break;
                        }
                    }
                }else{
                    item.setBlackColor();
                    item.btnDown.setEnabled(false);
                    item.txtValue.setEnabled(false);
                    item.setValue("");
                    for(BasicInfoListAdapter.Info info : customerInfo){
                        if(info.key.equals(getString(R.string.bigCusts))){
                            info.value = "";
                            info.pairKey = "";
                            info.required = false;
                            break;
                        }
                    }
                }
            }
            
            if(key.equals(getString(R.string.custType)) && getString(R.string.custType1).equals(value)){
                item = (BaseInfoRowViewItem) mCustomInfo.findViewWithTag(getString(R.string.idType));
                if(item != null){
                    item.setValue(getString(R.string.organization_id));
                }
                for(BasicInfoListAdapter.Info info : customerInfo){
                    if(info.key.equals(getString(R.string.idType))){
                        info.value = getString(R.string.organization_id);
                        info.pairKey = "B505B3C0-A9FE-4E6C-925E-15E77190371A";
                        break;
                    }
                }
                project.getCustomer().setIdType(getString(R.string.organization_id));
                project.getCustomer().setIdTypeCode("B505B3C0-A9FE-4E6C-925E-15E77190371A");
            }
            
            /**跟踪计划**/
            //执行时间
            if(key.equals(getString(R.string.executetime))){
                if(Long.parseLong(value) < DateFormatUtils.systemDate()){
                    Toast.makeText(getActivity(), getString(R.string.execute_time_small), Toast.LENGTH_LONG).show();
                    item = (BaseInfoRowViewItem)mFollowInfo.findViewWithTag(key);
                    item.setValue(String.valueOf(DateFormatUtils.systemDate()));
                    for(BasicInfoListAdapter.Info info : followPlanInfo){
                        if(key.equals(info.key)){
                            info.value = DateFormatUtils.formatSystemDate();
                            break;
                        }
                    }
                }
            }
            
        }
        /**
         * 
         * <pre>
         * 流程状态为递交新车进行的一些列操作
         * </pre>
         *
         * @param item 当前item
         * @param mFlowStatusCode 递交新车前状态code
         * @param mFlowStatus 递交新车前状态
         */
        private void isFlowStatus(BaseInfoRowViewItem item, String mFlowStatus, String mFlowStatusCode){
            isFirstSubmitNewCar = true;
            String errString = null;
            errString = DataVerify.checkOrder(project.getPurchaseCarIntention().getOrderStatus(),project.getPurchaseCarIntention().isHasActiveOrder());
            if(!StringUtils.isEmpty(errString)){
                Toast.makeText(getActivity(), errString, Toast.LENGTH_SHORT).show();
                item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.flowStatus));
                if (item != null) {
                    item.setValue(mFlowStatus);
                    item.setParentKey(mFlowStatusCode);
                    item.setParentKey2(mFlowStatusCode);
                }
                for(BasicInfoListAdapter.Info info : carInfo){
                    if(getString(R.string.flowStatus).equals(info.key)){
                        info.setValue(mFlowStatus);
                        info.setPairKey(mFlowStatusCode);
                        project.getPurchaseCarIntention().setFlowStatus(mFlowStatus);
                        project.getPurchaseCarIntention().setFlowStatusCode(mFlowStatusCode);
                        break;
                    }
                    
                }
                return;
            }else{
                isSubmitNewCar = true;
                item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.preorderDate));
                if(item == null){
                    item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.finish_preorderDate));
                    if(item == null){
                        item = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.lose_date));
                    }
                }
                item.setKey(getString(R.string.finish_preorderDate));
                item.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                for(BasicInfoListAdapter.Info info : carInfo){
                    if(info.key.equals(getString(R.string.preorderDate))){
                        info.key = getString(R.string.finish_preorderDate);
                        info.value = String.valueOf(DateFormatUtils.systemDate());
                        break;
                    }
                }
                BaseInfoRowViewItem itemTemp ;
                //存在活动订单 底盘号和发动机号带入
              if(project != null && project.getPurchaseCarIntention()!= null && getmHasProjectActive() != null){
                  project.getPurchaseCarIntention().setChassisNo(getmHasProjectActive().getPurchaseCarIntention().getChassisNo());
                  project.getPurchaseCarIntention().setEngineNo(getmHasProjectActive().getPurchaseCarIntention().getEngineNo());
               }
                for(BasicInfoListAdapter.Info info : carInfo){
                    if(getString(R.string.preorderDate).equals(info.key)){
                        info.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                    }
                    //递交新车 生日 市县 地址 现用车 车型 车身颜色 内饰 底盘号 发动机号 证件类型 证件号码 不能为空
                    if(getString(R.string.model).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.model));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.outsideColor).equals(info.key) ){
                        itemTemp = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.outsideColor));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.insideColor).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.insideColor));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.cust_chassis).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.cust_chassis));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.engineNo).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(getString(R.string.engineNo));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }
                }
                for(BasicInfoListAdapter.Info info : customerInfo){
                    //递交新车 生日 市县 地址 现用车 车型 车身颜色 内饰 底盘号 发动机号 证件类型 证件号码 不能为空
                    if(getString(R.string.birthday).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.birthday));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.province).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.province));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.city).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.city));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.address).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.address));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.existingCar).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.existingCar));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.idType).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.idType));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }else if(getString(R.string.idNumber).equals(info.key)){
                        itemTemp = (BaseInfoRowViewItem)mCustomInfo.findViewWithTag(getString(R.string.idNumber));
                        itemTemp.setRedColor();
                        itemTemp.setRequired(true);
                        info.required = true;
                    }
                }
                return;
            }
        
        
        }
        /**
         * 
         * <pre>
         * 根据流程状态设置成交可能性，异步线程调用API
         * </pre>
         *
         * @param item 传入列表item
         * @param flag
         */
        public void getDicDealPossibility(final BaseInfoRowViewItem item,final Boolean flag){
            new BaseTask<List<Dictionary>>(this.getActivity()) {
                @Override
                protected List<Dictionary> doInBackground(String... arg0) {
                    List<Dictionary> resultList = null;
                    TextView textView =new TextView(activity);
                    textView.setText(carInfoStrList1[9]);
                    if (crmManager == null)
                        crmManager = ((RoilandCRMApplication)((Activity) activity).getApplication()).getCRMManager();
                    try {
                        resultList = crmManager.getDictionariesByType(BaseInfoRowViewItem.getDicName(textView));
                    } catch (ResponseException e) {
                        responseException = e;
                    }
                    return resultList;
                }
                @Override
                protected void onPostExecute(List<Dictionary> result) {
                    super.onPostExecute(result);
                    Log.i(tag, "saveNewProjectInfo: "+String.valueOf(result));

                    if (responseException != null) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        for(int k=0;k<result.size();k++){
                            if(flag){
                                if(result.get(k).getDicKey().equals(newPairKey)){
                                    if (item != null)
                                        item.setValue(result.get(k).getDicValue());
                                        project.getPurchaseCarIntention().setDealPossibility(result.get(k).getDicValue());
                                        for (BasicInfoListAdapter.Info info : carInfo) {
                                            if(getString(R.string.dealPossibility).equals(info.key)){
                                                info.value = result.get(k).getDicValue();
                                            }
                                        }
                                }
                            }else{
                                if (item != null)
                                    item.setValue("");  
                            }
                        }
                    }
                }
            }.execute();
        }

        public Project getmHasProjectActive() {
            return mHasProjectActive;
        }

        public void setmHasProjectActive(Project mHasProjectActive) {
            this.mHasProjectActive = mHasProjectActive;
        }
        
        private void checkOrderInfo(){
            project.setPurchaseCarIntention(getUpdatedPurchaseCar());
            if(!project.getPurchaseCarIntention().getBrandCode().equals(mHasProjectActive.getPurchaseCarIntention().getBrandCode()) 
                    || !project.getPurchaseCarIntention().getModelCode().equals(mHasProjectActive.getPurchaseCarIntention().getModelCode()) 
                    || !project.getPurchaseCarIntention().getOutsideColorCode().equals(mHasProjectActive.getPurchaseCarIntention().getOutsideColorCode())
                    || !project.getPurchaseCarIntention().getInsideColorCode().equals(mHasProjectActive.getPurchaseCarIntention().getInsideColorCode())
                    || !project.getPurchaseCarIntention().getCarConfigurationCode().equals(mHasProjectActive.getPurchaseCarIntention().getCarConfigurationCode())){
                DialogUtils.confirm(getActivity(), getString(R.string.prompts), getString(R.string.synchronization),new Runnable() {
                    
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                },null);
            }
        }
        
        private Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        BaseInfoRowViewItem itemCar = null;
                        for(BasicInfoListAdapter.Info info : carInfo){
                            if(info.key.equals(getString(R.string.brand_1))){
                                itemCar = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(info.key);
                                info.value = mHasProjectActive.getPurchaseCarIntention().getBrand();
                                info.pairKey = mHasProjectActive.getPurchaseCarIntention().getBrandCode();
                                itemCar.setValue(info.value);
                                continue;
                            }else if(info.key.equals(getString(R.string.model))){
                                itemCar = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(info.key);
                                info.value = mHasProjectActive.getPurchaseCarIntention().getModel();
                                info.pairKey = mHasProjectActive.getPurchaseCarIntention().getModelCode();
                                itemCar.setValue(info.value);
                                continue;
                            }else if(info.key.equals(getString(R.string.outsideColor))){
                                itemCar = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(info.key);
                                info.value = mHasProjectActive.getPurchaseCarIntention().getOutsideColor();
                                info.pairKey = mHasProjectActive.getPurchaseCarIntention().getOutsideColorCode();
                                itemCar.setValue(info.value);
                                continue;
                            }else if(info.key.equals(getString(R.string.insideColor))){
                                itemCar = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(info.key);
                                info.value = mHasProjectActive.getPurchaseCarIntention().getInsideColor();
                                info.pairKey = mHasProjectActive.getPurchaseCarIntention().getInsideColorCode();
                                itemCar.setValue(info.value);
                                continue;
                            }else if(info.key.equals(getString(R.string.carConfiguration))){
                                itemCar = (BaseInfoRowViewItem)mCarInfo.findViewWithTag(info.key);
                                info.value = mHasProjectActive.getPurchaseCarIntention().getCarConfiguration();
                                info.pairKey = mHasProjectActive.getPurchaseCarIntention().getCarConfigurationCode();
                                itemCar.setValue(info.value);
                                continue;
                            }
                        }
                        break;
                    case 1:
                    	checkMust();
                    	break;
                }
            }
            
        };
        
        private void checkMust(){
        	String customMessage = "";
            String carMessage = "";
            String followMessage = "";
        	//客户信息保存前校验
            customMessage = customInfoValidation(); 
            if(!StringUtils.isEmpty(customMessage)){
                item.setIcon(R.drawable.save_selector);
                editFlag = true;
                isSuccess = false;
                Toast.makeText(getActivity(), customMessage, Toast.LENGTH_SHORT).show();
                return;
            } 
            //购车信息保存前校验
            carMessage = carInfoValidation();
            if(!StringUtils.isEmpty(carMessage)){
                item.setIcon(R.drawable.save_selector);
                editFlag = true;
                isSuccess = false;
                Toast.makeText(getActivity(), carMessage, Toast.LENGTH_SHORT).show();
                return;
            }
            //跟踪计划保存前校验
            if(newToggleBtnClick){
                followMessage = followInfoValidation();
                if(!StringUtils.isEmpty(followMessage)){
                    item.setIcon(R.drawable.save_selector);
                    editFlag = true;
                    isSuccess = false;
                    Toast.makeText(getActivity(), followMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            
            
            
            if(StringUtils.isEmpty(customMessage) && StringUtils.isEmpty(carMessage) && StringUtils.isEmpty(followMessage)){
                saveProjectInfo();
               //保存成功设置成显示状态
                carInfoAdapter.setEditable(false);
                customerInfoAdapter.setEditable(false);
                isSuccess = true;
            }
        }
        
}
