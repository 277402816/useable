package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Contacter;

/**
 * 
 * <pre>
 * 联系人详细信息Activity
 * </pre>
 * @extends BaseInfoActivity
 * @author liuyu
 * @version $Id: SmContacterInfoActivity.java, v 0.1 2013-5-22 上午10:12:01 liuyu Exp $
 */
public class SmContacterInfoActivity extends BaseInfoActivity {
	SmContacterInfoFragment mainFragment;
	public SmContacterInfoActivity(){
		super();
	}

	/**
	 * @param savedInstanceState    savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		
		//从Fragment传送过来的Intent中获取数据
		Intent intent = getIntent();
		Contacter contacter = intent.getParcelableExtra("CONTACTER");
		mainFragment = new SmContacterInfoFragment(contacter);
		addFragment(mainFragment);
		
		//设置UI控件
		setTitle(getResources().getString(R.string.contacter_info_title));
	}
}
