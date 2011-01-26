package com.jfxtools.database.tests;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.api.IConnectionConfig;

/**
 * @author Winston Prakash Version 1.0
 */
public class ConnectionMetadataTest {

	public void dumpMetadataInfo(IConnectionConfig dbConfig)
			throws SQLException {
		Connection connection = ConnectionManager.getInstance()
				.createConnection(dbConfig);
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		System.out.println("Product Name: ");
		System.out.println("  " + databaseMetaData.getDatabaseProductName());
		System.out.println("SQL Keywords: ");
		System.out.println("  " + databaseMetaData.getSQLKeywords());
		System.out.println("Vendor's preferred term for \"schema\": ");
		System.out.println("  " + databaseMetaData.getSchemaTerm());
		System.out.println("Vendor's preferred term for \"Procedures\": ");
		System.out.println("  " + databaseMetaData.getProcedureTerm());
		System.out.println("String Functions: ");
		System.out.println("  " + databaseMetaData.getStringFunctions());
		System.out.println("System Functions: ");
		System.out.println("  " + databaseMetaData.getSystemFunctions());

	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) {
		ConnectionMetadataTest connectionTest = new ConnectionMetadataTest();
		try {
			connectionTest.dumpMetadataInfo(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
