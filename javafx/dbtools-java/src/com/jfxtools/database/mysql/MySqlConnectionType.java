package com.jfxtools.database.mysql;

import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.api.ISqlHelper;
import com.jfxtools.database.api.ISqlType;

public class MySqlConnectionType implements IConnectionType {

	private ISqlHelper mysqlSqlHelper = new MysqlSqlHelper();

	public String getDefaultHostName() {
		return "localhost";
	}

	public String getDefaultPortNumber() {
		return "3306"; 
	}

	public String getDefaultSqlType() {
		return "VARCHAR"; 
	}

	public String getServerName() {
		return "MySQL"; 
	}

	public String getDriverName() {
		return "com.mysql.jdbc.Driver"; 
	}

	public String getIntegerSqlType() {
		return "INTEGER"; 
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
				+ "/" + IConnectionType.TEMPLATE_DB; 
	}

	public String getDefaultDatabasePath() {
		return ""; 
	}

	public boolean isNetworkDatabase() {
		return true;
	}

	public String getDefaultDatabaseName() {
		return ""; 
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
