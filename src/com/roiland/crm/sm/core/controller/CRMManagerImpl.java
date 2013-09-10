package com.roiland.crm.sm.core.controller;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.roiland.crm.sm.GlobalConstant.DBContact;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.core.database.CRMDBAgent;
import com.roiland.crm.sm.core.database.DatabaseBean;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Attach;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.Dictionary.DictionaryTable;
import com.roiland.crm.sm.core.model.DriveTest;
import com.roiland.crm.sm.core.model.OppoFunnel;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.TodayWorkContent;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.core.service.AttachAPI;
import com.roiland.crm.sm.core.service.ContacterAPI;
import com.roiland.crm.sm.core.service.CustOrderAPI;
import com.roiland.crm.sm.core.service.CustomerManagmentAPI;
import com.roiland.crm.sm.core.service.DictionaryAPI;
import com.roiland.crm.sm.core.service.DriveTestAPI;
import com.roiland.crm.sm.core.service.ProjectAPI;
import com.roiland.crm.sm.core.service.TodayWorkAPI;
import com.roiland.crm.sm.core.service.TracePlanAPI;
import com.roiland.crm.sm.core.service.VehicleAPI;
import com.roiland.crm.sm.core.service.VersionAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.core.service.impl.AttachAPIImpl;
import com.roiland.crm.sm.core.service.impl.ContacterAPIImpl;
import com.roiland.crm.sm.core.service.impl.CustOrderAPIImpl;
import com.roiland.crm.sm.core.service.impl.CustomerManagmentAPIImpl;
import com.roiland.crm.sm.core.service.impl.DictionaryAPIImpl;
import com.roiland.crm.sm.core.service.impl.DriveTestAPIImpl;
import com.roiland.crm.sm.core.service.impl.ProjectAPIImpl;
import com.roiland.crm.sm.core.service.impl.TodayWorkAPIImpl;
import com.roiland.crm.sm.core.service.impl.TracePlanAPIImpl;
import com.roiland.crm.sm.core.service.impl.VehicleAPIImpl;
import com.roiland.crm.sm.core.service.impl.VersionAPIImpl;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * CRM业务管理类接口实现类
 * </pre>
 *
 * @author cjyy
 * @version $Id: CRMManagerImpl.java, v 0.1 2013-5-13 下午10:56:19 cjyy Exp $
 */
public class CRMManagerImpl implements CRMManager {
    private static final String tag = Log.getTag(CRMManagerImpl.class);

    SoftReference<User> sr = null;
    public User user;
    private static TodayWorkAPI todayWorkAPI;
    private static CustOrderAPI custOrderAPI;
    private static ProjectAPI projectAPI;
    private static CustomerManagmentAPI customerAPI;
    private static DictionaryAPI dictionaryAPI;
    private static TracePlanAPI tracePlanAPI;
    private static VehicleAPI vehicleAPI;
    private static AttachAPI attachAPI;
    private static ContacterAPI contacterAPI;
    private static DriveTestAPI driveTestAPI;
    private static VersionAPI versionAPI;
    
    private static CRMDBAgent crmDBAgent;

    /**
     * 
     * <pre>
     * 获取用户对象。
     * </pre>
     *
     * @return user
     * @throws ResponseException
     */
    public User getUser() throws ResponseException {
        if (sr != null) {
            user = sr.get();
        }
        if(user==null){
            throw new ResponseException(StatusCodeConstant.UNAUTHORIZED);
        }
        return user;
    }
    
    /**
     * 
     * <pre>
     * 设置用户对象，并用SoftReference 缓存起来。
     * </pre>
     *
     * @param user 用户对象
     */
    public void setUser(User user) {
        this.user = user;
        if (sr != null) {
            sr.clear();
            sr = null;
        }
        sr = new SoftReference<User>(user);
    }
    
