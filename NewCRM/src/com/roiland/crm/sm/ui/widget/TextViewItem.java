package com.roiland.crm.sm.ui.widget;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

abstract public class TextViewItem extends LinearLayout {

	protected TextView key;
	protected TextView value;
	protected boolean editFlag = false;
	protected Context context;
	
	public abstract String getValue();
	public abstract void setKeyText(String label);
	public abstract void setValue(String value);
	public abstract void setEditFlag(boolean b);

	public TextViewItem(Context _context) {
		this(_context, null, null);
	}
	
	public TextViewItem(Context _context, String labelName, String value) {
		super(_context);
		context = _context;
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.list_item_textinfo, this);
//        
//		this.label = (TextView) findViewById(R.id.textViewItem);
//		this.value = (EditText) findViewById(R.id.textViewData);
//		if (labelName != null)
//			this.label.setText(labelName);
//		if (value != null)
//			this.value.setText(value);
//	
//		this.value.setEnabled(editFlag);
	
	}
//	
//	public String getLabelText() {
//		return label.getText().toString();
//	}
//	public void setLabelText(String label) {
//		this.label.setText(label);
//	}
//	public String getValueText() {
//		return value.getText().toString();
//	}
//	public void setValue(String value) {
//		this.value.setText(value);
//	}
//	public void setEditFlag(boolean b) {
//		editFlag = b;
//		this.value.setEnabled(editFlag);
//	}

}
