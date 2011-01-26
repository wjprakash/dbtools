
package com.mysql.driver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ISqlType;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.ITableColumnData;
import com.nayaware.dbtools.api.ITableRowData;
import com.nayaware.dbtools.api.IColumn.IForeignKeyReference;

public class MysqlSqlHelper implements ISqlHelper {

	public String generateSelectStatement(ITable table, int offset, int pageSize) {
		StringBuffer selectBuffer = new StringBuffer();
		selectBuffer.append("SELECT * FROM "); //$NON-NLS-1$
		selectBuffer.append(table.getQualifiedName());

		selectBuffer.append(" LIMIT " + pageSize); //$NON-NLS-1$
		selectBuffer.append(" OFFSET " + offset); //$NON-NLS-1$

		return selectBuffer.toString();
	}

	public String generateRowCountStatement(ITable table) {
		StringBuffer selectBuffer = new StringBuffer();
		selectBuffer.append("SELECT COUNT(*) FROM "); //$NON-NLS-1$
		selectBuffer.append(table.getQualifiedName());
		return selectBuffer.toString();
	}

	public String generateTableCreateStatement(ITable table,
			boolean generateForeignKey) {
		try {
			List<IColumn> columnList = table.getColumnList();
			StringBuffer tableCreateBuffer = new StringBuffer();
			tableCreateBuffer.append("CREATE TABLE "); //$NON-NLS-1$
			tableCreateBuffer.append(table.getQualifiedName());
			tableCreateBuffer.append(" (\n"); //$NON-NLS-1$
			List<IColumn> primaryKeys = new ArrayList<IColumn>();
			List<IColumn> foreignKeys = new ArrayList<IColumn>();
			for (IColumn column : columnList) {
				tableCreateBuffer.append("   " //$NON-NLS-1$
						+ quoteIdentifier(table.getDatabaseInfo(), column
								.getName()) + " "); //$NON-NLS-1$
				tableCreateBuffer.append(column.getType().getName());
				if (column.getSize() > 0) {
					tableCreateBuffer.append("(" + column.getSize() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if (!column.isNullAllowed()) {
					tableCreateBuffer.append(" NOT NULL "); //$NON-NLS-1$
				}
				
				if ((column.getDefaultValue() != null) && !column.getDefaultValue().trim().equals("")) {
					tableCreateBuffer.append(" DEFAULT " + column.getDefaultValue().trim()); //$NON-NLS-1$
				}

				if (isAutoIncrementSupported(column)) {
					if (column.isAutoIncrement()) {
						tableCreateBuffer.append(" AUTO_INCREMENT "); //$NON-NLS-1$
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
					tableCreateBuffer.append(",\n"); //$NON-NLS-1$
				}
			}
			if (primaryKeys.size() > 0) {
				tableCreateBuffer.append(",\n   PRIMARY KEY ("); //$NON-NLS-1$
				for (IColumn primaryKey : primaryKeys) {
					tableCreateBuffer.append(quoteIdentifier(table
							.getDatabaseInfo(), primaryKey.getName()));
					if (primaryKeys.indexOf(primaryKey) < (primaryKeys.size() - 1)) {
						tableCreateBuffer.append(","); //$NON-NLS-1$
					} else {
						tableCreateBuffer.append(")\n"); //$NON-NLS-1$
					}
				}
			}
			if (generateForeignKey) {
				if (foreignKeys.size() > 0) {
					tableCreateBuffer.append(",\n   FOREIGN KEY "); //$NON-NLS-1$
					for (IColumn foreignKey : foreignKeys) {
						tableCreateBuffer.append(" ("); //$NON-NLS-1$
						tableCreateBuffer.append(quoteIdentifier(table
								.getDatabaseInfo(), foreignKey.getName()));
						tableCreateBuffer.append(") "); //$NON-NLS-1$
						tableCreateBuffer.append(" REFRENCES "); //$NON-NLS-1$
						List<IForeignKeyReference> relationshipList = foreignKey
								.getForeignKeyReferenceList();

						for (int i = 0; i < relationshipList.size(); i++) {
							IForeignKeyReference relationship = relationshipList
									.get(i);
							if (i == 0) {
								tableCreateBuffer.append(relationship
										.getPrimaryKeyTable()
										+ "("); //$NON-NLS-1$
							}
							tableCreateBuffer.append(quoteIdentifier(table
									.getDatabaseInfo(), relationship
									.getPrimaryKeyColumn()));
							if ((relationshipList.size() > 1)
									&& (i < relationshipList.size() - 1)) {
								tableCreateBuffer.append(", "); //$NON-NLS-1$
							}
							if (i == relationshipList.size() - 1) {
								tableCreateBuffer.append(")"); //$NON-NLS-1$
							}
						}
					}
				}
			}
			tableCreateBuffer.append("\n);\n"); //$NON-NLS-1$
			return tableCreateBuffer.toString().trim();
		} catch (SQLException exc) {
			exc.printStackTrace();
			// ErrorManager.showException(exc);
		}
		return null;
	}

	private boolean isAutoIncrementSupported(IColumn column) {
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
				sb.append("ALTER TABLE "); //$NON-NLS-1$
				sb.append(table.getQualifiedName());
				sb.append("\n"); //$NON-NLS-1$
				sb.append("  ADD FOREIGN KEY "); //$NON-NLS-1$
				for (IColumn foreignKey : foreignKeys) {
					sb.append(" ("); //$NON-NLS-1$
					sb.append(quoteIdentifier(table.getDatabaseInfo(),
							foreignKey.getName()));
					sb.append(")\n"); //$NON-NLS-1$
					sb.append("     REFERENCES "); //$NON-NLS-1$
					List<IForeignKeyReference> relationshipList = foreignKey
							.getForeignKeyReferenceList();

					for (int i = 0; i < relationshipList.size(); i++) {
						IForeignKeyReference relationship = relationshipList
								.get(i);
						if (i == 0) {
							sb.append(relationship.getPrimaryKeyTable() + " ("); //$NON-NLS-1$
						}
						sb.append(quoteIdentifier(table.getDatabaseInfo(),
								relationship.getPrimaryKeyColumn()));
						if ((relationshipList.size() > 1)
								&& (i < relationshipList.size() - 1)) {
							sb.append(", "); //$NON-NLS-1$
						}
						if (i == relationshipList.size() - 1) {
							sb.append(")"); //$NON-NLS-1$
						}
					}
				}
				sb.append(";\n"); //$NON-NLS-1$
				return sb.toString().trim();
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return null;
	}

	public String generateTableRowInsertStatement(ITableRowData tableRowData) {
		StringBuffer insertBuffer = new StringBuffer();
		insertBuffer.append("INSERT INTO " //$NON-NLS-1$
				+ tableRowData.getTable().getQualifiedName() + " "); //$NON-NLS-1$
		StringBuffer columnsBuffer = new StringBuffer();
		columnsBuffer.append("( "); //$NON-NLS-1$
		StringBuffer valuesBuffer = new StringBuffer();
		valuesBuffer.append("( "); //$NON-NLS-1$
		for (int i = 0; i < tableRowData.getColumnCount(); i++) {
			ITableColumnData columnData = tableRowData.getTableColumnData(i);
			if (!columnData.isAutoIncrement() && !columnData.isReadOnly()
					&& (columnData.getValue() != null)
					&& !columnData.getValueAsString().trim().equals("")) {
				columnsBuffer.append(quoteIdentifier(tableRowData.getTable()
						.getDatabaseInfo(), columnData.getName()));
				if (i < tableRowData.getColumnCount() - 1) {
					columnsBuffer.append(","); //$NON-NLS-1$
				}
				valuesBuffer.append(getQuotedValue(columnData));

				if (i < tableRowData.getColumnCount() - 1) {
					valuesBuffer.append(","); //$NON-NLS-1$
				}
			}
		}
		columnsBuffer.append(") "); //$NON-NLS-1$
		valuesBuffer.append(") "); //$NON-NLS-1$

		insertBuffer.append(columnsBuffer.toString());
		insertBuffer.append("VALUES "); //$NON-NLS-1$
		insertBuffer.append(valuesBuffer.toString());
		insertBuffer.append(";\n"); //$NON-NLS-1$
		// if (generateCommit(tableRowData.getTable())) {
		// insertBuffer.append("COMMIT;");
		// }
		// ErrorManager.showInformationMessage(insertBuffer.toString());
		return insertBuffer.toString();
	}

	public String generateTableRowDeleteStatement(ITableRowData tableRowData) {
		StringBuffer deleteBuffer = new StringBuffer();
		deleteBuffer.append("DELETE FROM " //$NON-NLS-1$
				+ tableRowData.getTable().getQualifiedName() + " "); //$NON-NLS-1$
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
					whereClause = "WHERE "; //$NON-NLS-1$
					firstColumn++;
				} else {
					whereClause = " AND "; //$NON-NLS-1$
				}
				deleteBuffer.append(whereClause);
				deleteBuffer.append(quoteIdentifier(tableRowData.getTable()
						.getDatabaseInfo(), tableColumnData.getName()));
				if (tableColumnData.getValueAsString() == null) {
					deleteBuffer.append(" is null "); //$NON-NLS-1$
				} else {
					deleteBuffer.append(" = "); //$NON-NLS-1$
					deleteBuffer.append(getQuotedValue(tableColumnData));
				}
			}
		}
		deleteBuffer.append(";\n"); //$NON-NLS-1$
		// if (generateCommit(tableRowData.getTable())) {
		// deleteBuffer.append("COMMIT;");
		// }
		return deleteBuffer.toString();
	}

