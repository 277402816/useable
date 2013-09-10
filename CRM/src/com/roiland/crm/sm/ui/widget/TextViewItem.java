package com.roiland.crm.sm.ui.widget;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * <pre>
 * TextView控件 下拉 选择 编辑控件的基类
 * </pre>
 *
 * @author Administrator
 * @version $Id: TextViewItem.java, v 0.1 2013-8-6 上午10:29:30 Administrator Exp $
 */
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
	}
}
