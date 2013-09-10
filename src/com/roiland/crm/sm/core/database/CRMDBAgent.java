package com.roiland.crm.sm.core.database;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.Dictionary.DictionaryTable;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * SQLite数据库操作类
 * </pre>
 *
 * @author cjyy
 * @version $Id: CRMDBAgent.java, v 0.1 2013-5-13 下午05:23:12 cjyy Exp $
 */
public class CRMDBAgent extends DatabaseAgent {

	private static final String GET_DICS_BY_TYPE = "SELECT [COLUMES] FROM Dictionaries WHERE DicType = ?";
	private static final String GET_DICS_BY_TYPE_AND_PARENT = "SELECT [COLUMES] FROM Dictionaries WHERE DicType = ? AND DicParentKey = ?";
	
	private static final long DAY_OF_MILLIS = 1000 * 60 * 60 * 24; //数据字典有效期为 24小时
	
	public CRMDBAgent(SQLiteOpenHelper dbHelper) {
		super(dbHelper);
	}
	
	/**
	 * 
	 * <pre>
	 * 通过dicType获取数据字典.
	 * </pre>
	 *
	 * @param dicType 数据类型
	 * @return 返回数据字典列表
	 */
	public ArrayList<Dictionary> getDictionariesByType(final String dicType) {
		if (StringUtils.isEmpty(dicType))
			return null;
		SqlTask<ArrayList<Dictionary>> task = new SqlTask<ArrayList<Dictionary>> (dbHelper.getWritableDatabase()) {

			@Override
			protected ArrayList<Dictionary> doExecute() {
				String sql = getSql(GET_DICS_BY_TYPE, DictionaryTable.COLUMES);
				Cursor cursor = rawQuery(sql, dicType);
				ArrayList<Dictionary> dicArray = null;
				if (cursor != null && cursor.moveToFirst()) {
					dicArray = new ArrayList<Dictionary>();
					do {
						Dictionary dictionary = new Dictionary(cursor);
						//判断数据是否已过去，如果已过期将该数据删�?
						if (!isDicExpired(dictionary)) {
							dicArray.add(dictionary);
						} else {
							delete(dictionary);
						}
					} while (cursor.moveToNext());
				}
				return dicArray;
			}
			
		};
		
		return task.execute();
	}
	
	/**
	 * <pre>
	 * 判断数据是否已过期
     * </pre>
	 *
	 * @param dictionary
	 * @return
	 */
	private boolean isDicExpired(Dictionary dictionary) {
		if (dictionary == null) 
			return true;
		long pastTime = System.currentTimeMillis() - dictionary.getDicCreatedDate();  
		return pastTime > DAY_OF_MILLIS; 
	}
	
	/**
	 * 
	 * <pre>
	 * 获取级联数据字典，通过dicType和parentKey获取数据字典.
	 * </pre>
	 *
	 * @param dicType 数据字典类型
	 * @param parentKey 相关联的数据字典key
	 * @return  返回数据字典列表
	 */
	public ArrayList<Dictionary> getRelativeDictionaries(final String dicType, final String parentKey) {
		if (StringUtils.isEmpty(dicType))
			return null;
		SqlTask<ArrayList<Dictionary>> task = new SqlTask<ArrayList<Dictionary>> (dbHelper.getWritableDatabase()) {

			@Override
			protected ArrayList<Dictionary> doExecute() {
				String sql = getSql(GET_DICS_BY_TYPE_AND_PARENT, DictionaryTable.COLUMES);
				Cursor cursor = rawQuery(sql, dicType, parentKey);
				ArrayList<Dictionary> dicArray = null;
				if (cursor != null && cursor.moveToFirst()) {
					dicArray = new ArrayList<Dictionary>();
					do {
						Dictionary dictionary = new Dictionary(cursor);
						//判断数据是否已过去，如果已过期将该数据删除
						if (!isDicExpired(dictionary)) {
							dicArray.add(dictionary);
						} else {
							delete(dictionary);
						}
					} while (cursor.moveToNext());
				}
				return dicArray;
			}
			
		};
		
		return task.execute();
	}
	
}
