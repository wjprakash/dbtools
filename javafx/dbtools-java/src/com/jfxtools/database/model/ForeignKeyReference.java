package com.jfxtools.database.model;

import com.jfxtools.database.api.IColumn.IForeignKeyReference;

/**
 * Model representing a Connection Table Column Foreign Key Reference
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ForeignKeyReference implements IForeignKeyReference {
	private String primaryKeyTableShema;
	private String primaryKeyTable;
	private String primaryKeyColumn;
	private String primaryKeyName;

	public ForeignKeyReference(String primaryKeyTableShema,
			String primaryKeyTable, String primaryKeyColumn,
			String primaryKeyName) {
		this.primaryKeyTableShema = primaryKeyTableShema;
		this.primaryKeyTable = primaryKeyTable;
		this.primaryKeyColumn = primaryKeyColumn;
		this.primaryKeyName = primaryKeyName;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public String getPrimaryKeyTableShema() {
		return primaryKeyTableShema;
	}

	public void setPrimaryKeyTableShema(String primaryKeyTableShema) {
		this.primaryKeyTableShema = primaryKeyTableShema;
	}

	public String getPrimaryKeyTable() {
		return primaryKeyTable;
	}

	public void setPrimaryKeyTable(String primaryKeyTable) {
		this.primaryKeyTable = primaryKeyTable;
	}

	public String getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}

	public void setPrimaryKeyColumn(String primaryKeyColumn) {
		this.primaryKeyColumn = primaryKeyColumn;
	}
}