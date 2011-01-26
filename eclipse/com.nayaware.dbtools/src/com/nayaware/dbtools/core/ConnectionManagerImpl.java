
package com.nayaware.dbtools.core;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.PersistanceManager;

/**
 * Manages the database connection configurations
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionManagerImpl extends ConnectionManager {

	private List<IConnectionConfig> connections = new ArrayList<IConnectionConfig>();
	private List<ConnectionManagerListener> listeners = new CopyOnWriteArrayList<ConnectionManagerListener>();

	private Map<String, Driver> driverMap = new HashMap<String, Driver>();

	private static final int TIME_OUT_SEC = 10;

	private static final String CONNECTION_BASE_NAME = Messages
			.getString("ConnectionManagerImpl.1"); //$NON-NLS-1$

	private List<IConnectionType> availableonnectionTypes;

	@Override
	public void addConnectionManagerListener(ConnectionManagerListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeConnectionManagerListener(
			ConnectionManagerListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void addConnectionConfig(IConnectionConfig connectionConfig) {
		connections.add(connectionConfig);
		for (ConnectionManagerListener listener : listeners) {
			listener.connectionConfigAdded(connectionConfig);
		}
		PersistanceManager.getInstance()
				.persistConnectionInfo(connectionConfig);
	}

	@Override
	public IConnectionConfig createConnectionConfig(String name, String url,
			int type, String username, String password) {
		return new ConnectionConfig(name, url, type, username, password);
	}

	@Override
	public IConnectionConfig createConnectionConfig(String name,
			String hostname, String port, String database, int type,
			String username, String password) {
		return new ConnectionConfig(name, hostname, port, database, type,
				username, password);
	}

	@Override
	public void removeConnectionConfig(IConnectionConfig config) {
		connections.remove(config);
		for (ConnectionManagerListener listener : listeners) {
			listener.connectionConfigRemoved(config);
		}
		PersistanceManager.getInstance().removePersistedConnectionInfo(config);
	}

	@Override
	public void updateConnectionConfig(IConnectionConfig connectionConfig) {
		for (ConnectionManagerListener listener : listeners) {
			listener.connectionConfigModified(connectionConfig);
		}
		PersistanceManager.getInstance().updatePersistedConnectionInfo(
				connectionConfig);
	}

	@Override
	public synchronized List<IConnectionConfig> getConnectionConfigList() {
		return Collections.unmodifiableList(connections);
	}

	@Override
	public Connection createConnection(IConnectionConfig config)
			throws SQLException {

		Connection con = null;
		Driver driver;
		try {
			driver = getDriver(config.getDriverName());
		} catch (Exception exc) {
			throw new SQLException(Messages
					.getString("ConnectionManagerImpl.noDriver") //$NON-NLS-1$
					+ config.getDriverName());
		}

		if (driver != null) {
			try {
				DriverManager.setLoginTimeout(TIME_OUT_SEC);
			} catch (Exception exc) {
				// Some drivers might not have implemented, in that case simply
				// ignore
				// ErrorManager.showException(exc);
			}
			Properties conProps = new Properties();
			conProps.setProperty("user", config.getUsername()); //$NON-NLS-1$
			conProps.setProperty("password", config.getPassword()); //$NON-NLS-1$
			con = driver.connect(config.getUrl(), conProps);
		} else {
			throw new IllegalStateException(
					Messages.getString("ConnectionManagerImpl.noDriver") + config.getDriverName()); //$NON-NLS-1$
		}
		return con;
	}

	/**
	 * Find the JDBC Driver for the given driver name
	 * 
	 * @param driverName
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	private Driver getDriver(String driverName) throws InstantiationException,
			IllegalAccessException {
		if (driverMap.containsKey(driverName)) {
			return driverMap.get(driverName);
		} else {
			try {
				Class driverClass = getClass().getClassLoader().loadClass(
						driverName);
				return (Driver) driverClass.newInstance();
			} catch (ClassNotFoundException e) {
				// OK, the classpath does not contains any driver.
				// Ignore the exception and try to get the Driver from extension
				// point
				// e.printStackTrace();
				Driver driver = getExtensionDriver(driverName);
				driverMap.put(driverName, driver);
				return driver;
			}
		}
	}

	/**
	 * Get the available connection types from the plugin extensions for the
	 * "jdbcDriver" extension point
	 */
	public synchronized List<IConnectionType> getAvailableConnectionTypes() {
		if (availableonnectionTypes == null) {
			availableonnectionTypes = new ArrayList<IConnectionType>();

			IExtensionRegistry extensionRegistry = Platform
					.getExtensionRegistry();
			if (extensionRegistry != null) {
				IExtensionPoint extension = extensionRegistry
						.getExtensionPoint(DbExplorerPlugin.PLUGIN_ID,
								DbExplorerPlugin.JDBC_DRIVER_EXTENSION_POINT);

				if (extension != null) {
					IConfigurationElement[] elements = extension
							.getConfigurationElements();
					for (int i = 0; i < elements.length; i++) {
						try {
							IConfigurationElement confElement = elements[i];
							IConnectionType connectionTypeInfo = (IConnectionType) confElement
									.createExecutableExtension("connectionTypeInfo"); //$NON-NLS-1$
							availableonnectionTypes.add(connectionTypeInfo);
						} catch (InvalidRegistryObjectException e) {
							e.printStackTrace();
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return availableonnectionTypes;
	}
	
	@Override
	public void setAvailableConnectionTypes(
			List<IConnectionType> connectionTypeList) {
		availableonnectionTypes = connectionTypeList;
	}

	/**
	 * Find Extensions provided by Driver Provider plugins and get the
	 * corresponding JDBC Driver
	 * 
	 * @param driverName
	 * @return
	 */
	private Driver getExtensionDriver(String driverName) {
		IExtensionPoint extension = Platform.getExtensionRegistry()
				.getExtensionPoint(DbExplorerPlugin.PLUGIN_ID,
						DbExplorerPlugin.JDBC_DRIVER_EXTENSION_POINT);

		if (extension != null) {
			IConfigurationElement[] elements = extension
					.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement e = elements[i];
				String dn = e.getAttribute("jdbcDriverClass"); //$NON-NLS-1$
				if (dn != null && dn.equals(driverName)) {
					Object o;
					try {
						o = e.createExecutableExtension("jdbcDriverClass"); //$NON-NLS-1$
						if (o instanceof Driver) {
							return (Driver) o;
						} else {
							return null;
						}
					} catch (CoreException exc) {
						ErrorManager.showException(exc);
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getUniqueConnectionName() {
		int count = 0;
		String uniqueName = CONNECTION_BASE_NAME + ++count;
		List<String> connectionNames = new ArrayList<String>(connections.size());

		for (IConnectionConfig config : connections) {
			connectionNames.add(config.getName());
		}

		while (connectionNames.contains(uniqueName)) {
			uniqueName = CONNECTION_BASE_NAME + count++;
		}

		return uniqueName;
	}

	public synchronized IConnectionType findConnectionType(int dbType) {
		if (availableonnectionTypes == null) {
			getAvailableConnectionTypes();
		}
		for (IConnectionType connectionTypeInfo : availableonnectionTypes) {
			if (dbType == connectionTypeInfo.getType()) {
				return connectionTypeInfo;
			}
		}
		return null;
	}
}
