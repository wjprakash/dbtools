
package com.nayaware.dbtools.model;

import java.sql.SQLException;

import com.nayaware.dbtools.api.IAbstractDatabaseObject;
import com.nayaware.dbtools.api.IDatabaseInfo;

/**
 * Abstract model object for DB Explorer Nodes
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class AbstractDatabaseObject implements IAbstractDatabaseObject {

	private IDatabaseInfo databaseInfo;
	private String name;

	private AbstractDatabaseObject parent;

	public AbstractDatabaseObject(IDatabaseInfo dbMetadata, String name) {
		databaseInfo = dbMetadata;
		this.name = name;
	}

	public IDatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void refresh() throws SQLException {
		// To be implemented by sub classes
	}

	public boolean delete() {
		// To be implemented by sub classes
		return false;
	}

	public void addChild(AbstractDatabaseObject child) {
		// To be implemented by sub classes
	}

	public void removeChild(AbstractDatabaseObject child) {
		// To be implemented by sub classes
	}

	public AbstractDatabaseObject getParent() {
		return parent;
	}

	public void setParent(AbstractDatabaseObject parent) {
		this.parent = parent;
	}
}
