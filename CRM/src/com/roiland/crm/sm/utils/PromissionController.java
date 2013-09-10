/**
 * Roiland.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.roiland.crm.sm.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.view.BaseActivity;
import com.roiland.crm.sm.utils.PromissionController.Role;

import android.content.Context;
import android.content.res.XmlResourceParser;

/**
 * <pre>
 * 权限控制类
 * </pre>
 *
 * @author cjyy
 * @version $Id: PromissionController.java, v 0.1 2013-5-15 下午01:35:55 cjyy Exp $
 */
public class PromissionController {

    public static PromissionController instance;
    
    public static String currentModuleID;
    public static String roleName = null;
    public static String menuRoleName = null;
    public static Map<String, HashMap<String, Module>> permMap = null;
    public static Map<String, Role> roleMap = null;
    public static HashMap<String, Module> currentModuleArray = null;            //
    public static Module currentModule = null;
    public static Role currentRole = null;
   
    
    public HashMap<String, Module> moduleArray = null;
    private PromissionController(){}
    
    public static PromissionController getInstance() {
        if (instance == null)
            instance = new PromissionController();
        return instance;
    }
    
    public void loadPromissionConfig(Context context) {
        //通过Resources，获得XmlResourceParser实例
        XmlResourceParser xrp = context.getResources().getXml(R.xml.permission_config);
        //解析配置文件
        parsingData(xrp);
    }
    
    /**
     * 
     * <pre>
     * 设定现在使用的角色
     * </pre>
     *
     * @param roleName
     */
    public static void setCurrentRole(String name) {
        if (permMap == null) {
            throw new NullPointerException("权限配置信息没有被加载。");
        }
        currentModuleArray = permMap.get(name);
        currentRole = roleMap.get(name);
        menuRoleName = name;
    }
    
    
    /**
     * 
     * <pre>
     * 判断当前角色是否有权限， true：表示有权限； false:表示没有权限；
     * </pre>
     *
     * @return
     */
    public static boolean isCurrentRole(String packageName) throws ResponseException {
        if (currentRole == null) {
            throw new ResponseException(StatusCodeConstant.NO_ROLE);
        }
        if (!currentRole.packageName.equals(packageName)) {
            throw new ResponseException(StatusCodeConstant.NO_ROLE);
        }
        if(currentModuleArray == null){
            return false;
        }
        return true;
    }
    
