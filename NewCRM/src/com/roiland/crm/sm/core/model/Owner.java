package com.roiland.crm.sm.core.model;

/**
 * 
 * <pre>
 * 销售顾问实体
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: Owner.java, v 0.1 2013-6-13 上午10:37:45 shuang.gao Exp $
 */
public class Owner {
    private String strId;   //销售顾问ID
    private String strName; //销售顾问名称

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

}
