package com.roiland.crm.sm.core.database;

public interface DatabaseDefine {
    static final String COLUMN_ID = "id";
    
    public class ColumnField {
    	public final String name;
    	public final String type;
    	public final String other;
    	
    	public ColumnField(String name, String type, String other) {
    		this.name = name;
    		this.type = type;
    		this.other = other;
    	}
    }
}
