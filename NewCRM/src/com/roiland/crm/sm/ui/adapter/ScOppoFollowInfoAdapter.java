package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.ui.widget.EditTextViewItem;
import com.roiland.crm.sm.ui.widget.SelectTextViewItem;
import com.roiland.crm.sm.ui.widget.TextViewItem;
import com.roiland.crm.sm.utils.DateFormatUtils;

/**
 * 
 * <pre>
 * 销售线索跟踪计划Adapter
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScOppoFollowInfoAdapter.java, v 0.1 2013-6-24 下午12:36:55 shuang.gao Exp $
 */
public class ScOppoFollowInfoAdapter extends BaseAdapter{
    public static String[] infokeylist2 ;
    ArrayList<BasicInfoListAdapter.Info> oppoInfoList;
   Context context;
   boolean editData;
   int size;
   public boolean editFlag = false;

   public TracePlan getTracePlan(){
       TracePlan tracePlan = new TracePlan();
       tracePlan.setActivityType(oppoInfoList.get(0).value);
       tracePlan.setActivityTypeCode(oppoInfoList.get(0).pairKey);
       tracePlan.setExecuteTime(DateFormatUtils.parseDateToLong(oppoInfoList.get(1).value));
       tracePlan.setExecuteStatus(oppoInfoList.get(2).value);
       tracePlan.setExecuteStatusCode(oppoInfoList.get(2).pairKey);
       tracePlan.setActivityContent(oppoInfoList.get(3).value);
       tracePlan.setContactResult(oppoInfoList.get(4).value);
       tracePlan.setContactResultCode(oppoInfoList.get(4).pairKey);
       tracePlan.setCustFeedback(oppoInfoList.get(5).value);
       return tracePlan;
   }
   
   public void showEdit()
   {
       editFlag = true;
   }
   
   private void initKeyList(){
       infokeylist2 = new String[]{context.getString(R.string.activitytype),context.getString(R.string.executetime),context.getString(R.string.executestatus),context.getString(R.string.activitycontent),context.getString(R.string.contactresult),context.getString(R.string.custfeedback)};
       this.oppoInfoList = new ArrayList<BasicInfoListAdapter.Info>();
       for(int i=0; i<infokeylist2.length; i++){
           BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
           info.key = infokeylist2[i];
           info.value = "";
           info.pairKey = null;
           oppoInfoList.add(info);
       }
   }
   
   public ScOppoFollowInfoAdapter( ArrayList<String> l,Context context) {
       for (int i = 0; i < oppoInfoList.size(); i ++) {
           oppoInfoList.get(i).value = l.get(i);
       }
       this.context = context;
       initKeyList();
   }
   public ScOppoFollowInfoAdapter(Context context) {
       this.context = context;
       initKeyList();
   }
   
   public void setItem(String item, String data) {
       setItem(item, data, null);
   }
   public void setItem(String item, String data, String pairKey) {
       for (int i = 0; i < oppoInfoList.size(); i ++) {
           BasicInfoListAdapter.Info info = oppoInfoList.get(i);
           if (info.key.equals(item.replaceAll(":", "").trim())) {
               info.value = data;
               info.pairKey = pairKey;
           }
       }
   }

   
   @Override
   public int getCount() {
       size = oppoInfoList.size();
       return size;
   }

   @Override
   public Object getItem(int arg0) {
       if (arg0 < oppoInfoList.size()) {
       return oppoInfoList.get(arg0);
       } else {
           return null;
       }
   }

   @Override
   public long getItemId(int arg0) {
       return 0;
   }

   @Override
   public View getView(int arg0, View arg1, ViewGroup arg2) {

       TextViewItem textViewItem = null;
       if (arg1 == null) {
           textViewItem = getItemType(oppoInfoList.get(arg0).key);
       } else {
           textViewItem = (TextViewItem) arg1;
       }
       textViewItem.setKeyText(oppoInfoList.get(arg0).key + ":");
       textViewItem.setValue(oppoInfoList.get(arg0).value);
       textViewItem.setEditFlag(editFlag);
       textViewItem.setTag(oppoInfoList.get(arg0).key);
       
       
       return textViewItem;
   }
   //获得item类型
   public TextViewItem getItemType(String key){
       
       if(key.compareTo(context.getString(R.string.activitytype)) == 0 || 
               key.compareTo(context.getString(R.string.executetime)) == 0 ||
               key.compareTo(context.getString(R.string.executestatus)) == 0 ||
               key.compareTo(context.getString(R.string.contactresult)) == 0 
               ){
           SelectTextViewItem item=new SelectTextViewItem(context);
           if(!editData){
               item.transferData(editData);
           }
           if(key.compareTo(context.getString(R.string.activitytype))==0||
                   key.compareTo(context.getString(R.string.executetime)) == 0 ||
                   key.compareTo(context.getString(R.string.executestatus)) == 0 ){
               item.setMustItem();
           }
           return item;
       } else {
           EditTextViewItem item =new EditTextViewItem(context);
           
           if(key.compareTo(context.getString(R.string.activitycontent)) == 0){
               item.setMustItem();
           }
           return item;
       }
   }
   
   public void clearData()
   {
       for(int i=0; i<oppoInfoList.size(); i++){
           oppoInfoList.get(i).value = "";
           oppoInfoList.get(i).pairKey = null;
       }
   }

   public ArrayList<BasicInfoListAdapter.Info> getOppoInfoList() {
       return oppoInfoList;
   }
   public void transferStatus(boolean editData) {
       this.editData = editData;
   }
}
