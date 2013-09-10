package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.DriveTest;
import com.roiland.crm.sm.utils.DateFormatUtils;

/**
 * 
 * <pre>
 * 试乘试驾列表Adapter
 * </pre>
 * @extends BaseCacheListAdapter
 * @author liuyu
 * @version $Id: SmTestDriveListAdapter.java, v 0.1 2013-5-24 上午11:58:17 liuyu Exp $
 */
public class SmTestDriveListAdapter extends BaseCacheListAdapter<DriveTest> {
	public SmTestDriveListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return super.getView(arg0, arg1, arg2, R.layout.sm_list_test_drive_info_item);
	}


	@Override
	protected boolean fillView(View view, DriveTest item) {
		try {
			((TextView)view.findViewById(R.id.list_td_carnum)).setText(item.getDriveLicensePlate());
			((TextView)view.findViewById(R.id.list_td_car_id)).setText(item.getDriveChassisNo());
			((TextView)view.findViewById(R.id.list_td_data)).setText(DateFormatUtils.formatDate(item.getDriveStartTime()));
			((TextView)view.findViewById(R.id.list_td_status)).setText(item.getDriveStatus());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
