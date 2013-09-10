package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.TextView.OnEditorActionListener;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;
/**
 * 
 * <pre>
 * 详细信息画面Adapter基础类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: BasicInfoListAdapter.java, v 0.1 2013-8-2 下午4:41:55 shuang.gao Exp $
 */
public class BasicInfoListAdapter extends BaseCacheListAdapter<BasicInfoListAdapter.Info> {


    public CRMManager crmManager;

	public static class Info {
		public int type = -1;
		public String key = "";
		public String value = "";
		public String pairKey = null;
		public Object data = null;
		public boolean required = false;
		public boolean editable = true;
		private Context context ;
		public int inputType = InputType.TYPE_TEXT_VARIATION_NORMAL;
		public OnEditorActionListener editorActionListener = null;
		public OnKeyListener editorKeyListener = null;
		public DataUpdateListener dataUpdateListener = null;
		public DataChangeListener dataChangeListener = null;
		public TextChangeListener textChangeListener = null;
		
		public Info() {
		}

		public Info(String key, int type, Object data, String value, boolean required) {
			this(key, type, data, value, required, true);
		}
		public Info(String key, int type, Object data, String value, boolean required, boolean editable) {
			this(key, type, data, value, required, editable, InputType.TYPE_CLASS_TEXT);
		}
		public Info(String key, int type, Object data, String value, boolean required, int inputType) {
			this(key, type, data, value, required, true, inputType);
		}
		public Info(String key, int type, Object data, String value, boolean required, boolean editable, int inputType) {
			this.key = key;
			this.value = value;
			this.type = type;
			this.required = required;
			this.editable = editable;
			this.pairKey = null;
			this.inputType = inputType;
			
		}
		
		public void setKey(String key){
		    this.key = key;
		}
		
		public void setValue(String value)
		{
			if (dataChangeListener != null && this.value != null && !this.value.equals(value)) {
				dataChangeListener.chagedData(key, value);
			}
			if (value == null)
				this.value = "";
			else this.value = value;
			if (dataUpdateListener != null) {
				dataUpdateListener.updatedData(key, value);
			}
		}
		public void setPairKey(String pairKey)
		{
			this.pairKey = pairKey;
		}
		public Long getLongValue()
		{
			if (StringUtils.isEmpty(value)) {
				return 0L;
			}
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				Log.e("BasicInfoListAdapter", "Parsing long exception.", e);
			}
			return null;
		}
		public String getDateLongStringValue()
		{
			Long dateLong = getDateLongValue();
			if (dateLong != null) {
				return String.valueOf(dateLong);
			} else {
				return null;
			}
		}
		public Long getDateLongValue()
		{
			Date date = getDateValue();
			if (date != null) {
				return date.getTime();
			} else {
				return null;
			}
		}
		public Date getDateValue(){
			return DateFormatUtils.parseDate(value);
		}
		public String getBooleanStringValue()
		{
			try {
				return String.valueOf(getBooleanValue());
			} catch (Exception e) {
				return String.valueOf(false);
			}
		}
		public Boolean getBooleanValue()
		{
			try {
				return Boolean.parseBoolean(value);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public interface DataUpdateListener{
			public void updatedData(String key, String value);
		}
		public interface DataChangeListener {
			public void chagedData(String key, String value);
		}
		public interface TextChangeListener {
			public void textChanged(String key, String value);
		}
	}
	public List<BasicInfoListAdapter.Info> dataList;
	public List<BaseInfoRowViewItem> viewList;
	Boolean editable;
	public View clickedTarget;
	
	   public BasicInfoListAdapter(Context context) {
	        this(context, new ArrayList<BasicInfoListAdapter.Info>());
	    }
	public BasicInfoListAdapter(Context context, List<BasicInfoListAdapter.Info> caches) {
        super(context, caches);
        BaseInfoRowViewItem tempViewItem;
        if (viewList == null) {
            viewList = new ArrayList<BaseInfoRowViewItem>();
            for (int i = 0; i < caches.size(); i ++) {
                tempViewItem = new BaseInfoRowViewItem(context);
                tempViewItem.crmManager = this.crmManager;
                tempViewItem.basicadapter = BasicInfoListAdapter.this;
                viewList.add(tempViewItem);
            }
        }
        setEditable(false);
    }

    /**
     * 
     * @param position item位置
     * @return
     * @see com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter#getItem(int)
     */
	public BasicInfoListAdapter.Info getItem(int position) {
		if (dataList != null) {
			return dataList.get(position);
		}
		return null;
	}
	   public void refresh() {
	        if (viewList != null && dataList != null) {
	            for (int i = 0; i < viewList.size(); i ++) {
	                fillView(viewList.get(i), dataList.get(i));
	            }
	        }
	    }
	   @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        fillView(viewList.get(position), dataList.get(position));
	        return viewList.get(position);
	    }

