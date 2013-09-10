package com.roiland.crm.sm.ui.widget;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.view.BaseTask;
import com.roiland.crm.sm.ui.widget.NameTextViewItem.DataChangeListener;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 类型为下拉列表的TextViewItem
 * </pre>
 *
 * @author Administrator
 * @version $Id: SelectTextViewItem.java, v 0.1 2013-8-6 上午10:30:07 Administrator Exp $
 */
public class SelectTextViewItem  extends TextViewItem{
	
	public ImageButton btn;
	private LinearLayout llt;
	int whichChoose = 0;
	int whichChoose2 = 0;
	String newValue;
	private WeakReference<DataChangeListener> weak;
	private List<Customer> customerList=null;
	public boolean locked = false;
	private String searchPairKeyKey = null;
	private String searchPairKeyKey2 = null;
	private boolean ediData = true;
	private boolean isDone = false;

	public SelectTextViewItem(Context context) {
		this(context,null,null);
		
	}
	
	private List<Dictionary> selectListString;
	private String[] selectList;
	private String[] keyList;
	private CRMManager crmManager;
	private int isChangeNo;
	private boolean isChangeFlg = false;
	public SelectTextViewItem(Context _context, String _key, String _value) {
		super(_context, _key, _value);
		this.weak = new WeakReference<DataChangeListener>((DataChangeListener) _context);
		//设置Item属性
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sc_list_item_selecttextinfo, this);
 
		this.key = (TextView) findViewById(R.id.keyItem);
		this.value = (TextView) findViewById(R.id.valueItem);
		if (_key != null)
			this.key.setText(_key);
		if (_value != null && !_value.equals("null"))
			this.value.setText(_value);
		llt = (LinearLayout)findViewById(R.id.list_item_selection);
		btn = (ImageButton) findViewById(R.id.returnitem);
		
