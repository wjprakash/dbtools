
package com.nayaware.dbtools.api;

import java.sql.SQLException;
import java.util.List;

/**
 * Schema model
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface ISchema extends IAbstractDatabaseObject {

	public List<ITable> getTableList(boolean refresh) throws SQLException;

	public List<ITable> getTableList() throws SQLException;

	public List<IView> getViewList() throws SQLException;

	public void refresh() throws SQLException;

	public List<String> getTableNames() throws SQLException;

	public List<String> getTableNames(boolean refresh) throws SQLException;

	public ITable findTableByName(String tableName) throws SQLException;

}