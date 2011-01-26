
package com.nayaware.dbtools.tests;

import java.sql.SQLException;

import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.core.DatabaseInfo;

/**
 * @author Winston Prakash Version 1.0
 */
public class TableTypesTest {

	public void dumpTableTypes(IConnectionConfig dbConfig) throws SQLException {
		IDatabaseInfo databaseInfo = new DatabaseInfo(dbConfig);
		String[] tableTypes = databaseInfo.getTableTypes();
		for (String type : tableTypes) {
			System.out.println("Table Type:" + type);
		}
	}

	public static void main(String[] args) {
		TableTypesTest connectionTest = new TableTypesTest();
		try {
			connectionTest.dumpTableTypes(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