	public String generateTableRowUpdateStatement(ITableRowData currentRowData,
			ITableRowData updateRowData) {
		StringBuffer updateBuffer = new StringBuffer();
		updateBuffer.append("UPDATE " //$NON-NLS-1$
				+ currentRowData.getTable().getQualifiedName() + " "); //$NON-NLS-1$
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
					updateBuffer.append("SET "); //$NON-NLS-1$
				} else {
					updateBuffer.append(", "); //$NON-NLS-1$
				}

				updateBuffer.append(quoteIdentifier(currentRowData.getTable()
						.getDatabaseInfo(), columnData.getName()));
				updateBuffer.append(" = "); //$NON-NLS-1$
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
					whereClause = " WHERE "; //$NON-NLS-1$
				} else {
					whereClause = " AND "; //$NON-NLS-1$
				}
				updateBuffer.append(whereClause);
				updateBuffer.append(quoteIdentifier(currentRowData.getTable()
						.getDatabaseInfo(), tableColumnData.getName()));
				if (tableColumnData.getValueAsString() == null) {
					updateBuffer.append(" is null "); //$NON-NLS-1$
				} else {
					updateBuffer.append(" = "); //$NON-NLS-1$
					updateBuffer.append(getQuotedValue(tableColumnData));
				}
			}
		}
		updateBuffer.append(";\n"); //$NON-NLS-1$
		// if (generateCommit(currentRowData.getTable())) {
		// updateBuffer.append("COMMIT;");
		// }
		// ErrorManager.showInformationMessage(updateBuffer.toString());
		return updateBuffer.toString();
	}

	public String generateTableDropStatement(ITable table) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP TABLE "); //$NON-NLS-1$
		sb.append(table.getQualifiedName());
		sb.append(";\n"); //$NON-NLS-1$
		return sb.toString().trim();
	}

	public String generateCreateDatabaseStatement(String newDbName,
			IDatabaseInfo dbInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE DATABASE "); //$NON-NLS-1$
		sb.append(newDbName);
		return sb.toString();
	}

	public String generateDropDatabaseStatement(ISchema schema) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP DATABASE "); //$NON-NLS-1$
		sb.append(schema.getName());
		return sb.toString();
	}

	public String generateTableTruncateStatement(ITable table) {
		StringBuffer sb = new StringBuffer();
		// if (table.getSchema() != null) {
		// sb.append("use " + table.getSchema().getName() + ";\n");
		// }
		sb.append("TRUNCATE TABLE "); //$NON-NLS-1$
		sb.append(table.getQualifiedName());
		sb.append(";\n"); //$NON-NLS-1$
		return sb.toString().trim();
	}

	public String generateTableCopyStatement(ITable table,
			String duplicateTableName) {
		StringBuffer sb = new StringBuffer();
		if (table.getSchema() != null) {
			sb.append("use " + table.getSchema().getName() + ";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("CREATe TABLE "); //$NON-NLS-1$
		sb.append(duplicateTableName);
		sb.append(" SELECT * FROM "); //$NON-NLS-1$
		sb.append(table.getName());
		sb.append(";\n"); //$NON-NLS-1$
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

	private String getQuotedValue(ITableColumnData tableColumnData) {
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
