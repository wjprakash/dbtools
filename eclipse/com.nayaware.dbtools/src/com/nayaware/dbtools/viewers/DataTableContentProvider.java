
package com.nayaware.dbtools.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.nayaware.dbtools.api.ITableRowData;

/**
 * Provides content for the data table viewer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DataTableContentProvider implements IStructuredContentProvider {

	ITableRowData[] tableRowData;

	public Object[] getElements(Object inputElement) {
		return tableRowData;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof ITableRowData[]) {
			tableRowData = (ITableRowData[]) newInput;
		} else {
			tableRowData = null;
		}
	}

}
