
package com.nayaware.dbtools.tests;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ISqlType;

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
