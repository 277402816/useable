package com.roiland.crm.sm.core.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.roiland.crm.sm.core.database.DatabaseBean;
import com.roiland.crm.sm.core.database.DatabaseDefine;
import com.roiland.crm.sm.core.database.DatabaseDefine.ColumnField;

/**
 * 
 * <pre>
 * 数据字典对象
 * </pre>
 *
 * @author Chunji Li
 * @version $Id: Dictionary.java, v 0.1 2013-8-2 上午10:14:00 Chunji Li Exp $
 */
public class Dictionary extends DatabaseBean {
    private static final String TABLE_NAME = DictionaryTable.TABLE_NAME;

    private String              dicType;
    private String              dicKey;
    private String              dicValue;
    public String[]             dicValues;
    private String              dicParentKey;
    private long                dicCreatedDate;

    public static interface DictionaryTable {
        static String        TABLE_NAME              = "Dictionaries";

        static ColumnField   COLUMN_ID               = new ColumnField(DatabaseDefine.COLUMN_ID,
                                                         "TEXT", "PRIMARY KEY");
        static ColumnField   COLUMN_DIC_TYPE         = new ColumnField("DicType", "TEXT", null);
        static ColumnField   COLUMN_DIC_KEY          = new ColumnField("DicKey", "TEXT", null);
        static ColumnField   COLUMN_DIC_VALUE        = new ColumnField("DicValue", "TEXT", null);
        static ColumnField   COLUMN_DIC_PARENT_KEY   = new ColumnField("DicParentKey", "TEXT", null);
        static ColumnField   COLUMN_DIC_CREATED_DATE = new ColumnField("DicCreatedDate", "INTEGER",
                                                         null);

        static ColumnField[] COLUMES                 = { COLUMN_ID, COLUMN_DIC_TYPE,
                                                             COLUMN_DIC_KEY, COLUMN_DIC_VALUE,
                                                             COLUMN_DIC_PARENT_KEY,
                                                             COLUMN_DIC_CREATED_DATE };
    }

    public Dictionary(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(DictionaryTable.COLUMN_ID.name));
        dicType = cursor.getString(cursor.getColumnIndex(DictionaryTable.COLUMN_DIC_TYPE.name));
        dicKey = cursor.getString(cursor.getColumnIndex(DictionaryTable.COLUMN_DIC_KEY.name));
        dicValue = cursor.getString(cursor.getColumnIndex(DictionaryTable.COLUMN_DIC_VALUE.name));
        dicParentKey = cursor.getString(cursor
            .getColumnIndex(DictionaryTable.COLUMN_DIC_PARENT_KEY.name));
        dicCreatedDate = cursor.getLong(cursor
            .getColumnIndex(DictionaryTable.COLUMN_DIC_CREATED_DATE.name));
    }

    public Dictionary() {
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DictionaryTable.COLUMN_ID.name, id);
        values.put(DictionaryTable.COLUMN_DIC_TYPE.name, dicType);
        values.put(DictionaryTable.COLUMN_DIC_KEY.name, dicKey);
        values.put(DictionaryTable.COLUMN_DIC_VALUE.name, dicValue);
        values.put(DictionaryTable.COLUMN_DIC_PARENT_KEY.name, dicParentKey);
        values.put(DictionaryTable.COLUMN_DIC_CREATED_DATE.name, dicCreatedDate);
        return values;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public String getDicParentKey() {
        return dicParentKey;
    }

    public void setDicParentKey(String dicParentKey) {
        this.dicParentKey = dicParentKey;
    }

    public long getDicCreatedDate() {
        return dicCreatedDate;
    }

    public void setDicCreatedDate(long dicCreatedDate) {
        this.dicCreatedDate = dicCreatedDate;
    }

}
