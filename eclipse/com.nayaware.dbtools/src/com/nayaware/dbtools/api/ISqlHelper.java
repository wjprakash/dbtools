
package com.nayaware.dbtools.api;


public interface ISqlHelper {

	public String generateSelectStatement(ITable table, int currentRow, int pageSize);

	public String generateRowCountStatement(ITable table);

	public String generateTableCreateStatement(ITable table,
			boolean generateForeignKey);

	public String generateForeignKeyAddStatement(ITable table);

	public String generateTableRowInsertStatement(ITableRowData tableRowData);

	public String generateTableRowDeleteStatement(ITableRowData tableRowData);

	public String generateTableRowUpdateStatement(ITableRowData currentRowData,
			ITableRowData updateRowData);

	public String generateTableDropStatement(ITable table);

	public String generateCreateDatabaseStatement(String newDbName,
			IDatabaseInfo dbInfo);

	public String generateDropDatabaseStatement(ISchema schema);

	public String generateTableTruncateStatement(ITable table);

	public String generateTableCopyStatement(ITable table,
			String duplicateTableName);
}