package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.OppoFunnel;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.widget.BasicViewDraw;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 销售漏斗漏的显示Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmSalesFunnelPicActivity.java, v 0.1 2013-5-16 下午2:49:36 shuang.gao Exp $
 */
public class SmSalesFunnelPicActivity extends BaseInfoActivity {
    private final static String  tag               = Log.getTag(SmSalesFunnelPicActivity.class); //Log标志

    private OppoFunnel oppoFunnel ;
    private String StartTime;
    private String EndTime;
    public SmSalesFunnelPicActivity() {
        super();
//        mainFragment = new SmSalesFunnelPicFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标题
        setTitle(getResources().getString(R.string.sm_menu_sales_director_funnel));
      Intent intent = this.getIntent();
      StartTime = intent.getStringExtra("startTime");
      EndTime = intent.getStringExtra("endTime");
      Search();


    }
/**
 * 
 * <pre>
 * 获取销售漏斗统计图数据
 * </pre>
 *
 */
    @SuppressWarnings("unused")
    private void Search() {
        
        new BaseTask<OppoFunnel>(this) {
              

            @Override
            protected OppoFunnel doInBackground(String... params) {
                
                CRMManager manager = ((RoilandCRMApplication) this.activity.getApplication())
                    .getCRMManager();
                try {
                    return manager.getOppoFunnel(DateFormatUtils.parseDateToLong(StartTime), DateFormatUtils.parseDateToLong(EndTime)+24*60*60*1000);
                } catch (ResponseException e) {
                    Log.i("-----------downloadEmployee----------", e.getMessage());
                    responseException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(OppoFunnel result) {
                super.onPostExecute(result);
                if (responseException != null) {
                    Toast.makeText(activity, responseException.getMessage(), Toast.LENGTH_LONG)
                        .show();
                }
                if (result != null) {
                    oppoFunnel = result;
                    BasicViewDraw view = new BasicViewDraw(this.activity,oppoFunnel);
                    setTitle(getResources().getString(R.string.sm_menu_sales_director_funnel));
                    setContentView(view);
                } 
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                finish();
            }

        }.execute();
        
        
}
    /**
     * 监听物理键的操作
     * 主要用来监听返回物理键
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
           // bundle.putParcelable("tracePlan", mainFragment.getTracePlan());
            intent.putExtras(bundle);
            this.setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
