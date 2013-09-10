package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.DriveTest;

/**
 * 
 * <pre>
 * 试乘试驾详细信息Activity
 * </pre>
 * @extends BaseInfoActivity
 * @author liuyu
 * @version $Id: ScTestDriveInfoActivity.java, v 0.1 2013-7-2 下午2:37:34 liuyu Exp $
 */
public class ScTestDriveInfoActivity  extends BaseInfoActivity {

    public DriveTest driver;
    private ScTestDriveInfoFragment mTestDriveInfoFragment;
    
    public ScTestDriveInfoActivity(){
       super();
    }
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.customer_order_info_title));
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		Intent intent = getIntent();
		//获取用户点击列表的那条试乘试驾信息
		driver = intent.getParcelableExtra("driverinfo");
		 mTestDriveInfoFragment = new ScTestDriveInfoFragment(driver);
		addFragment(mTestDriveInfoFragment);
	}
}
