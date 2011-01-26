package com.jfxtools.database.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.ISqlHelper;
import com.jfxtools.database.api.ISqlType;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.ITableColumnData;
import com.jfxtools.database.api.ITableRowData;
import com.jfxtools.database.api.IColumn.IForeignKeyReference;

public class MysqlSqlHelper implements ISqlHelper {

	public String generateSelectStatement(ITable table, int offset, int pageSize) throws SQLException {
		StringBuffer selectBuffer = new StringBuffer();
		selectBuffer.append("SELECT * FROM "); 
		selectBuffer.append(table.getQualifiedName());

		selectBuffer.append(" LIMIT " + pageSize); 
		selectBuffer.append(" OFFSET " + offset); 

		return selectBuffer.toString();
	}

	public String generateRowCountStatement(ITable table) throws SQLException {
		StringBuffer selectBuffer = new StringBuffer();
		selectBuffer.append("SELECT COUNT(*) FROM "); 
		selectBuffer.append(table.getQualifiedName());
		return selectBuffer.toString();
	}

	public String generateTableCreateStatement(ITable table,
			boolean generateForeignKey) {
		try {
			List<IColumn> columnList = table.getColumnList();
			StringBuffer tableCreateBuffer = new StringBuffer();
			tableCreateBuffer.append("CREATE TABLE "); 
			tableCreateBuffer.append(table.getQualifiedName());
			tableCreateBuffer.append(" (\n"); 
			List<IColumn> primaryKeys = new ArrayList<IColumn>();
			List<IColumn> foreignKeys = new ArrayList<IColumn>();
			for (IColumn column : columnList) {
				tableCreateBuffer.append("   " 
						+ quoteIdentifier(table.getDatabaseInfo(), column
								.getName()) + " "); 
				tableCreateBuffer.append(column.getType().getName());
				if (column.getSize() > 0) {
					tableCreateBuffer.append("(" + column.getSize() + ")");  //$NON-NLS-2$
				}
				if (!column.isNullAllowed()) {
					tableCreateBuffer.append(" NOT NULL "); 
				}
				
				if ((column.getDefaultValue() != null) && !column.getDefaultValue().trim().equals("")) {
					tableCreateBuffer.append(" DEFAULT " + column.getDefaultValue().trim()); 
				}

				if (isAutoIncrementSupported(column)) {
					if (column.isAutoIncrement()) {
						tableCreateBuffer.append(" AUTO_INCREMENT "); 
					}
				}
				// else if (isDefaultSupported(column)){
				//					
				// }
				if (column.isPrimaryKey()) {
					primaryKeys.add(column);
				}
				if (column.isForeignKey()) {
					foreignKeys.add(column);
				}
				if (columnList.indexOf(column) < (columnList.size() - 1)) {
					tableCreateBuffer.append(",\n"); 
				}
			}
			if (primaryKeys.size() > 0) {
				tableCreateBuffer.append(",\n   PRIMARY KEY ("); 
				for (IColumn primaryKey : primaryKeys) {
					tableCreateBuffer.append(quoteIdentifier(table
							.getDatabaseInfo(), primaryKey.getName()));
					if (primaryKeys.indexOf(primaryKey) < (primaryKeys.size() - 1)) {
						tableCreateBuffer.append(","); 
					} else {
						tableCreateBuffer.append(")\n"); 
					}
				}
			}
			if (generateForeignKey) {
				if (foreignKeys.size() > 0) {
					tableCreateBuffer.append(",\n   FOREIGN KEY "); 
					for (IColumn foreignKey : foreignKeys) {
						tableCreateBuffer.append(" ("); 
						tableCreateBuffer.append(quoteIdentifier(table
								.getDatabaseInfo(), foreignKey.getName()));
						tableCreateBuffer.append(") "); 
						tableCreateBuffer.append(" REFRENCES "); 
						List<IForeignKeyReference> relationshipList = foreignKey
								.getForeignKeyReferenceList();

						for (int i = 0; i < relationshipList.size(); i++) {
							IForeignKeyReference relationship = relationshipList
									.get(i);
							if (i == 0) {
								tableCreateBuffer.append(relationship
										.getPrimaryKeyTable()
										+ "("); 
							}
							tableCreateBuffer.append(quoteIdentifier(table
									.getDatabaseInfo(), relationship
									.getPrimaryKeyColumn()));
							if ((relationshipList.size() > 1)
									&& (i < relationshipList.size() - 1)) {
								tableCreateBuffer.append(", "); 
							}
							if (i == relationshipList.size() - 1) {
								tableCreateBuffer.append(")"); 
							}
						}
					}
				}
			}
			tableCreateBuffer.append("\n);\n"); 
			return tableCreateBuffer.toString().trim();
		} catch (SQLException exc) {
			exc.printStackTrace();
			// ErrorManager.showException(exc);
		}
		return null;
	}

	private boolean isAutoIncrementSupported(IColumn column) throws SQLException {
		ISqlType sqlType = column.getType();
		return sqlType.isAutoIncrementable();
	}

