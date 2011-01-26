
package com.nayaware.dbtools.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.ITableColumnData;
import com.nayaware.dbtools.api.ITableRowData;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Table Data Form dialog that helps user to input to modify table data
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableDataFormDialog extends Dialog {

	private ITableRowData tableRowData;
	private ScrolledComposite scrollPane;

	private List<Text> textFields = new ArrayList<Text>();

	private final String READ_ONLY_TEXT = "<Read Only>";
	private final String AUTO_INCREMENT_TEXT = "<Auto Increment>";

	private boolean update;

	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	public TableDataFormDialog(Shell parentShell, ITableRowData rowData,
			boolean update) {
		super(parentShell);
		tableRowData = rowData;
		this.update = update;
	}

	public ITableRowData getTableRowData() {
		return tableRowData;
	}

	public void setTableRowData(ITableRowData tableRowData) {
		this.tableRowData = tableRowData;
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout());

		final Group group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		scrollPane = new ScrolledComposite(group, SWT.V_SCROLL | SWT.H_SCROLL);
		scrollPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrollPane.setExpandVertical(true);
		scrollPane.setExpandHorizontal(true);
		scrollPane.setMinSize(450, tableRowData.getColumnCount() * 30);

		final Composite composite = new Composite(scrollPane, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		scrollPane.setContent(composite);

		createForm(composite);

		return container;
	}

	private void createForm(Composite container) {
		for (int i = 0; i < tableRowData.getColumnCount(); i++) {
			ITableColumnData columnData = tableRowData.getTableColumnData(i);
			CLabel columnNameLabel = new CLabel(container, SWT.NONE);
			columnNameLabel.setText(columnData.getName());
			IColumn column = columnData.getColumn();
			if (column != null){
				String imageKey;
				if (column.isPrimaryKey()) {
					imageKey = ImageUtils.PRIMARY_KEY_COLUM;
				} else if (column.isForeignKey()) {
					imageKey = ImageUtils.FOREIGN_KEY_COLUM;
				} else if (column.isIndexKey()) {
					imageKey = ImageUtils.INDEX_COLUM;
				} else if (!column.isNullAllowed()) {
					imageKey = ImageUtils.NOT_NULL_COLUM;
				} else {
					imageKey = ImageUtils.COLUMN;
				}
				columnNameLabel.setImage(ImageUtils.getIcon(imageKey));
			}

			Text columnValueField = new Text(container, SWT.BORDER);
			columnValueField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false));
			if (columnData.isReadOnly()) {
				columnValueField.setText(READ_ONLY_TEXT);
				columnValueField.setEnabled(false);
			} else if (columnData.isAutoIncrement()) {
				columnValueField.setText(AUTO_INCREMENT_TEXT);
				columnValueField.setEnabled(false);
			} else if (columnData.getValue() != null) {
				columnValueField.setText(columnData.getValueAsString());
			}
			textFields.add(columnValueField);

			String typeValue = columnData.getJavaType();
			if (typeValue.lastIndexOf(".") > -1) { //$NON-NLS-1$
				int index = typeValue.lastIndexOf("."); //$NON-NLS-1$
				typeValue = typeValue.substring(index + 1);
			}
			
			Label columnInfoField = new Label(container, SWT.NONE);
			columnInfoField.setText("(" + typeValue + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		Point size = container.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		scrollPane.setMinSize(size.x, size.y);
	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 375);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("TableDataFormDialog.4")); //$NON-NLS-1$
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			for (int i = 0; i < tableRowData.getColumnCount(); i++) {
				ITableColumnData columnData = tableRowData
						.getTableColumnData(i);
				String value = textFields.get(i).getText();
				if (AUTO_INCREMENT_TEXT.equals(value)) {
					if (!update) {
						value = "<AI>";
					}else{
						value = columnData.getValueAsString();
					}
				} else if (READ_ONLY_TEXT.equals(value)) {
					if (!update) {
						value = "<RO>";
					}else{
						value = columnData.getValueAsString();
					}
				} 
				columnData.setValueAsString(value);
			}
		}
		super.buttonPressed(buttonId);
	}

}
