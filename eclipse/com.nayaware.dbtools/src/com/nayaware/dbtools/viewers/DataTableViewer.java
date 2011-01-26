
package com.nayaware.dbtools.viewers;

import java.sql.SQLException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.nayaware.dbtools.util.ErrorManager;

/**
 * Table Viewer to view data obtained via database query (select statement)
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DataTableViewer extends TableViewer {

	private Table dataTable;
	String[] columnNames;

	public DataTableViewer(Composite parent, String[] columnNames) {
		super(parent);
		this.columnNames = columnNames;
		initialize(columnNames);
	}

	private void initialize(String[] columnNames) {
		dataTable = this.getTable();
		configureDataTable(columnNames);
		setContentProvider(new DataTableContentProvider());
		setLabelProvider(new DataTableLabelProvider());
	}

	private void configureDataTable(String[] columnNames) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		dataTable.setLayoutData(gridData);
		dataTable.setHeaderVisible(true);
		dataTable.setLinesVisible(true);

		try {
			setTableColums(dataTable, columnNames);
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
		}
	}

	private void setTableColums(Table dataTable, String[] columnNames)
			throws SQLException {
		for (String colName : columnNames) {
			TableColumn tableCol = new TableColumn(dataTable, SWT.LEFT);
			tableCol.setText(colName);
			tableCol.setWidth(100);
			//tableCol.pack();
		}
	}
}