	    @Override
	    protected boolean fillView(View view, BasicInfoListAdapter.Info item) {
	        if (item != null && view.getClass() == BaseInfoRowViewItem.class) {
	            BaseInfoRowViewItem viewItem = (BaseInfoRowViewItem) view;
	            viewItem.crmManager = this.crmManager;
	            viewItem.basicadapter = BasicInfoListAdapter.this;
	            if (item.required) {
	                viewItem.txtKey.setTextColor(Color.RED);
	                viewItem.txtKeyTail.setTextColor(Color.RED);
	            } else {
	                viewItem.txtKey.setTextColor(Color.BLACK);
	                viewItem.txtKeyTail.setTextColor(Color.BLACK);
	            }
	            viewItem.btnDown.setEnabled(item.editable);
	            viewItem.btnRight.setEnabled(item.editable);
	            viewItem.edtValue.setEnabled(item.editable);
	            viewItem.chkValue.setEnabled(item.editable);
	            viewItem.mobileBtn.setEnabled(item.editable);
	            viewItem.pstnBtn.setEnabled(item.editable);
	            viewItem.layout.setEnabled(item.editable);
	            viewItem.txtValue.setEnabled(item.editable);
	            viewItem.btnDown.setEnabled(item.editable);
	            viewItem.setRequired(item.required);
	            viewItem.edtValue.setOnEditorActionListener(item.editorActionListener);
	            viewItem.edtValue.setOnKeyListener(item.editorKeyListener);
	            
	            viewItem.setKey(item.key);
	            viewItem.setValue(item.value);
	            
	            if(!item.editable){
                    viewItem.edtValue.setVisibility(View.GONE);
                    if(!item.key.equals(context.getString(R.string.lead_contacter)) 
                            && !item.key.equals(context.getString(R.string.rebuyStoreCustTag))
                            && !item.key.equals(context.getString(R.string.giveupTag))
                            && !item.key.equals(context.getString(R.string.dormancy))
                            && !item.key.equals(context.getString(R.string.preorderTag))
                            && !item.key.equals(context.getString(R.string.insideColorCheck))
                            ){
                        //非单选框才显示txtValue，否则单选框处显示false
                    viewItem.txtValue.setVisibility(View.VISIBLE);  
                    }
                }
	            viewItem.edtValue.setInputType(item.inputType);
	            if (item.type == BaseInfoRowViewItem.SELECTION_TYPE && item.data != null) {
	                viewItem.setSelectList((String[]) item.data);
	            }
	            viewItem.setTag(item.key);
	            if(item.key.equals(context.getString(R.string.contacter))){
	                viewItem.setEditTextFilter(context,item.key,26);
	            }else if( item.key.equals(context.getString(R.string.existLisenPlate))
	            		|| item.key.equals(context.getString(R.string.existingcarbrand))
	                    || item.key.equals(context.getString(R.string.engineNo))
	                    || item.key.equals(context.getString(R.string.licensePlate))){
                    viewItem.setEditTextFilter(context,item.key,20);
                }else if(item.key.equals(context.getString(R.string.address))){
                    viewItem.setEditTextFilter(context,item.key,51);
                }else if(item.key.equals(context.getString(R.string.cust_mobile_phone)) 
                        || item.key.equals(context.getString(R.string.contacter_mobiel))
                        || item.key.equals(context.getString(R.string.fax))){
                    viewItem.setEditTextFilter(context,item.key,13);
                }else if(item.key.equals(context.getString(R.string.cust_other_phone)) 
                        || item.key.equals(context.getString(R.string.contacter_other_phone))){
                    viewItem.setEditTextFilter(context,item.key,21);
                }else if(item.key.equals(context.getString(R.string.email)) 
                        || item.key.equals(context.getString(R.string.invoiceTitle))){
                    viewItem.setEditTextFilter(context,item.key,50);
                }else if(item.key.equals(context.getString(R.string.comment))){
                    viewItem.setEditTextFilter(context,item.key,200);
                }else if(item.key.equals(context.getString(R.string.salesQuote))){
                    viewItem.setEditTextFilter(context,item.key,9);
                }else if(item.key.equals(context.getString(R.string.chassisNo))){
                    viewItem.setEditTextFilter(context,item.key,18);
                }else if(item.key.equals(context.getString(R.string.activitycontent))){
                    viewItem.setEditTextFilter(context,item.key,51);
                }
                else if(item.key.equals(context.getString(R.string.custName))){
                    viewItem.setNameTextFilter(context,item.key,26);
                    viewItem.setEditTextFilter(context, item.key, 26);
                }else if(item.key.equals(context.getString(R.string.postcode))){
                    viewItem.setEditTextFilter(context,item.key,7);
                }else if(item.key.equals(context.getString(R.string.custfeedback))){
                    viewItem.setEditTextFilter(context,item.key,121);
                }else if(item.key.equals(context.getString(R.string.preorderCount))){
                    viewItem.setEditTextFilter(context,item.key,3);
                }else if(item.key.equals(context.getString(R.string.qq))){
                    viewItem.setEditTextFilter(context, item.key, 16);
                }
	            return true;
	        }
	        return false;
	    }
	    public List<BasicInfoListAdapter.Info> getContentList()
	    {
	        return getCaches();
	    }

