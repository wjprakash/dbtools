package com.jfxtools.database.tests;

import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.api.ISqlHelper;
import com.jfxtools.database.api.ISqlType;


public class AbstractConnectionType implements IConnectionType{

	public int getDefaultColumnSize(ISqlType sqlType) {
		return 20;
	}

	public String getDefaultDatabaseName() {
		return "test";
	}

	public String getDefaultDatabasePath() {
		return "";
	}

	public String getDefaultHostName() {
		return "";
	}

	public String getDefaultPortNumber() {
		return "";
	}

	public String getDefaultSqlType() {
		return "";
	}

	public String getDefaultUsername() {
		return "";
	}

	public String getIntegerSqlType() {
		return "";
	}

	public String getServerName() {
		return "";
	}

	public ISqlHelper getSqlHelper() {
		return null;
	}

	public int getType() {
		return 0;
	}

	public String getDriverName() {
		return "";
	}
	public String getUrlPattern() {
		return "";
	}

	public boolean hasScrollableResultSetSupport() {
		return true;
	}

	public boolean isLimitSupported() {
		return true;
	}

	public boolean isNetworkDatabase() {
		return true;
	}

}
