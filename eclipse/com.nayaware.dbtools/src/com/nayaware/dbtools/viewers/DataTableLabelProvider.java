
package com.nayaware.dbtools.viewers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.nayaware.dbtools.api.ITableRowData;

/**
 * Provides Labeling information for the data table viewer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DataTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ITableRowData) {
			ITableRowData tableRowData = (ITableRowData) element;
			return tableRowData.getTableColumnData(columnIndex).toString();
		}
		return null;
	}

}
