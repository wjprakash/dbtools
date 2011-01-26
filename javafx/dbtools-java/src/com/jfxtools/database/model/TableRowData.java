package com.jfxtools.database.model;

import java.util.ArrayList;
import java.util.List;

import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.ITableColumnData;
import com.jfxtools.database.api.ITableRowData;

/**
 * Model represents a record in the table
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableRowData implements ITableRowData {
	private int columnCount;
	private ITable table;
	private List<ITableColumnData> tableColumnData = new ArrayList<ITableColumnData>();

	public TableRowData(ITable table) {
		this.table = table;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public List<ITableColumnData> getTableColumnData() {
		return tableColumnData;
	}

	public ITableColumnData getTableColumnData(int index) {
		return tableColumnData.get(index);
	}

	public void setTableColumnData(List<ITableColumnData> tableColumnData) {
		this.tableColumnData = tableColumnData;
	}

	public void addTableColumnData(ITableColumnData data) {
		tableColumnData.add(data);
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

	public String[] getRowAsStringArray() {
		List<String> columnValues = new ArrayList<String>(tableColumnData
				.size());
		for (ITableColumnData columnData : tableColumnData) {
			columnValues.add(columnData.getValueAsString());
		}
		return columnValues.toArray(new String[columnValues.size()]);
	}

}
