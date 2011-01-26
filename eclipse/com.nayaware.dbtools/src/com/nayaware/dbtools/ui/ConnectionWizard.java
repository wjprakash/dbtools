
package com.nayaware.dbtools.ui;

import org.eclipse.jface.wizard.Wizard;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;

/**
 * Add or edit Connection Wizard dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionWizard extends Wizard {

	private ConnectionPropertyWizadPage connectionPropertyWizadPage;
	private boolean editing = false;
	private IConnectionConfig connectionConfig;

	public ConnectionWizard() {
		connectionPropertyWizadPage = new ConnectionPropertyWizadPage();
	}

	public ConnectionWizard(IConnectionConfig connectionConfig) {
		connectionPropertyWizadPage = new ConnectionPropertyWizadPage(
				connectionConfig);
		editing = true;
		this.setConnectionConfig(connectionConfig);
	}

	@Override
	public boolean performFinish() {

		if (!editing) {
			ConnectionManager.getInstance().addConnectionConfig(
					connectionPropertyWizadPage.getConnectionConfig());
		} else {
			IConnectionConfig editedConfig = connectionPropertyWizadPage
					.getConnectionConfig();
			connectionConfig.setDatabase(editedConfig.getDatabase());
			connectionConfig.setHostname(editedConfig.getHostname());
			connectionConfig.setPassword(editedConfig.getPassword());
			connectionConfig.setPort(editedConfig.getPort());
			connectionConfig.setDatabaseFilePath(editedConfig
					.getDatabaseFilePath());
			connectionConfig.setUrl(editedConfig.getUrl());
			connectionConfig.setDisplayName(editedConfig.getDisplayName());
		}
		return true;
	}

	@Override
	public boolean canFinish() {
		if (connectionPropertyWizadPage.isPageComplete()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addPages() {
		addPage(connectionPropertyWizadPage);
		super.addPages();
	}

	public void setConnectionConfig(IConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	public IConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}
}
