package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.service.DictionaryAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 数据字典接口实现类
 * @extends AbstractBaseAPI
 * @implements DictionaryAPI
 * @author zhao.jiaqi
 * @version $Id: DictionaryAPIImpl.java, v 0.1 2013-5-9 下午3:42:25 zhao.jiaqi Exp $
 */
/**
 * 
 * <pre>
 * 数据字典接口实现类
 * </pre>
 *
 * @author  zhao.jiaqi
 * @version $Id: DictionaryAPIImpl.java, v 0.1 2013-8-2 上午11:03:05 zhao.jiaqi Exp $
 */
public class DictionaryAPIImpl extends AbstractBaseAPI implements DictionaryAPI {

	private static final String tag = Log.getTag(DictionaryAPIImpl.class);
/**
 * 
 * @see com.roiland.crm.core.service.DictionaryAPI#getDictionary(java.lang.String)
 */
	@Override
	public List<Dictionary> getDictionary(String dicName)
			throws ResponseException {
		List<Dictionary> list = null;
		try {

			JSONObject params = new JSONObject();
			// 传入参数
			params.put("dicName", dicName);
			// 获得响应
			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_DICTIONARY), params, null);
			if (response.isSuccess()) {
				list = new ArrayList<Dictionary>();
				JSONObject jsonBean = new JSONObject(getSimpleString(response));
				JSONArray dicObject = jsonBean.getJSONArray("dicObject");
				long currentTime = System.currentTimeMillis();
				// 取得返回值
				for (int i = 0; i < dicObject.length(); i++) {
					JSONObject json = dicObject.getJSONObject(i);
					Dictionary dictionary = new Dictionary();
					dictionary.setDicKey(json.getString("key"));
					dictionary.setDicValue(json.getString("value"));
					dictionary.setDicType(dicName);
					dictionary.setDicCreatedDate(currentTime);
					list.add(dictionary);
				}
				return list;
			}
			throw new ResponseException();
		} catch (IOException e) {
			Log.e(tag, "Connection network error.", e);
			throw new ResponseException(e);
		} catch (JSONException e) {
			Log.e(tag, "Parsing data error.", e);
			throw new ResponseException(e);
		} catch (Exception e) {
			throw new ResponseException(e);
		}
	}

	/**
	 * 
	 * @see com.roiland.crm.core.service.DictionaryAPI#getCascadeDict(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<Dictionary> getCascadeDict(String dicType, String... parentKey)
			throws ResponseException {

		List<Dictionary> list = null;
		try {
			JSONObject params = new JSONObject();
			JSONArray array = new JSONArray();
			array.put(dicType);
			for (int i = 0; i < parentKey.length; i++) {
				array.put(parentKey[i]);
			}

			// 传入参数
			params.put("args", array);
			// 获得响应
			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_CASCADEDICT), params, null);
			if (response.isSuccess()) {
				list = new ArrayList<Dictionary>();
				JSONObject jsonBean = new JSONObject(getSimpleString(response));
				JSONArray dicObject = jsonBean.getJSONArray("dicObject");
				long currentTime = System.currentTimeMillis();
				// 取得返回值
				for (int i = 0; i < dicObject.length(); i++) {
					JSONObject json = dicObject.getJSONObject(i);
					Dictionary dictionary = new Dictionary();
					dictionary.setDicKey(json.getString("key"));
					dictionary.setDicValue(json.getString("value"));
					dictionary.setDicType(dicType);
					dictionary.setDicParentKey(StringUtils
							.arrayToString(parentKey));
					dictionary.setDicCreatedDate(currentTime);

					list.add(dictionary);
				}
				return list;
			}
			throw new ResponseException();
		} catch (IOException e) {
			Log.e(tag, "Connection network error.", e);
			throw new ResponseException(e);
		} catch (JSONException e) {
			Log.e(tag, "Parsing data error.", e);
			throw new ResponseException(e);
		} catch (Exception e) {
			throw new ResponseException(e);
		}
	}
}
