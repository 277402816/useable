package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter;
import com.roiland.crm.sm.ui.adapter.ScCarResourceItemAdapter;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 车辆资源信息fragment.
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScCarResourceInfoFragment.java, v 0.1 2013-7-15 下午12:14:20 shuang.gao Exp $
 */
public class ScCarResourceInfoFragment extends Fragment {
	private ScCarResourceItemAdapter vehicleInfoAdapter; // 实例化车辆资源信息adapter.
	private List<String[]> vehicleInfo; //车辆信息List.
	private Vehicle vehicle; //车辆资源信息类.
	
	/**
	 * 构造ScCarResourceInfoFragment
	 */
	public ScCarResourceInfoFragment()
	{
		super();
	}
	public ScCarResourceInfoFragment(Vehicle vehicle)
	{
		super();
		this.vehicle = vehicle;
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.sc_car_resource_info, container, false);
		
		LinearLayout vehicleInfoList = (LinearLayout)(view.findViewById(R.id.car_resource_info_list));
		vehicleInfoAdapter = new ScCarResourceItemAdapter(this.getActivity());
		displayVehicleInfo();
		for(int i=0;i< vehicleInfoAdapter.getCount();i++){
		    vehicleInfoList.addView(vehicleInfoAdapter.getView(i, null, null));
	          View dividerView = new View(getActivity());
              dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
              dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
              vehicleInfoList.addView(dividerView);
		}
		
		return view;
	}
	
	/**
	 * 
	 * <pre>
	 * 在ListView显示车辆信息的方法.
	 * </pre>
	 *
	 */
	public void displayVehicleInfo() {
	    BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
		vehicleInfo = new ArrayList<String[]>();
		for (int i = 0; i < 11; i ++) {
			vehicleInfo.add(new String[2]);
		}

	    vehicleInfo.get(0)[0] = getActivity().getResources().getString(R.string.cust_chassis);
	    vehicleInfo.get(1)[0] = getActivity().getResources().getString(R.string.brand_1);
		vehicleInfo.get(2)[0] = getActivity().getResources().getString(R.string.model);
		vehicleInfo.get(3)[0] = getActivity().getResources().getString(R.string.outsideColor_1);
		vehicleInfo.get(4)[0] = getActivity().getResources().getString(R.string.insideColor);
		vehicleInfo.get(5)[0] = getActivity().getResources().getString(R.string.carConfiguration);
		vehicleInfo.get(6)[0] = getActivity().getResources().getString(R.string.carstatus);
		vehicleInfo.get(7)[0] = getActivity().getResources().getString(R.string.car_identification);
		vehicleInfo.get(8)[0] = getActivity().getResources().getString(R.string.identification_status);
		vehicleInfo.get(9)[0] = getActivity().getResources().getString(R.string.under_sc);
		vehicleInfo.get(10)[0] = getActivity().getResources().getString(R.string.cust_order_match_id);
		
		if (vehicle != null) {
		    vehicleInfo.get(0)[1] = vehicle.getChassisNo();
		    vehicleInfo.get(1)[1] = vehicle.getBrand();
			vehicleInfo.get(2)[1] = vehicle.getModel();
			vehicleInfo.get(3)[1] = vehicle.getOutsideColor(); 
			vehicleInfo.get(4)[1] = vehicle.getInsideColor();
			vehicleInfo.get(5)[1] = vehicle.getVehiConfig();
			vehicleInfo.get(6)[1] = vehicle.getVehiStatus();
			vehicleInfo.get(7)[1] = vehicle.getStoreStatus();
			vehicleInfo.get(8)[1] = vehicle.getTagStatus();
			vehicleInfo.get(9)[1] = vehicle.getUserID();
			vehicleInfo.get(10)[1] = vehicle.getOrderID();
		}
	       for(int i=0;i<=10; i++){
	            info.key = vehicleInfo.get(i)[0];
	            info.value = vehicleInfo.get(i)[1];
	            vehicleInfoAdapter.addItem(StringUtils.notKMNull(info.key),StringUtils.notKMNull(info.value),null);
	        }
	       vehicleInfoAdapter.notifyDataSetChanged();
		
	}
}
