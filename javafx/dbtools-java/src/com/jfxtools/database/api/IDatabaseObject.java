package com.jfxtools.database.api;

import java.sql.SQLException;

/**
 * Abstract Database Object
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface IDatabaseObject {
	public IDatabaseInfo getDatabaseInfo();

	public void setDatabaseInfo(IDatabaseInfo databaseInfo);

	public void setName(String name);

	public String getName();

	public void refresh() throws SQLException;

	public IDatabaseObject getParent();

	public void setParent(IDatabaseObject parent);

	public void addChild(IDatabaseObject child);

	public void removeChild(IDatabaseObject child);

	public boolean delete();
}