
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

	public ScAttachInfoActivity(){
		super();
	}

	/**
	 * ui视图被创建时调用onCreate方法
	 * @param savedInstanceState
	 * @see com.roiland.crm.sm.ui.view.BaseInfoActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		findViewById(R.id.mybottom_bar).setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.attachment_list_title));
		Attach attach = getIntent().getParcelableExtra("ATTACH");
		mainFragment = new ScAttachInfoFragment(attach);
		addFragment(mainFragment);
	}

	/**
	 * 菜单点击事件
	 * @param item 点击了那个按钮
	 * @return
	 * @see com.roiland.crm.sm.ui.view.BaseInfoActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
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
    
    /**
     * @see com.roiland.crm.sm.ui.view.BaseInfoActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if(mainFragment.bitmap != null && !mainFragment.bitmap.isRecycled()){
            mainFragment.bitmap.recycle();
         }
         finish();
    }
	
}
