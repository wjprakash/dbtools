package com.jfxtools.database.tests;

import java.sql.SQLException;
import java.util.List;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.IView;
import com.jfxtools.database.model.Connection;

/**
 * @author Winston Prakash Version 1.0
 */
public class TableInfoTest {

	public void dumpTableInfo(IConnectionConfig dbConfig) throws SQLException {

		IDatabaseInfo databaseInfo = new DatabaseInfo(dbConfig);
		Connection database = new Connection(databaseInfo);
		String[] tableTypes = databaseInfo.getTableTypes();
		for (String type : tableTypes) {
			System.out.println("Table Type:" + type);
		}
		if (database.hasSchemaSupport()) {
			System.out.println(database.getName() + " has schema support");
			List<ISchema> schemas = database.getSchemaList();
			for (ISchema schema : schemas) {
				System.out.println("Schema:" + schema.getName());
				List<ITable> tables = schema.getTableList();
				for (ITable table : tables) {
					System.out.println("    Table:" + table.getName());
					List<IColumn> columns = table.getColumnList(true);
					for (IColumn column : columns) {
						System.out.println("          Column:"
								+ column.getName());
					}
				}
				List<IView> views = schema.getViewList();
				for (IView view : views) {
					System.out.println("    View:" + view.getName());
					List<IColumn> columns = view.getColumnList();
					for (IColumn column : columns) {
						System.out.println("          Column:"
								+ column.getName());
					}
				}
			}
		} else {
			System.out.println(database.getName() + " has no schema support");
		}

		System.out.println("\n\n");
	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) {
		TableInfoTest connectionTest = new TableInfoTest();
		try {
			connectionTest.dumpTableInfo(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
