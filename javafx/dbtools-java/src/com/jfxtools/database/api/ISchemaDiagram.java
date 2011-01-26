package com.jfxtools.database.api;

/**
 * Model that represents Script that can be executed to a given connection
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface ISchemaDiagram extends IDatabaseObject {
	public String getPath();

	public void setPath(String path);
}
