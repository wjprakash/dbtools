
package com.nayaware.dbtools.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class FileConnectionProprtiesPanel extends
		AbstractConnectionPropertiesPanel {

	public FileConnectionProprtiesPanel(Composite parent,
			IConnectionConfig connectionConfig) {
		super(parent, connectionConfig);
		initialize(parent);
	}

	private Text databaseFileText;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public void initialize(final Composite parent) {

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		setLayout(gridLayout);

		final Label databaseFileLabel = new Label(this, SWT.NONE);
		databaseFileLabel.setText(Messages
				.getString("FileConnectionProprtiesPanel.0")); //$NON-NLS-1$

		databaseFileText = new Text(this, SWT.BORDER);
		final GridData gd_databaseFileText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		databaseFileText.setLayoutData(gd_databaseFileText);

		final Button browseButton = new Button(this, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String resource = null;
				if (currentConnectionConfig.getDbType() == IConnectionType.DERBY_EMBEDDED) {
					DirectoryDialog dlg = new DirectoryDialog(
							parent.getShell(), SWT.OPEN);
					dlg.setFilterPath(databaseFileText.getText());
					resource = dlg.open();
				} else {
					FileDialog dlg = new FileDialog(parent.getShell(), SWT.OPEN);
					dlg.setFilterPath(databaseFileText.getText());
					resource = dlg.open();
				}
				if (resource != null) {
					databaseFileText.setText(resource);
				}
			}
		});
		browseButton.setText(Messages
				.getString("FileConnectionProprtiesPanel.1")); //$NON-NLS-1$

		fillData();
	}

	public void fillData() {
		databaseFileText.setText(currentConnectionConfig.getDatabaseFilePath());
	}

	private String getConnectionStringText() {
		String urlPattern = currentConnectionConfig.getConnectionType()
				.getUrlPattern();
		urlPattern = urlPattern.replaceFirst(IConnectionType.TEMPLATE_DB_PATH,
				databaseFileText.getText());
		return urlPattern;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public IConnectionConfig getConnectionConfig() {
		currentConnectionConfig.setDatabaseFilePath(databaseFileText.getText());
		currentConnectionConfig.setUrl(getConnectionStringText());
		return currentConnectionConfig;
	}

	@Override
	public void setConnectionConfig(IConnectionConfig connectionConfig) {
		currentConnectionConfig = connectionConfig;
		fillData();
	}

}
