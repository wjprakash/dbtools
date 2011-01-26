
package com.nayaware.dbtools.tests;

import java.sql.SQLException;
import java.util.List;

import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.Connection;

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
