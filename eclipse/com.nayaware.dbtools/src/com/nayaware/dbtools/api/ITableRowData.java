
package com.nayaware.dbtools.api;

import java.util.List;

/**
 * Model representing a record in the table
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface ITableRowData {

	public int getColumnCount();

	public void setColumnCount(int columnCount);

	public List<ITableColumnData> getTableColumnData();

	public ITableColumnData getTableColumnData(int index);

	public void setTableColumnData(List<ITableColumnData> tableColumnData);

	public void addTableColumnData(ITableColumnData data);

	public ITable getTable();

	public void setTable(ITable table);

	public String[] getRowAsStringArray();

}