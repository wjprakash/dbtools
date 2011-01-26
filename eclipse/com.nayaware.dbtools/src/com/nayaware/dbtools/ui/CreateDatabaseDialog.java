
package com.nayaware.dbtools.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog to create a Database (Schema) for Network based servers
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class CreateDatabaseDialog extends Dialog {

	private Text databaseNameText;
	private String databaseName;
	private boolean showAdvanced = false;

	public CreateDatabaseDialog(Shell parentShell, String initialDatabaseName) {
		super(parentShell);
		databaseName = initialDatabaseName;
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridData gd_container = new GridData(SWT.FILL, SWT.CENTER, true,
				true);
		container.setLayoutData(gd_container);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginRight = 10;
		gridLayout.marginLeft = 10;
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);
		getButton(IDialogConstants.OK_ID);

		final Label newDatbaseNameLabel = new Label(container, SWT.NONE);
		newDatbaseNameLabel.setText(Messages
				.getString("CreateDatabaseDialog.0")); //$NON-NLS-1$

		databaseNameText = new Text(container, SWT.BORDER);
		databaseNameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				databaseName = databaseNameText.getText();
				Button okButton = getButton(IDialogConstants.OK_ID);
				if (databaseName.equals("")) { //$NON-NLS-1$
					okButton.setEnabled(false);
				} else {
					okButton.setEnabled(true);
				}
			}
		});
		final GridData gd_databaseNameText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		databaseNameText.setLayoutData(gd_databaseNameText);
		databaseNameText.setText(databaseName); 
		new Label(container, SWT.NONE);

		final Button advancedButton = new Button(container, SWT.NONE);
		advancedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				showAdvanced = true;
				close();
			}
		});
		final GridData gd_advancedButton = new GridData(SWT.RIGHT, SWT.CENTER,
				false, false);
		advancedButton.setLayoutData(gd_advancedButton);
		advancedButton.setText(Messages.getString("CreateDatabaseDialog.2")); //$NON-NLS-1$

		return container;
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
		return new Point(500, 200);
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public boolean getShowAdvanced() {
		return showAdvanced;
	}

}
