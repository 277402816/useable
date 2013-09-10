package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.view.MenuFragment.ItemSelectedListener;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.PromissionController;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 这是基础视图画面，主要定义左边菜单，ActionBar和功能模块片段。
 * 
 * @author JKim
 * @extends SlidingFragmentActivity
 * @implements ItemSelectedListener
 */
public class BaseActivity extends SlidingFragmentActivity implements ItemSelectedListener {
    private final static String tag = Log.getTag(BaseActivity.class);
    
    public MenuFragment mFrag;                             //显示菜单的片段
    public Fragment mContent;                              //显示内容的片断
    public boolean advanceSearch = false;
    private Fragment mWelcomeContent = null;               //欢迎页面
    private BaseRefreshListFragment mOppoListFragment = null;             //销售线索页面
    private BaseRefreshListFragment mCustomerOrderListFragment = null;    //客户订单页面
    private BaseRefreshListFragment mFollowPlanListFragment = null;       //跟踪计划页面
    private BaseRefreshListFragment mCarResourceListFragment = null;      //车辆资源查询页面
    private BaseRefreshListFragment mCustomerListFragment = null;         //客户管理页面
    private Fragment mSalesFunnelSearchFragment = null;                   //销售总监漏斗页面
    
    // 定制的 the SlidingMenu
    protected SlidingMenu sm = null;                       //声明sliding menu变量
    protected int currentMenuIndex = 0;                    //被选择菜单的索引
    
    protected ActionBar actionBar = null;                  //ActionBar控件
    private int mTitleRes;                                 //标题资源
    private TextView mTitleView;                           //标题控件
    
    
    
    /**
     * 构造函数
     */
    public BaseActivity() {
        this(R.string.title_main_page);
    }
    
    /**
     * 构造函数
     * @param titleRes 标题
     */
    public BaseActivity(int titleRes) {
        mTitleRes = titleRes;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setTitle("");

        // 填充上边的VIEW
        if (savedInstanceState != null) {
            String roleName = savedInstanceState.getString("roleName");
            Log.d(tag, "savedInstanceState==menuRoleName======" + roleName);
            PromissionController.setCurrentRole(roleName);
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            mFrag = (MenuFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mFrag");
            
        }
        
        if (mWelcomeContent == null)
            mWelcomeContent = new WelcomeFragment();
        
        if (mContent == null)
            mContent = mWelcomeContent;
        
        if (mFrag == null)
            mFrag = new MenuFragment();
        //设置当前的功能模块
        PromissionController.setCurrentModule(WelcomeFragment.class.getName());
        
        // 填充后面的菜单视图
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.menu_frame, mFrag)
        .commit();
        
        // 填充画面UI
        setContentView(R.layout.content_frame);
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, mContent)
        .commit();
        
