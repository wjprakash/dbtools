
package com.nayaware.dbtools.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;

/**
 * Connection Properties panel
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionPropertiesPanel extends
		AbstractConnectionPropertiesPanel {

	private Text passwordText;
	private Text usernameText;
	private Text databaseText;
	private Text portText;
	private Text hostNameText;

	public ConnectionPropertiesPanel(Composite parent,
			IConnectionConfig connectionConfig) {
		super(parent, connectionConfig);
		initialize(parent);
	}

	public void initialize(Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);

		final Label hostLabel = new Label(this, SWT.NONE);
		hostLabel.setText(Messages.getString("ConnectionPropertiesPanel.0")); //$NON-NLS-1$

		hostNameText = new Text(this, SWT.BORDER);
		final GridData gd_hostNameText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		hostNameText.setLayoutData(gd_hostNameText);

		final Label portLabel = new Label(this, SWT.NONE);
		portLabel.setText(Messages.getString("ConnectionPropertiesPanel.1")); //$NON-NLS-1$

		portText = new Text(this, SWT.BORDER);
		final GridData gd_portText = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		portText.setLayoutData(gd_portText);

		final Label databaseLabel = new Label(this, SWT.NONE);
		databaseLabel
				.setText(Messages.getString("ConnectionPropertiesPanel.2")); //$NON-NLS-1$

		databaseText = new Text(this, SWT.BORDER);
		final GridData gd_databaseText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		databaseText.setLayoutData(gd_databaseText);

		final Label usernameLabel = new Label(this, SWT.NONE);
		usernameLabel
				.setText(Messages.getString("ConnectionPropertiesPanel.3")); //$NON-NLS-1$

		usernameText = new Text(this, SWT.BORDER);
		final GridData gd_usernameText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		usernameText.setLayoutData(gd_usernameText);

		final Label passwordLabel = new Label(this, SWT.NONE);
		passwordLabel
				.setText(Messages.getString("ConnectionPropertiesPanel.4")); //$NON-NLS-1$

		passwordText = new Text(this, SWT.BORDER);
		passwordText.setEchoChar('*');
		final GridData gd_passwordText = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		passwordText.setLayoutData(gd_passwordText);

		fillData();
	}

	public void fillData() {
		hostNameText.setText(currentConnectionConfig.getHostname());
		portText.setText(currentConnectionConfig.getPort());
		databaseText.setText(currentConnectionConfig.getDatabase());
		usernameText.setText(currentConnectionConfig.getUsername());
		passwordText.setText(currentConnectionConfig.getPassword());
	}

	private String getConnectionStringText() {
		String urlPattern = currentConnectionConfig.getConnectionType().getUrlPattern();
		urlPattern = urlPattern.replaceFirst(IConnectionType.TEMPLATE_HOST,
				hostNameText.getText());
		urlPattern = urlPattern.replaceFirst(IConnectionType.TEMPLATE_PORT,
				portText.getText());
		urlPattern = urlPattern.replaceFirst(IConnectionType.TEMPLATE_DB,
				databaseText.getText());
		return urlPattern;
	}

	@Override
	public IConnectionConfig getConnectionConfig() {
		currentConnectionConfig.setHostname(hostNameText.getText().trim());
		currentConnectionConfig.setPort(portText.getText().trim());
		currentConnectionConfig.setDatabase(databaseText.getText().trim());
		currentConnectionConfig.setUsername(usernameText.getText().trim());
		currentConnectionConfig.setPassword(passwordText.getText().trim());
		currentConnectionConfig.setUrl(getConnectionStringText());
		return currentConnectionConfig;
	}

	@Override
	public void setConnectionConfig(IConnectionConfig template) {
		currentConnectionConfig = template;
		fillData();
	}

}
