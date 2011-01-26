
package com.nayaware.dbtools.api;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public interface IConnectionConfig {

	public String getHostname();

	public void setHostname(String hostname);

	public String getPort();

	public void setPort(String port);

	public String getDatabase();

	public void setDatabase(String database);

	public String[] getProperties();

	public void setProperties(String[] properties);

	public String getUrl();

	public void setUrl(String url);

	public String getDriverName();

	public void setDriverName(String driverName);

	public String getUsername();

	public void setUsername(String username);

	public String getPassword();

	public void setPassword(String password);

	public void setName(String name);

	public String getName();

	public void setDisplayName(String displayName);

	public String getDisplayName();

	public void setDbType(int type);

	public int getDbType();

	public String getDatabaseFilePath();

	public void setDatabaseFilePath(String dbFilePath);

	public boolean isPersistable();

	public void setPersistable(boolean persistable);
	
	public void setConnectionType(IConnectionType connectionType);

	public IConnectionType getConnectionType();
}