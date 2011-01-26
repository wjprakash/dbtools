package com.jfxtools.database.model;

import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISqlType;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlType extends DatabaseObject implements ISqlType {

	public static final SqlType UNSUPPORTED_SQL_TYPE = new SqlType(
			"UNSUPPORTED", false, false); // $NON-NLS$ 

	private boolean nullAllowed = true;
	private boolean autoIncrement = false;
	private String literalPrefix;
	private String literalSuffix;
	private int searchable;

	public SqlType(IDatabaseInfo databaseInfo, String name) {
		super(null, name);
	}

	public SqlType(String name, boolean nullAllowed, boolean autoIncrement) {
		super(null, name);
		this.nullAllowed = nullAllowed;
		this.autoIncrement = autoIncrement;
	}

	public boolean isNullAllowed() {
		return nullAllowed;
	}

	public void setNullAllowed(boolean nullAllowed) {
		this.nullAllowed = nullAllowed;
	}

	public boolean isAutoIncrementable() {
		return autoIncrement;
	}

	public void setAutoIncrementable(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getLiteralPrefix() {
		return literalPrefix;
	}

	public void setLiteralPrefix(String literalPrefix) {
		this.literalPrefix = literalPrefix;
	}

	public String getLiteralSuffix() {
		return literalSuffix;
	}

	public void setLiteralSuffix(String literalSuffix) {
		this.literalSuffix = literalSuffix;
	}

	public int getSearchable() {
		return searchable;
	}

	public void setSearchable(int searchable) {
		this.searchable = searchable;
	}

}
