package com.roiland.crm.sm.ui.widget;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter;
import com.roiland.crm.sm.ui.view.BaseTask;
import com.roiland.crm.sm.ui.view.ScCustomerInfoListActivity;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.InputFilterUtil;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * info列表
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: BaseInfoRowViewItem.java, v 0.1 2013-7-10 下午1:14:25 shuang.gao Exp $
 */
public class BaseInfoRowViewItem extends RelativeLayout {
	public static final int SELECTION_TYPE		= 0; //选择类型常量
	public static final int SIMPLETEXT_TYPE		= 1; //编辑类型常量
	public static final int DATE_TYPE			= 2; //日期类型常量
	public static final int TIME_TYPE			= 3; //时间类型常量
	public static final int DATETIME_TYPE		= 4; //日期时间型常量
	public static final int SELECTTEXT_TYPE		= 5;
	public static final int BOOLEAN_TYPE		= 6; //checkbox类型常量
	public static final int BOOLEAN2_TYPE		= 7; //checkbox类型常量
	public static final int MOBILETEXT_TYPE		= 8; //移动电话类型常量
	public static final int PSTNTEXT_TYPE		= 9; //其他电话类型常量
	public static final int RIGHT_TYPE       = 10; //老客户选择类型常量
	
	private int type; //类型成员变量
	private boolean required; //必填字段标志
	private List<Dictionary> selectListString;
	private String[] selectList;
	public TextView txtKey; //显示key的textview
	public TextView txtKeyTail; //显示“：”的textview
	public TextView txtValue; //显示value的textview
	public View btnDown; //向下箭头按钮
	public View btnRight; //向右箭头按钮
	public EditText nameValue;//客户名称
	public EditText edtValue; //编辑框
	public CheckBox chkValue; //radio button
	public String edtValueString;
	public ImageButton mobileBtn; //移动电话图标
	public ImageButton pstnBtn; //其他电话图标
	public RelativeLayout valueLayout;
	public CRMManager crmManager;
	public BasicInfoListAdapter basicadapter;
	public int position;
	public Boolean isEditable; //编辑标志   false：显示不能编辑状态
	public RelativeLayout layout;
	public String action = "edit";
	public String prjID = null;
	public Context context; 
	
	private String parentKey = null;
	private String parentKey2 = null;
	
	private WeakReference<DataChangeListener> weak;
	
	public BaseInfoRowViewItem(Context context) {
		this(context,null,null, SIMPLETEXT_TYPE,false);
	}
	
	public BaseInfoRowViewItem(Context context, int type, boolean required) {
		this(context,null,null, type, required);
	}
	
	public BaseInfoRowViewItem(Context _context, String _key, String _value, int type, boolean required) {
		this(_context, _key, _value, type, required, true);
	}
	
	public BaseInfoRowViewItem(Context _context, String _key, String _value, int type, boolean required, boolean editable) {
		super(_context);
		this.context = _context;
		this.weak = new WeakReference<DataChangeListener>((DataChangeListener) _context);
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sc_base_info_row_view, this);
		txtKey = (TextView) findViewById(R.id.base_info_row_keytext);
		txtKeyTail = (TextView) findViewById(R.id.base_info_row_key_tail);
		txtValue = (TextView) findViewById(R.id.base_info_row_valuetext);
		btnDown = findViewById(R.id.base_info_row_downbutton);
		btnRight = findViewById(R.id.base_info_row_rightbutton);
		nameValue = (EditText) findViewById(R.id.base_info_name_valueedit);
		edtValue = (EditText) findViewById(R.id.base_info_row_valueedit);
		chkValue = (CheckBox) findViewById(R.id.base_info_row_checkbox);
		mobileBtn = (ImageButton)findViewById(R.id.base_info_mobile_phone_button);
		pstnBtn = (ImageButton) findViewById(R.id.base_info_pstn_phone_button);
		layout = (RelativeLayout) findViewById(R.id.re_layout);
		
		if (!editable) {
			btnDown.setEnabled(false);
			btnRight.setEnabled(false);
			nameValue.setEnabled(false);
			edtValue.setEnabled(false);
			chkValue.setEnabled(false);
			mobileBtn.setEnabled(false);
			pstnBtn.setEnabled(false);
		}
		