	public String generateForeignKeyAddStatement(ITable table) {
		try {
			List<IColumn> columnList = table.getColumnList();

			List<IColumn> foreignKeys = new ArrayList<IColumn>();
			for (IColumn column : columnList) {
				if (column.isForeignKey()) {
					foreignKeys.add(column);
				}
			}

			if (foreignKeys.size() > 0) {
				StringBuffer sb = new StringBuffer();
				sb.append("ALTER TABLE "); 
				sb.append(table.getQualifiedName());
				sb.append("\n"); 
				sb.append("  ADD FOREIGN KEY "); 
				for (IColumn foreignKey : foreignKeys) {
					sb.append(" ("); 
					sb.append(quoteIdentifier(table.getDatabaseInfo(),
							foreignKey.getName()));
					sb.append(")\n"); 
					sb.append("     REFERENCES "); 
					List<IForeignKeyReference> relationshipList = foreignKey
							.getForeignKeyReferenceList();

					for (int i = 0; i < relationshipList.size(); i++) {
						IForeignKeyReference relationship = relationshipList
								.get(i);
						if (i == 0) {
							sb.append(relationship.getPrimaryKeyTable() + " ("); 
						}
						sb.append(quoteIdentifier(table.getDatabaseInfo(),
								relationship.getPrimaryKeyColumn()));
						if ((relationshipList.size() > 1)
								&& (i < relationshipList.size() - 1)) {
							sb.append(", "); 
						}
						if (i == relationshipList.size() - 1) {
							sb.append(")"); 
						}
					}
				}
				sb.append(";\n"); 
				return sb.toString().trim();
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return null;
	}

	public String generateTableRowInsertStatement(ITableRowData tableRowData) throws SQLException {
		StringBuffer insertBuffer = new StringBuffer();
		insertBuffer.append("INSERT INTO " 
				+ tableRowData.getTable().getQualifiedName() + " "); 
		StringBuffer columnsBuffer = new StringBuffer();
		columnsBuffer.append("( "); 
		StringBuffer valuesBuffer = new StringBuffer();
		valuesBuffer.append("( "); 
		for (int i = 0; i < tableRowData.getColumnCount(); i++) {
			ITableColumnData columnData = tableRowData.getTableColumnData(i);
			if (!columnData.isAutoIncrement() && !columnData.isReadOnly()
					&& (columnData.getValue() != null)
					&& !columnData.getValueAsString().trim().equals("")) {
				columnsBuffer.append(quoteIdentifier(tableRowData.getTable()
						.getDatabaseInfo(), columnData.getName()));
				if (i < tableRowData.getColumnCount() - 1) {
					columnsBuffer.append(","); 
				}
				valuesBuffer.append(getQuotedValue(columnData));

				if (i < tableRowData.getColumnCount() - 1) {
					valuesBuffer.append(","); 
				}
			}
		}
		columnsBuffer.append(") "); 
		valuesBuffer.append(") "); 

		insertBuffer.append(columnsBuffer.toString());
		insertBuffer.append("VALUES "); 
		insertBuffer.append(valuesBuffer.toString());
		insertBuffer.append(";\n"); 
		// if (generateCommit(tableRowData.getTable())) {
		// insertBuffer.append("COMMIT;");
		// }
		// ErrorManager.showInformationMessage(insertBuffer.toString());
		return insertBuffer.toString();
	}

	public String generateTableRowDeleteStatement(ITableRowData tableRowData) throws SQLException {
		StringBuffer deleteBuffer = new StringBuffer();
		deleteBuffer.append("DELETE FROM " 
				+ tableRowData.getTable().getQualifiedName() + " "); 
		// Find unique (Primary Key) column to find the row
		List<ITableColumnData> uniqueColumns = new ArrayList<ITableColumnData>();
		for (int i = 0; i < tableRowData.getColumnCount(); i++) {
			IColumn column = tableRowData.getTableColumnData(i).getColumn();
			if ((column != null) && column.isPrimaryKey()) {
				uniqueColumns.add(tableRowData.getTableColumnData(i));
			}
		}
		if (uniqueColumns.isEmpty()) {
			// Treat all columns as Unique columns, result is unsure
			uniqueColumns = tableRowData.getTableColumnData();
		}

		int firstColumn = 0;
		for (ITableColumnData tableColumnData : uniqueColumns) {
			if (tableColumnData.getColumn().getType().getSearchable() == ISqlType.SEARCHABLE) {
				String whereClause;
				if (firstColumn == 0) {
					whereClause = "WHERE "; 
					firstColumn++;
				} else {
					whereClause = " AND "; 
				}
				deleteBuffer.append(whereClause);
				deleteBuffer.append(quoteIdentifier(tableRowData.getTable()
						.getDatabaseInfo(), tableColumnData.getName()));
				if (tableColumnData.getValueAsString() == null) {
					deleteBuffer.append(" is null "); 
				} else {
					deleteBuffer.append(" = "); 
					deleteBuffer.append(getQuotedValue(tableColumnData));
				}
			}
		}
		deleteBuffer.append(";\n"); 
		// if (generateCommit(tableRowData.getTable())) {
		// deleteBuffer.append("COMMIT;");
		// }
		return deleteBuffer.toString();
	}

	public String generateTableRowUpdateStatement(ITableRowData currentRowData,
			ITableRowData updateRowData) throws SQLException {
		StringBuffer updateBuffer = new StringBuffer();
		updateBuffer.append("UPDATE " 
				+ currentRowData.getTable().getQualifiedName() + " "); 
		// Find unique (Primary Key) column to find the row
		List<ITableColumnData> uniqueColumns = new ArrayList<ITableColumnData>();
		for (int i = 0; i < currentRowData.getColumnCount(); i++) {
			ITableColumnData tableColumnData = currentRowData
					.getTableColumnData(i);
			IColumn column = tableColumnData.getColumn();
			if ((column != null) && column.isPrimaryKey()) {
				uniqueColumns.add(tableColumnData);
			}
		}

		int updatedColumnCount = 0;
		for (int i = 0; i < updateRowData.getColumnCount(); i++) {
			ITableColumnData columnData = updateRowData.getTableColumnData(i);
			if (!columnData.isAutoIncrement() && !columnData.isReadOnly()
					&& (columnData.getValue() != null)
					&& !columnData.getValueAsString().trim().equals("")) {

				if (updatedColumnCount == 0) {
					updateBuffer.append("SET "); 
				} else {
					updateBuffer.append(", "); 
				}

				updateBuffer.append(quoteIdentifier(currentRowData.getTable()
						.getDatabaseInfo(), columnData.getName()));
				updateBuffer.append(" = "); 
				updateBuffer.append(getQuotedValue(columnData));
				updatedColumnCount++;
			}
		}
		if (uniqueColumns.isEmpty()) {
			// Treat all columns as Unique columns, result is unsure
			uniqueColumns = currentRowData.getTableColumnData();
		}

		int firstColumn = 0;
		for (ITableColumnData tableColumnData : uniqueColumns) {
			if (tableColumnData.getColumn().getType().getSearchable() == ISqlType.SEARCHABLE) {
				String whereClause;
				if (firstColumn == 0) {
					whereClause = " WHERE "; 
				} else {
					whereClause = " AND "; 
				}
				updateBuffer.append(whereClause);
				updateBuffer.append(quoteIdentifier(currentRowData.getTable()
						.getDatabaseInfo(), tableColumnData.getName()));
				if (tableColumnData.getValueAsString() == null) {
					updateBuffer.append(" is null "); 
				} else {
					updateBuffer.append(" = "); 
					updateBuffer.append(getQuotedValue(tableColumnData));
				}
			}
		}
		updateBuffer.append(";\n"); 
		// if (generateCommit(currentRowData.getTable())) {
		// updateBuffer.append("COMMIT;");
		// }
		// ErrorManager.showInformationMessage(updateBuffer.toString());
		return updateBuffer.toString();
	}

	public String generateTableDropStatement(ITable table) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP TABLE "); 
		sb.append(table.getQualifiedName());
		sb.append(";\n"); 
		return sb.toString().trim();
	}

	public String generateCreateDatabaseStatement(String newDbName,
			IDatabaseInfo dbInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE DATABASE "); 
		sb.append(newDbName);
		return sb.toString();
	}

	public String generateDropDatabaseStatement(ISchema schema) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP DATABASE "); 
		sb.append(schema.getName());
		return sb.toString();
	}

