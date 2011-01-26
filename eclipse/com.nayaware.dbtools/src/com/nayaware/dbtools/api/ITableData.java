
package com.nayaware.dbtools.api;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.nayaware.dbtools.execute.ExecutionStatus;

/**
 * Table Data representation model
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface ITableData {

	/**
	 * Get the total number of rows in the table. The fetch size may be
	 * diffreent
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getTotalRowCount() throws SQLException;

	/**
	 * Get all the fetched data.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ITableRowData[] getTableData() throws SQLException;

	/**
	 * Move the pointer to the next row
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean nextRow() throws SQLException;

	/**
	 * Get the Table Record data
	 * 
	 * @return data if available or return null
	 * @throws SQLException
	 */
	public ITableRowData getRowData() throws SQLException,
			ClassNotFoundException;

	public ResultSet getResultSet();

	public void setResultSet(ResultSet resultSet);

	public String[] getColumnNames() throws SQLException;

	public String getName();

	public void setName(String name);

	public ITableRowData createNewTableRowData() throws SQLException,
			ClassNotFoundException;

	public ExecutionStatus appendTableRow(ITableRowData tableRowData);

	public ExecutionStatus deleteTableRow(ITableRowData oldRowData);

	public ExecutionStatus updateTableRow(ITableRowData currentRowData,
			ITableRowData updateRowData);

	public ITable getTable();

	public void setTable(ITable table);

	public void close() throws SQLException;

	public boolean supportsPagination();

	public int getPageSize();

	public void setPageSize(int pageSize);

	public int getTotalPages() throws SQLException;

	public int getCurrentPage();

	public void firstPage();

	public void lastPage() throws SQLException;

	public boolean nextPage() throws SQLException;

	public boolean previousPage() throws SQLException;

	public ITableRowData[] getPageData() throws SQLException;
}