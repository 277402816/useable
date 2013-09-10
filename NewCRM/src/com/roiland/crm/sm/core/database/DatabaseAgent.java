package com.roiland.crm.sm.core.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.roiland.crm.sm.core.database.DatabaseDefine.ColumnField;
import com.roiland.crm.sm.utils.Log;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

/**
 * @author Chunji Li
 * 
 */
public class DatabaseAgent {
    private static final String tag = Log.getTag(DatabaseAgent.class);

    private final Lock mDbLock = new ReentrantLock();

    protected SQLiteOpenHelper dbHelper;

    public DatabaseAgent(SQLiteOpenHelper dbHelper) {
    	this.dbHelper = dbHelper;
    }

    public abstract class SqlTask<T> {
        public SQLiteDatabase db;
        private List<Cursor> cursors = new ArrayList<Cursor>();
        private List<SQLiteStatement> statements = new ArrayList<SQLiteStatement>();

        private boolean needStopWatch = false;
        private String stopName = "";
        private String sql = "";
        private String[] selectionArgs = null;

        protected abstract T doExecute();
        
        public SqlTask(SQLiteDatabase db) {
        	this.db = db;
        }

        public SqlTask(String stopName) {
            this.stopName = stopName;
            //needStopWatch = true;
        }

        public Cursor rawQuery(String sql, String... selectionArgs) {
            if (needStopWatch) {
                StringBuilder builder = new StringBuilder();
                if (selectionArgs != null) {
                    for (Object arg : selectionArgs) {
                        builder.append(arg).append(", ");
                    }
                }
                Log.d(tag, "QUERY SQL: " + sql + "\nARGS: " + builder);
            }

            //Sanitiize the SQL. This is needed so that any ' are escaped
            //TODO: it will cause SQL syntax error issue. 
            //String sqlSanitized=sql.replaceAll("'", "''");
            if (needStopWatch) {
                this.sql = sql;
                this.selectionArgs = selectionArgs;
            }

            Cursor cursor = db.rawQuery(sql, selectionArgs);
            cursors.add(cursor);
            return cursor;
        }
        
        protected SQLiteStatement compileStatement(String sql) {
            SQLiteStatement statement = db.compileStatement(sql);
            statements.add(statement);
            return statement;
        }

        protected boolean execSQL(String sql, Object... bindArgs) {
            boolean success = false;
            try {
                if (bindArgs == null || bindArgs.length == 0) {
                    if (needStopWatch) {
                        Log.d(tag, "EXECUTE SQL: " + sql);
                    }
                    db.execSQL(sql);
                } else {
                    if (needStopWatch) {
                        StringBuilder builder = new StringBuilder();
                        for (Object arg : bindArgs) {
                            builder.append(arg).append(", ");
                        }
                        Log.d(tag, "EXECUTE SQL: " + sql + "\nARGS: " + builder);
                    }
                    db.execSQL(sql, bindArgs);
                }

                success = true;
            } catch (SQLException e) {
                Log.e(tag, "SQL: \n" + sql.toString(), e);
            }
            return success;
        }

        private void closeResources() {
            for (Iterator<Cursor> iterator = cursors.iterator(); iterator.hasNext();) {
                Cursor cursor = (Cursor) iterator.next();
                cursor.close();
                iterator.remove();
            }
            
            for (Iterator<SQLiteStatement> iterator = statements.iterator(); iterator.hasNext();) {
                SQLiteStatement statement = (SQLiteStatement) iterator.next();
                statement.close();
                iterator.remove();
            }
        }
        
        public void closeDb() {
            db.close();
        }
        
        public T execute() {
            T t = null;
            long start = System.nanoTime();
            mDbLock.lock();
            try {
                db = openSQLiteDatabase();
                t = doExecute();
            } catch (Exception e) {
                Log.e(tag, e.getMessage(), e);
            } finally {
                try {
                    closeResources();
                } catch (Exception e) {
                    Log.e(tag, e.getMessage(), e);
                }

                try {
                    if (db != null) {
                        db.close();
                    }
                } catch (Exception e) {
                    Log.e(tag, e.getMessage(), e);
                }

                mDbLock.unlock();

                try {
                    if (needStopWatch) {
                        long duration = (System.nanoTime() - start) / 1000000L;
                        StringBuffer strOut = new StringBuffer();
                        strOut.append(">>> SqlExec Stopwatch >>>")
                                .append(stopName)
                                .append(": ")
                                .append(duration)
                                .append(" ms : ")
                                .append(sql.replaceAll("\n", " "))
                                .append(" : ");
                        if (selectionArgs != null) {
                            for (int i = 0; i < selectionArgs.length; i++) {
                                strOut.append("[").append(selectionArgs[i]).append("]");
                            }
                        }
                        Log.d(tag, strOut.toString());
                    }
                } catch (Exception e) {
                    Log.e(tag, e.getMessage(), e);
                }
            }
            
            return t;
        }
    }

    protected SQLiteDatabase openSQLiteDatabase() {
        return dbHelper.getWritableDatabase();
    }

