package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 销售漏斗详细列表Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmSalesFunnelActivity.java, v 0.1 2013-5-16 下午2:49:36 shuang.gao Exp $
 */
public class SmSalesFunnelActivity extends BaseInfoActivity {
    private final static String  tag               = Log.getTag(SmSalesFunnelActivity.class); //Log标志

    private SmSalesFunnelFragment mainFragment      = null;                                  //销售漏斗Fragment
    int currentPage = 0;
    private BottomBar                        mBottomBar;
    public SmSalesFunnelActivity() {
        super();
        mainFragment = new SmSalesFunnelFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标题
          setTitle(getResources().getString(R.string.sm_menu_sales_director_funnel1));
          mBottomBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
          mBottomBar.setVisible(false);
          Intent intent = this.getIntent();
          addFragment(mainFragment);

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
