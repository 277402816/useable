package com.roiland.crm.sm.ui.view;

import android.os.Bundle;
import android.view.View;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem.DataChangeListener;

/**
 * 
 * <pre>
 * 线索下联系人详细信息Activity
 * </pre>
 *
 * @author liuyu
 * @version $Id: ScContacterInfoActivity.java, v 0.1 2013-7-2 上午10:16:01 liuyu Exp $
 */
public class ScContacterInfoActivity extends BaseInfoActivity implements DataChangeListener{
    ScContacterInfoFragment mainFragment;
    public ScContacterInfoActivity(){
        super();
    }

    /**
     * @param savedInstanceState    savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
        mainFragment = new ScContacterInfoFragment();
        
        addFragment(mainFragment);
        //设置UI控件
        setTitle(getResources().getString(R.string.contacter_info_title));
    }

    @Override
    public void dataModify(String key, String value) {
    }

    @Override
    public void dataModify(String key, String value, String pairKey) {
    }
    
    

}
