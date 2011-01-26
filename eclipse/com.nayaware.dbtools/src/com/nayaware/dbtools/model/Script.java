
package com.nayaware.dbtools.model;

import java.io.File;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.IScript;

/**
 * Model for the Script Node in the Database Explorer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class Script extends AbstractDatabaseObject implements IScript {

	private String script;
	private String path;

	public Script(IDatabaseInfo dbMetadata, String name) {
		super(dbMetadata, name);
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
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
