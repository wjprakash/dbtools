
package com.nayaware.dbtools.util;

import java.util.ArrayList;
import java.util.List;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.core.ConnectionConfig;

/**
 * Connection Connection Templates for Add Connection Wizard
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionConfigTemplates {
	List<IConnectionConfig> templates = new ArrayList<IConnectionConfig>();

	public ConnectionConfigTemplates() {
		createTemplateFromExtensions();
	}

	public List<IConnectionConfig> getTemplates() {
		return templates;
	}

	private void createTemplateFromExtensions() {
		List<IConnectionType> availableonnectionTypes = ConnectionManager
				.getInstance().getAvailableConnectionTypes();
		for (IConnectionType connectionTypeInfo : availableonnectionTypes) {
			int type = connectionTypeInfo.getType();
			String name = connectionTypeInfo.getServerName();
			String url = connectionTypeInfo.getUrlPattern();
			String hostname = connectionTypeInfo.getDefaultHostName();
			String database = connectionTypeInfo.getDefaultDatabaseName();
			String port = connectionTypeInfo.getDefaultPortNumber();
			String username = connectionTypeInfo.getDefaultUsername();
			String password = "";
			IConnectionConfig connectionConfig = new ConnectionConfig(name, url,
					type, username, password);
			connectionConfig.setDatabase(database);
			connectionConfig.setHostname(hostname);
			connectionConfig.setPort(port);
			templates.add(connectionConfig);
		}
	}

	public String[] getTemplateNames() {
		String[] templateNames = new String[templates.size()];
		for (int i = 0; i < templateNames.length; i++) {
			templateNames[i] = templates.get(i).getConnectionType().getServerName();
		}
		return templateNames;
	}

	public IConnectionConfig getTemplate(int index) {
		return templates.get(index);
	}

	public IConnectionConfig findTemplateConnectionConfig(int dbType) {
		for (int i = 0; i < templates.size(); i++) {
			if (templates.get(i).getDbType() == dbType) {
				return templates.get(i);
			}
		}
		return null;
	}

}