    public CRMManagerImpl(Context mContext) {
        ResponseException.loadMessageData(mContext);
        todayWorkAPI = new TodayWorkAPIImpl();
        custOrderAPI = new CustOrderAPIImpl();
        customerAPI = new CustomerManagmentAPIImpl();
        vehicleAPI = new VehicleAPIImpl();
        projectAPI = new ProjectAPIImpl();
        tracePlanAPI = new TracePlanAPIImpl();
        dictionaryAPI = new DictionaryAPIImpl();
        attachAPI = new AttachAPIImpl();
        contacterAPI = new ContacterAPIImpl();
        driveTestAPI = new DriveTestAPIImpl();
        versionAPI = new VersionAPIImpl();
        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(mContext,
                DBContact.DB_NAME, null, DBContact.DB_VERSION) {

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion,
                    int newVersion) {
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                createTable(db);
            }

            @Override
            public void onOpen(SQLiteDatabase db) {
                super.onOpen(db);
            }
        };
        crmDBAgent = new CRMDBAgent(dbHelper);
    }

    /**
     * <pre>
     * 创建数据库Table
     * </pre>
     *
     * @param db
     */
    private void createTable(SQLiteDatabase db) {
        try {
            db.execSQL(DatabaseBean.getCreateTableSQL(
                    DictionaryTable.TABLE_NAME, DictionaryTable.COLUMES));
        } catch (SQLException e) {
            Log.e(tag, "dbHelper --> onCreate", e);
            throw e;
        }
    }

    /**
     * 用户登录方法。
     * @param user 用户对象
     * @return User 返回用户对象
     * @throws ResponseException
     * @see com.roiland.crm.sm.core.controller.CRMManager#login(com.roiland.crm.sm.core.model.User)
     */
    @Override
    public User login(User user) throws ResponseException {
        boolean success = todayWorkAPI.login(user);
        if (success) {
            //设置用户
            setUser(user);
            return user;
        }
        return null;
    }

    /** 
     * 获取今日待办事项信息方法。
     * @return TodayWorkContent 待办事项信息
     * @throws ResponseException
     * @see com.roiland.crm.sm.core.controller.CRMManager#getTodayWorking()
     */
    @Override
    public TodayWorkContent getTodayWorking() throws ResponseException {
       
       return todayWorkAPI.getTodayWorking(getUser());
    }

    /**
     * 
     * @param orderNo
     * @return CustOrder
     * @throws ResponseException
     * @see com.roiland.crm.sm.core.controller.CRMManager#getOrderDetailInfo(java.lang.String)
     */
    @Override
    public CustOrder getOrderDetailInfo(String orderNo)
            throws ResponseException {
        
        return custOrderAPI.getOrderDetailInfo(getUser().getUserID(), getUser().getDealerOrgID(), orderNo);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getProjectInfo(java.lang.String, java.lang.String)
     */
    @Override
    public Project getProjectInfo(String projectID, String customerID) throws ResponseException {
        return projectAPI.getProjectInfo(getUser().getUserID(), getUser().getDealerOrgID(), projectID, customerID);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#isExistProject(java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, String> isExistProject(String mobileNumber, String otherPhone)
                                                                                     throws ResponseException {
        return projectAPI.isExistProject(mobileNumber, otherPhone, getUser().getDealerOrgID());
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#createProject(com.roiland.crm.sm.core.model.Project, com.roiland.crm.sm.core.model.TracePlan)
     */
    public Boolean createProject(Project project, TracePlan TracePlan)
            throws ResponseException {
        
        return projectAPI.createProject(getUser().getUserID(),
                getUser().getDealerOrgID(), project, TracePlan);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#updateProjectInfo(com.roiland.crm.sm.core.model.Project)
     */
    public Boolean updateProjectInfo(Project project) throws ResponseException {
        
        return projectAPI.updateProjectInfo(getUser().getUserID(),
                getUser().getDealerOrgID(), project);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#syncContacter(java.lang.String, com.roiland.crm.sm.core.model.Customer)
     */
    @Override
    public boolean syncContacter(String projectID, Customer customer) throws ResponseException {
        return projectAPI.syncContacter(getUser().getUserID(), getUser().getDealerOrgID(), projectID, customer);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getCustomerList(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Customer> getCustomerList(String searchWord,
            String searchColumns, Integer startNum, Integer rowCount,
            String sortRule,String advanceCustOwer, String advanceCustStatus,
            String asvanceOwnerID) throws ResponseException {
        if (getUser() == null) {
            throw new ResponseException("用户认证失败！");
        }
        List<Customer> list = customerAPI.getCustomerList(getUser().getUserID(),
                getUser().getDealerOrgID(), searchWord, searchColumns, startNum,
                rowCount, sortRule, advanceCustOwer,advanceCustStatus,asvanceOwnerID);
        return list;
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getCustomerDetail(java.lang.String)
     */
    @Override
    public Customer getCustomerDetail(String customerID)
            throws ResponseException {
        
        return customerAPI.getCustomerDetail(getUser().getUserID(),
                getUser().getDealerOrgID(), customerID);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getCarResList(java.lang.String, java.lang.String, java.lang.String, com.roiland.crm.sm.core.model.Vehicle, java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    @Override
    public List<Vehicle> getCarResList(String searchType, String searchWord,
            String searchColumns, Vehicle vehicle, Integer startNum,
            Integer rowCount, String sortRule) throws ResponseException {

        List<Vehicle> list = vehicleAPI.getCarResList(getUser(), searchType,
                searchWord, searchColumns, vehicle, startNum, rowCount,
                sortRule);
        return list;

    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getDictionariesByType(java.lang.String)
     */
    @Override
    public List<Dictionary> getDictionariesByType(String dicType) throws ResponseException {
        List<Dictionary> dicArray = crmDBAgent.getDictionariesByType(dicType);
        if (dicArray == null || dicArray.isEmpty()) {
            dicArray = dictionaryAPI.getDictionary(dicType);
            //Cache to database.缓存数据
            for (Dictionary dic : dicArray) {
                crmDBAgent.insert(dic);
            }
        }
        return dicArray;
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getRelativeDictionaries(java.lang.String, java.lang.String[])
     */
    @Override
    public List<Dictionary> getRelativeDictionaries(String dicType, String...parentKey) throws ResponseException {
        List<Dictionary> dicArray = crmDBAgent.getRelativeDictionaries(dicType, StringUtils.arrayToString(parentKey));
        if (dicArray == null || dicArray.isEmpty()) {
            dicArray = dictionaryAPI.getCascadeDict(dicType, parentKey);
            //Cache to database.缓存数据
            for (Dictionary dic : dicArray) {
                crmDBAgent.insert(dic);
            }
        }
        return dicArray;
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getCustomerCarIntention(java.lang.String)
     */
    @Override
    public String getCustomerCarIntention(String customerID) throws ResponseException {
        return null;
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getTracePlanList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, com.roiland.crm.sm.core.model.TracePlan.AdvancedSearch)
     */
    @Override
    public List<TracePlan> getTracePlanList(String projectID,
            String customerID, String searchWord, String searchColumns,
            int startNum, int rowCount,TracePlan.AdvancedSearch advancedSearch) throws ResponseException {
        List<TracePlan> list = null;
        if(advancedSearch == null){
            list = tracePlanAPI.getTracePlanList(getUser().getUserID(),
                getUser().getDealerOrgID(), projectID, customerID, searchWord,
                searchColumns, startNum, rowCount);
        }else{
            list = tracePlanAPI.advSearchTracePlan(advancedSearch,startNum, rowCount);
        }

        return list;
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#createTracePlan(java.lang.String, java.lang.String, com.roiland.crm.sm.core.model.TracePlan)
     */
    @Override
    public String createTracePlan(String projectID, String customerID,
            TracePlan tracePlan) throws ResponseException {
        
        return tracePlanAPI.createTracePlan(getUser().getUserID(),
                getUser().getDealerOrgID(), projectID, customerID, tracePlan);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#updateTracePlan(java.lang.String, java.lang.String, com.roiland.crm.sm.core.model.TracePlan)
     */
    @Override
    public Boolean updateTracePlan(String projectID, String customerID,
            TracePlan tracePlan) throws ResponseException {
        
        return tracePlanAPI.updateTracePlan(getUser().getUserID(),
                getUser().getDealerOrgID(), projectID, customerID, tracePlan);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#updateTracePlan(java.lang.String, java.lang.String, com.roiland.crm.sm.core.model.TracePlan, com.roiland.crm.sm.core.model.TracePlan)
     */
    @Override
    public Boolean updateTracePlan(String projectID, String customerID,
            TracePlan existTracePlan, TracePlan newTracePlan) throws ResponseException {
        
        return tracePlanAPI.updateTracePlan(getUser().getUserID(),
                getUser().getDealerOrgID(), projectID, customerID, existTracePlan, newTracePlan);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getCustomerProjectList(java.lang.String)
     */
    @Override
    public List<Project> getCustomerProjectList(String customerID)
            throws ResponseException {
        
        return projectAPI.getCustomerProjectList(getUser().getUserID(), customerID);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getOrderList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<CustOrder> getOrderList(String projectID, String customerID, String searchWord,
                                        String searchColumns, Integer startNum, Integer rowCount)
                                        throws ResponseException {
        return custOrderAPI.getOrderList(getUser().getUserID(), getUser().getDealerOrgID(), projectID, customerID, searchWord, searchColumns, startNum, rowCount);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#isProjectActive(java.lang.String)
     */
    public Project isProjectActive(String projectID) throws ResponseException{
        return custOrderAPI.isProjectActive(projectID,getUser().getDealerOrgID());
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getProjectList(java.lang.String, java.lang.String, int, int, com.roiland.crm.sm.core.model.Project.AdvancedSearch)
     */
    @Override
    public List<Project> getProjectList(String searchText, String searchColumns,String expired,int startNum,
                                        int rowCount, Project.AdvancedSearch advancedSearch) throws ResponseException {
        return projectAPI.getProjectList(searchText, searchColumns,expired,startNum, rowCount,advancedSearch);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getAttachmentList(java.lang.String, java.lang.String)
     */
    @Override
    public List<Attach> getAttachmentList(String projectID, String customerID)
                                                                              throws ResponseException {
        return attachAPI.getAttachmentList(getUser().getUserID(), getUser().getDealerOrgID(), projectID, customerID);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getContacterList(java.lang.String, java.lang.String)
     */
    @Override
    public List<Contacter> getContacterList(String projectID, String customerID)
                                                                                throws ResponseException {
        return contacterAPI.getContacterList(getUser().getUserID(), getUser().getDealerOrgID(), projectID, customerID);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getProjectDriveTest(java.lang.String)
     */
    @Override
    public List<DriveTest> getProjectDriveTest(String projectID) throws ResponseException {
        return driveTestAPI.getProjectDriveTest(projectID);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getEmployeeList()
     */
    @Override
    public List<Dictionary> getEmployeeList() throws ResponseException {
        return contacterAPI.getEmployeeList();
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getTodayProjectList(java.lang.String, java.lang.String, int, int, java.lang.String)
     */
    @Override
    public List<Project> getTodayProjectList(String searchText, String searchColumns, int startNum,
                                             int rowCount, String status) throws ResponseException {
        return projectAPI.getTodayProjectList(searchText, searchColumns, startNum, rowCount, status);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getExpiredProjectList(java.lang.String, java.lang.String, int, int)
     */
    @Override
    public List<Project> getExpiredProjectList(String searchText, String searchColumns,
                                               int startNum, int rowCount)
                                                                                         throws ResponseException {
        return projectAPI.getExpiredProjectList(searchText, searchColumns, startNum, rowCount);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getExpiredActionPlanList(int, int)
     */
    @Override
    public List<TracePlan> getExpiredActionPlanList(int startNum, int rowCount)
                                                                               throws ResponseException {
        return tracePlanAPI.getExpiredActionPlanList(getUser().getUserID(), startNum, rowCount);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getTodayActionPlanList(int, int)
     */
    @Override
    public List<TracePlan> getTodayActionPlanList(int startNum, int rowCount)
                                                                             throws ResponseException {
        return tracePlanAPI.getTodayActionPlanList(getUser().getUserID(), startNum, rowCount);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getThreeDaysDeliveryOrderList(java.lang.String, java.lang.String, int, int)
     */
    @Override
    public List<CustOrder> getThreeDaysDeliveryOrderList(String searchWord, String searchColumns,
                                                         int startNum, int rowCount) throws ResponseException{
        return custOrderAPI.getThreeDaysDeliveryOrderList(searchWord, searchColumns, startNum, rowCount);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#noActionPlanProjectCount(java.lang.String, java.lang.String, int, int)
     */
    @Override
    public List<Customer> noActionPlanProjectCount(String searchWord, String searchColumns,
                                                   int startNum, int rowCount)
                                                                              throws ResponseException {
        return customerAPI.noPlanCustomerList(searchWord, searchColumns, startNum, rowCount);
    }

    /** 
     * @see com.roiland.crm.sm.core.controller.CRMManager#searchSalesOppoFunncelList(long, long)
     */
    @Override
    public List<Project> searchSalesOppoFunncelList(long startDate, long endDate,int startNum ,int rowCount) throws ResponseException {
        return projectAPI.searchSalesOppoFunncelList(startDate, endDate, startNum, rowCount);
    }

    /** 
     * @see com.roiland.crm.sm.core.controller.CRMManager#getOppoFunnel(long, long)
     */
    @Override
    public OppoFunnel getOppoFunnel(long startDate, long endDate) throws ResponseException {
        return projectAPI.getOppoFunnel(startDate, endDate);
    }

    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#assignCustomer(java.lang.String, java.lang.String)
     */
    @Override
    public boolean assignCustomer(String customerID, String ownerID) throws ResponseException {
        return customerAPI.assignCustomer(customerID, ownerID);
    }
    
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#downloadFile(java.lang.String)
     */
    @Override
    public RLHttpResponse downloadFile(String attachmentID) throws ResponseException {
        return attachAPI.downloadFile(attachmentID);
    }

    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getVersion()
     */
    @Override
    public String[] getVersion() throws ResponseException {
        return versionAPI.getVersion();
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#getOldCustomer()
     */
    @Override
    public List<Customer> getOldCustomer() throws ResponseException{
        
        
        return customerAPI.getOldCustomer();
        
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#updateCustomer(com.roiland.crm.sm.core.model.Customer)
     */
    @Override
    public Boolean updateCustomer(Customer customer) throws ResponseException {
        
        return customerAPI.updateCustomer(getUser().getUserID(),
                getUser().getDealerOrgID(), customer);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#createContacter(com.roiland.crm.sm.core.model.Contacter)
     */
    @Override
    public Contacter createContacter(Contacter contacter) throws ResponseException {
        return contacterAPI.createContacter(getUser().getUserID(),
            getUser().getDealerOrgID(), contacter);
    }
    /**
     * @see com.roiland.crm.sm.core.controller.CRMManager#updateContacter(com.roiland.crm.sm.core.model.Contacter)
     */
    @Override
    public Contacter updateContacter(Contacter contacter) throws ResponseException {
        return contacterAPI.updateContacter(getUser().getUserID(),
            getUser().getDealerOrgID(), contacter);
    }
    
}
