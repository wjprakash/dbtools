package com.jfxtools.database.tests;

import java.sql.SQLException;
import java.util.List;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.api.IConnection;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.IColumn.IForeignKeyReference;
import com.jfxtools.database.model.Connection;

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
