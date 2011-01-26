
package com.nayaware.dbtools.api;

/**
 * Model that represents Script that can be executed to a given connection
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface IScript extends IAbstractDatabaseObject {

	public String getScript();

	public void setScript(String script);

	public String getPath();

	public void setPath(String path);
}
