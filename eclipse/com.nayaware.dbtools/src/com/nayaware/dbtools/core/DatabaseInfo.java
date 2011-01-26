
package com.nayaware.dbtools.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ISqlType;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.IView;
import com.nayaware.dbtools.model.Column;
import com.nayaware.dbtools.model.Schema;
import com.nayaware.dbtools.model.SqlType;
import com.nayaware.dbtools.model.Table;
import com.nayaware.dbtools.model.View;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Connection Meta Data information
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DatabaseInfo implements IDatabaseInfo {

	private static final String DATABASE_BASE_NAME = "Database"; //$NON-NLS-1$
	
	private String name;

	private IConnectionConfig connectionConfig;

	private Connection connection;

	private DatabaseMetaData databaseMetaData;

	private List<ISqlType> typeList;
	private List<ISchema> schemaList;

	private Map<String, DatabaseMetaData> mySqlSchemaConnections = new HashMap<String, DatabaseMetaData>();

	public DatabaseInfo(IConnectionConfig conConfig) {
		connectionConfig = conConfig;
		name = conConfig.getDisplayName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public synchronized List<ISchema> getSchemas() throws SQLException {
		if (databaseMetaData == null) {
			refreshMetaData();
		}
		if (schemaList == null) {
			schemaList = new ArrayList<ISchema>();
			ResultSet schemaResultSet = null;
			try {
				// Hack for MySQL, which interchangeably uses database and
				// schema
				if (connectionConfig.getDbType() == IConnectionType.MYSQL
						&& databaseMetaData.getDatabaseMajorVersion() >= 5) {
					String s = "SELECT SCHEMA_NAME AS TABLE_SCHEM FROM information_schema.SCHEMATA"; //$NON-NLS-1$
					schemaResultSet = connection.createStatement()
							.executeQuery(s);
				} else {
					schemaResultSet = databaseMetaData.getSchemas();
				}

				while (schemaResultSet.next()) {
					schemaList.add(new Schema(this, schemaResultSet
							.getString("TABLE_SCHEM"))); //$NON-NLS-1$
				}
			} finally {
				schemaResultSet.close();
			}
		}
		return schemaList;
	}

	public synchronized boolean hasSchemaSupport() throws SQLException {
		if (databaseMetaData == null) {
			refreshMetaData();
		}
		// Hack for MySQL, which interchangeably uses database and schema
		if (connectionConfig.getDbType() == IConnectionType.MYSQL
				&& databaseMetaData.getDatabaseMajorVersion() >= 5) {
			return true;
		} else {
			return databaseMetaData.supportsSchemasInTableDefinitions();
		}
	}

	public IConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	public void setConnectionConfig(IConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection dbconnection) {
		this.connection = dbconnection;
	}

	public void disconnect() throws SQLException {
		connection.close();
		connection = null;
	}

	public DatabaseMetaData getDatabaseMetaData() {
		return databaseMetaData;
	}

	public void setDatabaseMetaData(DatabaseMetaData databaseMetaData) {
		this.databaseMetaData = databaseMetaData;
	}

	public synchronized String[] getTableTypes() throws SQLException {
		if (databaseMetaData == null) {
			refreshMetaData();
		}

		List<String> tableTypeList = new ArrayList<String>();
		ResultSet tableTypeRs = databaseMetaData.getTableTypes();
		try {
			while (tableTypeRs.next()) {
				tableTypeList.add(tableTypeRs.getString("TABLE_TYPE").trim()); //$NON-NLS-1$
			}

			switch (connectionConfig.getDbType()) {
			case IConnectionType.ORACLE:
				tableTypeList.add("SEQUENCE"); //$NON-NLS-1$
				tableTypeList.add("FUNCTION"); //$NON-NLS-1$
				break;
			default:
				break;
			}
		} finally {
			tableTypeRs.close();
		}
		return tableTypeList.toArray(new String[tableTypeList.size()]);
	}

	public synchronized List<ISqlType> getSqlTypes() throws SQLException {
		if (typeList == null) {
			if (databaseMetaData == null) {
				refreshMetaData();
			}
			typeList = new ArrayList<ISqlType>();
			ResultSet rs = databaseMetaData.getTypeInfo();
			try {
				while (rs.next()) {
					String typeName = rs.getString("TYPE_NAME"); //$NON-NLS-1$
					boolean autoInc = rs.getBoolean("AUTO_INCREMENT"); //$NON-NLS-1$
					int nullable = rs.getInt("NULLABLE"); //$NON-NLS-1$
					String literalPrefix = rs.getString("LITERAL_PREFIX"); //$NON-NLS-1$
					String literalSuffix = rs.getString("LITERAL_SUFFIX"); //$NON-NLS-1$
					int searchable = rs.getInt("SEARCHABLE"); //$NON-NLS-1$

					ISqlType sqlType = new SqlType(this, typeName);

					if (nullable == DatabaseMetaData.columnNoNulls) { 
						sqlType.setNullAllowed(false);
					}
					sqlType.setAutoIncrementable(autoInc);
					sqlType.setLiteralPrefix(literalPrefix);
					sqlType.setLiteralSuffix(literalSuffix);
					sqlType.setSearchable(searchable);

					typeList.add(sqlType);
				}
				typeList.add(SqlType.UNSUPPORTED_SQL_TYPE);
			} finally {
				rs.close();
			}
		}
		return typeList;
	}

	public synchronized List<IColumn> getColumns(ITable table)
			throws SQLException {
		if (databaseMetaData == null) {
			refreshMetaData();
		}
		String schemaPattern = "%"; //$NON-NLS-1$
		String tablePattern = "%"; //$NON-NLS-1$

		if (table != null) {
			tablePattern = table.getName();
			if (table.getSchema() != null) {
				schemaPattern = table.getSchema().getName();
			}
		}
		DatabaseMetaData dbMetaData = null;
		// Hack for MySQL, which interchangeably uses database and schema
		if (connectionConfig.getDbType() == IConnectionType.MYSQL
				&& databaseMetaData.getDatabaseMajorVersion() >= 5) {
			dbMetaData = getMySqlSchemaMetadata(table.getSchema().getName());
		} else {
			dbMetaData = databaseMetaData;
		}

		// Ensure primary keys and foreign keys are initialized first

		ResultSet rs = dbMetaData.getColumns(null, schemaPattern, tablePattern,
				"%"); //$NON-NLS-1$
		List<IColumn> columnList = new ArrayList<IColumn>();
		try {
			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
				ISqlType sqlType = findSqlTypeByName(rs.getString("TYPE_NAME")); //$NON-NLS-1$
				int size = rs.getInt("COLUMN_SIZE"); //$NON-NLS-1$
				int decDigits = rs.getInt("DECIMAL_DIGITS"); //$NON-NLS-1$
				int nullable = rs.getInt("NULLABLE"); //$NON-NLS-1$
				String comment = rs.getString("REMARKS"); //$NON-NLS-1$
				String defaultValue = rs.getString("COLUMN_DEF"); //$NON-NLS-1$

				IColumn column = createColumn(table, name);
				column.setSqlType(sqlType);
				column.setSize(size);
				column.setDecimalDigits(decDigits);

				if (nullable == DatabaseMetaData.columnNoNulls) { 
					column.setNullAllowed(false);
				}
				column.setComment(comment);
				column.setDefaultValue(defaultValue);

				columnList.add(column);
			}
		} finally {
			rs.close();
		}
		return columnList;
	}

	private IColumn createColumn(ITable table, String name) throws SQLException {
		// SQLite JDBC driver (sqlitejdbc-v054) doesn't support fetching foreign
		// keys
		if ((connectionConfig.getDbType() != IConnectionType.SQLITE)) {
			List<IColumn> pkColumns = table.getPrimaryKeyColumns();

			for (IColumn pkColumn : pkColumns) {
				if (pkColumn.getName().toUpperCase().trim().equals(
						name.toUpperCase().trim())) {
					return pkColumn;
				}
			}
			List<IColumn> fkColumns = table.getForeignKeyColumns();

			for (IColumn fkColumn : fkColumns) {
				if (fkColumn.getName().toUpperCase().trim().equals(
						name.toUpperCase().trim())) {
					return fkColumn;
				}
			}
		}
		return new Column(this, table, name);
	}

	public synchronized List<IColumn> getPrimaryKeyColumns(ITable table)
			throws SQLException {
		if (databaseMetaData == null) {
			refreshMetaData();
		}
		String schemaPattern = "%"; //$NON-NLS-1$
		String tablePattern = "%"; //$NON-NLS-1$

		if (table != null) {
			tablePattern = table.getName();
			if (table.getSchema() != null) {
				schemaPattern = table.getSchema().getName();
			}
		}
		DatabaseMetaData dbMetaData = null;
		// Hack for MySQL, which interchangeably uses database and schema
		if (connectionConfig.getDbType() == IConnectionType.MYSQL
				&& databaseMetaData.getDatabaseMajorVersion() >= 5) {
			dbMetaData = getMySqlSchemaMetadata(table.getSchema().getName());
		} else {
			dbMetaData = databaseMetaData;
		}

		ResultSet rs = dbMetaData.getPrimaryKeys(null, schemaPattern,
				tablePattern);
		List<IColumn> columnList = new ArrayList<IColumn>();
		try {
			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
				String pkName = rs.getString("PK_NAME"); //$NON-NLS-1$
				short keySeq = rs.getShort("KEY_SEQ"); //$NON-NLS-1$

				IColumn column = new Column(this, table, name);
				column.setPrimaryKeyFlag(true);
				column.setPrimaryKeyName(pkName);
				column.setPrimaryKeySequence(keySeq);

				columnList.add(column);
			}
		} finally {
			rs.close();
		}
		return columnList;
	}

	public synchronized List<IColumn> getForeignKeyColumns(ITable fkTable)
			throws SQLException {
		if (databaseMetaData == null) {
			refreshMetaData();
		}
		String schemaPattern = "%"; //$NON-NLS-1$
		String tablePattern = "%"; //$NON-NLS-1$

		if (fkTable != null) {
			tablePattern = fkTable.getName();
			if (fkTable.getSchema() != null) {
				schemaPattern = fkTable.getSchema().getName();
			}
		}

		DatabaseMetaData dbMetaData = null;
		if (connectionConfig.getDbType() == IConnectionType.MYSQL
				&& databaseMetaData.getDatabaseMajorVersion() >= 5) {
			dbMetaData = getMySqlSchemaMetadata(fkTable.getSchema().getName());
		} else {
			dbMetaData = databaseMetaData;
		}

		ResultSet rs;

		rs = dbMetaData.getImportedKeys(null, schemaPattern, tablePattern);

		List<IColumn> columnList = new ArrayList<IColumn>();
		try {
			while (rs.next()) {
				String name = rs.getString("FKCOLUMN_NAME"); //$NON-NLS-1$
				String fkName = rs.getString("FK_NAME"); //$NON-NLS-1$
				short keySeq = rs.getShort("KEY_SEQ"); //$NON-NLS-1$
				String pkTableSchema = rs.getString("PKTABLE_SCHEM"); //$NON-NLS-1$
				String pkTableName = rs.getString("PKTABLE_NAME"); //$NON-NLS-1$
				String pkColumnName = rs.getString("PKCOLUMN_NAME"); //$NON-NLS-1$
				String pkName = rs.getString("PK_NAME"); //$NON-NLS-1$

				IColumn fkColumn = new Column(this, fkTable, name);
				fkColumn.setForeignKeyFlag(true);
				fkColumn.setForeignKeyName(fkName);
				fkColumn.setForeignKeySequence(keySeq);

				fkColumn.addForeignKeyReference(new Column.ForeignKeyReference(
						pkTableSchema, pkTableName, pkColumnName, pkName));
				columnList.add(fkColumn);
			}
		} finally {
			rs.close();
		}
		return columnList;
	}

	private DatabaseMetaData getMySqlSchemaMetadata(String schema)
			throws SQLException {
		if (mySqlSchemaConnections.containsKey(schema)) {
			return mySqlSchemaConnections.get(schema);
		} else {
			String url = connectionConfig.getUrl().trim();
			String name = connectionConfig.getName();
			String username = connectionConfig.getUsername();
			String password = connectionConfig.getPassword();
			int dbType = connectionConfig.getDbType();
			if (url.endsWith("/")) { //$NON-NLS-1$
				url += schema;
			} else {
				url += ("/" + schema); //$NON-NLS-1$
			}
			IConnectionConfig newConnectionConfig = new ConnectionConfig(name,
					url, dbType, username, password);
			ConnectionManager conManager = ConnectionManager.getInstance();
			Connection connection = conManager
					.createConnection(newConnectionConfig);
			DatabaseMetaData dbMetadata = connection.getMetaData();
			mySqlSchemaConnections.put(schema, dbMetadata);
			return dbMetadata;
		}
	}

	public List<IView> getViews(ISchema schema) throws SQLException {
		ResultSet rs = getTableInfo(schema, "VIEW"); //$NON-NLS-1$
		List<IView> tableList = new ArrayList<IView>();
		while (rs.next()) {
			tableList.add(new View(this, schema, rs.getString("TABLE_NAME"))); //$NON-NLS-1$
		}
		return tableList;
	}

	public List<ITable> getTables(ISchema schema) throws SQLException {
		ResultSet rs = getTableInfo(schema, "TABLE"); //$NON-NLS-1$
		List<ITable> tableList = new ArrayList<ITable>();
		while (rs.next()) {
			tableList.add(new Table(this, schema, rs.getString("TABLE_NAME"))); //$NON-NLS-1$
		}
		return tableList;
	}

	private synchronized ResultSet getTableInfo(ISchema schema, String type)
			throws SQLException {
		String[] tableTypes = { type };

		String schemaPattern = "%"; //$NON-NLS-1$
		if (schema != null) {
			schemaPattern = schema.getName();
		}

		if (databaseMetaData == null) {
			refreshMetaData();
		}

		DatabaseMetaData dbMetaData = null;
		// Hack for MySQL, which interchangeably uses database and schema
		if (connectionConfig.getDbType() == IConnectionType.MYSQL
				&& databaseMetaData.getDatabaseMajorVersion() >= 5) {
			dbMetaData = getMySqlSchemaMetadata(schema.getName());
		} else {
			dbMetaData = databaseMetaData;
		}

		ResultSet rs = dbMetaData.getTables(null, schemaPattern, "%", //$NON-NLS-1$
				tableTypes);

		return rs;
	}

	public void refresh() throws SQLException {
		refreshMetaData();
		// Remove the caches so that it will be fetched again
		typeList = null;
		schemaList = null;
	}

	private synchronized void refreshMetaData() throws SQLException {
		if (connection == null) {
			refreshConnection();
		}
		databaseMetaData = connection.getMetaData();
	}

	public void refreshConnection() throws SQLException {
		ConnectionManager conManager = ConnectionManager.getInstance();
		connection = conManager.createConnection(connectionConfig);
	}

	public ISchema findSchemaByName(String name) throws SQLException {
		for (ISchema schema : getSchemas()) {
			if (schema.getName().toUpperCase().trim().equals(
					name.toUpperCase().trim())) {
				return schema;
			}
		}
		return null;
	}

	public synchronized ISqlType findSqlTypeByName(String name) {
		if (typeList == null) {
			try {
				getSqlTypes();
			} catch (SQLException exc) {
				ErrorManager.showException(exc);
			}
		}
		for (ISqlType sqlType : typeList) {
			if (sqlType.getName().toUpperCase().trim().equals(
					name.toUpperCase().trim())) {
				return sqlType;
			}
		}
		return SqlType.UNSUPPORTED_SQL_TYPE;
	}

	public boolean isConnected() {
		return connection != null;
	}

	public String getIdentifierQuoteString() throws SQLException {
		return databaseMetaData.getIdentifierQuoteString();
	}

	public boolean isTransactionSupported() throws SQLException {
		return databaseMetaData.supportsTransactions();
	}

	public String getUniqueDatabaseName() {
		int count = 0;
		String uniqueName = DATABASE_BASE_NAME + ++count;
		try {
			while (getSchemas().contains(uniqueName)) {
				uniqueName = DATABASE_BASE_NAME + count++;
			}
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
		}
		return uniqueName;
	}
}