		setType(type);
		setRequired(required);
		setEditable(false);

	}
	
	//设置CheckBox的监听事件
    private final View.OnClickListener mClickListenerCheckBox = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if(viewId == R.id.base_info_row_checkbox){
                if(chkValue.isChecked()){
                    chkValue.setChecked(false);
                }else{
                    chkValue.setChecked(true);
                }
                
            }
        }
    };
    
    private TextWatcher textWatcher = new TextWatcher() {
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            txtValue.setText(s.toString());
            updateData(s.toString());
            weak.get().dataModify(basicadapter.dataList.get(position).key, s.toString());
        }
        
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
	
	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setKey(String key) {
		if (key.equals("老客户推荐标识"))
			txtKey.setText("老客户推荐");
		else if (key.equals("总部VIP客户标识"))
			txtKey.setText("总部VIP客户");
		else if (key.equals("联系人其他电话"))
			txtKey.setText("其他电话");
		else
			txtKey.setText(key);
	}
	
	@SuppressLint("SimpleDateFormat")
	public void setValue(String value) {
		if (StringUtils.isEmpty(value)) {
			txtValue.setText("");
		} else {
			if (type == DATE_TYPE) {
				try {
					Date date = new Date(Long.decode(value));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					txtValue.setText(sdf.format(date));
				} catch (Exception e) {
					e.printStackTrace();
					txtValue.setText("");
				}
			} else {
				txtValue.setText(value);
				if (type == SIMPLETEXT_TYPE || type == MOBILETEXT_TYPE || type == PSTNTEXT_TYPE){
					edtValue.setText(txtValue.getText());
				}
				if(type == RIGHT_TYPE){
				    nameValue.setText(txtValue.getText());
				}
			}
		}
		if (type == BOOLEAN_TYPE || type == BOOLEAN2_TYPE) {
		    if (!StringUtils.isEmpty(value) && value.equals("true")) {
                chkValue.setChecked(true);
            } else {
                chkValue.setChecked(false);
            }
		}
	}

	public void setSelectList(String[] selectList) {
		this.selectList = selectList;
	}
	
	public boolean getRequired()
	{
		return this.required;
	}
	
	public void setRequired(boolean b)
	{
		this.required = b;
	}

	public void setRedColor(){
	    txtKey.setTextColor(Color.RED);
        txtKeyTail.setTextColor(Color.RED);
	}
	
	public void setBlackColor(){
	    txtKey.setTextColor(Color.BLACK);
	    txtKeyTail.setTextColor(Color.BLACK);
	}
	
	public void setEditable(Boolean isEditable) {
		this.isEditable = isEditable;
		if (required) {
			txtKey.setTextColor(Color.RED);
			txtKeyTail.setTextColor(Color.RED);
		} else {
			txtKey.setTextColor(Color.BLACK);
			txtKeyTail.setTextColor(Color.BLACK);
		}
		if (isEditable) {
			switch(type) {
			case SELECTION_TYPE:
				btnDown.setVisibility(View.VISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				btnRight.setVisibility(View.GONE);
				nameValue.setVisibility(View.GONE);
				edtValue.setVisibility(View.INVISIBLE);
				txtValue.setVisibility(View.VISIBLE);
				chkValue.setVisibility(View.GONE);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				break;
			case SIMPLETEXT_TYPE:
				edtValue.addTextChangedListener(new TextWatcher(){
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					    
					}

					@Override
					public void afterTextChanged(Editable s) {
//						try {
//							if (!edtValue.isFocused()) return;
//							
//							BasicInfoListAdapter.Info info = basicadapter.dataList.get(position);
//							if (info != null && info.textChangeListener != null) {
//								info.textChangeListener.textChanged(info.key, s.toString());
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
					}
				});
			case MOBILETEXT_TYPE:
			case PSTNTEXT_TYPE:
				btnDown.setVisibility(View.INVISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				btnRight.setVisibility(View.GONE);
				nameValue.setVisibility(View.GONE);
				edtValue.setVisibility(View.VISIBLE);
				txtValue.setVisibility(View.INVISIBLE);
				chkValue.setVisibility(View.INVISIBLE);
				edtValue.setText(txtValue.getText());
				edtValue.addTextChangedListener(textWatcher);
				if (txtKey.getText().equals("客户移动电话") || txtKey.getText().equals("客户其他电话"))
					edtValue.setInputType(InputType.TYPE_CLASS_PHONE);
				else if (txtKey.getText().equals("证件号码"))
					edtValue.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
				else if (txtKey.getText().equals("E-mail"))
					edtValue.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				break;
			case DATE_TYPE:
				btnDown.setVisibility(View.VISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				btnRight.setVisibility(View.GONE);
				nameValue.setVisibility(View.GONE);
				edtValue.setVisibility(View.INVISIBLE);
				txtValue.setVisibility(View.VISIBLE);
				chkValue.setVisibility(View.GONE);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				break;
			case TIME_TYPE:
				btnDown.setVisibility(View.VISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				btnRight.setVisibility(View.GONE);
				nameValue.setVisibility(View.GONE);
				edtValue.setVisibility(View.INVISIBLE);
				txtValue.setVisibility(View.VISIBLE);
				chkValue.setVisibility(View.GONE);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				break;
			case DATETIME_TYPE:
				btnDown.setVisibility(View.VISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				nameValue.setVisibility(View.GONE);
				btnRight.setVisibility(View.GONE);
				edtValue.setVisibility(View.INVISIBLE);
				txtValue.setVisibility(View.VISIBLE);
				chkValue.setVisibility(View.GONE);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				break;
			case SELECTTEXT_TYPE:
				btnDown.setVisibility(View.INVISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				btnRight.setVisibility(View.VISIBLE);
				nameValue.setVisibility(View.VISIBLE);
				edtValue.setVisibility(View.VISIBLE);
				txtValue.setVisibility(View.INVISIBLE);
				chkValue.setVisibility(View.GONE);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				break;
			case BOOLEAN_TYPE:
				btnDown.setVisibility(View.INVISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				btnRight.setVisibility(View.GONE);
				nameValue.setVisibility(View.GONE);
				edtValue.setVisibility(View.INVISIBLE);
				txtValue.setVisibility(View.INVISIBLE);
				chkValue.setVisibility(View.VISIBLE);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				if (action == "edit" && (prjID != null || txtValue.getText().equals("true")) && txtKey.getText().toString().equals("主联系人")){
                    chkValue.setClickable(false);
				}
				else{
	                 chkValue.setClickable(true);
				}
				break;
			case BOOLEAN2_TYPE:
				btnDown.setVisibility(View.INVISIBLE);
				txtKey.setVisibility(View.VISIBLE);
				btnRight.setVisibility(View.GONE);
				nameValue.setVisibility(View.GONE);
				edtValue.setVisibility(View.INVISIBLE);
				txtValue.setVisibility(View.INVISIBLE);
				chkValue.setVisibility(View.VISIBLE);
				mobileBtn.setVisibility(View.GONE);
				pstnBtn.setVisibility(View.GONE);
				if (action == "edit" && (prjID != null || txtValue.getText().equals("true")) && txtKey.getText().toString().equals("主联系人"))
					chkValue.setClickable(false);
				else
					chkValue.setClickable(true);
				if (txtValue.getText().equals("true"))
					chkValue.setChecked(true);
				else
					chkValue.setChecked(false);
				break;
			case RIGHT_TYPE:
			    txtValue.setVisibility(View.INVISIBLE);
			    nameValue.setVisibility(View.VISIBLE);
                edtValue.setVisibility(View.GONE);
                btnRight.setVisibility(View.VISIBLE);
                nameValue.addTextChangedListener(new TextWatcher(){
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        updateData(s.toString());
                        txtValue.setText(s.toString());
                        weak.get().dataModify(basicadapter.dataList.get(position).key, s.toString());
                        try {
                            if (!nameValue.isFocused()) return;
                            
                            BasicInfoListAdapter.Info info = basicadapter.dataList.get(position);
                            if (info != null && info.textChangeListener != null) {
                                info.textChangeListener.textChanged(info.key, s.toString());
                            }
                        } catch (Exception e) {
                            ResponseException responseException = new ResponseException(e);
                            Toast.makeText(context, responseException.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        
                    }
                });
			    break;
			}
			if (btnDown.getVisibility() == View.VISIBLE) {
				btnDown.setOnClickListener(mClickListener);
				txtValue.setOnClickListener(mClickListener);
			}
			if(btnRight.getVisibility() == View.VISIBLE){
			    btnRight.setOnClickListener(mClickListener);
			}
			chkValue.setOnClickListener(mClickListenerCheckBox);
	        chkValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	            @Override
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	                    if(isChecked){
	                        weak.get().dataModify(basicadapter.dataList.get(position).key, String.valueOf(true));
	                    }else{
	                        weak.get().dataModify(basicadapter.dataList.get(position).key, String.valueOf(false));
	                    }
	                    txtValue.setText(String.valueOf(isChecked));
	                    updateData(String.valueOf(isChecked));
	                }
	        });
	        
		} else {
			btnDown.setVisibility(View.GONE);
			txtKey.setVisibility(View.VISIBLE);
			btnRight.setVisibility(View.GONE);
			edtValue.setVisibility(View.GONE);
			mobileBtn.setVisibility(View.GONE);
			pstnBtn.setVisibility(View.GONE);
			nameValue.setVisibility(View.GONE);
			if (type == BOOLEAN2_TYPE) {
			    txtValue.setVisibility(View.INVISIBLE);
				chkValue.setVisibility(View.VISIBLE);
				if (txtValue.getText().equals("true"))
					chkValue.setChecked(true);
				else
					chkValue.setChecked(false);
			} else if (type == BOOLEAN_TYPE) {
			    txtValue.setVisibility(View.INVISIBLE);
				chkValue.setVisibility(View.VISIBLE);
				if (txtValue.getText().equals("1"))
					chkValue.setChecked(true);
				else
					chkValue.setChecked(false);
			} else {
			    txtValue.setVisibility(View.VISIBLE);
				chkValue.setVisibility(View.GONE);
			}
			if (type == MOBILETEXT_TYPE) {
				mobileBtn.setVisibility(View.VISIBLE);
			} else if (type == PSTNTEXT_TYPE) {
				pstnBtn.setVisibility(View.VISIBLE);
			}
			chkValue.setClickable(false);
		}

		if (isEditable){
				txtValue.setOnClickListener(mClickListener);
				if (type == BOOLEAN_TYPE || type == BOOLEAN2_TYPE){
					layout.setOnClickListener(mClickListener);
				}
		}else{
			mobileBtn.setOnClickListener(mClickListener);
			pstnBtn.setOnClickListener(mClickListener);
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 对输入进行限制
	 * </pre>
	 *
	 * @param length 长度
	 */
	public void setEditTextFilter(Context context,String key,int length){
	    InputFilter[] filters = { new InputFilterUtil(context,key,length) }; 
        edtValue.setFilters(filters);
	}
        
	/**
	 * 
	 * <pre>
	 * 对输入进行限制
	 * </pre>
	 *
	 * @param length 长度
	 */
	public void setNameTextFilter(Context context,String key,int length){
	    InputFilter[] filters = {new InputFilterUtil(context,key,length)};
	    nameValue.setFilters(filters);
	}
	

	private void displayList(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		int checkedNum = 0;

		if ((txtKey.getText().equals("老客户选择") || txtKey.getText().equals("老客户选择:")) && oldCustomerList != null){
			selectList = new String[oldCustomerList.size()];
			for (int i = 0; i < oldCustomerList.size(); i ++) {
				selectList[i] = oldCustomerList.get(i).getCustName() != null ? oldCustomerList.get(i).getCustName() : "";
				if (txtValue.getText().equals(selectList[i]))
					checkedNum = i;
			}
		}else if (selectListString != null){
			selectList = new String[selectListString.size()];
			for (int i = 0; i < selectListString.size(); i ++) {
				selectList[i] = selectListString.get(i).getDicValue();
				if (txtValue.getText().equals(selectList[i]))
					checkedNum = i;
			}
		}
		builder.setTitle(txtKey.getText());
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setNegativeButton(R.string.cancel, null);  
		
		builder.setSingleChoiceItems(selectList, checkedNum, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				txtValue.setText(selectList[item]);
				updateData(item);
				if (selectListString != null && item >= 0 && item < selectListString.size()) {
				    weak.get().dataModify(txtKey.getText().toString(), selectListString.get(item).getDicValue(),selectListString.get(item).getDicKey());
				}else{
				    weak.get().dataModify(basicadapter.dataList.get(position).key,null);
				}
				dialog.cancel();
			}
		});
		builder.create().show();
	}
	
	private List<Customer> oldCustomerList = null;
	private void showSelectPopup() {
	    if(txtKey.getText().toString().equals("老客户选择") || txtKey.getText().toString().equals("老客户选择:")){
	            new BaseTask<List<Dictionary>>((Activity)(this.getContext()), "", true, true) {

	                @Override
	                protected List<Dictionary> doInBackground(String... params) {
	                    List<Dictionary> downloadedList = null;
	                    try {
	                        oldCustomerList = crmManager.getOldCustomer();
                            if (oldCustomerList != null) {
                                downloadedList = new ArrayList<Dictionary>();
                                for (int i = 0; i < oldCustomerList.size(); i ++) {
                                    Dictionary dic = new Dictionary();
                                    dic.setDicKey(oldCustomerList.get(i).getCustomerID());
                                    dic.setDicValue(oldCustomerList.get(i).getCustName());
                                    downloadedList.add(dic);
                                }
                            }
	                    } catch (ResponseException e) {
	                        responseException = e;
	                    }
	                    if (downloadedList == null) {
	                        downloadedList = new ArrayList<Dictionary>();
	                    }
	                    selectListString = downloadedList;
	                    return downloadedList;
	                }
	                
	                @Override
	                protected void onPostExecute(List<Dictionary> result) {
	                    super.onPostExecute(result);
	                    if(responseException != null){
	                        Toast.makeText(getContext(), responseException.getMessage(), Toast.LENGTH_LONG).show();
	                        return;
	                    }
	                    if(result.size() != 0){
	                        displayList();
	                    }
	                }
	            }.execute();
	    }else if(txtKey.getText().toString().equals("市县") || txtKey.getText().toString().equals("行政区划")){
	        new BaseTask<List<Dictionary>>((Activity)(this.getContext()), "", true, true) {

                @Override
                protected List<Dictionary> doInBackground(String... params) {
                    List<Dictionary> downloadedList = null;
                    try {
                        if(parentKey != null && parentKey2 == null){
                        	Log.i("print", "*****"+"parentKey"+"*****"+parentKey);
                            downloadedList = crmManager.getRelativeDictionaries(getCasecadeDicName(txtKey.getText().toString()), parentKey);
                        }
                        if(parentKey != null && parentKey2 != null){
                        	Log.i("print", "*****"+"parentKey"+"*****"+parentKey);
                        	Log.i("print", "*****"+"parentKey2"+"*****"+parentKey2);
                            downloadedList = crmManager.getRelativeDictionaries(getCasecadeDicName(txtKey.getText().toString()), parentKey,parentKey2);
                        }
                        
                    } catch (ResponseException e) {
                        responseException = e;
                    }
                    if (downloadedList == null) {
                        downloadedList = new ArrayList<Dictionary>();
                    }
                    selectListString = downloadedList;
                    return downloadedList;
                }
                
                @Override
                protected void onPostExecute(List<Dictionary> result) {
                    super.onPostExecute(result);
                    if(responseException != null){
                        Toast.makeText(getContext(), responseException.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(result.size() != 0){
                        displayList();
                    }
                }
            }.execute();
	    }else if(txtKey.getText().toString().equals("车型") || txtKey.getText().toString().equals("内饰颜色") || txtKey.getText().toString().equals("车身颜色") || txtKey.getText().toString().equals("配置")){
	    	 new BaseTask<List<Dictionary>>((Activity)(this.getContext()), "", true, true) {

	                @Override
	                protected List<Dictionary> doInBackground(String... params) {
	                    List<Dictionary> downloadedList = null;
	                    try {
	                        if(parentKey != null && (txtKey.getText().toString().equals("内饰颜色") || txtKey.getText().toString().equals("车身颜色") || txtKey.getText().toString().equals("车型"))){
	                        	Log.i("print", "*****"+"parentKey"+"*****"+parentKey);
	                            downloadedList = crmManager.getRelativeDictionaries(getCasecadeDicName(txtKey.getText().toString()), parentKey);
	                        }
	                        if(parentKey2 != null && txtKey.getText().toString().equals("配置")){
	                            downloadedList = crmManager.getRelativeDictionaries(getCasecadeDicName(txtKey.getText().toString()), parentKey2);
	                        }
	                        
	                    } catch (ResponseException e) {
	                        responseException = e;
	                    }
	                    if (downloadedList == null) {
	                        downloadedList = new ArrayList<Dictionary>();
	                    }
	                    selectListString = downloadedList;
	                    return downloadedList;
	                }
	                
	                @Override
	                protected void onPostExecute(List<Dictionary> result) {
	                    super.onPostExecute(result);
	                    if(responseException != null){
	                        Toast.makeText(getContext(), responseException.getMessage(), Toast.LENGTH_LONG).show();
	                        return;
	                    }
	                    if(result.size() != 0){
	                        displayList();
	                    }
	                }
	            }.execute();
	    }else{
	        new BaseTask<List<Dictionary>>((Activity)(this.getContext()), "", true, true) {

                @Override
                protected List<Dictionary> doInBackground(String... params) {
                    List<Dictionary> downloadedList = null;
                    try {
                        downloadedList = crmManager.getDictionariesByType(getDicName(txtKey));
                    } catch (ResponseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (downloadedList == null) {
                        downloadedList = new ArrayList<Dictionary>();
                    }
                    selectListString = downloadedList;
                    return downloadedList;
                }
                
                @Override
                protected void onPostExecute(List<Dictionary> result) {
                    super.onPostExecute(result);
                    if(responseException != null){
                        Toast.makeText(getContext(), responseException.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(result != null){
                        displayList();
                    }
                }
            }.execute();
	    }
		
	}
	
	private final View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewId = v.getId();

			if ((!isEditable && viewId != R.id.base_info_mobile_phone_button && viewId != R.id.base_info_pstn_phone_button) ||
				(action == "edit" && (prjID != null || txtValue.getText().equals("true")) && txtKey.getText().toString().equals("主联系人")))
				return;

			switch (viewId) {
			case R.id.base_info_row_downbutton:
			case R.id.base_info_row_valuetext:
				if (!isEditable)
					break;
				if (type == SELECTION_TYPE) {
					showSelectPopup();
				} else if (type == DATE_TYPE) {
					String datastr = txtValue.getText().toString();
					int year = 0;     
				    int month = 0;     
				    int day = 0;
					if(!datastr.isEmpty())
					{
						try {
							String temp[] = datastr.split("-");
							if(temp.length == 3)
							{
								year =  Integer.parseInt(temp[0]);
								month =  Integer.parseInt(temp[1]) - 1;
								day =  Integer.parseInt(temp[2]);
							}
						}
				        catch (NumberFormatException e) {
				            e.printStackTrace();
				            year = 0;     
						    month = 0;     
						    day = 0; 
				        }
					}
					if(year == 0 || day == 0)
					{
						final Calendar c = Calendar.getInstance();     
					    year = c.get(Calendar.YEAR);     
					    month = c.get(Calendar.MONTH);     
					    day = c.get(Calendar.DAY_OF_MONTH); 
					}
					
				    new DatePickerDialog(BaseInfoRowViewItem.this.getContext(), mDateSetListener,     
	                        year, month, day).show();
				}
				break;
			case R.id.base_info_row_rightbutton:
			    Activity activity = (Activity) context;
                Intent intent = new Intent(activity,ScCustomerInfoListActivity.class);
                activity.startActivityForResult(intent, 1);
                activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				break;
			case R.id.base_info_mobile_phone_button:
			case R.id.base_info_pstn_phone_button:
				if (isEditable)
					break;
				call(txtValue.getText().toString());
				break;
			case R.id.re_layout:
				if (txtValue.getText().equals("true")){
					txtValue.setText("false");
					chkValue.setChecked(false);
				}else{
					txtValue.setText("true");
					chkValue.setChecked(true);
				}
			}
		}
	};
	
    private DatePickerDialog.OnDateSetListener mDateSetListener =     
        new DatePickerDialog.OnDateSetListener() {     
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				Log.i("DatePickerDialog:",String.valueOf(arg1)+String.valueOf(arg2+1)+String.valueOf(arg3));
				StringBuilder date = new StringBuilder().append(arg1).append("-")  
	                .append(formateTime(arg2+1)).append("-")  
	                .append(formateTime(arg3));
				if(DateFormatUtils.parseDateToLong(date.toString())<DateFormatUtils.parseDateToLong("1970-01-01"))
				    return;
				txtValue.setText(date.toString());
				if (type == DATE_TYPE) {
				    weak.get().dataModify(basicadapter.dataList.get(position).key, DateFormatUtils.parseDateToLongString(txtValue.getText().toString()));
				}else{
				    weak.get().dataModify(basicadapter.dataList.get(position).key, txtValue.getText().toString());
				}
				updateData();
			}
    };
    
    private String formateTime(int time) {  
        String timeStr = "";  
        if (time < 10){  
            timeStr = "0" + String.valueOf(time);  
        }else {  
            timeStr = String.valueOf(time);  
        }  
        return timeStr;  
    }  
	
	private void call(String num) {
	    try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:"+num));
	        getContext().startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	    }
	 }

	public TextView getTxtKey() {
		return txtKey;
	}
	
	public void setTxtKey(TextView txtKey) {
		this.txtKey = txtKey;
	}
	
	public TextView getTxtValue() {
		return txtValue;
	}
	
	public void setTxtValue(TextView txtValue) {
		this.txtValue = txtValue;
	}
	
	public EditText getEdtValue() {
		return edtValue;
	}
	
	public void setEdtValue(EditText edtValue) {
		this.edtValue = edtValue;
	}
	
	public CheckBox getChkValue() {
		return chkValue;
	}
	
	public void setChkValue(CheckBox chkValue) {
		this.chkValue = chkValue;
	}
	
	public String getEdtValueString() {
		return edtValueString;
	}
	
	public void setEdtValueString(String edtValueString) {
		this.edtValueString = edtValueString;
	}
	
	public static String getCasecadeDicName(String keyString){

		if(keyString.equals("市县") || keyString.equals("市县:")){
			return "city";
		}else if(keyString.equals("行政区划") || keyString.equals("行政区划:"))
		{
			return "district";
		}else if(keyString.equals("车型") || keyString.equals("车型:"))
		{
			return "model";
		}else if(keyString.equals("内饰颜色") || keyString.equals("内饰颜色:"))
		{
			return "inner";
		}else if(keyString.equals("车身颜色") || keyString.equals("车身颜色:"))
		{
			return "outer";
		}else if(keyString.equals("配置") || keyString.equals("配置:"))
		{
			return "customerCfg";
		}
		
		return "";
	}
	
	public static String getDicName(TextView txtKey) {
		String keyString = txtKey.getText().toString().trim();
		if (keyString.equals("客户来源") || keyString.equals("客户来源:"))
			return "dicCustomerFrom";
		if (keyString.equals("客户类别") || keyString.equals("客户类别:"))
			return "dicCustomerType";
		if (keyString.equals("信息来源") || keyString.equals("信息来源:"))
			return "dicInfoFrom";
		if (keyString.equals("证件类型") || keyString.equals("证件类型:"))
			return "dicCardType";
		if (keyString.equals("客户性别") || keyString.equals("客户性别:"))
			return "dicSex";
		if (keyString.equals("省区") || keyString.equals("省区:"))
			return "dicProvince";
		if (keyString.equals("市县") || keyString.equals("市县:"))
			return "dicCity";
		if (keyString.equals("行政区划") || keyString.equals("行政区划:"))
			return "dicDistrict";
		if (keyString.equals("方便联系时间") || keyString.equals("方便联系时间:"))
			return "dicContractTime";
		if (keyString.equals("希望联系方式") || keyString.equals("希望联系方式:"))
			return "dicContact";
		if (keyString.equals("现用车") || keyString.equals("现用车:"))
			return "dicNowCarType";
		if (keyString.equals("所处行业") || keyString.equals("所处行业:"))
			return "dicIndustry";
		if (keyString.equals("职务") || keyString.equals("职务:"))
			return "dicPosition";
		if (keyString.equals("教育程度") || keyString.equals("教育程度:"))
			return "dicEducation";
		if (keyString.equals("现用车品牌") || keyString.equals("现用车品牌:") 
				|| keyString.equals("品牌") || keyString.equals("品牌:"))
			return "dicBrand";
		if (keyString.equals("客户爱好1") || keyString.equals("客户爱好1:"))
			return "dicInterest";
		if (keyString.equals("客户爱好2") || keyString.equals("客户爱好2:"))
			return "dicInterest";
		if (keyString.equals("客户爱好3") || keyString.equals("客户爱好3:"))
			return "dicInterest";
		if (keyString.equals("现用车牌照号") || keyString.equals("现用车牌照号:"))
			return "dicModel";
		if (keyString.equals("企业性质") || keyString.equals("企业性质:"))
			return "dicEnterpType";
		if (keyString.equals("企业人数") || keyString.equals("企业人数:"))
			return "dicEnterpPeople";
		if (keyString.equals("注册资本") || keyString.equals("注册资本:"))
			return "dicRegistered";
		if (keyString.equals("竞争车型") || keyString.equals("竞争车型:"))
			return "dicCompeCarModel";
		if (keyString.equals("大客户选择") || keyString.equals("大客户选择:"))
			return "dicDkh";
		if (keyString.equals("性别") || keyString.equals("性别:"))
			return "dicSex";
		if (keyString.equals("年龄范围") || keyString.equals("年龄范围:"))
			return "dicAgeScope";
		if (keyString.equals("联系人类型") || keyString.equals("联系人类型:"))
			return "diccontType";
		if (keyString.equals("与客户关系") || keyString.equals("与客户关系:"))
			return "dicContRelation";
		if (keyString.equals("驾驶证有效期") || keyString.equals("驾驶证有效期:"))
			return "dicLicenseValid";
		if (keyString.equals("集客方式") || keyString.equals("集客方式:"))
			return "dicCollectWay";
		if (keyString.equals("车型") || keyString.equals("车型:"))
			return "dicModel";
		if (keyString.equals("车身颜色") || keyString.equals("车身颜色:"))
			return "dicOutsideColor";
		if (keyString.equals("内饰颜色") || keyString.equals("内饰颜色:"))
			return "dicInsideColor";
		if (keyString.equals("配置") || keyString.equals("配置:"))
			return "dicCarConfiguration";
		if (keyString.equals("交易价格区间") || keyString.equals("交易价格区间:"))
			return "dicDealPriceInterval";
		if (keyString.equals("付款方式") || keyString.equals("付款方式:"))
			return "dicPayment";
		if (keyString.equals("流程状态") || keyString.equals("流程状态:"))
			return "dicFlowStatus";
		if (keyString.equals("购买动机") || keyString.equals("购买动机:"))
			return "dicPurchMotivation";
		if (keyString.equals("放弃原因") || keyString.equals("放弃原因:"))
			return "dicGiveupReason";
		if(keyString.equals("执行状态") || keyString.equals("执行状态:"))
			return "dicExecuteStatus";
		if(keyString.equals("活动类型") || keyString.equals("活动类型:"))
			return "dicActivityType";
		if(keyString.equals("联系结果") || keyString.equals("联系结果:"))
			return "dicVisitstatus";
		if(keyString.equals("牌照属性") || keyString.equals("牌照属性:"))
			return "dicPZH";
		if(keyString.equals("成交可能性") || keyString.equals("成交可能性:"))
			return "dicDealPossibility";
		if(keyString.equals("活动内容") || keyString.equals("活动内容:"))
			return "";
		return "dic";
	}
	
	private void updateData()
	{
		if (type == DATE_TYPE) {
			this.basicadapter.dataList.get(position).setValue(DateFormatUtils.parseDateToLongString(this.txtValue.getText().toString()));
		} else {
			this.basicadapter.dataList.get(position).setValue(this.txtValue.getText().toString());
			
		}
	}
	
	private void updateData(String data) {
	    
		edtValueString = data;
		this.basicadapter.dataList.get(position).setValue(data);
	}
	
	private void updateData(int selectIndex) {
		if (selectListString != null && selectIndex >= 0 && selectIndex < selectListString.size()) {
			this.basicadapter.dataList.get(position).setValue(selectListString.get(selectIndex).getDicValue());
			this.basicadapter.dataList.get(position).setPairKey(selectListString.get(selectIndex).getDicKey());
		} else {
			this.basicadapter.dataList.get(position).setValue(null);
			this.basicadapter.dataList.get(position).setPairKey(null);	
			
		}
		
	}
	
	public interface DataChangeListener{
	    public void dataModify(String key,String value);
	    public void dataModify(String key,String value,String pairKey);
	}
	
	public void setParentKey(String pairKey){
	    this.parentKey = pairKey;
	}
	
	public void setParentKey2(String pairKey){
	    this.parentKey2 = pairKey;
	}
	
}
