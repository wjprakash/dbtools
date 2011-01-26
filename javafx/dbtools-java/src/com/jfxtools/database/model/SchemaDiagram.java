package com.jfxtools.database.model;

import java.io.File;

import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchemaDiagram;


/**
 * Model for the QueryData Node in the Database Explorer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDiagram extends DatabaseObject implements
		ISchemaDiagram {

	private String path;

	public SchemaDiagram(IDatabaseInfo dbMetadata, String name) {
		super(dbMetadata, name);
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
