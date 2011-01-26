
package com.nayaware.dbtools.tests;

import java.sql.SQLException;
import java.util.List;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.IColumn.IForeignKeyReference;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.Connection;

/**
 * @author Winston Prakash Version 1.0
 */
public class ForeignKeyTest {

	public void dumpForeignKeys(IConnectionConfig dbConfig) throws SQLException {
		IDatabaseInfo dbInfo = new DatabaseInfo(dbConfig);
		IConnection database = new Connection(dbInfo);
		List<ISchema> schemas = database.getSchemaList();
		for (ISchema schema : schemas) {
			System.out.println("Schema:" + schema.getName());
			List<ITable> tables = schema.getTableList();
			for (ITable table : tables) {
				System.out.println("    Table:" + table.getName());
				List<IColumn> fkColumns = table.getForeignKeyColumns();
				for (IColumn column : fkColumns) {

					List<IForeignKeyReference> relationshipList = column
							.getForeignKeyReferenceList();
					for (IForeignKeyReference relationship : relationshipList) {
						System.out.println("     Foreign Key Column:"
								+ column.getName());
						System.out.println("     -> "
								+ relationship.getPrimaryKeyTable() + "."
								+ relationship.getPrimaryKeyColumn() + "("
								+ relationship.getPrimaryKeyName() + ")");
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		ForeignKeyTest connectionTest = new ForeignKeyTest();
		try {
			// connectionTest.dumpForeignKeys(createMySqlConnectionConfig());
			connectionTest.dumpForeignKeys(ConnectionUtils
					.createDerbyConnectionConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
