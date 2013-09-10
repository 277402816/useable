package com.roiland.crm.sm.ui.widget;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.widget.NameTextViewItem.DataChangeListener;

/**
 * 类型为CheckBox的TextViewItem
 * @extends TextViewItem
 */
public class CheckBoxViewItem extends TextViewItem{
	
	private CheckBox checkbox;
	private TextView chkValue;
	public boolean locked = false;
	private WeakReference<DataChangeListener> weak;
	public CheckBoxViewItem(Context context) {
		this(context,null,null);
	}
	
	public CheckBoxViewItem(Context _context, String _key, String _value) {
		super(_context, _key, _value);
		this.weak = new WeakReference<DataChangeListener>((DataChangeListener) _context);
		//设置Item属性
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sc_list_item_checkbox, this);
 
		this.key = (TextView) findViewById(R.id.keyItem);
		checkbox = (CheckBox) findViewById(R.id.valuecheck);
		chkValue = (TextView) findViewById(R.id.chkValue);
		checkbox.setClickable(false);
		checkbox.setEnabled(false);
		if (_key != null)
			this.key.setText(_key);
		
		if (_value != null)
			checkbox.setChecked(Boolean.valueOf(_value));	
	 
	}
	//设置CheckBox的监听事件
	private final View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewId = v.getId();
			if(viewId == R.id.valuecheck || viewId == R.id.chkValue){
				if(checkbox.isChecked()){
					checkbox.setChecked(false);
				}else{
					checkbox.setChecked(true);
				}
				
			}
		}
	};
	
	@Override
	public String getValue() {
		
		return String.valueOf(this.checkbox.isChecked());
	}

	@Override
	public void setKeyText(String label) {
		
		this.key.setText(label);
	}

	@Override
	public void setValue(String value) {
		this.checkbox.setChecked(Boolean.valueOf(value));
	}
	//初期载入画面CheckBox状态判断及设置
	@Override
	public void setEditFlag(boolean b) {
		
		this.editFlag = b;
		
		if(b){
			this.checkbox.setClickable(true);
			this.checkbox.setEnabled(true);
			if(locked){
				this.checkbox.setClickable(false);
				this.checkbox.setEnabled(false);
			}else{
				//设置监听事件
			chkValue.setOnClickListener(mClickListener);
			checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//根据状态改变CheckBox ，并将数据返回相应的Activity
					if(isChecked){
						weak.get().dataModify(key.getText().toString(), String.valueOf(true));
					}else{
						weak.get().dataModify(key.getText().toString(), String.valueOf(false));
					}
				}
			});
			}
		}else{
			this.checkbox.setClickable(false);
			this.checkbox.setEnabled(false);
		}
		
	}

}
