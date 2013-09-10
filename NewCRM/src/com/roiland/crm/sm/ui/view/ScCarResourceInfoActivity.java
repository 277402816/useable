package com.roiland.crm.sm.ui.view;

import android.os.Bundle;
import android.view.View;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Vehicle;

/**
 * 
 * <pre>
 * 车辆资源信息activity.
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScCarResourceInfoActivity.java, v 0.1 2013-7-15 下午12:13:22 shuang.gao Exp $
 */
public class ScCarResourceInfoActivity extends BaseInfoActivity{

	/**
	 * 构造CarResourceInfoActivity
	 */
	public ScCarResourceInfoActivity(){
		super();
	}

	/**
	 * 加载Activity
	 * @param savedInstanceState
	 * @see com.roiland.crm.sm.ui.view.BaseInfoActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.car_resource_info_title));
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		Vehicle vehicle = getIntent().getParcelableExtra("resultVehicle");
		ScCarResourceInfoFragment frg = new ScCarResourceInfoFragment(vehicle);
		addFragment(frg);
	}
}