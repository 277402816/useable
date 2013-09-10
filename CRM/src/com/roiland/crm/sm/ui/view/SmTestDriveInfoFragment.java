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
import com.roiland.crm.sm.ui.adapter.SmTestDriveInfoAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;
/**
 * 
 * <pre>
 * 试乘试驾详细信息Fragment
 * </pre>
 * @extends Fragment
 * @author liuyu
 * @version $Id: SmTestDriveInfoFragment.java, v 0.1 2013-5-17 下午3:59:39 liuyu Exp $
 */

@SuppressLint("ValidFragment")
public class SmTestDriveInfoFragment extends Fragment{

    private SmTestDriveInfoAdapter infoAdapter;
    private ListView mTestDriveInfoList;
    private DriveTest driver;
    
    
    public SmTestDriveInfoFragment(DriveTest driver){
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
        View view = inflater.inflate(R.layout.sm_activity_test_drive_info, container, false);
        mTestDriveInfoList = (ListView)view.findViewById(R.id.testdrive_info_list1);
        if(infoAdapter == null){
            infoAdapter = new SmTestDriveInfoAdapter(getActivity());
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
        infoAdapter.addView("试驾人姓名", StringUtils.notNull(driver.getDriverName()));
        infoAdapter.addView("试驾人手机", StringUtils.notNull(driver.getDriverMobile()));
        infoAdapter.addView("驾驶证号", StringUtils.notNull(driver.getDriverLicenseNo()));
        infoAdapter.addView("试驾车底盘号", StringUtils.notNull(driver.getDriveChassisNo()));
        infoAdapter.addView("试驾车牌照号", StringUtils.notNull(driver.getDriveLicensePlate()));
        infoAdapter.addView("试驾状态", StringUtils.notNull(driver.getDriveStatus()));
        infoAdapter.addView("试驾开始时间",DateFormatUtils.formatDate(StringUtils.notNull(driver.getDriveStartTime())));
        infoAdapter.addView("试驾结束时间", DateFormatUtils.formatDate(StringUtils.notNull(driver.getDriveEndTime())));
        if("null".equals(driver.getDriveStartKM())){
            infoAdapter.addView("试驾起始里程", "");
        }else{
            infoAdapter.addView("试驾起始里程", StringUtils.notNull(driver.getDriveStartKM()) + getResources().getString(R.string.km));
        }
        if(StringUtils.isEmpty(driver.getDriveEndKM())){
            infoAdapter.addView("试驾结束里程", StringUtils.notNull(driver.getDriveEndKM()));
        }
        else{
            infoAdapter.addView("试驾结束里程", StringUtils.notNull(driver.getDriveEndKM()) + getResources().getString(R.string.km));
        }
        infoAdapter.addView("备注", StringUtils.notNull(driver.getDriveComment()));
        mTestDriveInfoList.setAdapter(infoAdapter);
    }
    
    
}
