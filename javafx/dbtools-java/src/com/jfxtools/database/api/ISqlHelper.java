package com.jfxtools.database.api;

import java.sql.SQLException;


public interface ISqlHelper {

	public String generateSelectStatement(ITable table, int currentRow, int pageSize) throws SQLException;

	public String generateRowCountStatement(ITable table) throws SQLException;

	public String generateTableCreateStatement(ITable table,
			boolean generateForeignKey);

	public String generateForeignKeyAddStatement(ITable table);

	public String generateTableRowInsertStatement(ITableRowData tableRowData) throws SQLException;

	public String generateTableRowDeleteStatement(ITableRowData tableRowData) throws SQLException;

	public String generateTableRowUpdateStatement(ITableRowData currentRowData,
			ITableRowData updateRowData) throws SQLException;

	public String generateTableDropStatement(ITable table) throws SQLException;

	public String generateCreateDatabaseStatement(String newDbName,
			IDatabaseInfo dbInfo);

	public String generateDropDatabaseStatement(ISchema schema);

	public String generateTableTruncateStatement(ITable table) throws SQLException;

	public String generateTableCopyStatement(ITable table,
			String duplicateTableName);
}