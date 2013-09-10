package com.roiland.crm.sm.core.model;

/**
 * 
 * <pre>
 * 试乘试驾
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: TestDriveInfo.java, v 0.1 2013-8-2 上午10:40:08 shuang.gao Exp $
 */
public class TestDriveInfo {
    private String carNum;
    private String carId;      //试驾车ID
    private String testData;   //试驾数据
    private String testStatus; //试驾状态

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String n) {
        carNum = n;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String p) {
        carId = p;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String ps) {
        testData = ps;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String ct) {
        testStatus = ct;
    }
}
