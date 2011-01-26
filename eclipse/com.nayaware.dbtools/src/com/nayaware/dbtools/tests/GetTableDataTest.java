
package com.nayaware.dbtools.tests;

import java.sql.SQLException;
import java.util.List;

import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.ITableColumnData;
import com.nayaware.dbtools.api.ITableData;
import com.nayaware.dbtools.api.ITableRowData;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.Connection;

/**
 * @author Winston Prakash Version 1.0
 */
public class GetTableDataTest {

	public void dumpTableData(IConnectionConfig dbConfig) throws SQLException,
			ClassNotFoundException {
		IDatabaseInfo dbInfo = new DatabaseInfo(dbConfig);
		IConnection database = new Connection(dbInfo);
		List<ISchema> schemas = database.getSchemaList();
		for (ISchema schema : schemas) {
			System.out.println("Schema:" + schema.getName());
			List<ITable> tables = schema.getTableList();
			for (ITable table : tables) {
				ITableData tableData = table.getData();
				ITableRowData[] rowData = tableData.getTableData();
				for (int i = 0; i < rowData.length; i++) {
					int colCount = rowData[i].getColumnCount();
					for (int j = 0; j < colCount; j++) {
						ITableColumnData colData = rowData[i]
								.getTableColumnData(j);
						System.out.print(colData.toString() + "  ");
					}
					System.out.println("\n");
				}
			}
		}
	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) {
		GetTableDataTest connectionTest = new GetTableDataTest();
		try {
			connectionTest.dumpTableData(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
