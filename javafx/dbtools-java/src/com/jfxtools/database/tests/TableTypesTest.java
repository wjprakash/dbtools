package com.jfxtools.database.tests;

import java.sql.SQLException;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IDatabaseInfo;

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