	public String generateTableTruncateStatement(ITable table) throws SQLException {
		StringBuffer sb = new StringBuffer();
		// if (table.getSchema() != null) {
		// sb.append("use " + table.getSchema().getName() + ";\n");
		// }
		sb.append("TRUNCATE TABLE "); 
		sb.append(table.getQualifiedName());
		sb.append(";\n"); 
		return sb.toString().trim();
	}

	public String generateTableCopyStatement(ITable table,
			String duplicateTableName) {
		StringBuffer sb = new StringBuffer();
		if (table.getSchema() != null) {
			sb.append("use " + table.getSchema().getName() + ";\n");  //$NON-NLS-2$
		}
		sb.append("CREATe TABLE "); 
		sb.append(duplicateTableName);
		sb.append(" SELECT * FROM "); 
		sb.append(table.getName());
		sb.append(";\n"); 
		return sb.toString().trim();
	}

	private String quoteIdentifier(IDatabaseInfo databaseInfo, String identifier) {
		String quoteString = null;
		try {
			quoteString = databaseInfo.getIdentifierQuoteString();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		if (quoteString != null) {
			return quoteString + identifier + quoteString;
		} else {
			return identifier;
		}
	}

	private String getQuotedValue(ITableColumnData tableColumnData) throws SQLException {
		if ((tableColumnData.getValueAsString() != null)
				&& !tableColumnData.getValueAsString().trim().equals("")) {
			ISqlType sqltype = tableColumnData.getColumn().getType();
			String prefixQuoteString = sqltype.getLiteralPrefix();
			String suffixQuoteString = sqltype.getLiteralSuffix();

			if ((prefixQuoteString != null) && (suffixQuoteString != null)) {
				return prefixQuoteString + tableColumnData.getValueAsString()
						+ suffixQuoteString;
			}
		}
		return tableColumnData.getValueAsString();

	}
}
