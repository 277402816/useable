
package com.roiland.crm.sm.ui.view;

import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Attach;


/**
 * 
 * <pre>
 * 文档图片信息activity
 * </pre>
 * @extends BaseInfoActivity
 * @author liuyu
 * @version $Id: ScAttachInfoActivity.java, v 0.1 2013-7-5 上午9:04:18 liuyu Exp $
 */

public class ScAttachInfoActivity extends BaseInfoActivity {
	ScAttachInfoFragment mainFragment = null; 
	
	/**
	 * 构造方法
	 */
	public ScAttachInfoActivity(){
		super();
	}

	/**
	 * ui视图被创建时调用onCreate方法
	 * Activity 创建时调用此方法
	 * @param savedInstanceState    savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.attachment_list_title));
		Attach attach = getIntent().getParcelableExtra("ATTACH");
		mainFragment = new ScAttachInfoFragment(attach);
		addFragment(mainFragment);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(mainFragment.bitmap != null && !mainFragment.bitmap.isRecycled()){
                   mainFragment.bitmap.recycle();
                }
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(mainFragment.bitmap != null && !mainFragment.bitmap.isRecycled()){
            mainFragment.bitmap.recycle();
         }
         finish();
    }
	
    
	
}
