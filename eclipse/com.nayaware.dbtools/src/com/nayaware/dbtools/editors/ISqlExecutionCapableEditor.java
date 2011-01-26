
package com.nayaware.dbtools.editors;

/**
 * An interface for the Editors to tell that they are capable of executing SQL
 * Editors: SqlEditor SchemaDesigner QueryBuilder
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface ISqlExecutionCapableEditor {
	public void execute();
}
