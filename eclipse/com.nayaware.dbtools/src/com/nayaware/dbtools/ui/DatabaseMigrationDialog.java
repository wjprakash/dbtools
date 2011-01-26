
package com.nayaware.dbtools.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionGroupNode;
import com.nayaware.dbtools.nodes.ConnectionNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class DatabaseMigrationDialog extends TitleAreaDialog {

	private Tree tree;
	private ConnectionGroupNode databaseGroupNode = new ConnectionGroupNode();

	private Combo targetDatabaseesCombo;
	private Combo targetConnectionCombo;
	private Combo sourceDatabaseesCombo;
	private Combo sourceConnectionCombo;
	List<AbstractNode> databaseList = databaseGroupNode.getDatabaseList();

	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	public DatabaseMigrationDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		final FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 5;
		fillLayout.marginWidth = 5;
		fillLayout.marginHeight = 5;
		container.setLayout(fillLayout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group sourceGroup = new Group(container, SWT.NONE);
		sourceGroup.setLayout(new GridLayout());

		final Label label = new Label(sourceGroup, SWT.NONE);
		final GridData gd_label = new GridData();
		label.setLayoutData(gd_label);
		label.setText(Messages.getString("DatabaseMigrationDialog.3")); //$NON-NLS-1$

		sourceConnectionCombo = new Combo(sourceGroup, SWT.READ_ONLY);
		sourceConnectionCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				AbstractNode databaseNode = databaseList
						.get(sourceConnectionCombo.getSelectionIndex());
				// Listen for the lazy initialization of children
				databaseNode
						.addPropertyChangeListener(new DatabasePropertyChangeListener(
								sourceDatabaseesCombo, databaseNode));
				// Get the children.Might fire an event after lazy
				// initialization
				setComboItems(sourceDatabaseesCombo, databaseNode);
			}
		});
		final GridData gd_sourceConnectionCombo = new GridData(SWT.FILL,
				SWT.CENTER, true, false);
		sourceConnectionCombo.setLayoutData(gd_sourceConnectionCombo);

		final Label label_1 = new Label(sourceGroup, SWT.NONE);
		final GridData gd_label_1 = new GridData();
		label_1.setLayoutData(gd_label_1);
		label_1.setText(Messages.getString("DatabaseMigrationDialog.4")); //$NON-NLS-1$

		sourceDatabaseesCombo = new Combo(sourceGroup, SWT.READ_ONLY);
		final GridData gd_sourceDatabasees = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		sourceDatabaseesCombo.setLayoutData(gd_sourceDatabasees);

		final Label label_3 = new Label(sourceGroup, SWT.NONE);
		label_3.setText(Messages.getString("DatabaseMigrationDialog.5")); //$NON-NLS-1$

		final CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(
				sourceGroup, SWT.BORDER);
		tree = checkboxTreeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Group targetGroup = new Group(container, SWT.NONE);
		targetGroup.setLayout(new GridLayout());

		final Label label_2 = new Label(targetGroup, SWT.NONE);
		final GridData gd_label_2 = new GridData();
		label_2.setLayoutData(gd_label_2);
		label_2.setText(Messages.getString("DatabaseMigrationDialog.6")); //$NON-NLS-1$

		targetConnectionCombo = new Combo(targetGroup, SWT.READ_ONLY);
		targetConnectionCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				AbstractNode databaseNode = databaseList
						.get(sourceConnectionCombo.getSelectionIndex());
				// Listen for the lazy initialization of children
				databaseNode
						.addPropertyChangeListener(new DatabasePropertyChangeListener(
								targetDatabaseesCombo, databaseNode));
				// Get the children.Might fire an event after lazy
				// initialization
				setComboItems(targetDatabaseesCombo, databaseNode);
			}
		});
		final GridData gd_targetConnectionCombo = new GridData(SWT.FILL,
				SWT.CENTER, true, false);
		targetConnectionCombo.setLayoutData(gd_targetConnectionCombo);

		final Label label_1_1 = new Label(targetGroup, SWT.NONE);
		final GridData gd_label_1_1 = new GridData();
		label_1_1.setLayoutData(gd_label_1_1);
		label_1_1.setText(Messages.getString("DatabaseMigrationDialog.7")); //$NON-NLS-1$

		targetDatabaseesCombo = new Combo(targetGroup, SWT.NONE);
		final GridData gd_targetDatabasees = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		targetDatabaseesCombo.setLayoutData(gd_targetDatabasees);

		final Label label_4 = new Label(targetGroup, SWT.NONE);
		label_4.setText(Messages.getString("DatabaseMigrationDialog.8")); //$NON-NLS-1$

		final Button createDbButton = new Button(targetGroup, SWT.CHECK);
		createDbButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		createDbButton.setText(Messages.getString("DatabaseMigrationDialog.9")); //$NON-NLS-1$

		final Button createTableButton = new Button(targetGroup, SWT.CHECK);
		createTableButton.setText(Messages.getString("DatabaseMigrationDialog.10")); //$NON-NLS-1$

		final Button droptableButton = new Button(targetGroup, SWT.CHECK);
		droptableButton.setText(Messages.getString("DatabaseMigrationDialog.11")); //$NON-NLS-1$
		setTitle(Messages.getString("DatabaseMigrationDialog.0")); //$NON-NLS-1$
		setMessage(Messages.getString("DatabaseMigrationDialog.1")); //$NON-NLS-1$

		setConnectionComboItems();
		sourceConnectionCombo.select(0);
		targetConnectionCombo.select(0);

		return area;
	}

	private void setConnectionComboItems() {
		String[] items = new String[databaseList.size()];
		for (int i = 0; i < databaseList.size(); i++) {
			items[i] = databaseList.get(i).getDisplayName();
		}
		sourceConnectionCombo.setItems(items);
		targetConnectionCombo.setItems(items);
	}

	private void setComboItems(Combo combo, AbstractNode databaseNode) {
		combo.removeAll();
		List<AbstractNode> schemaList = ((ConnectionNode) databaseNode)
				.getSchemaNodeList();
		String[] items = new String[schemaList.size()];
		for (int i = 0; i < schemaList.size(); i++) {
			items[i] = schemaList.get(i).getDisplayName();
		}
		combo.setItems(items);
		combo.select(0);
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
		return new Point(600, 500);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("DatabaseMigrationDialog.2")); //$NON-NLS-1$
	}

	public class DatabasePropertyChangeListener implements
			PropertyChangeListener {
		Combo combo;
		AbstractNode databaseNode;

		DatabasePropertyChangeListener(Combo combo, AbstractNode databaseNode) {
			this.combo = combo;
			this.databaseNode = databaseNode;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					setComboItems(combo, databaseNode);
				}
			});
		}
		// private void setComboItems(Combo combo, AbstractNode databaseNode) {
		// combo.removeAll();
		// List<AbstractNode> schemaList = ((ConnectionNode) databaseNode)
		// .getSchemaList();
		// String[] items = new String[schemaList.size()];
		// for (int i = 0; i < schemaList.size(); i++) {
		// items[i] = schemaList.get(i).getDisplayName();
		// }
		// combo.setItems(items);
		// combo.select(0);
		// }
	};

}