		if(selectListString!=null){
			for(int i=0;i<selectListString.size();i++){
				selectListString.get(i).equals(this.value.getText());
				whichChoose =i;
			}
		}
		if(customerList!=null){
			for(int i=0;i<customerList.size();i++){
				customerList.get(i).equals(this.value.getText());
				whichChoose2 =i;
			}
		}
		
	}
	//下拉列表单击的监听事件
	private final View.OnClickListener mClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//判断是否locked，是则下拉列表无法点击。
			if(locked)
			{
				Log.i("SelectTextViewItem:","locked is true");
			}else
			{
			Log.i("SelectTextViewItem:","btn download");
			
			String str = key.getText().toString();
			int year = 0;     
		    int month = 0;     
		    int day = 0; 
		    //判断如果下拉列表包括如下关键字，则为日期类型下拉列表。
			if(!str.equals("方便联系时间:")&&(str.contains("日期") || str.contains("生日") || str.contains("时间"))){
				
				String datastr = getValue();
				if(!datastr.isEmpty())
				{
					try {
						String temp[] = datastr.split("-");
						if(temp.length == 3)
						{
							year =  Integer.parseInt(temp[0]);
							month =  Integer.parseInt(temp[1]) - 1;
							day =  Integer.parseInt(temp[2]);
						}
					}
			        catch (NumberFormatException e) {
			            e.printStackTrace();
			            year = 0;     
					    month = 0;     
					    day = 0; 
			        }
				}
				if(year == 0 || day == 0)
				{
					final Calendar c = Calendar.getInstance();     
				    year = c.get(Calendar.YEAR);     
				    month = c.get(Calendar.MONTH);     
				    day = c.get(Calendar.DAY_OF_MONTH); 
				}
				
									
			    new DatePickerDialog(context, mDateSetListener,     
                        year, month, day).show();   
			
			  //判断如果下拉列表包括如下关键字，则为老客户下拉列表。调用API。
			}else if(str.equals("老客户选择") || str.equals("老客户选择:")){
				new BaseTask<List<Customer>>((Activity) context) {

					@Override
					protected List<Customer> doInBackground(
							String... params) {
						
						try {
							if (crmManager == null){
								crmManager = ((RoilandCRMApplication)((Activity) context).getApplication()).getCRMManager();
							}
							customerList = crmManager.getOldCustomer();
						} catch (ResponseException e) {
						
							e.printStackTrace();
						}
						return customerList;
					}

					@Override
					protected void onPostExecute(List<Customer> result) {
						
						super.onPostExecute(result);
						if(customerList!=null){
							keyList = new String[customerList.size()];
							selectList = new String[customerList.size()];
							int n = customerList.size();
							for (int i = 0; i < n; i ++) {
								keyList[i] = customerList.get(i).getCustomerID();
								selectList[i] = customerList.get(i).getCustName();
							}
							if(customerList!=null){
								for(int i=0;i<customerList.size();i++){
									if(customerList.get(i).getCustName().equals(newValue)){
										whichChoose2 =i;
										
										break;
									}
									
								}
							}
							new AlertDialog.Builder(context)  
							.setTitle(key.getText().toString())  
							.setIcon(android.R.drawable.ic_dialog_info)                  
							.setSingleChoiceItems(selectList, whichChoose2,   
							  new DialogInterface.OnClickListener() {  
							                              
							     public void onClick(DialogInterface dialog, int which) {  
							        dialog.dismiss();
							        whichChoose2=which;
							        value.setText(selectList[which]);
							        newValue = value.getText().toString();
							        weak.get().dataModify(key.getText().toString(), selectList[which],keyList[which]);
							     }  
							  }  
							)  
							.setNegativeButton("取消", null)
							.show(); 
						}
					}
					
				}.execute();
				//下拉列表为数据字典检索的数据，调用数据字典API
			}else{
				
				new BaseTask<List<Dictionary>>((Activity) context) {
					@Override
					protected List<Dictionary> doInBackground(String... arg0) {
						
						List<Dictionary> items = null;
						try {
							if (crmManager == null)
								crmManager = ((RoilandCRMApplication)((Activity) context).getApplication()).getCRMManager();
							//下拉列表联动判断
							if(searchPairKeyKey != null && searchPairKeyKey2 == null)
							{
								Log.i("getRelativeDictionaries",BaseInfoRowViewItem.getCasecadeDicName(key.getText().toString().trim()) + " " + searchPairKeyKey);
								items = crmManager.getRelativeDictionaries(BaseInfoRowViewItem.getCasecadeDicName(key.getText().toString().trim()),searchPairKeyKey);
							}else if (searchPairKeyKey2 != null){
								items = crmManager.getRelativeDictionaries(BaseInfoRowViewItem.getCasecadeDicName(key.getText().toString().trim()),searchPairKeyKey,searchPairKeyKey2);
							}else{
								items = crmManager.getDictionariesByType(BaseInfoRowViewItem.getDicName(key));
							}
							
						} catch (ResponseException e) {
							e.printStackTrace();
						}
						selectListString = items;
						return items;
					}
					
					@Override
					protected void onPostExecute(List<Dictionary> result) {
						super.onPostExecute(result);
						
						if (selectListString != null)
						{
							selectList = new String[selectListString.size()];
							keyList = new String[selectListString.size()];
							for (int i = 0; i < selectListString.size(); i ++) {
								selectList[i] = selectListString.get(i).getDicValue();
								keyList[i] = selectListString.get(i).getDicKey();
							}
							if(selectListString!=null){
								for(int i=0;i<selectListString.size();i++){
									if(selectListString.get(i).getDicValue().equals(newValue)){
										whichChoose =i;
										
										break;
									}
									
								}
							}
							new AlertDialog.Builder(context)  
							.setTitle(key.getText().toString().trim())  
							.setIcon(android.R.drawable.ic_dialog_info)                  
							.setSingleChoiceItems(selectList, whichChoose,   
							  new DialogInterface.OnClickListener() {  
							                              
							     public void onClick(DialogInterface dialog, int which) {  
							        dialog.dismiss();
							        value.setText(selectList[which]);
							        whichChoose=which;
							        if(whichChoose!=isChangeNo){
										isChangeFlg =true;
									}
							        isChangeNo = which;
							        newValue = value.getText().toString();
							        //将数据返回相应的Activity
							        weak.get().dataModify(key.getText().toString(), selectList[which], keyList[which]);
							     }  
							  }  
							)  
							.setNegativeButton("取消", null)
							.show(); 
						}
					}
				}.execute();

			}
			}
		}
	};
	
    private DatePickerDialog.OnDateSetListener mDateSetListener =     
        new DatePickerDialog.OnDateSetListener() {     
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				
				Log.i("DatePickerDialog:",String.valueOf(arg1)+String.valueOf(arg2+1)+String.valueOf(arg3));
				StringBuilder data = new StringBuilder().append(arg1).append("-")  
	                .append(formateTime(arg2+1)).append("-")  
	                .append(formateTime(arg3));
				value.setText(data.toString());
				weak.get().dataModify(key.getText().toString(), data.toString());
			}
    };
    
    private String formateTime(int time) {  
    	  
        String timeStr = "";  
        if (time < 10){  
            timeStr = "0" + String.valueOf(time);  
        }else {  
            timeStr = String.valueOf(time);  
        }  
        return timeStr;  
     }  

	@Override
	public String getValue() {
		
		return value.getText().toString();
	}

	@Override
	public void setKeyText(String label) {
		
		this.key.setText(label);
	}

	public String getKeyText() {
		return this.key.getText().toString();
	}
	
	@Override
	public void setValue(String value) {
		
		if(!StringUtils.isEmpty(value)){
			if(value.equals("null")){
				value = "";
			}			
		}
		this.value.setText(value);
		newValue = this.value.getText().toString();
		
	}
	//判断初次载入时画面下拉列表活性
	@Override
	public void setEditFlag(boolean b) {
		
		this.editFlag = b;
		if(b){
			String str = key.getText().toString();
			if(!ediData && (str.contains("执行状态")||str.contains("流程状态")||str.contains("大客户选择")||str.contains("老客户选择"))){ //
				this.btn.setVisibility(View.GONE);
				if(str.contains("执行状态"))
					this.value.setText("未执行");
			}else if (isDone && (str.contains("成交日期")||str.contains("品牌")||str.contains("车型")||str.contains("车身颜色")||str.contains("内饰颜色")||str.contains("配置"))){
				this.btn.setVisibility(View.GONE);
			}else{
				if(!str.contains("成交可能性")){
					if (!ediData && str.contains("执行时间")){
						this.value.setText(DataVerify.systemDate());
					}
					this.btn.setVisibility(View.VISIBLE);
					llt.setOnClickListener(mClickListener);
					btn.setOnClickListener(mClickListener);
				}
			}
		}else{
			this.btn.setVisibility(View.GONE);
		}
	}
	//设置字体颜色：红色
	public void setMustItem(){
		this.key.setTextColor(Color.RED);
	}
	//设置字体颜色：黑色
	public void setBlackItem(){
		this.key.setTextColor(Color.BLACK);
	}
	
	public void setSearchPairKeyKey(String pkey)
	{
		searchPairKeyKey = pkey;
	}
	public void setSearchPairKeyKey2(String pkey)
	{
		searchPairKeyKey2 = pkey;
	}

	public void transferData(boolean editData) {
		
		this.ediData = editData;
	}
	public void setDoneFlag(boolean isDone) {
		
		this.isDone = isDone;
	}
	public boolean getChangeFlg() {
		
		return isChangeFlg;
	}
	public void setChangeFlg() {
		
		this.isChangeFlg = false;
	}
	
}
