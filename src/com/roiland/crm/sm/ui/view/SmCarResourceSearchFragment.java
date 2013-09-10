package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sm.GlobalConstant;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.core.service.exception.ResponseException;

/**
 * 
 * <pre>
 * 车辆资源高级检索fragment.
 * </pre>
 * @extends SherlockFragment
 * @author shuang.gao
 * @version $Id: SmCarResourceSearchFragment.java, v 0.1 2013-6-8 下午12:29:22 shuang.gao Exp $
 */
public class SmCarResourceSearchFragment extends SherlockFragment {
	private CarResourceSearchListAdapter adapter; //adapter
	int selectedIndex; //选定的搜索项的索引
	private Dictionary brandDic; //brand dictionary
	private Dictionary modelDic; //model dictionary
	private Dictionary outColorDic;
	private Dictionary inColorDic;
	private Dictionary configDic;
	BaseActivity activity;
	
	

	List<Dictionary> downloadedResult;
	
	public SmCarResourceSearchFragment() {
		super();
	}
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.activity = (BaseActivity) activity;
        }
    }


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	};
	
	
	public void destroy_adapter() {
		adapter = null;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sm_car_resource_search_info,	container, false);
		
		if (adapter == null) {
			adapter = new CarResourceSearchListAdapter(getActivity());
		}
		adapter.clearData();
		ListView listView = (ListView) view.findViewById(R.id.car_info_list);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		//获取选择数据字典事件
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				downloadDicTask(arg2);
			}
		});
		
		//设置开始搜索按钮事件
		view.findViewById(R.id.car_search_start_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Vehicle vehicle = new Vehicle();
			    vehicle.setBrandCode(adapter.getBrandDic().getDicKey());
			    vehicle.setModelCode(adapter.getModelDic().getDicKey());
			    vehicle.setOutsideColorCode(adapter.getOutsideColorDic().getDicKey());
			    vehicle.setInsideColorCode(adapter.getInsideColorDic().getDicKey());
			    vehicle.setVehiConfigCode(adapter.getConfigDic().getDicKey());
			    doSearch(vehicle);
			}
		});
		
		return view;
	}
	 /**
     * 
     * <pre>
     * 开始查询任务
     * </pre>
     *
     */
    private void doSearch(Vehicle vehicle) {
       
        
        Bundle bundle = new Bundle();
        bundle.putParcelable("advancedSearch", vehicle);
        activity.onItemSelected(5, -1, bundle,null);
    }
	
    /**
     * 
     * <pre>
     * 运行车辆资源高级检索词典任务.
     * </pre>
     *
     * @param index 选定词典索引.
     */
	public void downloadDicTask(int index) {
		new BaseTask<List<Dictionary>>(getActivity()) {
			
			@Override
			protected List<Dictionary> doInBackground(String... params) {
				
				int index = Integer.parseInt(params[0]);
				selectedIndex = index;
				try {
					CRMManager manager = ((RoilandCRMApplication)getActivity().getApplication()).getCRMManager();
					switch (selectedIndex) {
					case 0:
						return manager.getDictionariesByType(GlobalConstant.DictionaryKeyConstant.BRAND_DIC_KEY);
					case 1:
						if (adapter.getBrandDic().getDicKey() == null) return null;
						return manager.getRelativeDictionaries(GlobalConstant.DictionaryKeyConstant.MODEL_KEY, adapter.getBrandDic().getDicKey());
					case 2:
						if (adapter.getBrandDic().getDicKey() == null) return null;
						return manager.getRelativeDictionaries(GlobalConstant.DictionaryKeyConstant.OUTER_KEY, adapter.getBrandDic().getDicKey());
					case 3:
						if (adapter.getBrandDic().getDicKey() == null) return null;
						return manager.getRelativeDictionaries(GlobalConstant.DictionaryKeyConstant.INNER_KEY, adapter.getBrandDic().getDicKey());
					case 4:
						if (adapter.getModelDic().getDicKey() == null) return null;
				
						return manager.getRelativeDictionaries(GlobalConstant.DictionaryKeyConstant.CUSTOMER_CFG_DIC_KEY, adapter.getModelDic().getDicKey());
					}
				} catch (ResponseException e) {
					responseException = e;
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Dictionary> result) {
				if (responseException != null) {
					if (getActivity() != null) {
						Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} else {
					if (result != null) {
						downloadedResult = result;
						String[] valueList = new String[result.size()];
						for (int i = 0; i < result.size(); i ++) {
							valueList[i] = result.get(i).getDicValue();
						}
						
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle(adapter.getCaption(selectedIndex));
		
						builder.setItems(valueList, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								Dictionary dic = downloadedResult.get(item);
								switch (selectedIndex) {
								case 0:
									adapter.setBrandDic(dic);
									if(brandDic==null){
										brandDic = dic;
									}
									if(!dic.getDicValue().equals(brandDic.getDicValue())){
										Dictionary dicty = new Dictionary();
										adapter.setModelDic(dicty);
										adapter.setOutsideColorDic(dicty);
										adapter.setInsideColorDic(dicty);
										adapter.setConfigDic(dicty);
										adapter.notifyDataSetChanged();
										brandDic = dic;
									}
									break;
								case 1:
									adapter.setModelDic(dic);
									if(modelDic==null){
										modelDic = dic;
									}
									if(!dic.getDicValue().equals(modelDic.getDicValue())){
										Dictionary dicty = new Dictionary();
										adapter.setConfigDic(dicty);
										adapter.notifyDataSetChanged();
										modelDic = dic;
									}
									break;
								case 2:
									adapter.setOutsideColorDic(dic);
									if(outColorDic==null){
									    outColorDic = dic;
                                    }
                                    if(!dic.getDicValue().equals(outColorDic.getDicValue())){
                                        outColorDic = dic;
                                    }
									break;
								case 3:
									adapter.setInsideColorDic(dic);
									if(inColorDic==null){
									    inColorDic = dic;
                                    }
                                    if(!dic.getDicValue().equals(inColorDic.getDicValue())){
                                        inColorDic = dic;
                                    }
									break;
								case 4:
									adapter.setConfigDic(dic);
									if(configDic==null){
									    configDic = dic;
                                    }
                                    if(!dic.getDicValue().equals(configDic.getDicValue())){
                                        configDic = dic;
                                    }
									break;
								}
								adapter.notifyDataSetChanged();
							}
						});
						builder.create().show();
					}
				}
				super.onPostExecute(result);
			}
			
		}.execute(String.valueOf(index));
	}
	
	
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemCancle = menu.findItem(R.id.cancel);
        itemCancle.setVisible(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.cancel:
                activity.onItemSelected(5, -1,null);
                break;
                default:
                    break;
                case android.R.id.home:
                    activity.toggle();
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 
     * <pre>
     * 高级检索item list adapter.
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: SmCarResourceSearchFragment.java, v 0.1 2013-6-8 下午12:30:17 shuang.gao Exp $
     */
	class CarResourceSearchListAdapter extends BaseAdapter {
		Dictionary brandDic = null;
		Dictionary modelDic = null;
		Dictionary outsideColorDic = null;
		Dictionary insideColorDic = null;
		Dictionary configDic = null;
		String captions[] = new String[] {"品牌", "车型", "外饰颜色", "内饰颜色", "配置"};
		List<View> viewList;
		Context context;
		
		public CarResourceSearchListAdapter(Context context) {
			this.context = context;
			try {
				viewList = new ArrayList<View>();
				for (int i = 0; i < captions.length; i ++) {
					View view = LayoutInflater.from(context).inflate(R.layout.sm_car_resource_search_list_item, null);
					viewList.add(view);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			switch (position) {
			case 0:
				fillView(viewList.get(position), captions[0], brandDic);
				break;
			case 1:
				fillView(viewList.get(position), captions[1], modelDic);
				break;
			case 2:
				fillView(viewList.get(position), captions[2], outsideColorDic);
				break;
			case 3:
				fillView(viewList.get(position), captions[3], insideColorDic);
				break;
			case 4:
				fillView(viewList.get(position), captions[4], configDic);
				break;
			}
			return viewList.get(position);
		}

		public void fillView(View view, String caption, Dictionary dic) {
			((TextView) view.findViewById(R.id.txt_title)).setText(caption);
			if (dic != null) {
				((TextView) view.findViewById(R.id.txt_content)).setText(dic.getDicValue());
			} else {
				((TextView) view.findViewById(R.id.txt_content)).setText("");
			}
		}
		public void clearData(){
			Dictionary dic = new Dictionary();
			this.setBrandDic(dic);
			this.setModelDic(dic);
			this.setOutsideColorDic(dic);
			this.setInsideColorDic(dic);
			this.setConfigDic(dic);
			this.notifyDataSetChanged();
		}
		public String getCaption(int index) {
			return captions[index];
		}
		
		public Dictionary getBrandDic() {
			return brandDic;
		}

		public void setBrandDic(Dictionary brandDic) {
			this.brandDic = brandDic;
			notifyDataSetChanged();
		}

		public Dictionary getModelDic() {
			return modelDic;
		}

		public void setModelDic(Dictionary modelDic) {
			this.modelDic = modelDic;
			notifyDataSetChanged();
		}

		public Dictionary getOutsideColorDic() {
			return outsideColorDic;
		}

		public void setOutsideColorDic(Dictionary outsideColorDic) {
			this.outsideColorDic = outsideColorDic;
			notifyDataSetChanged();
		}

		public Dictionary getInsideColorDic() {
			return insideColorDic;
		}

		public void setInsideColorDic(Dictionary insideColorDic) {
			this.insideColorDic = insideColorDic;
			notifyDataSetChanged();
		}

		public Dictionary getConfigDic() {
			return configDic;
		}

		public void setConfigDic(Dictionary configDic) {
			this.configDic = configDic;
			notifyDataSetChanged();
		}
		
	}
	
	public Dictionary getBrandDic() {
		return adapter != null ? adapter.getBrandDic() : null;
	}
	public Dictionary getModelDic() {
		return adapter != null ? adapter.getModelDic() : null;
	}
	public Dictionary getOutsideColorDic() {
		return adapter != null ? adapter.getOutsideColorDic() : null;
	}
	public Dictionary getInsideColorDic() {
		return adapter != null ? adapter.getInsideColorDic() : null;
	}
	public Dictionary getConfigDic() {
		return adapter != null ? adapter.getConfigDic() : null;
	}
}
