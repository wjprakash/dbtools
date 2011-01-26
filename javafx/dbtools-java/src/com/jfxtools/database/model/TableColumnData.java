package com.jfxtools.database.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.api.ITableColumnData;

/**
 * Model to hold the Table Column data
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableColumnData implements ITableColumnData {

	private String javaType;
	private int sqlType;

	private Object value;
	private String name;
	private boolean isNullable;
	private IColumn column;
	
	private boolean autoIncrement;
	
	private boolean readOnly;

	public TableColumnData(IColumn column, String name, String javaType,
			int sqlType, Object value) {
		this.name = name;
		this.javaType = javaType;
		this.sqlType = sqlType;
		this.value = value;
		this.column = column;
		// Derby returns ROWDATA as first column. Mark it as read only as we
		// don't have a true column for it.
		if (column == null){
			readOnly = true;
		}
	}

	public IColumn getColumn() {
		return column;
	}

	public void setColumn(IColumn column) {
		this.column = column;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String type) {
		this.javaType = type;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setValueAsString(String value) {
		this.value = value;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	@Override
	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return ""; 
		}
	}

	public String getValueAsString() {
		if (value != null) {
			return value.toString();
		} else {
			return null;
		}
	}

	public void setValueAsString1(String valueString) {
		if (javaType.equals("java.sql.Date")) { // NOI18N 
			value = Date.valueOf(valueString);
		} else if (javaType.equals("java.sql.Time")) { // NOI18N 
			value = Time.valueOf(valueString);
		} else if (javaType.equals("java.sql.Timestamp")) { // NOI18N 
			value = Timestamp.valueOf(valueString);
		} else if (javaType.equals("java.math.BigDecimal")) { // NOI18N 
			value = new BigDecimal(valueString.toCharArray());
		} else if (javaType.equals("java.lang.Boolean")) { // NOI18N 
			value = Boolean.valueOf(valueString);
		} else if (javaType.equals("java.lang.Byte")) { // NOI18N 
			value = new Byte(valueString);
		} else if (javaType.equals("java.lang.Character")) { // NOI18N 
			value = new Character(valueString.toCharArray()[0]); // NOI18N
		} else if (javaType.equals("java.lang.Double")) { // NOI18N 
			value = Double.valueOf(valueString);
		} else if (javaType.equals("java.lang.Float")) { // NOI18N 
			value = Float.valueOf(valueString);
		} else if (javaType.equals("java.lang.Integer")) { // NOI18N 
			value = Integer.valueOf(valueString);
		} else if (javaType.equals("java.lang.Long")) { // NOI18N 
			value = Long.valueOf(valueString);
		} else if (javaType.equals("java.lang.Short")) { // NOI18N 
			value = Short.valueOf(valueString);
		} else if (javaType.equals("java.lang.String")) { // NOI18N 
			value = valueString; // NOI18N
		} else if (javaType.equals("java.sql.Blob")) { // NOI18N 
			try {
				value = new javax.sql.rowset.serial.SerialBlob(valueString
						.getBytes());
			} catch (SQLException e) {
				value = new Object();
			}
		} else if (javaType.equals("javax.sql.SerialClob")) { // NOI18N 
			try {
				value = new javax.sql.rowset.serial.SerialBlob(valueString
						.getBytes());
			} catch (SQLException e) {
				value = new Object();
			}
		} else if (javaType.equals("java.net.URL")) { // NOI18N 
			try {
				value = new java.net.URL(valueString); // NOI18N
			} catch (java.net.MalformedURLException e) {
				value = new Object();
			}
		} else if (javaType.equals("char[]")) { // NOI18N 
			value = valueString.toCharArray();
		} else if (javaType.equals("byte[]")) { // NOI18N 
			value = valueString.getBytes();
		}
		value = valueString;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
