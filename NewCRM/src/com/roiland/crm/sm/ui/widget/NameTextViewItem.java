package com.roiland.crm.sm.ui.widget;


import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.view.ScCustomerInfoListActivity;
import com.roiland.crm.sm.utils.MyInputFilter;
/**
 * 类型为姓名输入框的TextViewItem
 * @extends TextViewItem
 */
public class NameTextViewItem extends TextViewItem {
	
	private EditText editText;
	private ImageButton btn;
	private WeakReference<DataChangeListener> weak;
	private LinearLayout llt;
	private boolean editData = true;  
	private boolean addFlag ;
	private Context _context;
	public NameTextViewItem(Context context) {
		this(context, null,null);
		
	}
	//设置监听事件
	View.OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Log.i("NameTextViewItem:",context.getClass().toString());
			
			if(key.getText().toString().compareTo("客户名称:") == 0){
				Activity activity = (Activity)_context;
				Intent intent = new Intent(activity,ScCustomerInfoListActivity.class);
				activity.startActivityForResult(intent, 1);
				activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		}
	};
	
	public NameTextViewItem(final Context _context, String _key, String _value) {
		super(_context);
		this._context = _context;
        this.weak = new WeakReference<DataChangeListener>((DataChangeListener)_context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sc_list_item_nametextinfo, this);
        
		this.key = (TextView) findViewById(R.id.keyItem);
		this.value = (TextView) findViewById(R.id.valueItem);
		if (_key != null)
			this.key.setText(_key);
		if (_value != null && !_value.equals("null"))
			this.value.setText(_value);
		
		editText = (EditText) findViewById(R.id.editItem);
		editText.addTextChangedListener(textWatcher);
		llt = (LinearLayout)findViewById(R.id.returnitem_layout);
		btn = (ImageButton) findViewById(R.id.returnitem);
		
	}
	
	  /*
	   * Set edit text input max length constraint to border.
	   */ 
	  public  void setEditTextFilter() { 
	     
	     int width = editText.getWidth(); 
	         
	     Paint paint = new Paint(); 
	     paint.setTextSize(editText.getTextSize()); 
	     
	     InputFilter[] filters = { new MyInputFilter(paint, width,2) }; 
	     
	     editText.setFilters(filters); 
	  } 
	@Override
	public String getValue() {
		
		return value.getText().toString();
	}

	@Override
	public void setKeyText(String label) {
		
		this.key.setText(label);
	}

	@Override
	public void setValue(String value) {
		
		if(value != null){
			if(value.equals("null")){
				value = "";
			}
		}
		
		this.value.setText(value);
	}
	//判断初次载入时画面姓名输入框活性
	@Override
	public void setEditFlag(boolean b) {
		
		
		if(b){
			this.value.setVisibility(View.GONE);
			this.editText.setVisibility(View.VISIBLE);
			this.editText.setText(this.getValue());
			if(this.key.getText().toString().contains("客户名称") && !addFlag){
				btn.setVisibility(View.GONE);
			}else{
				this.btn.setVisibility(View.VISIBLE);
				llt.setOnClickListener(mClickListener);
			}
		}else{
			this.value.setVisibility(View.VISIBLE);
			this.editText.setVisibility(View.GONE);
			this.btn.setVisibility(View.GONE);
		}
		this.editFlag = b;
	}

	private TextWatcher textWatcher = new TextWatcher() {  
        @Override 
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
              
            Log.d("TAG","beforeTextChanged--------------->");  
        }  
 
        @Override    
        public void onTextChanged(CharSequence s, int start, int before,     
                int count) {     
            
            String str = editText.getText().toString();
            Log.d("onTextChanged",str);
            weak.get().dataModify(key.getText().toString(), str);
        }

		@Override
		public void afterTextChanged(Editable arg0) {
			
			Log.d("TAG","afterTextChanged--------------->");   
		}                    
    };
    
    public void setMustItem(){
		this.key.setTextColor(Color.RED);
	}
    
    public interface DataChangeListener {
    	void dataModify(String fromStr, String toStr);
    	void dataModify(String fromStr, String toStr, String pairKey);
    }

	public void transferData(boolean editData) {
		
		this.editData = editData;
	}

    public void setAddFlag(boolean addFlag) {
        this.addFlag = addFlag;
    }
}
