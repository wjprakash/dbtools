
package com.nayaware.dbtools.tests;

import java.sql.SQLException;
import java.util.List;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.Connection;

/**
 * @author Winston Prakash Version 1.0
 */
public class PrimaryKeyTest {

	public void dumpPrimaryKeys(IConnectionConfig dbConfig) throws SQLException {
		IDatabaseInfo dbInfo = new DatabaseInfo(dbConfig);
		IConnection database = new Connection(dbInfo);
		List<ISchema> schemas = database.getSchemaList();
		for (ISchema schema : schemas) {
			System.out.println("Schema:" + schema.getName());
			List<ITable> tables = schema.getTableList();
			for (ITable table : tables) {
				System.out.println("    Table:" + table.getName());
				List<IColumn> pkColumns = table.getPrimaryKeyColumns();
				for (IColumn column : pkColumns) {
					System.out.println("      Primary Key Column:"
							+ column.getName());
				}
			}
		}
	}

	public static void main(String[] args) {
		PrimaryKeyTest connectionTest = new PrimaryKeyTest();
		try {
			connectionTest.dumpPrimaryKeys(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
