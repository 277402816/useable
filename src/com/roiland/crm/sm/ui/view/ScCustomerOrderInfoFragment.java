package com.roiland.crm.sm.ui.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScCustomerOrderInfoAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 *  客户订单fragment
 * </pre>
 * @extends Fragment
 * @author liuyu
 * @version $Id: ScCustomerOrderInfoFragment.java, v 0.1 2013-7-3 上午9:43:11 liuyu Exp $
 */
@SuppressLint({ "ValidFragment", "SimpleDateFormat" })
public class ScCustomerOrderInfoFragment extends Fragment {
	private ScCustomerOrderInfoAdapter customerOrderInfoAdapter;
	private CustOrder custOrder;
	private Boolean isOrderDetail;
	LinearLayout orderInfoList;
	public CRMManager crmManager;
	private ImageButton MoreBtn;
	private ImageButton HideBtn;
	
	public ScCustomerOrderInfoFragment(CustOrder custOrder) {
		this.custOrder = custOrder;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 页面创建时调用此方法
	 * @param inflater 用来加载页面的布局文件
	 * @param container 布局文件放的容器
	 * @param savedInstanceState
	 * @return view 加载后的页面
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sc_customer_order_info, container, false);
		isOrderDetail = false;
		orderInfoList = (LinearLayout)(view.findViewById(R.id.customer_order_info_list));
		if (customerOrderInfoAdapter == null) {
			customerOrderInfoAdapter = new ScCustomerOrderInfoAdapter(this.getActivity());
		}
		MoreBtn = (ImageButton)view.findViewById(R.id.customer_order_info_more_button);
		HideBtn = (ImageButton)view.findViewById(R.id.customer_order_info_hide_button);
		HideBtn.setVisibility(View.GONE);
		/**
		 * 点击“更多”按钮事件
		 */
		((ImageButton)(MoreBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isOrderDetail = !isOrderDetail;
				HideBtn.setVisibility(View.VISIBLE);
				MoreBtn.setVisibility(View.GONE);
				displayOrderInfo(isOrderDetail);
			}
		});
		
		/**
		 * 点击“隐藏”按钮事件
		 */
		((ImageButton)(HideBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isOrderDetail = !isOrderDetail;
				HideBtn.setVisibility(View.GONE);
				MoreBtn.setVisibility(View.VISIBLE);
				displayOrderInfo(isOrderDetail);
			}
		});
		displayOrderInfo(isOrderDetail);
		return view;
	}
	
	/**
	 * 
	 * <pre>
	 * 加载页面信息时调用
	 * </pre>
	 *
	 * @param isDetail 是否显示更多信息
	 */
	public void displayOrderInfo(Boolean isDetail) {
		Customer customer = null;
		PurchaseCarIntention purchaseCarIntention = null;
		if (custOrder != null) {
			customer = custOrder.getCustomer();
			purchaseCarIntention = custOrder.getPurchaseCarIntention();
		}
		
		//当isDetail为true显示“更多”信息
		if (!isDetail) {
		    customerOrderInfoAdapter.clear();
		    orderInfoList.removeAllViews();
		    customerOrderInfoAdapter.addItem(getString(R.string.custName), (customer != null && customer.getCustName() != null ? customer.getCustName() : ""));
		    customerOrderInfoAdapter.addItem(getString(R.string.cust_mobile_phone), (customer != null && customer.getCustName() != null ? customer.getCustMobile() : ""));
		    customerOrderInfoAdapter.addItem(getString(R.string.cust_other_phone), (customer != null && customer.getCustName() != null ? customer.getCustOtherPhone() : ""));
		    customerOrderInfoAdapter.addItem(getString(R.string.cust_order_id), (custOrder != null && custOrder.getOrderID() != null ? custOrder.getOrderID() : ""));
		    customerOrderInfoAdapter.addItem(getString(R.string.cust_brand), (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getBrand() : ""));
		    customerOrderInfoAdapter.addItem(getString(R.string.cust_model), (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getModel() : ""));
		    customerOrderInfoAdapter.addItem(getString(R.string.cust_configure), (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getCarConfiguration() : ""));
		    customerOrderInfoAdapter.addItem(getString(R.string.cust_order_type), (custOrder != null && custOrder.getOrderType() != null ? custOrder.getOrderType() : ""));
		    String preDeliveryDateString = null;
            if (custOrder != null && !StringUtils.isEmpty(custOrder.getPreDeliveryDate())) {
                    preDeliveryDateString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getPreDeliveryDate()));
                    customerOrderInfoAdapter.addItem(getString(R.string.cust_expect_date), (preDeliveryDateString != null ? preDeliveryDateString : ""));
               }else{
                   customerOrderInfoAdapter.addItem(getString(R.string.cust_expect_date), "");
               }
            customerOrderInfoAdapter.addItem(getString(R.string.cust_subscription), (custOrder != null && custOrder.getDeposit() != null ? custOrder.getDeposit() : ""));
            customerOrderInfoAdapter.addItem(getString(R.string.cust_order_status), (custOrder != null && custOrder.getOrderStatus() != null ? custOrder.getOrderStatus() : ""));
            customerOrderInfoAdapter.addItem(getString(R.string.cust_order_match_id), (custOrder != null && custOrder.getMatchingVehicleID() != null ? custOrder.getMatchingVehicleID() : ""));
            customerOrderInfoAdapter.addItem(getString(R.string.cust_chassis), (custOrder != null && custOrder.getMatchingChassisNo() != null ? custOrder.getMatchingChassisNo() : "")); 
           } else {
               customerOrderInfoAdapter.clear();
               orderInfoList.removeAllViews();
               customerOrderInfoAdapter.addItem(getString(R.string.custName), (customer != null && customer.getCustName() != null ? customer.getCustName() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_mobile_phone), (customer != null && customer.getCustName() != null ? customer.getCustMobile() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_other_phone), (customer != null && customer.getCustName() != null ? customer.getCustOtherPhone() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_paper_id), (customer != null && customer.getIdNumber() != null ? customer.getIdNumber() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.invoiceTitle), (custOrder != null && custOrder.getInvoiceTitle() != null ? custOrder.getInvoiceTitle() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_id), (custOrder != null && custOrder.getOrderID() != null ? custOrder.getOrderID() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_type), (custOrder != null && custOrder.getOrderType() != null ? custOrder.getOrderType() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_car_type), (custOrder != null && custOrder.getOrderVehiType() != null ? custOrder.getOrderVehiType() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_brand), (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getBrand() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_model), (purchaseCarIntention != null && purchaseCarIntention.getModel() != null ? purchaseCarIntention.getModel() : ""));
               
               customerOrderInfoAdapter.addItem(getString(R.string.outsideColor_1), (purchaseCarIntention != null && purchaseCarIntention.getOutsideColor() != null ? purchaseCarIntention.getOutsideColor().trim() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.insideColor), (purchaseCarIntention != null && purchaseCarIntention.getInsideColor() != null ? purchaseCarIntention.getInsideColor().trim() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.insideColorCheck), (purchaseCarIntention != null && purchaseCarIntention.getInsideColorCheck() != null ? String.valueOf(purchaseCarIntention.getInsideColorCheck().booleanValue()) : "false"));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_configure), (purchaseCarIntention != null && purchaseCarIntention.getCarConfiguration() != null ? purchaseCarIntention.getCarConfiguration().trim() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_decorate), (custOrder != null && custOrder.getDecorate() != null ? custOrder.getDecorate() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_prioritize), (custOrder != null && custOrder.getOrderPriority() != null ? custOrder.getOrderPriority() : ""));
               String preDeliveryDateString = null;
               if (custOrder != null && !StringUtils.isEmpty(custOrder.getPreDeliveryDate())) {
                 try {
                       preDeliveryDateString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getPreDeliveryDate()));
                       customerOrderInfoAdapter.addItem(getString(R.string.cust_expect_date), (preDeliveryDateString != null ? preDeliveryDateString : ""));
                     } catch (Exception e) {
                         ResponseException responseException = new ResponseException(e);
                         Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_LONG).show();
                         }
                  }
               String custExpeDeliDateString = null;
             if (custOrder != null && !StringUtils.isEmpty(custOrder.getCustExpeDeliDate())) {
                 try {
                     custExpeDeliDateString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getCustExpeDeliDate()));
                 } catch (Exception e) {
                	 ResponseException responseException = new ResponseException(e);
                     Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_LONG).show();
                 }
             }   
               customerOrderInfoAdapter.addItem(getString(R.string.cust_hope_time), (custOrder != null && custExpeDeliDateString != null ? custExpeDeliDateString : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_place), (custOrder != null && custOrder.getDeliveryPlace() != null ? custOrder.getDeliveryPlace() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_subscription), (custOrder != null && custOrder.getDeposit() != null ? custOrder.getDeposit() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_quote), (custOrder != null && custOrder.getOfferPrice() != null ? custOrder.getOfferPrice() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_pay_for_type), (custOrder != null && custOrder.getPayment() != null ? custOrder.getPayment() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_match_id), (custOrder != null && custOrder.getMatchingVehicleID() != null ? custOrder.getMatchingVehicleID() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_status), (custOrder != null && custOrder.getOrderStatus() != null ? custOrder.getOrderStatus() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_chassis), (custOrder != null && custOrder.getMatchingChassisNo() != null ? custOrder.getMatchingChassisNo() : ""));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_lose_flag), (custOrder != null ? String.valueOf(custOrder.getOrderFailureFlag()) : null ));
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_lose_reason), (custOrder != null && custOrder.getOrderFailure() != null ? custOrder.getOrderFailure() : ""));
               String orderSignTimeString = null;
             if (custOrder != null && !StringUtils.isEmpty(custOrder.getOrderSignTime())) {
                 try {
                     orderSignTimeString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getOrderSignTime()));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }   
               customerOrderInfoAdapter.addItem(getString(R.string.cust_order_time), (orderSignTimeString != null ? orderSignTimeString : ""));
               
           }
		for(int i=0;i<customerOrderInfoAdapter.getCount();i++){
		    orderInfoList.addView(customerOrderInfoAdapter.getView(i, null, null));
		}
	}
	/**
	 * 
	 * <pre>
	 * 获得订单信息
	 * </pre>
	 *
	 * @return
	 */
	public CustOrder getCustOrder() {
		return custOrder;
	}
	/**
	 * 
	 * <pre>
	 * 设置订单信息
	 * </pre>
	 *
	 * @param custOrder
	 */
	public void setCustOrder(CustOrder custOrder) {
		this.custOrder = custOrder;
	}
}
