
package com.nayaware.dbtools.querybuilder;

import java.util.Arrays;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.querybuilder.model.QbColumnNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Viewer to view the details of a selected column in the tables displayed in
 * the query builder designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderColumnTableViewer extends Composite {

	private String[] columnProperties = { "Column", "Sort", "Sort Order", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"Group By", "Where Criteria", "Function", "Criteria", "Or" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private String[] sortTypes = { "NONE", "ASC", "DESC" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private String[] aggregateFunctions = { "NONE", "AVG", "COUNT", "MAX", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"MIN", "STDDEV", "SUM", "VARIANCE" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private TableViewer tableViewer;

	private QueryData queryData;

	public QueryBuilderColumnTableViewer(Composite parent, QueryData queryData) {
		super(parent, SWT.BORDER);
		this.queryData = queryData;
		setLayout(new GridLayout());

		final Label seectedColumnsLabel = new Label(this, SWT.NONE);
		seectedColumnsLabel.setText(Messages
				.getString("QueryBuilderColumnTableViewer.18")); //$NON-NLS-1$
		tableViewer = new TableViewer(this);
		initialize();
	}

	private void initialize() {
		initializeTable();
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columnProperties);

		createCellEditors(tableViewer.getTable());
		tableViewer.setCellModifier(new CellModifier());
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
	}

	private void initializeTable() {
		Table table = tableViewer.getTable();
		createTableColumns(table);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.getHorizontalBar().setVisible(false);
	}

	private void createTableColumns(Table table) {
		final TableColumn columnNameColumn = new TableColumn(table, SWT.NONE);
		columnNameColumn.setResizable(false);
		columnNameColumn.setWidth(175);
		columnNameColumn.setText(columnProperties[0]);

		final TableColumn sortFlagColumn = new TableColumn(table, SWT.NONE);
		sortFlagColumn.setResizable(false);
		sortFlagColumn.setWidth(125);
		sortFlagColumn.setText(columnProperties[1]);

		final TableColumn sortOrderColumn = new TableColumn(table, SWT.NONE);
		sortOrderColumn.setResizable(false);
		sortOrderColumn.setWidth(80);
		sortOrderColumn.setText(columnProperties[2]);

		final TableColumn groupByColumn = new TableColumn(table, SWT.NONE);
		groupByColumn.setResizable(false);
		groupByColumn.setWidth(65);
		groupByColumn.setText(columnProperties[3]);

		final TableColumn whereColumn = new TableColumn(table, SWT.NONE);
		whereColumn.setResizable(false);
		whereColumn.setWidth(200);
		whereColumn.setText(columnProperties[4]);

		// final TableColumn aggregateFunctionColumn = new TableColumn(table,
		// SWT.NONE);
		// aggregateFunctionColumn.setResizable(false);
		// aggregateFunctionColumn.setWidth(125);
		// aggregateFunctionColumn.setText(columnProperties[4]);
		//
		// final TableColumn criteraiColumn = new TableColumn(table, SWT.NONE);
		// criteraiColumn.setResizable(false);
		// criteraiColumn.setWidth(180);
		// criteraiColumn.setText(columnProperties[5]);
		//
		// final TableColumn orColumn = new TableColumn(table, SWT.NONE);
		// orColumn.setResizable(false);
		// orColumn.setWidth(100);
		// orColumn.setText(columnProperties[6]);
	}

	private void createCellEditors(Table table) {
		// Create the cell editors
		CellEditor[] editors = new CellEditor[5];

		// Column 1 : Column Name (read only)
		TextCellEditor textEditor = new TextCellEditor(table);
		textEditor.deactivate();
		editors[0] = textEditor;

		// Column 2 : Sort Flag (ComboBox)
		editors[1] = new ComboBoxCellEditor(table, sortTypes, SWT.READ_ONLY);

		// Column 3 : Sort order
		TextCellEditor textEditor1 = new TextCellEditor(table);
		((Text) textEditor1.getControl()).setTextLimit(60);
		((Text) textEditor1.getControl())
				.addVerifyListener(new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						// Make sure the default values are as per type
					}
				});
		editors[2] = textEditor1;

		// Column 4 : Group By Flag (Checkbox)
		editors[3] = new CheckboxCellEditor(table);

		// Column 4 : Where clause (Text)
		TextCellEditor whereClauseEditor = new TextCellEditor(table);
		editors[4] = whereClauseEditor;

		// // Column 5: Aggregate Function (ComboBox)
		// editors[4] = new ComboBoxCellEditor(table, aggregateFunctions,
		// SWT.READ_ONLY);
		//
		// // Column 6 : Criteria
		// TextCellEditor textEditor2 = new TextCellEditor(table);
		// ((Text) textEditor2.getControl()).setTextLimit(60);
		// ((Text) textEditor2.getControl())
		// .addVerifyListener(new VerifyListener() {
		// public void verifyText(VerifyEvent e) {
		// // Make sure the default values are as per type
		// }
		// });
		// editors[5] = textEditor2;
		//
		// // Column 6 : OR condition (Free text)
		// TextCellEditor textEditor3 = new TextCellEditor(table);
		// ((Text) textEditor3.getControl()).setTextLimit(60);
		// ((Text) textEditor3.getControl())
		// .addVerifyListener(new VerifyListener() {
		// public void verifyText(VerifyEvent e) {
		// // Make sure the default values are as per type
		// }
		// });
		// editors[6] = textEditor3;

		// Assign the cell editors to the viewer
		tableViewer.setCellEditors(editors);
	}

	class ContentProvider implements IStructuredContentProvider {
		QueryData query;

		public Object[] getElements(Object inputElement) {
			if (query != null) {
				return query.getColumnNodes();
			}
			return null;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof QueryData) {
				query = (QueryData) newInput;
			} else {
				query = null;
			}
		}
	}

	class TableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			QbColumnNode qbColumnNode = (QbColumnNode) element;
			String result = ""; //$NON-NLS-1$
			switch (columnIndex) {

			case 0: // Column Name
				result = qbColumnNode.getName();
				break;
			case 1: // Sort Criteria
				result = qbColumnNode.getSortCriteria();
				break;
			case 2: // Sort Order
				result = String.valueOf(qbColumnNode.getSortOrder());
				break;
			case 3: // Group By
				// result = sdColumnNode.isUsedForGroupBy();
				break;
			case 4: // Where Clause
				result = qbColumnNode.getWhereClauseText();
				break;
			// case 4: // Aggregate Function
			// result = qbColumnNode.getAggregateFunctionType();
			// break;
			// case 5: // Criteria
			// result = qbColumnNode.getCriteria();
			// break;
			// case 6: // OR Flag
			// result = qbColumnNode.getOrFlag();
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			QbColumnNode qbColumnNode = (QbColumnNode) element;
			switch (columnIndex) {
			case 0: // Column Name
				break;
			case 1: // Sort Criteria
				break;
			case 2: // Sort Order
				break;
			case 3: // Group By
				result = getImage(qbColumnNode.isUsedForGroupBy());
				break;
			// case 4: // Aggregate Function
			// break;
			// case 5: // Criteria
			// break;
			// case 6: // OR Flag
			default:
			}
			return result;
		}

		private Image getImage(boolean isSelected) {
			String key = isSelected ? ImageUtils.CHECKED : ImageUtils.UNCHECKED;
			return ImageUtils.getIcon(key);
		}
	}

	class CellModifier implements ICellModifier {

		public boolean canModify(Object element, String property) {
			int columnIndex = Arrays.asList(columnProperties).indexOf(property);
			if (columnIndex == 0) {
				return false;
			}
			return true;
		}

		public Object getValue(Object element, String property) {
			// Find the index of the column
			int columnIndex = Arrays.asList(columnProperties).indexOf(property);

			Object result = null;
			QbColumnNode qbColumnNode = (QbColumnNode) element;

			switch (columnIndex) {
			case 0: // Column Name

				break;
			case 1: // Sort Criteria
				result = new Integer(Arrays.asList(sortTypes).indexOf(
						qbColumnNode.getSortCriteria()));
				break;
			case 2: // Sort Order
				result = String.valueOf(qbColumnNode.getSortOrder());
				break;
			case 3: // Group By
				result = new Boolean(qbColumnNode.isUsedForGroupBy());
				break;
			case 4: // Where Clause
				result = qbColumnNode.getWhereClauseText();
				break;
			// case 4: // Aggregate Function
			// result = new Integer(Arrays.asList(aggregateFunctions).indexOf(
			// qbColumnNode.getAggregateFunctionType()));
			// break;
			// case 5: // Criteria
			// result = qbColumnNode.getCriteria();
			// break;
			// case 6: // OR Flag
			// result = qbColumnNode.getOrFlag();
			default:
				result = ""; //$NON-NLS-1$
			}
			return result;
		}

		public void modify(Object element, String property, Object value) {
			// Find the index of the column
			int columnIndex = Arrays.asList(columnProperties).indexOf(property);
			TableItem item = (TableItem) element;
			QbColumnNode qbColumnNode = (QbColumnNode) item.getData();
			switch (columnIndex) {
			case 0: // Column Name

				break;
			case 1: // Sort Criteria
				int index = ((Integer) value).intValue();
				if ((index >= 0) && (index < sortTypes.length)) {
					qbColumnNode.setSortCriteria(sortTypes[index]);
				}
				break;
			case 2: // Sort Order
				try {
					qbColumnNode.setSortOrder(Integer.parseInt(((String) value)
							.trim()));
				} catch (NumberFormatException exc) {
					ErrorManager.showException(exc);
				}
				break;
			case 3: // Group By
				boolean boolValue = ((Boolean) value).booleanValue();
				qbColumnNode.setUsedForGroupBy(boolValue);
				break;
			case 4: // Where Clause
				qbColumnNode.setWhereClauseText(value.toString());
				break;
			// case 4: // Aggregate Function
			// int index1 = ((Integer) value).intValue();
			// if ((index1 > 0) && (index1 < aggregateFunctions.length)) {
			// qbColumnNode
			// .setAggregateFunctionType(aggregateFunctions[index1]);
			// }
			// break;
			// case 5: // Criteria
			// qbColumnNode.setCriteria(((String) value).trim());
			// break;
			// case 6: // OR Flag
			// qbColumnNode.setOrFlag(((String) value).trim());
			default:
			}
			// updateScript();
			queryData.setDirty(true);
			tableViewer.update(qbColumnNode, null);
		}
	}

	public void setInput(QueryData queryData) {
		tableViewer.setInput(queryData);
	}

	public void add(QbColumnNode columnNode) {
		tableViewer.add(columnNode);
	}

	public void remove(QbColumnNode columnNode) {
		tableViewer.remove(columnNode);
	}
}
