package com.jfxtools.database.tests;

import java.sql.SQLException;
import java.util.List;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.api.IConnection;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.ISqlHelper;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.model.Connection;

/**
 * @author Winston Prakash Version 1.0
 */
public class TableStructureTest {

	public void dumpTableStructure(IConnectionConfig dbConfig)
			throws SQLException, ClassNotFoundException {
		IDatabaseInfo dbInfo = new DatabaseInfo(dbConfig);
		IConnection database = new Connection(dbInfo);
		List<ISchema> schemas = database.getSchemaList();
		ISqlHelper sqlHelper = dbConfig.getConnectionType().getSqlHelper();
		for (ISchema schema : schemas) {
			System.out.println("Schema:" + schema.getName());
			List<ITable> tables = schema.getTableList();
			for (ITable table : tables) {
				System.out.println(sqlHelper.generateTableCreateStatement(table,
						true));
			}
		}
	}

	public static void main(String[] args) {
		TableStructureTest connectionTest = new TableStructureTest();
		try {
			connectionTest.dumpTableStructure(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
