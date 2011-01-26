
package org.postgresql.driver;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ISqlType;

public class PostgreSqlConnectionType implements IConnectionType {

	private ISqlHelper postgreSqlHelper = new PostgresSqlHelper();

	public String getDefaultHostName() {
		return "localhost"; //$NON-NLS-1$
	}

	public String getDefaultPortNumber() {
		return "5432"; //$NON-NLS-1$
	}

	public String getDefaultSqlType() {
		return "VARCHAR"; //$NON-NLS-1$
	}

	public String getServerName() {
		return "PostgreSQL"; //$NON-NLS-1$
	}

	public String getDriverName() {
		return "org.postgresql.Driver"; //$NON-NLS-1$
	}

	public String getIntegerSqlType() {
		return "int8"; //$NON-NLS-1$
	}

	public int getType() {
		return IConnectionType.POSTGRESQL;
	}

	public boolean isLimitSupported() {
		return true;
	}

	public String getDefaultDatabasePath() {
		return null;
	}

	public String getUrlPattern() {
		return "jdbc:postgresql://" + IConnectionType.TEMPLATE_HOST + ":"
				+ IConnectionType.TEMPLATE_PORT
				+ "/" + IConnectionType.TEMPLATE_DB; //$NON-NLS-1$
	}

	public boolean isNetworkDatabase() {
		return true;
	}

	public String getDefaultDatabaseName() {
		return "postgres";//$NON-NLS-1$
	}

	public String getDefaultUsername() {
		return "postgres";//$NON-NLS-1$
	}

	public ISqlHelper getSqlHelper() {
		return postgreSqlHelper;
	}

	public int getDefaultColumnSize(ISqlType sqlType) {
		return 20;
	}

	public boolean hasScrollableResultSetSupport() {
		return true;
	}

}
