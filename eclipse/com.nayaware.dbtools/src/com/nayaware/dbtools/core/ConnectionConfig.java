
package com.nayaware.dbtools.core;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;

/**
 * Connection connection configuration implementation
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionConfig implements IConnectionConfig {

	private boolean persistable = true;

	private String name;
	private String displayName;
	private int dbType = IConnectionType.UNKNOWN;
	private String url = ""; //$NON-NLS-1$
	private String driverName = ""; //$NON-NLS-1$
	private String username = ""; //$NON-NLS-1$
	private String password = ""; //$NON-NLS-1$
	private String hostname = ""; //$NON-NLS-1$
	private String port = ""; //$NON-NLS-1$
	private String database = ""; //$NON-NLS-1$
	private String[] properties;
	private String databaseFilePath = ""; //$NON-NLS-1$
	
	private IConnectionType connectionType;

	public ConnectionConfig(String name, String url, int type, String username,
			String password) {
		this.name = name;
		this.displayName = name;
		this.url = url;
		this.dbType = type;
		this.username = username;
		this.password = password;
		connectionType = ConnectionManager.getInstance().findConnectionType(dbType);
		this.driverName = connectionType.getDriverName();
	}

	public ConnectionConfig(String name, String hostname, String port,
			String database, int type, String username, String password) {
		this(name, null, type, username, password);
		
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		
		url = connectionType.getUrlPattern();
		if (databaseFilePath != null) {
			url = url.replaceFirst(IConnectionType.TEMPLATE_DB_PATH,
					databaseFilePath);
		} else {
			url = url.replaceFirst(IConnectionType.TEMPLATE_HOST, hostname);
			url = url.replaceFirst(IConnectionType.TEMPLATE_PORT, port);
			url = url.replaceFirst(IConnectionType.TEMPLATE_DB, database);
		}
	}

	public ConnectionConfig() {
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String[] getProperties() {
		return properties;
	}

	public void setProperties(String[] properties) {
		this.properties = properties;
	}

	public String getUrl() {
		if (url == null) {
			url = connectionType.getUrlPattern();
			if (databaseFilePath != null) {
				url = url.replaceFirst(IConnectionType.TEMPLATE_DB_PATH,
						databaseFilePath);
			} else {
				url = url.replaceFirst(IConnectionType.TEMPLATE_HOST, hostname);
				url = url.replaceFirst(IConnectionType.TEMPLATE_PORT, port);
				url = url.replaceFirst(IConnectionType.TEMPLATE_DB, database);
			}
		}
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
		if ((displayName == null) || displayName.equals(name)) {
			displayName = name;
		}
	}

	public String getName() {
		return name;
	}

	public void setDbType(int type) {
		this.dbType = type;
		connectionType = ConnectionManager.getInstance().findConnectionType(dbType);
	}

	public int getDbType() {
		return dbType;
	}
	
	public String getDatabaseFilePath() {
		return databaseFilePath;
	}

	public void setDatabaseFilePath(String dbFilePath) {
		databaseFilePath = dbFilePath;
	}

	public boolean isPersistable() {
		return persistable;
	}

	public void setPersistable(boolean persistable) {
		this.persistable = persistable;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setConnectionType(IConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	public IConnectionType getConnectionType() {
		return connectionType;
	}
}
