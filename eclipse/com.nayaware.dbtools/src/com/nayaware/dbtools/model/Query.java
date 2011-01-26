
package com.nayaware.dbtools.model;

import java.io.File;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.IQuery;

/**
 * Model for the QueryData Node in the Database Explorer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class Query extends AbstractDatabaseObject implements IQuery {

	private String query;
	private String path;

	public Query(IDatabaseInfo dbMetadata, String name) {
		super(dbMetadata, name);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String script) {
		this.query = script;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean delete() {
		File file = new File(getPath());
		if (file.exists()) {
			file.delete();
			if (!file.exists()) {
				getParent().removeChild(this);
				return true;
			}
		}
		return false;
	}
}