	    public void setContentList(List<BasicInfoListAdapter.Info> contentList) {
	        dataList =contentList;
	        viewList.clear();
	        for (int i = 0; i < contentList.size(); i ++) {
	            int type = contentList.get(i).type;
	            boolean bReq = contentList.get(i).required;
	            BaseInfoRowViewItem viewItem;
	            if (type != -1) {
	                viewItem = new BaseInfoRowViewItem(getContext(), type, bReq);
	            } else {
	                viewItem = new BaseInfoRowViewItem(getContext(), BaseInfoRowViewItem.SIMPLETEXT_TYPE, bReq);
	            }
	            viewItem.crmManager = this.crmManager;
	            viewItem.basicadapter = BasicInfoListAdapter.this;
	            viewItem.position = i;
	            viewList.add(viewItem);
	        }
	        
	        notifyDataChanged(dataList);
	        notifyDataSetChanged();
	    }

	    public Boolean getEditable() {
	        return editable;
	    }

	    public void setEditable(Boolean editable) {
	        this.editable = editable;
	        for (BaseInfoRowViewItem row : viewList) {
	            row.crmManager = this.crmManager;
	            row.basicadapter = BasicInfoListAdapter.this;
	            row.setEditable(editable);
	        }
	    }
	    
	    public void setAction(String action) {
	        for (BaseInfoRowViewItem row : viewList) {
	            row.action = action;
	        }
	    }
	    
	    public void setPrjID(String prjID) {
	        for (BaseInfoRowViewItem row : viewList) {
	            row.prjID = prjID;
	        }
	    }

	    public List<BaseInfoRowViewItem> getViewList() {
	        return viewList;
	    }

	    public void setViewList(List<BaseInfoRowViewItem> viewList) {
	        this.viewList = viewList;
	        for (BaseInfoRowViewItem row: viewList)
	        {
	            row.crmManager = this.crmManager;
	            row.basicadapter = BasicInfoListAdapter.this;
	        }
	    }
}
