package com.jfxtools.database.api;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public interface ITable extends IDatabaseObject {

	public ISchema getSchema();

	/**
	 * Get the list of primary key columns
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<IColumn> getPrimaryKeyColumns() throws SQLException;

	public List<IColumn> getForeignKeyColumns() throws SQLException;

	public List<IColumn> getColumnList(boolean refresh) throws SQLException;

	/**
	 * Get the list of Columns associated with this table
	 * 
	 * @return List<Column>
	 * @throws SQLException
	 */
	public List<IColumn> getColumnList() throws SQLException;

	public void refresh() throws SQLException;

	public List<String> getColumnNames();

	public IColumn[] getColumns(boolean refresh) throws SQLException;

	public IColumn removeColumn(IColumn oldColumn);

	public void addColumn(IColumn newColumn);

	public ITableData getData();

	public String getQualifiedName() throws SQLException;

	public IColumn findColumnByName(String colName) throws SQLException;

	public IColumn findColumnByName(String colName, boolean refresh)
			throws SQLException;
}