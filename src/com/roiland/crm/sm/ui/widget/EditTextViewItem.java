package com.roiland.crm.sm.ui.widget;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.widget.NameTextViewItem.DataChangeListener;
import com.roiland.crm.sm.utils.MyInputFilter;

/**
 * 类型为入力框的TextViewItem
 * @extends TextViewItem
 */
public class EditTextViewItem  extends TextViewItem{

			private EditText editText;
			private WeakReference<DataChangeListener> weak;
			private boolean bEdit  = true;
			private ImageButton callbtn;
			private boolean bCall  = false;
			
			public EditTextViewItem(Context context) {
				this(context,null,null);
				
			}
			
			public EditTextViewItem(Context _context, String _key, String _value) {
				super(_context, _key, _value);
				this.weak = new WeakReference<DataChangeListener>((DataChangeListener) _context);
				
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        inflater.inflate(R.layout.sc_list_item_edittextinfo, this);
		  
				this.key = (TextView) findViewById(R.id.keyItem);
				this.value = (TextView) findViewById(R.id.valueItem);
				if (_key != null){
					this.key.setText(_key);
					bCall = _key.contains("电话");
				}
				
				if(bCall){
					setCallBackground(_key);
				}
					
				if (_value != null && !_value.equals("null"))
					this.value.setText(_value);
				
				editText = (EditText) findViewById(R.id.editItem);
				editText.addTextChangedListener(textWatcher);
				
				
				if(bCall){
					callbtn.setVisibility(View.VISIBLE);
				}
				//设置监听事件
				callbtn.setOnClickListener(new OnClickListener() {
		
					@Override
					public void onClick(View v) {
						
						Log.i("EditTextViewItem", "call");
						Intent tn = new Intent(
		                        "android.intent.action.CALL",
		                        Uri.parse("tel:"+getValue()));
						context.startActivity(tn);
					}
				});
			}
			
			/*
			   * Set edit text input max length constraint to border.
			   */ 
			  public  void setEditTextFilter() { 
			     
			     int width = editText.getWidth(); 
			         
			     Paint paint = new Paint(); 
			     paint.setTextSize(editText.getTextSize()); 
			     
			     InputFilter[] filters = { new MyInputFilter(paint, width,1) }; 
			     
			     editText.setFilters(filters); 
			  } 
			  public  void setEditTextFilter2() { 
				     
				     int width = editText.getWidth(); 
				         
				     Paint paint = new Paint(); 
				     paint.setTextSize(editText.getTextSize()); 
				     
				     InputFilter[] filters = { new MyInputFilter(paint, width,3) }; 
				     
				     editText.setFilters(filters); 
				  } 
			private void setCallBackground(String key){
				if(key.contains("移动电话")){
					callbtn.setBackgroundResource(R.drawable.mobile_icon);
					return;
				}else{
					callbtn.setBackgroundResource(R.drawable.phone_icon);
				}
			}
			
			@Override
			public String getValue() {
				
				return value.getText().toString();
			}
		
			@Override
			public void setKeyText(String label) {
				
				this.key.setText(label);
				bCall = label.contains("电话");
				if(bCall){
					callbtn.setVisibility(View.VISIBLE);
					setCallBackground(label);
				}
			}
		
			@Override
			public void setValue(String value) {
				
				if(value != null){
					if("null".equals(value)){
						value = "";
					}
				}
				
				this.value.setText(value);
			}
			
			public void setDisable(){
				bEdit = false;
			}
			//判断初次载入时画面输入框活性
			@Override
			public void setEditFlag(boolean b) {
				
				if(bEdit){
					if(b){
						callbtn.setVisibility(View.GONE);
						this.value.setVisibility(View.GONE);
						this.editText.setVisibility(View.VISIBLE);
						this.editText.setText(this.getValue());
					}else{
						if(bCall){
							callbtn.setVisibility(View.VISIBLE);
						}
						this.value.setVisibility(View.VISIBLE);
						this.editText.setVisibility(View.GONE);
		
					}
					this.editFlag = b;
				}
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
		             Log.d("onTextChanged",key.getText().toString() + " " + str);
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
		
			public void setInputType(int typeClassNumber) {
				
				editText.setInputType(typeClassNumber);
			}
			
}
