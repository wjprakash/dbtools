
package com.nayaware.dbtools.api;

/**
 * Represents a Table Record column data
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface ITableColumnData {

	public IColumn getColumn();

	public void setColumn(IColumn column);

	public String getName();

	public void setName(String name);

	public String getJavaType();

	public void setJavaType(String type);

	public int getSqlType();

	public void setSqlType(int type);

	public Object getValue();

	public void setValue(Object value);

	public void setValueAsString(String stringValue);

	public boolean isNullable();

	public void setNullable(boolean isNullable);

	public String toString();

	public String getValueAsString();
	
	public boolean isReadOnly();
	
	public void setReadOnly(boolean readOnly);
	
	public boolean isAutoIncrement();
	
	public void setAutoIncrement(boolean autoInc);

}