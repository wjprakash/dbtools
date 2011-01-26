package com.jfxtools.database.model;

import java.sql.SQLException;

import com.jfxtools.database.api.IDatabaseObject;
import com.jfxtools.database.api.IDatabaseInfo;


/**
 * Abstract model object for DB Explorer Nodes
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DatabaseObject implements IDatabaseObject {

	private IDatabaseInfo databaseInfo;
	private String name;

	private IDatabaseObject parent;

	public DatabaseObject(IDatabaseInfo dbMetadata, String name) {
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

	public void addChild(IDatabaseObject child) {
		// To be implemented by sub classes
	}

	public void removeChild(IDatabaseObject child) {
		// To be implemented by sub classes
	}

	public IDatabaseObject getParent() {
		return parent;
	}

	public void setParent(IDatabaseObject parent) {
		this.parent = parent;
	}
}
