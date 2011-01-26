package com.jfxtools.database.tests;

import java.util.List;

import com.jfxtools.database.ConnectionConfig;
import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.mysql.MySqlConnectionType;

/**
 * Test utility to create connections
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ConnectionUtils {

	public static IConnectionConfig createMySqlConnectionConfig() {
		List<IConnectionType> connectionTypes = ConnectionManager.getInstance()
				.getAvailableConnectionTypes();
		connectionTypes.add(new MySqlConnectionType());
		ConnectionManager.getInstance().setAvailableConnectionTypes(
				connectionTypes);

		String name = "Local MySql";
		String url = "jdbc:mysql://localhost:3306/";
		String username = "root";
		String password = "";
		return new ConnectionConfig(name, url, IConnectionType.MYSQL, username,
				password);
	}

	public static IConnectionConfig createDerbyConnectionConfig() {
		List<IConnectionType> connectionTypes = ConnectionManager.getInstance()
				.getAvailableConnectionTypes();
		connectionTypes.add(new AbstractConnectionType() {
			public int getType() {
				return IConnectionType.DERBY_CLIENT;
			}

			public String getDriverName() {
				return "org.apache.derby.jdbc.ClientDriver"; 
			}

			public String getUrlPattern() {
				return "jdbc:derby://" + IConnectionType.TEMPLATE_HOST + ":"
						+ IConnectionType.TEMPLATE_PORT
						+ "/" + IConnectionType.TEMPLATE_DB; 
			}

			public boolean isNetworkDatabase() {
				return true;
			}

		});
		ConnectionManager.getInstance().setAvailableConnectionTypes(
				connectionTypes);

		String name1 = "Local Derby";
		String url1 = "jdbc:derby://localhost:1527/travel";
		String username1 = "travel";
		String password1 = "travel";
		return new ConnectionConfig(name1, url1, IConnectionType.DERBY_CLIENT,
				username1, password1);
	}

	public static IConnectionConfig createEmbeddedDerbyConnectionConfig() {
		List<IConnectionType> connectionTypes = ConnectionManager.getInstance()
				.getAvailableConnectionTypes();
		connectionTypes.add(new AbstractConnectionType() {
			public int getType() {
				return IConnectionType.DERBY_EMBEDDED;
			}

			public String getDriverName() {
				return "org.apache.derby.jdbc.EmbeddedDriver"; 
			}

			public String getUrlPattern() {
				return "jdbc:derby:" + IConnectionType.TEMPLATE_DB_PATH; 
			}

			public boolean isNetworkDatabase() {
				return false;
			}

		});
		ConnectionManager.getInstance().setAvailableConnectionTypes(
				connectionTypes);

		String name1 = "Local Embedded Derby";
		String url1 = "jdbc:derby:" + "/Users/winston/DerbyDatabase1";
		String username1 = "";
		String password1 = "";
		return new ConnectionConfig(name1, url1,
				IConnectionType.DERBY_EMBEDDED, username1, password1);
	}

	public static IConnectionConfig createPostgresConnectionConfig() {
		List<IConnectionType> connectionTypes = ConnectionManager.getInstance()
				.getAvailableConnectionTypes();
		connectionTypes.add(new AbstractConnectionType() {
			public int getType() {
				return IConnectionType.POSTGRESQL;
			}

			public String getDriverName() {
				return "org.postgresql.Driver"; 
			}

			public String getUrlPattern() {
				return "jdbc:postgresql://" + IConnectionType.TEMPLATE_HOST
						+ ":" + IConnectionType.TEMPLATE_PORT
						+ "/" + IConnectionType.TEMPLATE_DB; 
			}

		});
		ConnectionManager.getInstance().setAvailableConnectionTypes(
				connectionTypes);

		String name1 = "Local Postgres";
		String url1 = "jdbc:postgresql://localhost:5432/postgres";
		String username1 = "postgres";
		String password1 = "wjp987";
		return new ConnectionConfig(name1, url1, IConnectionType.POSTGRESQL,
				username1, password1);
	}

	public static IConnectionConfig createSqliteConnectionConfig() {
		List<IConnectionType> connectionTypes = ConnectionManager.getInstance()
				.getAvailableConnectionTypes();
		connectionTypes.add(new AbstractConnectionType() {
			public int getType() {
				return IConnectionType.SQLITE;
			}

			public String getDriverName() {
				return "org.sqlite.JDBC"; 
			}

			public String getUrlPattern() {
				return "jdbc:sqlite:/" + IConnectionType.TEMPLATE_DB_PATH; 
			}

		});
		ConnectionManager.getInstance().setAvailableConnectionTypes(
				connectionTypes);

		String name1 = "Local Sqlite";
		String url1 = "jdbc:sqlite:" + "/Users/winston/SqliteDatabase1";
		String username1 = "";
		String password1 = "";
		return new ConnectionConfig(name1, url1, IConnectionType.SQLITE,
				username1, password1);
	}

	public static IConnectionConfig createRemoteMySqlConnectionConfig() {
		List<IConnectionType> connectionTypes = ConnectionManager.getInstance()
				.getAvailableConnectionTypes();
		connectionTypes.add(new AbstractConnectionType() {
			public int getType() {
				return IConnectionType.MYSQL;
			}

			public String getDriverName() {
				return "com.mysql.jdbc.Driver"; 
			}

			public String getUrlPattern() {
				return "jdbc:mysql://" + IConnectionType.TEMPLATE_HOST + ":"
						+ IConnectionType.TEMPLATE_PORT
						+ "/" + IConnectionType.TEMPLATE_DB; 
			}

		});
		ConnectionManager.getInstance().setAvailableConnectionTypes(
				connectionTypes);

		String name = "Remote MySql";
		String url = "jdbc:mysql://ajaxlibraryprojectw2.aptanacloud.com:3306/?allowMultiQueries=true&zeroDateTimeBehavior=convertToNull";
		String username = "wjprakash";
		String password = "";
		return new ConnectionConfig(name, url, IConnectionType.MYSQL, username,
				password);
	}
}
