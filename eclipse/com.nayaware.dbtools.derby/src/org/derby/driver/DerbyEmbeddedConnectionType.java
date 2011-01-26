
package org.derby.driver;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ISqlType;

public class DerbyEmbeddedConnectionType implements IConnectionType {

	private ISqlHelper derbyEmbeddedSqlHelper = new DerbyEmbeddedSqlHelper();

	public String getDefaultHostName() {
		return ""; //$NON-NLS-1$
	}

	public String getDefaultPortNumber() {
		return ""; //$NON-NLS-1$
	}

	public String getDefaultSqlType() {
		return "VARCHAR"; //$NON-NLS-1$
	}

	public String getServerName() {
		return "Derby (Embedded)";
	}

	public String getDriverName() {
		return "org.apache.derby.jdbc.EmbeddedDriver"; //$NON-NLS-1$
	}

	public String getIntegerSqlType() {
		return "INTEGER";
	}

	public int getType() {
		return IConnectionType.DERBY_EMBEDDED;
	}

	public boolean isLimitSupported() {
		return true;
	}

	public String getDefaultDatabasePath() {
		return null;
	}

	public String getUrlPattern() {
		return "jdbc:derby:" + IConnectionType.TEMPLATE_DB_PATH; //$NON-NLS-1$
	}

	public boolean isNetworkDatabase() {
		return false;
	}

	public String getDefaultDatabaseName() {
		return ""; //$NON-NLS-1$
	}

	public String getDefaultUsername() {
		return ""; //$NON-NLS-1$
	}

	public ISqlHelper getSqlHelper() {
		return derbyEmbeddedSqlHelper;
	}

	public int getDefaultColumnSize(ISqlType sqlType) {
		return 20;
	}

	public boolean hasScrollableResultSetSupport() {
		return true;
	}

}