    /**
     * 
     * <pre>
     * 判断是否可用
     * </pre>
     *
     * @param tag       标题名称
     * @return boolean  
     */
    public static boolean isAvaliable(String tag) {
        if (currentModuleArray == null)
            throw new NullPointerException("权限配置信息没有被加载。");
        for (Iterator<Module> i = currentModuleArray.values().iterator(); i.hasNext();) {
            Module module = i.next();
            if (module.name.equals(tag)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * <pre>
     * 设定当前的功能模块
     * </pre>
     *
     * @param moduleID
     */
    public static void setCurrentModule(String moduleID) {
        if (permMap == null) {
            throw new NullPointerException("权限配置信息没有被加载。");
        }
        if (currentModuleArray == null) {
            if (permMap != null && roleName != null)
                currentModuleArray = permMap.get(roleName);
            if (currentModuleArray == null)
                throw new NullPointerException("没有当前角色的相关权限配置信息。");
        }
        currentModule = currentModuleArray.get(moduleID);
    }
    
    
    /**
     * 
     * <pre>
     * 判断是否有权限
     * </pre>
     *
     * @param funcID    功能点标识
     * @return
     */
    public static boolean hasPermission(String funcID) {
        if (currentModule == null) {
            throw new NullPointerException("没有选择当前功能模块。");
        }
        if (currentModule.funcMap != null) {
            Boolean isValiable = currentModule.funcMap.get(funcID);
            
            return isValiable == null ? false:isValiable.booleanValue();
        }
        return false;
    }
    
    /**
     * 
     * <pre>
     * 解析权限配置文件
     * </pre>
     *
     * @param xrp
     */
    private void parsingData(XmlResourceParser xrp ) {
        if (xrp == null) {
            throw new NullPointerException();
        }
        
        try {
            permMap = new HashMap<String, HashMap<String, Module>>();
            roleMap = new HashMap<String, Role>();
            Module module = null;   
            Module subModule = null;
            Role role = null;
            //如果没有到文件尾继续执行
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                String tagName = xrp.getName();
//                Log.e("parsingData", "tagName == " + tagName);
                switch (xrp.getEventType()) {
                    case XmlResourceParser.START_TAG: 
                        if (tagName.equalsIgnoreCase("role")) {
                            role = new Role();
                            roleName = xrp.getAttributeValue(0);
                            role.roleName = xrp.getAttributeValue(0);
                            role.packageName = xrp.getAttributeValue(1);
                            moduleArray = new HashMap<String, Module>();
//                            Log.e("parsingData", "roleName == " + roleName);
                        } else if (tagName.equalsIgnoreCase("module")) {
                            module = new Module();
                            module.name = xrp.getAttributeValue(0);   //模块名称
                            module.id = xrp.getAttributeValue(1);   //模块ID
                        } else if (tagName.equalsIgnoreCase("sub_module")) {
                            subModule = new Module();
                            subModule.name = xrp.getAttributeValue(0);   //子模块名称
                            subModule.id = xrp.getAttributeValue(1);   //子模块ID
                            if (module != null) {
                                subModule.parentID = module.id;
//                                module.addSubModule(subModule);        //将子模块添加到主模块中
                            }
                        } else if (tagName.equalsIgnoreCase("func")) {
                            String funcName = xrp.getAttributeValue(0);
                            String flagElement = xrp.nextText();
                            Boolean flag = Boolean.valueOf(flagElement);
                            if (subModule != null) {
                                subModule.addFunc(funcName, flag);
                            } else if (module != null) {
                                module.addFunc(funcName, flag);
                            }
                            
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        if(tagName.equalsIgnoreCase("module")) {
                            moduleArray.put(module.id, module);
                        } else if(tagName.equalsIgnoreCase("sub_module")) {
                            moduleArray.put(subModule.id, subModule);
                            subModule = null;
                        } else if (tagName.equalsIgnoreCase("role")) {
                            permMap.put(roleName, moduleArray);
                            roleMap.put(roleName, role);
                        } else if (tagName.equalsIgnoreCase("func")) {
                            
                        }
                        break;
                }
                
                xrp.next();
            }
        } catch (XmlPullParserException e) {
            Log.e("PromissionController","Parsing config file error.", e);
        } catch (IOException e) {
            Log.e("PromissionController", "", e);
        }
    }
    
    /**
     * 
     * <pre>
     * 角色对象
     * </pre>
     *
     * @author cjyy
     * @version $Id: PromissionController.java, v 0.1 2013年7月29日 上午10:09:08 cjyy Exp $
     */
    public class Role {
        String roleName = null;
        String packageName = null;
        
    }
    
    /**
     * 
     * <pre>
     * 功能模块类
     * </pre>
     *
     * @author cjyy
     * @version $Id: PromissionController.java, v 0.1 2013-5-20 下午04:08:17 cjyy Exp $
     */
    public class Module {
        
        String id = null;
        String name = null;
        String parentID = null;
        ArrayList<Module> subModuleArray = null;
        HashMap<String, Boolean> funcMap = null; 
        
        public Module() {
            
        }
        
        /**
         * 
         * <pre>
         * 添加子模块
         * </pre>
         *
         * @param subModule 子模块
         */
        public void addSubModule(Module subModule) {
            if (subModuleArray == null) {
                subModuleArray = new ArrayList<Module>();
            }
            subModuleArray.add(subModule);
        }
        
        /**
         * 
         * <pre>
         * 添加功能点
         * </pre>
         *
         * @param name  功能点名称
         * @param flag  判断是否用
         */
        public void addFunc(String name, Boolean flag) {
            if (funcMap == null) {
                funcMap = new HashMap<String, Boolean>();
            }
            funcMap.put(name, flag);
        }
        
        /**
         * 
         * <pre>
         * 获得子模块数量
         * </pre>
         *
         * @return int 数量
         */
        public int getSize() {
            if (subModuleArray == null) {
                return 0;
            }
            return subModuleArray.size();
        }
        
        /**
         * 
         * @return
         * @see java.lang.Object#toString()
         */
        public String toString() {
            StringBuffer str = new StringBuffer();
            str.append(" id = ").append(id)
            .append(", name = ").append(name)
            .append(", parentID = ").append(parentID);
            if (subModuleArray != null)
                str.append(", module = ").append(subModuleArray.toString());
            if (funcMap != null)
                str.append(", func = ").append(funcMap);
            return str.toString();
        }
        
        /**
         * 
         * @return
         * @see java.lang.Object#hashCode()
         */
        public int hashCode() {
            return 0;
        }
    }
}
