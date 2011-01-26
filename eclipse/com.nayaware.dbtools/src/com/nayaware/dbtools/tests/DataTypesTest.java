
package com.nayaware.dbtools.tests;

import java.sql.SQLException;
import java.util.List;

import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISqlType;
import com.nayaware.dbtools.core.DatabaseInfo;

/**
 * Database data type test
 * @author Winston Prakash Version 1.0
 */
public class DataTypesTest {

	public void dumpDataTypes(IConnectionConfig dbConfig) throws SQLException {
		System.out.println("Fetching SQL Types:");
		IDatabaseInfo dbInfo = new DatabaseInfo(dbConfig);
		List<ISqlType> sqlTypes = dbInfo.getSqlTypes();
		for (ISqlType sqlType : sqlTypes) {
			System.out.println("\nSQL Type:" + sqlType.getName());
			if (sqlType.isAutoIncrementable()) {
				System.out.println("  AUTO INCREMENT Supported");
			} else {
				System.out.println("  AUTO INCREMENT Not Supported");
			}
			if (sqlType.isNullAllowed()) {
				System.out.println("  NULL Allowed");
			} else {
				System.out.println("  NULL Not Allowed");
			}
			System.out.println("  Literal Prefix: - " + sqlType.getLiteralPrefix());
			System.out.println("  Literal Suffix: - " + sqlType.getLiteralSuffix());
		}
	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) {
		DataTypesTest connectionTest = new DataTypesTest();
		try {
			
//			connectionTest.dumpDataTypes(ConnectionUtils
//					.createEmbeddedDerbyConnectionConfig());
		
//			connectionTest.dumpDataTypes(ConnectionUtils
//					.createPostgresConnectionConfig());
			
//			 connectionTest.dumpDataTypes(ConnectionUtils.
//			 createMySqlConnectionConfig());
			 
			 connectionTest.dumpDataTypes(ConnectionUtils.
					 createSqliteConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
