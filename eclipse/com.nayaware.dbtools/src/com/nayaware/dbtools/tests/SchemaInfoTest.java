
package com.nayaware.dbtools.tests;

import java.sql.SQLException;
import java.util.List;

import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.Connection;

/**
 * @author Winston Prakash Version 1.0
 */
public class SchemaInfoTest {

	public void dumpSchemaInfo(IConnectionConfig dbConfig) throws SQLException {

		IDatabaseInfo databaseInfo = new DatabaseInfo(dbConfig);
		Connection database = new Connection(databaseInfo);

		if (database.hasSchemaSupport()) {
			System.out.println(database.getName() + " has schema support");
			List<ISchema> schemas = database.getSchemaList();
			for (ISchema schema : schemas) {
				System.out.println("Schema:" + schema.getName());
			}
		} else {
			System.out.println(database.getName() + " has no schema support");
		}
	}

	public static void main(String[] args) {
		SchemaInfoTest connectionTest = new SchemaInfoTest();
		try {
			connectionTest.dumpSchemaInfo(ConnectionUtils
					.createMySqlConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
