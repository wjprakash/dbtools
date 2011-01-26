
package com.nayaware.dbtools.api;

import java.sql.SQLException;

import com.nayaware.dbtools.model.AbstractDatabaseObject;

/**
 * Abstract Database Object
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface IAbstractDatabaseObject {
	public IDatabaseInfo getDatabaseInfo();

	public void setDatabaseInfo(IDatabaseInfo databaseInfo);

	public void setName(String name);

	public String getName();

	public void refresh() throws SQLException;

	public AbstractDatabaseObject getParent();

	public void setParent(AbstractDatabaseObject parent);

	public void addChild(AbstractDatabaseObject child);

	public void removeChild(AbstractDatabaseObject child);

	public boolean delete();
}