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
 * @version $Id: SmTestDriveInfoActivity.java, v 0.1 2013-5-17 下午4:33:04 liuyu Exp $
 */

public class SmTestDriveInfoActivity  extends BaseInfoActivity {

    public DriveTest driver;
    private SmTestDriveInfoFragment mTestDriveInfoFragment;
    
    public SmTestDriveInfoActivity(){
       super();
    }
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.drivetest_tab));
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		Intent intent = getIntent();
		//获取用户点击列表的那条试乘试驾信息
		driver = intent.getParcelableExtra("driverinfo");
		 mTestDriveInfoFragment = new SmTestDriveInfoFragment(driver);
		addFragment(mTestDriveInfoFragment);
	}
}
