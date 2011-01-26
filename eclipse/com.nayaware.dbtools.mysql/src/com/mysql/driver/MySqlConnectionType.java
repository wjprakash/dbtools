
package com.mysql.driver;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ISqlType;

public class MySqlConnectionType implements IConnectionType {

	private ISqlHelper mysqlSqlHelper = new MysqlSqlHelper();

	public String getDefaultHostName() {
		return "localhost";
	}

	public String getDefaultPortNumber() {
		return "3306"; //$NON-NLS-1$
	}

	public String getDefaultSqlType() {
		return "VARCHAR"; //$NON-NLS-1$
	}

	public String getServerName() {
		return "MySQL"; //$NON-NLS-1$
	}

	public String getDriverName() {
		return "com.mysql.jdbc.Driver"; //$NON-NLS-1$
	}

	public String getIntegerSqlType() {
		return "INTEGER"; //$NON-NLS-1$
	}

	public int getType() {
		return IConnectionType.MYSQL;
	}

	public boolean isLimitSupported() {
		return true;
	}

	public String getUrlPattern() {
		return "jdbc:mysql://" + IConnectionType.TEMPLATE_HOST + ":"
				+ IConnectionType.TEMPLATE_PORT
				+ "/" + IConnectionType.TEMPLATE_DB; //$NON-NLS-1$
	}

	public String getDefaultDatabasePath() {
		return ""; //$NON-NLS-1$
	}

	public boolean isNetworkDatabase() {
		return true;
	}

	public String getDefaultDatabaseName() {
		return ""; //$NON-NLS-1$
	}

	public String getDefaultUsername() {
		return "root";
	}

	public ISqlHelper getSqlHelper() {
		return mysqlSqlHelper;
	}

	public int getDefaultColumnSize(ISqlType sqlType) {
		return 20;
	}

	public boolean hasScrollableResultSetSupport() {
		return true;
	}

}
