package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info;
import com.roiland.crm.sm.utils.DateFormatUtils;

/**
 * 
 * <pre>
 * 车辆信息adapter
 * </pre>
 *
 * @author liuyu
 * @version $Id: SmCarInfoListAdapter.java, v 0.1 2013-8-2 下午4:55:21 liuyu Exp $
 */
public class SmCarInfoListAdapter extends BaseAdapter{
    ArrayList<BasicInfoListAdapter.Info> carInfoList;
    Context context;
    int size;
    public boolean moreFlag = true;
    public boolean editFlag = false;
    
    public PurchaseCarIntention getPurchaseCarIntention(PurchaseCarIntention p){
        PurchaseCarIntention purchaseCarIntention = null;
        if(p == null){
            purchaseCarIntention = new PurchaseCarIntention();
        }else{
            purchaseCarIntention = p;
        }

        purchaseCarIntention.setBrand(carInfoList.get(0).value);
        purchaseCarIntention.setBrandCode(carInfoList.get(0).pairKey);
        purchaseCarIntention.setModel(carInfoList.get(1).value);
        purchaseCarIntention.setModelCode(carInfoList.get(1).pairKey);
        purchaseCarIntention.setOutsideColor(carInfoList.get(2).value);
        purchaseCarIntention.setOutsideColorCode(carInfoList.get(2).pairKey);
        purchaseCarIntention.setInsideColor(carInfoList.get(3).value);
        purchaseCarIntention.setInsideColorCode(carInfoList.get(3).pairKey);
        purchaseCarIntention.setInsideColorCheck(Boolean.parseBoolean(carInfoList.get(4).value));
        purchaseCarIntention.setCarConfiguration(carInfoList.get(5).value);
        purchaseCarIntention.setCarConfigurationCode(carInfoList.get(5).pairKey);
        purchaseCarIntention.setSalesQuote(carInfoList.get(6).value);
        purchaseCarIntention.setDealPriceInterval(carInfoList.get(7).value);
        purchaseCarIntention.setDealPriceIntervalCode(carInfoList.get(7).pairKey);
        purchaseCarIntention.setPayment(carInfoList.get(8).value);
        purchaseCarIntention.setPaymentCode(carInfoList.get(8).pairKey);
        purchaseCarIntention.setPreorderCount(carInfoList.get(9).value);
        purchaseCarIntention.setPreorderDate(DateFormatUtils.parseDateToLong(carInfoList.get(10).value));
        purchaseCarIntention.setFlowStatus(carInfoList.get(11).value);
        purchaseCarIntention.setFlowStatusCode(carInfoList.get(11).pairKey);
        purchaseCarIntention.setDealPossibility(carInfoList.get(12).value);
        purchaseCarIntention.setPurchMotivation(carInfoList.get(13).value);
        purchaseCarIntention.setPurchMotivationCode(carInfoList.get(13).pairKey);
        purchaseCarIntention.setChassisNo(carInfoList.get(14).value);
        purchaseCarIntention.setEngineNo(carInfoList.get(15).value);
        purchaseCarIntention.setLicensePlate(carInfoList.get(16).value);
        purchaseCarIntention.setLicenseProp(carInfoList.get(17).value);
        purchaseCarIntention.setLicensePropCode(carInfoList.get(17).pairKey);
        purchaseCarIntention.setPickupDate(DateFormatUtils.parseDateToLongString(carInfoList.get(18).value));
        purchaseCarIntention.setPreorderTag(carInfoList.get(19).value);
        purchaseCarIntention.setGiveupTag(Boolean.parseBoolean(carInfoList.get(20).value));
        purchaseCarIntention.setGiveupReason(carInfoList.get(21).value);
        purchaseCarIntention.setGiveupReasonCode(carInfoList.get(21).pairKey);
        purchaseCarIntention.setInvoiceTitle(carInfoList.get(22).value);
        purchaseCarIntention.setProjectComment(carInfoList.get(23).value);
        
        return purchaseCarIntention;
    }
    
