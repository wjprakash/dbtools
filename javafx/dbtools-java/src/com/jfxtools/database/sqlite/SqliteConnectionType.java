package com.jfxtools.database.sqlite;

import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.api.ISqlHelper;
import com.jfxtools.database.api.ISqlType;

public class SqliteConnectionType implements IConnectionType {

	private ISqlHelper sqliteSqlHelper = new SqliteSqlHelper();

	public String getDefaultHostName() {
		return "";//$NON-NLS-1$
	}

	public String getDefaultPortNumber() {
		return "";//$NON-NLS-1$
	}

	public String getDefaultSqlType() {
		return "TEXT"; //$NON-NLS-1$
	}

	public String getServerName() {
		return "SQLite"; //$NON-NLS-1$
	}

	public String getDriverName() {
		return "org.sqlite.JDBC"; //$NON-NLS-1$
	}

	public String getIntegerSqlType() {
		return "INTEGER"; //$NON-NLS-1$
	}

	public int getType() {
		return IConnectionType.SQLITE;
	}

	public boolean isLimitSupported() {
		return false;
	}

	public String getDefaultDatabasePath() {
		return null;
	}

	public String getUrlPattern() {
		return "jdbc:sqlite:/" + IConnectionType.TEMPLATE_DB_PATH; //$NON-NLS-1$
	}

	public boolean isNetworkDatabase() {
		return false;
	}

	public String getDefaultDatabaseName() {
		return "";//$NON-NLS-1$
	}

	public String getDefaultUsername() {
		return "";//$NON-NLS-1$
	}

	public ISqlHelper getSqlHelper() {
		return sqliteSqlHelper;
	}

	public int getDefaultColumnSize(ISqlType sqlType) {
		return 20;
	}

	public boolean hasScrollableResultSetSupport() {
		return false;
	}

}
