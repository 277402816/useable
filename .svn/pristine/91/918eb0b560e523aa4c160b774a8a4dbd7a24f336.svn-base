package com.roiland.crm.sm.ui.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.ui.adapter.SmCustomerOrderInfoAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客户订单fragment
 * </pre>
 * @extends Fragment
 * @author liuyu
 * @version $Id: SmCustomerOrderInfoFragment.java, v 0.1 2013-5-16 下午4:59:31 liuyu Exp $
 */

@SuppressLint({ "ValidFragment", "SimpleDateFormat" })
public class SmCustomerOrderInfoFragment extends SherlockFragment {
	private SmCustomerOrderInfoAdapter customerOrderInfoAdapter;
	private CustOrder custOrder;
	private Boolean isOrderDetail;
	LinearLayout orderInfoList;
	public CRMManager crmManager;
	private ImageButton MoreBtn;
	private ImageButton HideBtn;
	
	public SmCustomerOrderInfoFragment(CustOrder custOrder) {
		this.custOrder = custOrder;
	}
	public SmCustomerOrderInfoFragment() {
	       
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
		View view = inflater.inflate(R.layout.sm_customer_order_info, container, false);
		isOrderDetail = false;
		orderInfoList = (LinearLayout)(view.findViewById(R.id.customer_order_info_list));
		if (customerOrderInfoAdapter == null) {
			customerOrderInfoAdapter = new SmCustomerOrderInfoAdapter(this.getActivity());
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
		    customerOrderInfoAdapter.addItem("客户名称", (customer != null && customer.getCustName() != null ? customer.getCustName() : ""));
		    customerOrderInfoAdapter.addItem("客户移动电话", (customer != null && customer.getCustName() != null ? customer.getCustMobile() : ""));
		    customerOrderInfoAdapter.addItem("客户其他电话", (customer != null && customer.getCustName() != null ? customer.getCustOtherPhone() : ""));
		    customerOrderInfoAdapter.addItem("订单号", (custOrder != null && custOrder.getOrderID() != null ? custOrder.getOrderID() : ""));
		    customerOrderInfoAdapter.addItem("品牌", (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getBrand() : ""));
		    customerOrderInfoAdapter.addItem("车型", (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getModel() : ""));
		    customerOrderInfoAdapter.addItem("配置", (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getCarConfiguration() : ""));
		    customerOrderInfoAdapter.addItem("客户订单类别", (custOrder != null && custOrder.getOrderType() != null ? custOrder.getOrderType() : ""));
		    String preDeliveryDateString = null;
            if (custOrder != null && !StringUtils.isEmpty(custOrder.getPreDeliveryDate())) {
              try {
                    preDeliveryDateString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getPreDeliveryDate()));
                    customerOrderInfoAdapter.addItem("预计交车日期", (preDeliveryDateString != null ? preDeliveryDateString : ""));
                  } catch (Exception e) {
                      e.printStackTrace();
                      }
               }
            customerOrderInfoAdapter.addItem("订金", (custOrder != null && custOrder.getDeposit() != null ? custOrder.getDeposit() : ""));
            customerOrderInfoAdapter.addItem("订单状态", (custOrder != null && custOrder.getOrderStatus() != null ? custOrder.getOrderStatus() : ""));
            customerOrderInfoAdapter.addItem("订单匹配编号", (custOrder != null && custOrder.getMatchingVehicleID() != null ? custOrder.getMatchingVehicleID() : ""));
            customerOrderInfoAdapter.addItem("底盘号", (custOrder != null && custOrder.getMatchingChassisNo() != null ? custOrder.getMatchingChassisNo() : "")); 
           } else {
               customerOrderInfoAdapter.clear();
               orderInfoList.removeAllViews();
               customerOrderInfoAdapter.addItem("客户名称", (customer != null && customer.getCustName() != null ? customer.getCustName() : ""));
               customerOrderInfoAdapter.addItem("客户移动电话", (customer != null && customer.getCustName() != null ? customer.getCustMobile() : ""));
               customerOrderInfoAdapter.addItem("客户其他电话", (customer != null && customer.getCustName() != null ? customer.getCustOtherPhone() : ""));
               customerOrderInfoAdapter.addItem("证件号码", (customer != null && customer.getIdNumber() != null ? customer.getIdNumber() : ""));
               customerOrderInfoAdapter.addItem("发票客户名称", (custOrder != null && custOrder.getInvoiceTitle() != null ? custOrder.getInvoiceTitle() : ""));
               customerOrderInfoAdapter.addItem("订单号", (custOrder != null && custOrder.getOrderID() != null ? custOrder.getOrderID() : ""));
               customerOrderInfoAdapter.addItem("客户订单类别", (custOrder != null && custOrder.getOrderType() != null ? custOrder.getOrderType() : ""));
               customerOrderInfoAdapter.addItem("订单车辆类别", (custOrder != null && custOrder.getOrderVehiType() != null ? custOrder.getOrderVehiType() : ""));
               customerOrderInfoAdapter.addItem("品牌", (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getBrand() : ""));
               customerOrderInfoAdapter.addItem("车型", (purchaseCarIntention != null && purchaseCarIntention.getBrand() != null ? purchaseCarIntention.getModel() : ""));
               
               customerOrderInfoAdapter.addItem("外饰颜色", (purchaseCarIntention != null && purchaseCarIntention.getOutsideColor() != null ? purchaseCarIntention.getOutsideColor().trim() : ""));
               customerOrderInfoAdapter.addItem("内饰颜色", (purchaseCarIntention != null && purchaseCarIntention.getInsideColor() != null ? purchaseCarIntention.getInsideColor().trim() : ""));
               customerOrderInfoAdapter.addItem("内饰颜色必选", (purchaseCarIntention != null && purchaseCarIntention.getInsideColorCheck() != null ? String.valueOf(purchaseCarIntention.getInsideColorCheck().booleanValue()) : "false"));
               customerOrderInfoAdapter.addItem("配置", (purchaseCarIntention != null && purchaseCarIntention.getCarConfiguration() != null ? purchaseCarIntention.getCarConfiguration().trim() : ""));
               customerOrderInfoAdapter.addItem("装潢", (custOrder != null && custOrder.getDecorate() != null ? custOrder.getDecorate() : ""));
               customerOrderInfoAdapter.addItem("订单优先级", (custOrder != null && custOrder.getOrderPriority() != null ? custOrder.getOrderPriority() : ""));
               String preDeliveryDateString = null;
               if (custOrder != null && !StringUtils.isEmpty(custOrder.getPreDeliveryDate())) {
                 try {
                       preDeliveryDateString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getPreDeliveryDate()));
                       customerOrderInfoAdapter.addItem("预计交车日期", (preDeliveryDateString != null ? preDeliveryDateString : ""));
                     } catch (Exception e) {
                         e.printStackTrace();
                         }
                  }
               String custExpeDeliDateString = null;
             if (custOrder != null && !StringUtils.isEmpty(custOrder.getCustExpeDeliDate())) {
                 try {
                     custExpeDeliDateString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getCustExpeDeliDate()));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }   
               customerOrderInfoAdapter.addItem("希望交车时间", (custOrder != null && custExpeDeliDateString != null ? custExpeDeliDateString : ""));
               customerOrderInfoAdapter.addItem("交车地点", (custOrder != null && custOrder.getDeliveryPlace() != null ? custOrder.getDeliveryPlace() : ""));
               customerOrderInfoAdapter.addItem("订金", (custOrder != null && custOrder.getDeposit() != null ? custOrder.getDeposit() : ""));
               customerOrderInfoAdapter.addItem("报价", (custOrder != null && custOrder.getOfferPrice() != null ? custOrder.getOfferPrice() : ""));
               customerOrderInfoAdapter.addItem("付款方式", (custOrder != null && custOrder.getPayment() != null ? custOrder.getPayment() : ""));
               customerOrderInfoAdapter.addItem("订单匹配编号", (custOrder != null && custOrder.getMatchingVehicleID() != null ? custOrder.getMatchingVehicleID() : ""));
               customerOrderInfoAdapter.addItem("订单状态", (custOrder != null && custOrder.getOrderStatus() != null ? custOrder.getOrderStatus() : ""));
               customerOrderInfoAdapter.addItem("底盘号", (custOrder != null && custOrder.getMatchingChassisNo() != null ? custOrder.getMatchingChassisNo() : ""));
               customerOrderInfoAdapter.addItem("自购车标识", (custOrder != null ? String.valueOf(custOrder.getBuyForSelfFlag()) : null ));
               customerOrderInfoAdapter.addItem("订单流失标识", (custOrder != null ? String.valueOf(custOrder.getOrderFailureFlag()) : null ));
               customerOrderInfoAdapter.addItem("订单流失原因", (custOrder != null && custOrder.getOrderFailure() != null ? custOrder.getOrderFailure() : ""));
               String orderSignTimeString = null;
             if (custOrder != null && !StringUtils.isEmpty(custOrder.getOrderSignTime())) {
                 try {
                     orderSignTimeString = DateFormatUtils.formatDate(Long.parseLong(custOrder.getOrderSignTime()));
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }   
               customerOrderInfoAdapter.addItem("签订时间", (orderSignTimeString != null ? orderSignTimeString : ""));
               
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
