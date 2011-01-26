
package com.nayaware.dbtools.schemadesigner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.ISqlType;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * panel that displays the details of the column
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableColumnDetailPanel extends Composite implements
		PropertyChangeListener {

	private Text nameText;
	private Combo typeCombo;
	private Combo nullableCombo;
	private Combo autoIncCombo;
	private Text defaultText;
	private Button primaryKeycheckBox;

	private SdColumnNode currentColumnNode;

	private String[] autoIncValues = { "true", "false" }; //$NON-NLS-1$ //$NON-NLS-2$
	private String[] nullableValues = { "true", "false" }; //$NON-NLS-1$ //$NON-NLS-2$

	private List<ISqlType> sqlTypes;
	
	private Schemata schemata;

	public TableColumnDetailPanel(Composite parent, Schemata schemata) {
		super(parent, SWT.BORDER);
		this.schemata = schemata;
		createPanel();
	}

	private void createPanel() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		setLayout(gridLayout);

		final Label nameLabel = new Label(this, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		nameLabel.setText(Messages.getString("TableColumnDetailPanel.4")); //$NON-NLS-1$

		nameText = new Text(this, SWT.BORDER);
		nameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				currentColumnNode.setName(nameText.getText().trim());
				schemata.setDirty(true);
			}
		});
		final GridData gd_nameText = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		nameText.setLayoutData(gd_nameText);

		final Label typeLabel = new Label(this, SWT.NONE);
		typeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		typeLabel.setText(Messages.getString("TableColumnDetailPanel.5")); //$NON-NLS-1$

		typeCombo = new Combo(this, SWT.READ_ONLY);
		typeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				int index = typeCombo.getSelectionIndex();
				if (index >= 0) {
					IColumn column = currentColumnNode.getColumnNode().getColumn();
					column.setSqlType(sqlTypes.get(typeCombo
							.getSelectionIndex()));
					schemata.setDirty(true);
				}
			}
		});
		final GridData gd_typeCombo = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		typeCombo.setLayoutData(gd_typeCombo);

		final Label nullableLabel = new Label(this, SWT.NONE);
		nullableLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		nullableLabel.setText(Messages.getString("TableColumnDetailPanel.6")); //$NON-NLS-1$

		nullableCombo = new Combo(this, SWT.READ_ONLY);
		nullableCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				IColumn column = currentColumnNode.getColumnNode().getColumn();
				if (nullableCombo.getSelectionIndex() == 0) {
					column.setNullAllowed(true);
				} else {
					column.setNullAllowed(false);
				}
				schemata.setDirty(true);
			}
		});
		final GridData gd_nullableCombo = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		nullableCombo.setLayoutData(gd_nullableCombo);
		nullableCombo.setItems(nullableValues);

		final Label autoIncLabel = new Label(this, SWT.NONE);
		autoIncLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		autoIncLabel.setText(Messages.getString("TableColumnDetailPanel.7")); //$NON-NLS-1$

		autoIncCombo = new Combo(this, SWT.READ_ONLY);
		autoIncCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				IColumn column = currentColumnNode.getColumnNode().getColumn();
				if (autoIncCombo.getSelectionIndex() == 0) {
					column.setAutoIncrement(true);
				} else {
					column.setAutoIncrement(false);
				}
				schemata.setDirty(true);
			}
		});
		final GridData gd_autoIncCombo = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		autoIncCombo.setLayoutData(gd_autoIncCombo);
		autoIncCombo.setItems(autoIncValues);

		final Label defaultValueLabel = new Label(this, SWT.NONE);
		defaultValueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false));
		defaultValueLabel.setText(Messages.getString("TableColumnDetailPanel.8")); //$NON-NLS-1$

		defaultText = new Text(this, SWT.BORDER);
		final GridData gd_defaultText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		defaultText.setLayoutData(gd_defaultText);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		defaultText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				IColumn column = currentColumnNode.getColumnNode().getColumn();
				column.setDefaultValue(defaultText.getText().trim());
				schemata.setDirty(true);
			}
		});

		primaryKeycheckBox = new Button(this, SWT.CHECK);
		primaryKeycheckBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				IColumn column = currentColumnNode.getColumnNode().getColumn();
				column.setPrimaryKeyFlag(primaryKeycheckBox.getSelection());
				schemata.setDirty(true);
			}
		});

		final GridData gd_primaryKeycheckBox = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 2, 1);
		primaryKeycheckBox.setLayoutData(gd_primaryKeycheckBox);
		primaryKeycheckBox.setText(Messages.getString("TableColumnDetailPanel.9")); //$NON-NLS-1$
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		//
	}

	public synchronized void setColumnNode(SdColumnNode sdColumnNode) {
		// Update the previous column data
		updateColumnData();

		if (currentColumnNode != null) {
			currentColumnNode.removePropertyChangeListener(this);
		}
		currentColumnNode = sdColumnNode;
		currentColumnNode.addPropertyChangeListener(this);
		IColumn column = currentColumnNode.getColumnNode().getColumn();
		nameText.setText(column.getName());
		defaultText.setText(column.getDefaultValue());
		if (column.isPrimaryKey()) {
			primaryKeycheckBox.setSelection(column.isPrimaryKey());
		} else {
			primaryKeycheckBox.setSelection(false);
		}
		if (sqlTypes == null) {
			try {
				sqlTypes = column.getDatabaseInfo().getSqlTypes();
				typeCombo.setItems(getTypeNames());
			} catch (SQLException exc) {
				ErrorManager.showException(exc);
			}
		}
		typeCombo.select(sqlTypes.indexOf(column.getType()));
		if (column.isNullAllowed()) {
			nullableCombo.select(0);
		} else {
			nullableCombo.select(1);
		}
		if (column.isAutoIncrement()) {
			autoIncCombo.select(0);
		} else {
			autoIncCombo.select(1);
		}
	}

	public void updateColumnData() {
		if (currentColumnNode != null) {
			IColumn prevColumn = currentColumnNode.getColumnNode().getColumn();
			String name = nameText.getText().trim();
			if (name.length() > 1) {
				prevColumn.setName(name);
			}
			String defValue = defaultText.getText().trim();
			if (defValue.length() > 1) {
				prevColumn.setDefaultValue(defValue);
			}
			prevColumn.setPrimaryKeyFlag(primaryKeycheckBox.getSelection());
			int index = typeCombo.getSelectionIndex();
			if (index >= 0) {
				prevColumn.setSqlType(sqlTypes.get(typeCombo
						.getSelectionIndex()));
			}
			if (nullableCombo.getSelectionIndex() == 0) {
				prevColumn.setNullAllowed(true);
			} else {
				prevColumn.setNullAllowed(false);
			}
			if (autoIncCombo.getSelectionIndex() == 0) {
				prevColumn.setAutoIncrement(true);
			} else {
				prevColumn.setAutoIncrement(false);
			}
			currentColumnNode.fireColumnModifiedEvent();
		}
	}

	private String[] getTypeNames() {
		List<String> names = new ArrayList<String>();
		if ((sqlTypes != null) && (sqlTypes.size() > 0)) {
			for (ISqlType sqlType : sqlTypes) {
				names.add(sqlType.getName());
			}
		}
		return names.toArray(new String[names.size()]);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (AbstractNode.NODE_NAME_MODIFIED.equals(evt.getPropertyName())) {
			nameText.setText(currentColumnNode.getName());
		}
	}

}