    public void setPurchaseCarIntentionInfo(PurchaseCarIntention p){
        carInfoList.get(0).value = p.getBrand();
        carInfoList.get(0).pairKey = p.getBrandCode();
        carInfoList.get(1).value = p.getModel();
        carInfoList.get(1).pairKey = p.getModelCode();
        carInfoList.get(2).value = p.getOutsideColor();
        carInfoList.get(2).pairKey = p.getOutsideColorCode();
        carInfoList.get(3).value = p.getInsideColor();
        carInfoList.get(3).pairKey = p.getInsideColorCode();
        carInfoList.get(4).value = String.valueOf(p.isInsideColorCheck());
        carInfoList.get(5).value = p.getCarConfiguration();
        carInfoList.get(5).pairKey = p.getCarConfigurationCode();
        carInfoList.get(6).value = p.getSalesQuote();
        carInfoList.get(7).value = p.getDealPriceInterval();
        carInfoList.get(7).pairKey = p.getDealPriceIntervalCode();
        carInfoList.get(8).value = p.getPayment();
        carInfoList.get(8).pairKey = p.getPaymentCode();
        carInfoList.get(9).value = p.getPreorderCount();
        carInfoList.get(10).value = DateFormatUtils.formatDate(p.getPreorderDate());
        carInfoList.get(11).value = p.getFlowStatus();
        carInfoList.get(11).pairKey = p.getFlowStatusCode();
        carInfoList.get(12).pairKey = p.getFlowStatusCode();
        carInfoList.get(12).value = p.getDealPossibility();
        carInfoList.get(13).value = p.getPurchMotivation();
        carInfoList.get(13).pairKey = p.getPurchMotivationCode();
        carInfoList.get(14).value = p.getChassisNo();
        carInfoList.get(15).value = p.getEngineNo();
        carInfoList.get(16).value = p.getLicensePlate();
        carInfoList.get(17).value = p.getLicenseProp();
        carInfoList.get(17).pairKey = p.getLicensePropCode();
        carInfoList.get(18).value = p.getPickupDate();
        carInfoList.get(19).value = p.getPreorderTag();
        carInfoList.get(20).value = String.valueOf(p.isGiveupTag());
        carInfoList.get(21).value = p.getGiveupReason();
        carInfoList.get(21).pairKey = p.getGiveupReasonCode();
        carInfoList.get(22).value = p.getInvoiceTitle();
        carInfoList.get(23).value = p.getProjectComment();
        notifyDataSetChanged();
    }
    public SmCarInfoListAdapter(Context context) {
        this(new ArrayList<BasicInfoListAdapter.Info>(), context);
    }       
    public SmCarInfoListAdapter(ArrayList<BasicInfoListAdapter.Info> carInfoList, Context context) {
        this.carInfoList = carInfoList;
        this.context = context;
    }
    
    public void addItem(String item, String data) {
        addItem(item, data, null);
    }
    
    public void addItem(String item, String data, String pairKey) {
        BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
        info.key = item;
        info.value = data;
        info.pairKey = pairKey;
        carInfoList.add(info);
        this.notifyDataSetChanged();
    }
    
    public void removeItem(String item, String data) {
        for (int i = 0; i < carInfoList.size(); i ++) {
            if (carInfoList.get(i).key.equals(item)) {
                carInfoList.remove(i);
            }
        }
        this.notifyDataSetChanged();
    }
    
    public void removeItem(int index) {
        carInfoList.remove(index);
        this.notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return carInfoList != null?carInfoList.size():0;
    }

    @Override
    public Object getItem(int arg0) {
        if (arg0 < carInfoList.size()) {
            return carInfoList.get(arg0);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sm_opp_info_item, null);
        }
        fillView(convertView,carInfoList.get(position));
        return convertView;
    }
    private void fillView(View convertView, Info info) {
        TextView lbName = (TextView)convertView.findViewById(R.id.lb_name);
        TextView custValue = (TextView)convertView.findViewById(R.id.cust_value);
        CheckBox ckValue = (CheckBox)convertView.findViewById(R.id.ck_value);
        if("内饰颜色必选".equals(info.key) ||
           "预定标识".equals(info.key) ||
           "放弃销售机会".equals(info.key)){
            custValue.setVisibility(View.GONE);
            ckValue.setVisibility(View.VISIBLE);
            lbName.setText(info.key+":");
            ckValue.setChecked(Boolean.parseBoolean(info.value));
        }else{
            lbName.setText(info.key+":");
            custValue.setText(info.value);
            ckValue.setVisibility(View.GONE);
        }
    }
    
    public void clearData(){
        carInfoList.clear();
    } 
}
