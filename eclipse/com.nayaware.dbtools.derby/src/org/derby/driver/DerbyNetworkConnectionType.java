
package org.derby.driver;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ISqlType;

public class DerbyNetworkConnectionType implements IConnectionType {

	private ISqlHelper derbyNetworkSqlHelper = new DerbyNetworkSqlHelper();

	public String getDefaultHostName() {
		return "localhost"; //$NON-NLS-1$
	}

	public String getDefaultPortNumber() {
		return "1527"; //$NON-NLS-1$
	}

	public String getDefaultSqlType() {
		return "VARCHAR"; //$NON-NLS-1$
	}

	public String getServerName() {
		return "Derby (Network)"; //$NON-NLS-1$
	}

	public String getDriverName() {
		return "org.apache.derby.jdbc.ClientDriver"; //$NON-NLS-1$
	}

	public String getIntegerSqlType() {
		return "INTEGER"; //$NON-NLS-1$
	}

	public int getType() {
		return IConnectionType.DERBY_CLIENT;
	}

	public boolean isLimitSupported() {
		return true;
	}

	public String getDefaultDatabasePath() {
		return null;
	}

	public String getUrlPattern() {
		return "jdbc:derby://" + IConnectionType.TEMPLATE_HOST + ":"
				+ IConnectionType.TEMPLATE_PORT
				+ "/" + IConnectionType.TEMPLATE_DB; //$NON-NLS-1$
	}

	public boolean isNetworkDatabase() {
		return true;
	}

	public String getDefaultDatabaseName() {
		return "sample"; //$NON-NLS-1$
	}

	public String getDefaultUsername() {
		return ""; //$NON-NLS-1$
	}

	public ISqlHelper getSqlHelper() {
		return derbyNetworkSqlHelper;
	}

	public int getDefaultColumnSize(ISqlType sqlType) {
		return 20;
	}

	public boolean hasScrollableResultSetSupport() {
		return true;
	}

}
