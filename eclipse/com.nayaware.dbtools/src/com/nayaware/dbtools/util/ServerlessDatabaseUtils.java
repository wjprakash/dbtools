
package com.nayaware.dbtools.util;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.core.ConnectionConfig;

/**
 * Utilities for Server-less databases such as Embedded Derby or SQLite
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ServerlessDatabaseUtils {

	public static void createEmbeddedDerbyDatabase(String dbFolder,
			String dbName, String username, String password,
			String connectionName, boolean connect) {
		String dbPath = dbFolder + "/" + dbName; //$NON-NLS-1$
		String dbUrl = "jdbc:derby:" + dbPath + ";create=true"; //$NON-NLS-1$ //$NON-NLS-2$
		String username1 = ""; //$NON-NLS-1$
		String password1 = ""; //$NON-NLS-1$
		IConnectionConfig conConfig = new ConnectionConfig(connectionName,
				dbUrl, IConnectionType.DERBY_EMBEDDED, username1, password1);
		conConfig.setDatabaseFilePath(dbPath);
		ConnectionManager connectionManager = ConnectionManager.getInstance();
		try {
			connectionManager.createConnection(conConfig);
			if (connect) {
				connectionManager.addConnectionConfig(conConfig);
			}
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
		}
	}

	public static void createSqliteDatabase(String dbFolder, String dbName,
			String connectionName, boolean connect) {

		Bundle bundle = Platform.getBundle(DbExplorerPlugin.PLUGIN_ID);
		String execName = null;
		String os = Platform.getOS();
		if (Platform.OS_WIN32.equals(os)) {
			execName = "sqlite3.exe"; //$NON-NLS-1$
		} else if (Platform.OS_LINUX.equals(os)) {
			execName = "sqlite3-3.6.6.2.bin"; //$NON-NLS-1$
		} else if (Platform.OS_MACOSX.equals(os)) {
			execName = "sqlite3-3.6.6.2-osx-x86.bin"; //$NON-NLS-1$
		}
		if (execName != null) {
			try {
				Path path = new Path("external/sqlite/" + execName); //$NON-NLS-1$
				URL url = FileLocator.find(bundle, path, null);
				URL fileUrl = FileLocator.toFileURL(url);
				String sqlliteProcessString = ".schema CREATE TABLE table .exit"; //$NON-NLS-1$
				if (Platform.OS_LINUX.equals(os)
						|| Platform.OS_MACOSX.equals(os)) {
					Runtime.getRuntime().exec(
							new String[] { "chmod", "755", fileUrl.getPath() }); //$NON-NLS-1$ //$NON-NLS-2$
				}
				Runtime.getRuntime().exec(
						new String[] { fileUrl.getPath(), dbName,
								sqlliteProcessString }, null,
						new File(dbFolder));

				String dbPath = dbFolder + "/" + dbName; //$NON-NLS-1$
				String dbUrl = "jdbc:sqlite:" + dbPath; //$NON-NLS-1$
				IConnectionConfig conConfig = new ConnectionConfig(
						connectionName, dbUrl, IConnectionType.SQLITE, "", ""); //$NON-NLS-1$ //$NON-NLS-2$
				conConfig.setDatabaseFilePath(dbPath);
				ConnectionManager connectionManager = ConnectionManager
						.getInstance();

				connectionManager.createConnection(conConfig);
				if (connect) {
					connectionManager.addConnectionConfig(conConfig);
				}
			} catch (Exception exc) {
				ErrorManager.showException(exc);
			}
		} else {
			ErrorManager.showInformationMessage(Messages.getString("ServerlessDatabaseUtils.16") //$NON-NLS-1$
					+ Platform.getOS());
		}
	}
}
