
package com.nayaware.dbtools.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ITableData;

/**
 * Input for the SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableDataEditorInput implements IEditorInput {

	private ITableData tableData;

	private IDatabaseInfo databaseInfo;

	public TableDataEditorInput(IDatabaseInfo dbInfo, ITableData tableData) {
		this.tableData = tableData;
		databaseInfo = dbInfo;
	}

	public ITableData getTableData() {
		return tableData;
	}

	public void setTableData(ITableData tableData) {
		this.tableData = tableData;
	}

	public IDatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return "Table Data (" + tableData.getName() + ")";
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return "Table Data Editor (" + databaseInfo.getName() + " - "
				+ databaseInfo.getConnectionConfig().getUrl() + ")";
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(ITableData.class)) {
			return tableData;
		}
		return null;
	}
}