    public boolean insert(final DatabaseBean bean) {
        SqlTask<Boolean> task = new SqlTask<Boolean>(dbHelper.getWritableDatabase()) {
            @Override
            protected Boolean doExecute() {
                long rowId =  -1;
                String table = bean.getTableName();
                ContentValues values = bean.toContentValues();
                String id = values.getAsString(DatabaseDefine.COLUMN_ID);
                if (TextUtils.isEmpty(id)) {
                    id = generateId();
                    values.put(DatabaseDefine.COLUMN_ID, id);
                    bean.setId(id);
                }

                if (table != null) {
                    if (Log.DEBUG.get()) {
                        Log.format(tag, "INSERT INTO %s VALUES [%s]", table, values);
                    }

                    rowId = db.insert(table, null, values);
                }

                return rowId > 0;
            }
        };

        if (bean == null) {
            Log.w(tag, "bean is NULL!", new Exception());
            return false;
        } else {
            return task.execute();
        }
    }

    public boolean update(final DatabaseBean bean) {
        SqlTask<Boolean> task = new SqlTask<Boolean>(dbHelper.getWritableDatabase()) {
            @Override
            protected Boolean doExecute() {
                int rowCount =  -1;
                String table = bean.getTableName();
                ContentValues values = bean.toContentValues();
                String whereClause = null;
                String[] whereArgs = null; 
                if (table != null && values.containsKey("id")) {
                    if (Log.DEBUG.get()) {
                        Log.format(tag, "UPDATE %s SET [%s]", table, values);
                    }

                    String id = values.getAsString(DatabaseDefine.COLUMN_ID);
                    values.remove(DatabaseDefine.COLUMN_ID);
                    whereClause = "id = ?";
                    whereArgs = new String[] {id};
                    rowCount = db.update(table, values, whereClause, whereArgs);
                }

                return rowCount > 0;
            }
        };

        if (bean == null) {
            Log.w(tag, "bean is NULL!", new Exception());
            return false;
        } else {
            return task.execute();
        }
    }

    public boolean delete(final DatabaseBean bean) {
        SqlTask<Boolean> task = new SqlTask<Boolean>(dbHelper.getWritableDatabase()) {
            @Override
            protected Boolean doExecute() {
                int rowCount =  -1;
                String table = bean.getTableName();
                ContentValues values = bean.toContentValues();
                String id = values.getAsString(DatabaseDefine.COLUMN_ID);
                if (table != null && !TextUtils.isEmpty(id)) {
                    if (Log.DEBUG.get()) {
                        Log.d(tag, "DELETE " + table + " FROM id = " + id);
                    }
                    String whereClause = "id = ?";
                    String[] whereArgs = new String[] {id};
                    rowCount = db.delete(table, whereClause, whereArgs);
                }

                return rowCount > 0;
            }
        };

        if (bean == null) {
            Log.w(tag, "bean is NULL!");
            return false;
        } else {
            return task.execute();
        }
    }

    protected static String getSql(String fomatSql, ColumnField[] columes) {
        if (columes == null || columes.length == 0) {
            return fomatSql;
        }
        
        if (fomatSql.contains("[COLUMES]")) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < columes.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(columes[i].name);
            }
            fomatSql = fomatSql.replace("[COLUMES]", builder.toString());
        }
        
        if (fomatSql.contains("[ALL_?]")) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < columes.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append("?");
            }
            
            fomatSql = fomatSql.replace("[ALL_?]", builder.toString());
        }
        
        return fomatSql;
    }

    protected static String generateId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String debugSql(String sql, Object... args) {
        if (args != null) {
            Object[] strArgs = new String[args.length];
            for (int i = 0; i < strArgs.length; i++) {
                Object v = args[i];
                strArgs[i] = v == null? null: v.toString();
            }
            
            sql = sql.replace("?", "?[%s]");
            return String.format(sql, strArgs);
        } else {
            return sql;
        }
    }
    
    public static void executeSqlScript(SQLiteDatabase db, InputStream input) throws IOException {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(input));
            executeSqlScript(db, reader);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(tag, e.getMessage(), e);
            }
        }
    }

    public static void executeSqlScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        StringBuilder sql = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#") || TextUtils.isEmpty(line)) {
                continue;
            }

            line = line.trim();
            int index = line.indexOf(';');
            if (index >= 0) {
                String firstStr = line.substring(0, index + 1);
                sql.append(firstStr).append('\n');
                try {
                    if (Log.DEBUG.get()) {
                        Log.d(tag, sql.toString());
                    }

                    db.execSQL(sql.toString());
                } catch (SQLException e) {
                    Log.e(tag, e.getMessage(), e);
                    if (Log.DEBUG.get()) {
                        Log.d(tag, sql.toString());
                    }
                }

                sql = new StringBuilder();
                if (index < line.length()) {
                    String lastStr = line.substring(index + 1);
                    if (!TextUtils.isEmpty(lastStr)) {
                        sql.append(lastStr);
                    }
                }
            } else {
                sql.append(line).append('\n');
            }
        }

        if (sql.length() > 0) {
            try {
                if (Log.DEBUG.get()) {
                    Log.d(tag, sql.toString());
                }
                db.execSQL(sql.toString());
            } catch (SQLException e) {
                Log.e(tag, e.getMessage(), e);
                if (Log.DEBUG.get()) {
                    Log.d(tag, sql.toString());
                }
            }
        }
    }
}