        // 定制 SlidingMenu菜单
        sm = getSlidingMenu();
        sm.setBehindWidthRes(R.dimen.slidingmenu_width);
        sm.setBehindScrollScale(0.0f);
        sm.setFadeDegree(0.0f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        // 定义ActionBar控件
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_background));
        actionBar.setIcon(getResources().getDrawable(R.drawable.menu_top_btn));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.actionbar_title_layout);
        actionBar.setHomeButtonEnabled(true);
        
        //标题控件
        mTitleView = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_title);
        setTitleName(getString(R.string.menu_main)+"                ");
        //添加左侧 menu的默认显示.
        getSlidingMenu().toggle();
    }

    
    /**
     * 当sub activity结束后调用.
     * @param requestCode : 从Activity传过来的Code
     * @param resultCode : 返回值
     * @param data
     */
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    } 
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("roleName", PromissionController.menuRoleName);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        getSupportFragmentManager().putFragment(outState, "mFrag", mFrag);
    }
    
    /**
     * 菜单项被选事件.
     * @param item : 点击的item.
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(tag, "onOptionsItemSelected : " + String.valueOf(item.getItemId()));
        
        return super.onOptionsItemSelected(item);
    }
    
    
    /**
     * 创建菜单.
     * @param menu
     * @return
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(tag, "onCreateOptionsMenu : ===" );
        getSupportMenuInflater().inflate(R.menu.base_menu, menu);
        
        return false;
    }
    
    /** 
     * <pre>
     * 左边菜单被选中事件。
     * </pre>
     * 
     * @param position 所选菜单位置
     * @param otherFlags 标记
     * @param flag 列表显示数据的标记
     * @see com.roiland.crm.sm.ui.view.MenuFragment.ItemSelectedListener#onItemSelected(int, int)
     */
    public void onItemSelected(int position, int otherFlags,String flag) {
        onItemSelected(position, otherFlags, null,flag);
    }
    
    /**
     * <pre>
     * 左边菜单被选中事件。
     * </pre>
     * 
     * @param position     被选择菜单的下标
     * @param otherFlags   其他标识
     * @param bundle       传递的参数
     * @param flag 显示数据的标识("0":代表过期的 "1":代表没过期的 null:代表正常显示数据)
     * @see com.roiland.crm.sm.ui.view.MenuFragment.ItemSelectedListener#onItemSelected(int, int)
     */
    @Override
    public void onItemSelected(int position, int otherFlags, Bundle bundle,String flag) {
        currentMenuIndex = position;
        actionBar.setIcon(getResources().getDrawable(R.drawable.menu_top_btn));
        if(PromissionController.menuRoleName.equals(getString(R.string.custSm)) || PromissionController.menuRoleName.equals((getString(R.string.SaleTreasurer)))){
            switch (position) {
                
                case 0:
                    advanceSearch = false;
                    setTitleName(getString(R.string.menu_main));
                    if (mWelcomeContent == null)
                        mWelcomeContent = new WelcomeFragment();
                    mContent = mWelcomeContent;
                    
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(WelcomeFragment.class.getName());
                    switchContent(mContent);
                    break;
                case 1:
                    advanceSearch = false;
                    setTitleName(R.string.sm_menu_sales_oppo);
                    if (mOppoListFragment == null)
                        mOppoListFragment = new SmOppoListFragment();
                    mContent = mOppoListFragment;
                    mOppoListFragment.setFlag(flag);
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(SmOppoListFragment.class.getName());
                    if(bundle == null){
                        mOppoListFragment = new SmOppoListFragment();
                        mContent = mOppoListFragment;
                        switchContent(mContent);
                    }else{
                        switchContent(mContent, bundle);
                    }
                    break;

                case 2:
                    advanceSearch = false;
                    setTitleName(R.string.sm_menu_trace_plan);
                    if (mFollowPlanListFragment == null)
                        mFollowPlanListFragment = new SmFollowPlanListFragment();
                    mContent = mFollowPlanListFragment;
                    mFollowPlanListFragment.setFlag(flag);
                    PromissionController.setCurrentModule(SmFollowPlanListFragment.class.getName());
                    if(bundle == null){
                        mFollowPlanListFragment = new SmFollowPlanListFragment();
                        mContent = mFollowPlanListFragment;
                        switchContent(mContent);
                    }else{
                        switchContent(mContent, bundle);
                    }
                    break;

                case 3:
                    advanceSearch = false;
                    setTitleName(R.string.sm_menu_cust_order);
                    if (mCustomerOrderListFragment == null)
                        mCustomerOrderListFragment = new SmCustomerOrderListFragment();
                    mContent = mCustomerOrderListFragment;
                    mCustomerOrderListFragment.setFlag(flag);
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(SmCustomerOrderListFragment.class.getName());
                    switchContent(mContent);
                    break;

                case 4:   //跳转到车辆资源查看模块
                    advanceSearch = false;
                    setTitleName(R.string.sm_menu_cust_manage);
                    if (mCustomerListFragment == null)
                        mCustomerListFragment = new SmCustomerListFragment();
                    mContent = mCustomerListFragment;
                    mCustomerListFragment.setFlag(flag);
                    PromissionController.setCurrentModule(SmCustomerListFragment.class.getName());
                    if(bundle == null){
                        mCustomerListFragment = new SmCustomerListFragment();
                        mContent = mCustomerListFragment;
                        switchContent(mContent);
                    }else{
                        switchContent(mContent, bundle);
                    }
                    break;
                   
                case 5:
                    advanceSearch = false;
                    setTitleName(R.string.sm_menu_vehicle_search); 
                    if (mCarResourceListFragment == null)
                        mCarResourceListFragment = new SmCarResourceListFragment();
                    mContent = mCarResourceListFragment;
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(SmCarResourceListFragment.class.getName());
                    if(bundle == null){
                        mCarResourceListFragment = new SmCarResourceListFragment();
                        mContent = mCarResourceListFragment;
                        switchContent(mContent);
                    }else{
                        switchContent(mContent, bundle);
                    }
                    break;
                case 6: //跳转到车辆资源查看模块
                    setTitleName(getString(R.string.sm_menu_sales_director_funnel));
                    if (mSalesFunnelSearchFragment == null)
                        mSalesFunnelSearchFragment = new SmSalesFunnelSearchFragment();
                    mContent = mSalesFunnelSearchFragment;
                    switchContent(mContent);
                    break;
                default:
                    break;
                }
        }else if (PromissionController.menuRoleName.equals(getString(R.string.custSc))){
            switch (position) {
                
                case 0:
                    advanceSearch = false;
                    setTitleName(getString(R.string.menu_main));
                    if (mWelcomeContent == null)
                        mWelcomeContent = new WelcomeFragment();
                    mContent = mWelcomeContent;
                    
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(WelcomeFragment.class.getName());
                    switchContent(mContent);
                    break;
                case 1: //跳转到销售线索
                    setTitleName(R.string.sc_menu_sales_oppo);
                    mOppoListFragment = new ScOppoListFragment();
                    mContent = mOppoListFragment;
                    mOppoListFragment.setFlag(flag);
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(ScOppoListFragment.class.getName());
                    switchContent(mContent);
                    break;
                case 2:
                    setTitleName(R.string.sc_menu_cust_order);
                    mCustomerOrderListFragment = new ScCustomerOrderListFragment();
                    mContent = mCustomerOrderListFragment;
                    mCustomerOrderListFragment.setFlag(flag);
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(ScCustomerOrderListFragment.class.getName());
                    switchContent(mContent);
                    break;
                    
                case 3:
                    advanceSearch = false;
                    setTitleName(R.string.sc_menu_trace_plan);
                    if (mFollowPlanListFragment == null)
                        mFollowPlanListFragment = new ScFollowPlanListFragment();
                    mContent = mFollowPlanListFragment;
                    mFollowPlanListFragment.setFlag(flag);
                    PromissionController.setCurrentModule(ScFollowPlanListFragment.class.getName());
                    if(bundle == null){
                        mFollowPlanListFragment = new ScFollowPlanListFragment();
                        mContent = mFollowPlanListFragment;
                        switchContent(mContent);
                    }else{
                        switchContent(mContent, bundle);
                    }
                    break;
                case 4: //跳转到车辆资源查看模块
                    advanceSearch = false;
                    setTitleName(R.string.sc_menu_vehicle_search); 
                    if (mCarResourceListFragment == null)
                        mCarResourceListFragment = new ScCarResourceListFragment();
                    mContent = mCarResourceListFragment;
                    //设置当前的功能模块
                    PromissionController.setCurrentModule(ScCarResourceListFragment.class.getName());
                    if(bundle == null){
                        mCarResourceListFragment = new ScCarResourceListFragment();
                        mContent = mCarResourceListFragment;
                        switchContent(mContent);
                    }else{
                        switchContent(mContent, bundle);
                    }
                    break; 
                   
                case 5:
                advanceSearch = false;
                setTitleName(R.string.sc_menu_cust_manage);
                if (mCustomerListFragment == null)
                    mCustomerListFragment = new ScCustomerListFragment();
                mContent = mCustomerListFragment;
                mCustomerListFragment.setFlag(flag);
                PromissionController.setCurrentModule(ScCustomerListFragment.class.getName());
                if(bundle == null){
                    mCustomerListFragment = new ScCustomerListFragment();
                    mContent = mCustomerListFragment;
                    switchContent(mContent);
                }else{
                    switchContent(mContent, bundle);
                }
                break;
                default:
                    break;
                }
        }
      
        Log.e(tag, "===onItemSelected==" + position);
    }
    
    /**
     * 
     * <pre>
     * 设置标题名称
     * </pre>
     *
     * @param resid 资源唯一标识
     */
    public void setTitleName(int resid) {
        this.mTitleView.setText(resid);
    }
    
    /**
     * 
     * <pre>
     * 设置标题名称
     * </pre>
     *
     * @param titleName    标题名称
     */
    public void setTitleName(String titleName) {
        this.mTitleView.setText(titleName);
    }
    
    /**
     * 切换内容视图片段方法
     * @param fragment      切换的视图
     */
    public void switchContent(Fragment fragment) {
        switchContent(fragment, null);
    }

    
    /**
     * 切换内容视图片段方法
     * @param   fragment    切换的视图
     * @param   bundle      传递参数
     */
    public void switchContent(Fragment fragment, Bundle bundle) {
        mContent = fragment;
        if (bundle != null)
            fragment.setArguments(bundle);
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, fragment)
        .commit();
        getSlidingMenu().showContent();
    }

    /**
     * 
     * <pre>
     * 切换内容页面的方法，并加入到栈中。
     * </pre>
     *
     * @param fragment
     */
    public void switchContentWithStack(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, fragment)
        .addToBackStack(null) //添加到 stack
        .commit();
        getSlidingMenu().showContent();
    }

    /**
     * 物理键返回
     * @see com.slidingmenu.lib.app.SlidingFragmentActivity#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //退出返回
            if(currentMenuIndex==0){
                DialogUtils.WarningExit(BaseActivity.this, getString(R.string.prompts), getString(R.string.exit)).show();
            }else{
                if(PromissionController.menuRoleName.equals(getString(R.string.custSm)) || PromissionController.menuRoleName.equals(getString(R.string.SaleTreasurer))){
                    if(!advanceSearch){
                        //模糊搜索返回
                        if(!(currentMenuIndex == 6)){
                            if(((BaseRefreshListFragment) mContent).mSearchMode){
                                ((BaseRefreshListFragment) mContent).defaultSearch();
                                ((BaseRefreshListFragment) mContent).cancelSearch();
                                ((BaseRefreshListFragment) mContent).mSearchMode = false;
                            }else{
                                //其他返回
                                setTitleName(getString(R.string.menu_main));
                                if (mWelcomeContent == null)
                                    mWelcomeContent = new WelcomeFragment();
                                mContent = mWelcomeContent;
                                switchContent(mContent);
                                currentMenuIndex=0;
                                mFrag.changeSelectMenu(currentMenuIndex); 
                            }
                        }else{
                            //销售总监漏斗返回
                            setTitleName(getString(R.string.menu_main));
                            if (mWelcomeContent == null)
                                mWelcomeContent = new WelcomeFragment();
                            mContent = mWelcomeContent;
                            switchContent(mContent);
                            currentMenuIndex=0;
                            mFrag.changeSelectMenu(currentMenuIndex);
                        }
                    }else{
                        //高级搜索返回
                         if(currentMenuIndex == 2 && advanceSearch){
                            mContent = mFollowPlanListFragment;
                            switchContent(mContent);
                        }else if(currentMenuIndex == 4 && advanceSearch){
                            if(mCustomerListFragment == null){
                                mCustomerListFragment = new SmCustomerListFragment();
                            }
                            mContent = mCustomerListFragment;
                            switchContent(mContent);
                        }else if (currentMenuIndex == 1 && advanceSearch){
                            if(mOppoListFragment == null){
                                mOppoListFragment = new SmOppoListFragment();
                            }
                            mContent = mOppoListFragment;
                            switchContent(mContent);
                        }else if(currentMenuIndex == 5 && advanceSearch){
                            mContent = mCarResourceListFragment;
                            switchContent(mContent);
                        }
                         advanceSearch = false;
                    }
                }else if(PromissionController.menuRoleName.equals(getString(R.string.custSc))){
                    if(!advanceSearch){
                        //模糊搜索返回

                            //销售总监漏斗返回
                            setTitleName(getString(R.string.menu_main));
                            if (mWelcomeContent == null)
                                mWelcomeContent = new WelcomeFragment();
                            mContent = mWelcomeContent;
                            switchContent(mContent);
                            currentMenuIndex=0;
                            mFrag.changeSelectMenu(currentMenuIndex);
                    }else{
                        //高级搜索返回
                        if(currentMenuIndex == 4 && advanceSearch){
                            mContent = mCarResourceListFragment;
                            switchContent(mContent);
                        }
                         advanceSearch = false;
                    }
                 
                }
                
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    
    public void showLoading(boolean show) {
        if (show) {
            findViewById(R.id.content_frame_loading_view).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.content_frame_loading_view).setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
