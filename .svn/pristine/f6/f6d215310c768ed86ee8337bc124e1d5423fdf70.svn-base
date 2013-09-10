package com.roiland.crm.sm.ui.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.DriveTest;
import com.roiland.crm.sm.ui.adapter.ScTestDriveInfoAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 试乘试驾详细信息Fragment
 * </pre>
 * @extends Fragment
 * @author liuyu
 * @version $Id: ScTestDriveInfoFragment.java, v 0.1 2013-7-2 下午2:38:04 liuyu Exp $
 */

@SuppressLint("ValidFragment")
public class ScTestDriveInfoFragment extends Fragment{

    private ScTestDriveInfoAdapter infoAdapter;
    private ListView mTestDriveInfoList;
    private DriveTest driver;
    
    
    public ScTestDriveInfoFragment(DriveTest driver){
        this.driver = driver;
    }
    
    /**
     * 页面初始化时调用此方法
     * @param inflater 页面加载工具
     * @param container 容器
     * @param savedInstanceState
     * @return 页面视图
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sc_activity_test_drive_info, container, false);
        mTestDriveInfoList = (ListView)view.findViewById(R.id.testdrive_info_list1);
        if(infoAdapter == null){
            infoAdapter = new ScTestDriveInfoAdapter(getActivity());
        }
        disPlayTestDriveInfo();
        return view;
    }

    /**
     * 
     * <pre>
     * 显示试乘试驾信息
     * </pre>
     *
     */
    private void disPlayTestDriveInfo() {
        infoAdapter.addView(getString(R.string.driverName), StringUtils.notNull(driver.getDriverName()));
        infoAdapter.addView(getString(R.string.driverMobile), StringUtils.notNull(driver.getDriverMobile()));
        infoAdapter.addView(getString(R.string.driverLicenseNo), StringUtils.notNull(driver.getDriverLicenseNo()));
        infoAdapter.addView(getString(R.string.driveChassisNo), StringUtils.notNull(driver.getDriveChassisNo()));
        infoAdapter.addView(getString(R.string.driveLicensePlate), StringUtils.notNull(driver.getDriveLicensePlate()));
        infoAdapter.addView(getString(R.string.driveStatus), StringUtils.notNull(driver.getDriveStatus()));
        infoAdapter.addView(getString(R.string.driveStartTime),DateFormatUtils.formatDate(StringUtils.notNull(driver.getDriveStartTime())));
        infoAdapter.addView(getString(R.string.driveEndTime), DateFormatUtils.formatDate(StringUtils.notNull(driver.getDriveEndTime())));
        if("null".equals(driver.getDriveStartKM())){
            infoAdapter.addView(getString(R.string.driveStartKM), "");
        }else{
            infoAdapter.addView(getString(R.string.driveStartKM), StringUtils.notNull(driver.getDriveStartKM()) + getResources().getString(R.string.km));
        }
        if(StringUtils.isEmpty(driver.getDriveEndKM())){
            infoAdapter.addView(getString(R.string.driveEndKM), StringUtils.notNull(driver.getDriveEndKM()));
        }
        else{
            infoAdapter.addView(getString(R.string.driveEndKM), StringUtils.notNull(driver.getDriveEndKM()) + getResources().getString(R.string.km));
        }
        infoAdapter.addView(getString(R.string.comment), StringUtils.notNull(driver.getDriveComment()));
        mTestDriveInfoList.setAdapter(infoAdapter);
    }
    
    
}
