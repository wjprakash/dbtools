
package com.nayaware.dbtools.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.util.ServerlessDatabaseUtils;

/**
 * Dialog to create server-less databasees such as Embedded Derby or SQLite
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class CreateServerlessDatabaseDialog extends TitleAreaDialog {

	private Text databaseText;
	private Text folderText;

	private String dbUrl;
	private boolean connect = true;

	private int dbType;

	private String defaultDerbyDatabaseName = Messages.getString("CreateServerlessDatabaseDialog.11"); //$NON-NLS-1$
	private String defaultSqliteDatabaseName = Messages.getString("CreateServerlessDatabaseDialog.12"); //$NON-NLS-1$

	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	public CreateServerlessDatabaseDialog(Shell parentShell, int dbType) {
		super(parentShell);
		this.dbType = dbType;
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group group = new Group(container, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		group.setLayout(gridLayout);

		final Label folderPathLabel = new Label(group, SWT.NONE);
		folderPathLabel.setText(Messages
				.getString("CreateServerlessDatabaseDialog.0")); //$NON-NLS-1$

		folderText = new Text(group, SWT.BORDER);
		final GridData gd_folderText = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		folderText.setLayoutData(gd_folderText);
		folderText.setText(System.getProperty("user.home")); //$NON-NLS-1$

		final Button browseButton = new Button(group, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(parent.getShell());
				dlg.setFilterPath(folderText.getText());
				dlg.setText(Messages
						.getString("CreateServerlessDatabaseDialog.1")); //$NON-NLS-1$
				dlg.setMessage(Messages
						.getString("CreateServerlessDatabaseDialog.2")); //$NON-NLS-1$
				String dir = dlg.open();
				if (dir != null) {
					folderText.setText(dir);
				}
			}
		});
		browseButton.setText(Messages
				.getString("CreateServerlessDatabaseDialog.3")); //$NON-NLS-1$

		final Label databaseNameLabel = new Label(group, SWT.NONE);
		databaseNameLabel.setText(Messages
				.getString("CreateServerlessDatabaseDialog.4")); //$NON-NLS-1$

		databaseText = new Text(group, SWT.BORDER);
		final GridData gd_databaseText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		databaseText.setLayoutData(gd_databaseText);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);

		final Button connectCheckbox = new Button(group, SWT.CHECK);
		connectCheckbox.setSelection(true);
		connectCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent evt) {
				connect = connectCheckbox.getSelection();
			}
		});
		connectCheckbox.setText(Messages
				.getString("CreateServerlessDatabaseDialog.5")); //$NON-NLS-1$
		new Label(group, SWT.NONE);
		if (dbType == IConnectionType.DERBY_EMBEDDED) {
			databaseText.setText(defaultDerbyDatabaseName);
			setTitle(Messages.getString("CreateServerlessDatabaseDialog.6")); //$NON-NLS-1$
			setMessage(Messages.getString("CreateServerlessDatabaseDialog.7")); //$NON-NLS-1$
		} else if (dbType == IConnectionType.SQLITE) {
			databaseText.setText(defaultSqliteDatabaseName);
			setTitle(Messages.getString("CreateServerlessDatabaseDialog.8")); //$NON-NLS-1$
			setMessage(Messages.getString("CreateServerlessDatabaseDialog.9")); //$NON-NLS-1$
		}
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages
				.getString("CreateServerlessDatabaseDialog.10"), true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}
	
	@Override
	protected boolean isResizable() {
	   return true;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			String dbFolder = folderText.getText().trim();
			String dbName = databaseText.getText().trim();
			String connectionName = databaseText.getText().trim();
			if (dbType == IConnectionType.DERBY_EMBEDDED) {
				String username = ""; //$NON-NLS-1$
				String password = ""; //$NON-NLS-1$
				ServerlessDatabaseUtils.createEmbeddedDerbyDatabase(dbFolder,
						dbName, username, password, connectionName, connect);
			} else if (dbType == IConnectionType.SQLITE) {
				ServerlessDatabaseUtils.createSqliteDatabase(dbFolder, dbName,
						connectionName, connect);
			}
			close();
		}
		super.buttonPressed(buttonId);
	}

	public String getUrl() {
		return dbUrl;
	}

}
