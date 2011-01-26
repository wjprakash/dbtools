
package com.nayaware.dbtools.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.IQuery;
import com.nayaware.dbtools.api.ISchemaDiagram;
import com.nayaware.dbtools.api.IScript;
import com.nayaware.dbtools.core.ConnectionConfig;
import com.nayaware.dbtools.model.Query;
import com.nayaware.dbtools.model.SchemaDiagram;
import com.nayaware.dbtools.model.Script;

/**
 * A manager that persists the information from the Connection manager
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class PersistanceManager {

	private static PersistanceManager instance;
	private final String CONNECTION_PROPERTIES_FILE = "Connection_props.xml"; //$NON-NLS-1$
	
	private boolean initialized = false;

	public synchronized static PersistanceManager getInstance() {
		if (instance == null) {
			instance = new PersistanceManager();
		}
		return instance;
	}
	
	/**
	 * MySql Sample connections for testing purpose
	 */
	public ConnectionConfig createMySqlConnection() {
		String name = "Local MySql"; //$NON-NLS-1$
		String url = "jdbc:mysql://localhost:3306/"; //$NON-NLS-1$
		String username = "root"; //$NON-NLS-1$
		String password = ""; //$NON-NLS-1$
		ConnectionConfig config = new ConnectionConfig(name, url, IConnectionType.MYSQL,
				username, password);
		config.setHostname("localhost"); //$NON-NLS-1$
		config.setPort("3306"); //$NON-NLS-1$
		config.setPersistable(false);
		return config;
	}

	/**
	 * Derby Sample connections for testing purpose
	 */
	public ConnectionConfig createDerbyConnection() {
		String name = "Local Derby"; //$NON-NLS-1$
		String url = "jdbc:derby://localhost:1527/travel"; //$NON-NLS-1$
		String username = "travel"; //$NON-NLS-1$
		String password = "travel"; //$NON-NLS-1$
		ConnectionConfig config = new ConnectionConfig(name, url,
				IConnectionType.DERBY_CLIENT, username, password);
		config.setHostname("localhost"); //$NON-NLS-1$
		config.setPort("1527"); //$NON-NLS-1$
		config.setDatabase("travel"); //$NON-NLS-1$
		config.setPersistable(false);
		return config;
	}

	public void persistConnections() {
		ConnectionManager connectionManager = ConnectionManager.getInstance();
		List<IConnectionConfig> configs = connectionManager
				.getConnectionConfigList();
		for (IConnectionConfig config : configs) {
			persistConnectionInfo(config);
		}
	}

	public void loadConnections() {
		ConnectionManager connectionManager = ConnectionManager.getInstance();
		
		//TODO: Remove these test connections when the plugin is finalized
		connectionManager.addConnectionConfig(createMySqlConnection());
		connectionManager.addConnectionConfig(createDerbyConnection());
		
		File pluginDir = getPluginUserDataDir();
		File[] connectionDirs = pluginDir.listFiles();
		for (File connectionDir : connectionDirs) {
			File propertiesFile = new File(connectionDir,
					CONNECTION_PROPERTIES_FILE);
			if (propertiesFile.exists()) {
				try {
					Properties properties = new Properties();
					properties.loadFromXML(new BufferedInputStream(
							new FileInputStream(propertiesFile)));
					IConnectionConfig config = new ConnectionConfig();
					config.setName(connectionDir.getName());
					config.setDisplayName((String) properties
							.get("displayName")); //$NON-NLS-1$
					config.setDbType(Integer.parseInt((String) properties
							.get("type"))); //$NON-NLS-1$
					config.setUrl((String) properties.get("url")); //$NON-NLS-1$
					config.setDriverName((String) properties.get("driverName")); //$NON-NLS-1$
					config.setUsername((String) properties.get("username")); //$NON-NLS-1$
					config.setPassword((String) properties.get("password")); //$NON-NLS-1$
					config.setHostname((String) properties.get("hostname")); //$NON-NLS-1$
					config.setPort((String) properties.get("port")); //$NON-NLS-1$
					config.setDatabase((String) properties.get("database")); //$NON-NLS-1$
					config.setDatabaseFilePath((String) properties
							.get("databaseFilePath")); //$NON-NLS-1$
					connectionManager.addConnectionConfig(config);

				} catch (IOException exc) {
					ErrorManager.showException(exc);
				}
			}
		}
		initialized = true;
	}

	public void removePersistedConnectionInfo(IConnectionConfig config) {
		File connectionDir = getConnectionDir(config);
		deleteDir(connectionDir);
	}

	public void updatePersistedConnectionInfo(IConnectionConfig connectionConfig) {
		File connectionDir = getConnectionDir(connectionConfig);
		File propertiesFile = new File(connectionDir,
				CONNECTION_PROPERTIES_FILE);
		if (propertiesFile.exists()) {
			propertiesFile.delete();
		}
		persistConnectionInfo(connectionConfig);
	}

	public void persistConnectionInfo(IConnectionConfig config) {
		if (config.isPersistable()) {
			File connectionDir = getConnectionDir(config);
			File propertiesFile = new File(connectionDir,
					CONNECTION_PROPERTIES_FILE);
			Properties properties = new Properties();
			properties.put("displayName", config.getDisplayName()); //$NON-NLS-1$
			properties.put("type", String.valueOf(config.getDbType())); //$NON-NLS-1$
			properties.put("url", config.getUrl()); //$NON-NLS-1$
			properties.put("driverName", config.getDriverName()); //$NON-NLS-1$
			properties.put("username", config.getUsername()); //$NON-NLS-1$
			properties.put("password", config.getPassword()); //$NON-NLS-1$
			properties.put("hostname", config.getHostname()); //$NON-NLS-1$
			properties.put("port", config.getPort()); //$NON-NLS-1$
			properties.put("database", config.getDatabase()); //$NON-NLS-1$
			properties.put("databaseFilePath", config.getDatabaseFilePath()); //$NON-NLS-1$

			try {
				properties
						.storeToXML(new BufferedOutputStream(
								new FileOutputStream(propertiesFile)), config
								.getName());
			} catch (IOException exc) {
				ErrorManager.showException(exc);
			}
		}
	}

	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public File getPluginUserDataDir() {
		IPath pluginUserDataDir = DbExplorerPlugin.getDefault()
				.getStateLocation();
		return pluginUserDataDir.toFile();
	}

	public File getConnectionDir(IConnectionConfig connectionConfig) {
		File pluginDir = getPluginUserDataDir();
		File connectionDir = new File(pluginDir + "/" //$NON-NLS-1$
				+ connectionConfig.getName());
		if (!connectionDir.exists()) {
			connectionDir.mkdirs();
		}
		return connectionDir;
	}

	public File getScriptDir(IConnectionConfig connectionConfig) {
		File connectionDir = getConnectionDir(connectionConfig);
		File scriptDir = new File(connectionDir + "/scripts"); //$NON-NLS-1$

		if (!scriptDir.exists()) {
			scriptDir.mkdirs();
		}
		return scriptDir;
	}

	public List<IScript> getScripts(IDatabaseInfo databaseInfo) {
		File scriptDir = getScriptDir(databaseInfo.getConnectionConfig());
		File[] scripts = scriptDir.listFiles();
		List<IScript> sqlScripts = new ArrayList<IScript>();
		for (File file : scripts) {
			String fileName = file.getName();
			IScript script = new Script(databaseInfo, fileName.substring(0,
					fileName.lastIndexOf("."))); //$NON-NLS-1$
			script.setPath(file.getAbsolutePath());
			sqlScripts.add(script);
		}
		return sqlScripts;
	}

	public File getQueryDir(IConnectionConfig connectionConfig) {
		File connectionDir = getConnectionDir(connectionConfig);
		File queryDir = new File(connectionDir + "/queries"); //$NON-NLS-1$

		if (!queryDir.exists()) {
			queryDir.mkdirs();
		}
		return queryDir;
	}

	public List<IQuery> getQueries(IDatabaseInfo databaseInfo) {
		File queryDir = getQueryDir(databaseInfo.getConnectionConfig());
		File[] queries = queryDir.listFiles();
		List<IQuery> sqlQueries = new ArrayList<IQuery>();
		for (File file : queries) {
			String fileName = file.getName();
			IQuery script = new Query(databaseInfo, fileName.substring(0,
					fileName.lastIndexOf("."))); //$NON-NLS-1$
			script.setPath(file.getAbsolutePath());
			sqlQueries.add(script);
		}
		return sqlQueries;
	}

	public File getSchemaDiagramDir(IConnectionConfig connectionConfig) {
		File connectionDir = getConnectionDir(connectionConfig);
		File schemaDiagramDir = new File(connectionDir + "/schemaDiagrams"); //$NON-NLS-1$

		if (!schemaDiagramDir.exists()) {
			schemaDiagramDir.mkdirs();
		}
		return schemaDiagramDir;
	}

	public List<ISchemaDiagram> getSchemaDiagrams(IDatabaseInfo databaseInfo) {
		File schemaDiagramDir = getSchemaDiagramDir(databaseInfo
				.getConnectionConfig());
		File[] schemaDiagramFiles = schemaDiagramDir.listFiles();
		List<ISchemaDiagram> schemaDiagrams = new ArrayList<ISchemaDiagram>();
		for (File file : schemaDiagramFiles) {
			String fileName = file.getName();
			ISchemaDiagram diagram = new SchemaDiagram(databaseInfo, fileName
					.substring(0, fileName.lastIndexOf("."))); //$NON-NLS-1$
			diagram.setPath(file.getAbsolutePath());
			schemaDiagrams.add(diagram);
		}
		return schemaDiagrams;
	}

	public boolean isInitialized() {
		return initialized;
	}
}
