package com.jfxtools.database.tests;

import java.sql.SQLException;
import java.util.List;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.api.IConnection;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.ITableColumnData;
import com.jfxtools.database.api.ITableData;
import com.jfxtools.database.api.ITableRowData;
import com.jfxtools.database.model.Connection;

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
