package com.jfxtools.database.api;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public interface IDatabaseInfo {

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name);

	/**
	 * Get the names of the schemas in the database
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ISchema> getSchemas() throws SQLException;

	/**
	 * Check if the database has Schema support
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean hasSchemaSupport() throws SQLException;

	/**
	 * Get the Connection Connection Configuration information
	 * 
	 * @return ConnectionConfig - connection configuration
	 */
	public IConnectionConfig getConnectionConfig();

	/**
	 * Set the Connection Connection Configuration information
	 * 
	 * @param connectionConfig
	 */
	public void setConnectionConfig(IConnectionConfig connectionConfig);

	/**
	 * Get the database connection
	 * 
	 * @return the dbconnection
	 */
	public Connection getConnection();

	/**
	 * Set the database connection
	 * 
	 * @param dbconnection
	 *            , the dbconnection to set
	 */
	public void setConnection(Connection dbconnection);

	/**
	 * Get the database metadata
	 * 
	 * @return the databaseMetaData
	 */
	public DatabaseMetaData getDatabaseMetaData();

	/**
	 * Set the database metadata
	 * 
	 * @param databaseMetaData
	 *            the databaseMetaData to set
	 */
	public void setDatabaseMetaData(DatabaseMetaData databaseMetaData);

	/**
	 * Get the table types supported by the database Typical types are "TABLE",
	 * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	 * "SYNONYM"
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String[] getTableTypes() throws SQLException;

	public List<ISqlType> getSqlTypes() throws SQLException;

	/**
	 * Get the column of a particular table and schema
	 * 
	 * @param schemaName
	 *            , tableName
	 * @return list of column names
	 * @throws SQLException
	 */
	public List<IColumn> getColumns(ITable table) throws SQLException;

	/**
	 * Get the primary key columns of a particular table and schema
	 * 
	 * @param schemaName
	 *            , tableName
	 * @return list of column names
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<IColumn> getPrimaryKeyColumns(ITable table) throws SQLException;

	/**
	 * Get the primary key columns of a particular table and schema
	 * 
	 * @param schemaName
	 *            , tableName
	 * @return list of column names
	 * @throws SQLException
	 */
	public List<IColumn> getForeignKeyColumns(ITable table) throws SQLException;

	/**
	 * Get the views in the database matching the schema
	 * 
	 * @return list of tables
	 * @throws SQLException
	 */
	public List<IView> getViews(ISchema schema) throws SQLException;

	/**
	 * Get the tables in the database matching the schema
	 * 
	 * @return list of tables
	 * @throws SQLException
	 */
	public List<ITable> getTables(ISchema schema) throws SQLException;

	public void refresh() throws SQLException;

	public void disconnect() throws SQLException;

	public void refreshConnection() throws SQLException;

	public ISqlType findSqlTypeByName(String name) throws SQLException;

	/**
	 * Check if a connection exist in this database Info
	 */
	public boolean isConnected();

	/**
	 * Find the Schema object by its name
	 * 
	 * @param pkTableSchema
	 * @return
	 * @throws SQLException
	 */
	public ISchema findSchemaByName(String schemaName) throws SQLException;

	/**
	 * Get the string used to quote identifier string
	 * 
	 * @throws SQLException
	 */
	public String getIdentifierQuoteString() throws SQLException;

	public boolean isTransactionSupported() throws SQLException;

	/**
	 * Get a Unique schema/database for this database info
	 * @return string unique database/schema name
	 * @throws SQLException 
	 */
	public String getUniqueDatabaseName() throws SQLException;

}