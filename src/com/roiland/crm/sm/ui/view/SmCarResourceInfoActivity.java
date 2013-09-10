package com.roiland.crm.sm.ui.view;

import android.os.Bundle;
import android.view.View;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Vehicle;

/**
 * 车辆资源信息activity.
 * @author Administrator
 * @version 1.0
 * @created 26-1-2013 
 */
public class SmCarResourceInfoActivity extends BaseInfoActivity{

	/**
	 * 构造CarResourceInfoActivity
	 */
	public SmCarResourceInfoActivity(){
		super();
	}

	/**
	 * 加载Activity
	 * @param savedInstanceState    savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.car_resource_info_title));
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		Vehicle vehicle = getIntent().getParcelableExtra("resultVehicle");
		SmCarResourceInfoFragment frg = new SmCarResourceInfoFragment(vehicle);
		addFragment(frg);
	}
}