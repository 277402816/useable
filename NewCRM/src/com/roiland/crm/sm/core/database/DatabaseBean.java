package com.roiland.crm.sm.core.database;

import com.roiland.crm.sm.core.database.DatabaseDefine.ColumnField;
import com.roiland.crm.sm.utils.Log;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * @author Chunli Li
 *
 */
public abstract class DatabaseBean {
    public String id;

	public abstract String getTableName();
	public abstract ContentValues toContentValues();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getCreateTableSQL(String tableName, ColumnField[] columns)
    {
    	StringBuilder sql = new StringBuilder();
    	sql.append("CREATE TABLE '").append(tableName).append("' (\n");
    	boolean isFirst = true;
    	for (ColumnField columnField : columns) {
        	if (isFirst) {
        		isFirst = false;
        	} else {
        		sql.append(" ,\n");
        	}

        	sql.append(" '").append(columnField.name).append("' ").append(columnField.type).append(" ");

        	if (columnField.other != null)
        		sql.append(columnField.other);
		}
    	sql.append("\n)");
    	if (Log.DEBUG.get()) {
    		Log.d("DatabaseBean", "CreateTableSQL >> " + sql.toString());
    	}
    	return sql.toString();
    }
    
    protected class CursorWrap {
        private Cursor mCursor;

        public CursorWrap(Cursor cursor) {
            mCursor = cursor;
        }

        public byte[] getBlob(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.getBlob(columnIndex);
        }

        public String getString(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.getString(columnIndex);
        }

        public short getShort(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.getShort(columnIndex);
        }

        public int getInt(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.getInt(columnIndex);
        }

        public Integer getInteger(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            if (mCursor.isNull(columnIndex)) {
                return null;
            } else {
                return mCursor.getInt(columnIndex);
            }
        }

        public long getLong(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.getLong(columnIndex);
        }

        public Long getLongObject(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            if (mCursor.isNull(columnIndex)) {
                return null;
            } else {
                return mCursor.getLong(columnIndex);
            }
        }

        public float getFloat(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.getFloat(columnIndex);
        }

        public double getDouble(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.getDouble(columnIndex);
        }

        public boolean isNull(String columnName) {
            int columnIndex = mCursor.getColumnIndex(columnName);
            return mCursor.isNull(columnIndex);
        }
    }
}
