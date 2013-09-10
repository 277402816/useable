
package com.roiland.crm.sm.ui.view;

import android.os.Bundle;
import android.view.View;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Attach;


/**
 * 
 * <pre>
 * 文档图片信息activity
 * </pre>
 * @extends BaseInfoActivity
 * @author liuyu
 * @version $Id: SmAttachInfoActivity.java, v 0.1 2013-5-22 上午9:52:17 liuyu Exp $
 */
public class SmAttachInfoActivity extends BaseInfoActivity {
	SmAttachInfoFragment mainFragment = null; //fragment to dispay the attach info.
	
	/**
	 * 构造方法
	 */
	public SmAttachInfoActivity(){
		super();
	}

	/**
	 * ui视图被创建时调用onCreate方法
	 * Activity 创建时调用此方法
	 * @param savedInstanceState    savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.attachment_list_title));
		Attach attach = getIntent().getParcelableExtra("ATTACH");
		mainFragment = new SmAttachInfoFragment(attach);
		addFragment(mainFragment);
	}
}
