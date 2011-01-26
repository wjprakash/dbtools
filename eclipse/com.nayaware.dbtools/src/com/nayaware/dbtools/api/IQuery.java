
package com.nayaware.dbtools.api;

/**
 * Model that represents Script that can be executed to a given connection
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface IQuery extends IAbstractDatabaseObject {

	public String getQuery();

	public void setQuery(String query);

	public String getPath();

	public void setPath(String path);
}
