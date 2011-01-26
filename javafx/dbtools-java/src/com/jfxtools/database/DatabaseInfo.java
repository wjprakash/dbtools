package com.jfxtools.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.ISqlType;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.IView;
import com.jfxtools.database.model.Column;
import com.jfxtools.database.model.ForeignKeyReference;
import com.jfxtools.database.model.Schema;
import com.jfxtools.database.model.SqlType;
import com.jfxtools.database.model.Table;
import com.jfxtools.database.model.View;

/**
 * Connection Meta Data information
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DatabaseInfo implements IDatabaseInfo {

	private static final String DATABASE_BASE_NAME = "Database"; 

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
					String s = "SELECT SCHEMA_NAME AS TABLE_SCHEM FROM information_schema.SCHEMATA"; 
					schemaResultSet = connection.createStatement()
							.executeQuery(s);
				} else {
					schemaResultSet = databaseMetaData.getSchemas();
				}

				while (schemaResultSet.next()) {
					schemaList.add(new Schema(this, schemaResultSet
							.getString("TABLE_SCHEM"))); 
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
				tableTypeList.add(tableTypeRs.getString("TABLE_TYPE").trim()); 
			}

			switch (connectionConfig.getDbType()) {
			case IConnectionType.ORACLE:
				tableTypeList.add("SEQUENCE"); 
				tableTypeList.add("FUNCTION"); 
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
					String typeName = rs.getString("TYPE_NAME"); 
					boolean autoInc = rs.getBoolean("AUTO_INCREMENT"); 
					int nullable = rs.getInt("NULLABLE"); 
					String literalPrefix = rs.getString("LITERAL_PREFIX"); 
					String literalSuffix = rs.getString("LITERAL_SUFFIX"); 
					int searchable = rs.getInt("SEARCHABLE"); 

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
		String schemaPattern = "%"; 
		String tablePattern = "%"; 

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
				"%"); 
		List<IColumn> columnList = new ArrayList<IColumn>();
		try {
			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME"); 
				ISqlType sqlType = findSqlTypeByName(rs.getString("TYPE_NAME")); 
				int size = rs.getInt("COLUMN_SIZE"); 
				int decDigits = rs.getInt("DECIMAL_DIGITS"); 
				int nullable = rs.getInt("NULLABLE"); 
				String comment = rs.getString("REMARKS"); 
				String defaultValue = rs.getString("COLUMN_DEF"); 

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
		String schemaPattern = "%"; 
		String tablePattern = "%"; 

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
				String name = rs.getString("COLUMN_NAME"); 
				String pkName = rs.getString("PK_NAME"); 
				short keySeq = rs.getShort("KEY_SEQ"); 

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
		String schemaPattern = "%"; 
		String tablePattern = "%"; 

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
				String name = rs.getString("FKCOLUMN_NAME"); 
				String fkName = rs.getString("FK_NAME"); 
				short keySeq = rs.getShort("KEY_SEQ"); 
				String pkTableSchema = rs.getString("PKTABLE_SCHEM"); 
				String pkTableName = rs.getString("PKTABLE_NAME"); 
				String pkColumnName = rs.getString("PKCOLUMN_NAME"); 
				String pkName = rs.getString("PK_NAME"); 

				IColumn fkColumn = new Column(this, fkTable, name);
				fkColumn.setForeignKeyFlag(true);
				fkColumn.setForeignKeyName(fkName);
				fkColumn.setForeignKeySequence(keySeq);

				fkColumn.addForeignKeyReference(new ForeignKeyReference(
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
			if (url.endsWith("/")) { 
				url += schema;
			} else {
				url += ("/" + schema); 
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
		ResultSet rs = getTableInfo(schema, "VIEW"); 
		List<IView> tableList = new ArrayList<IView>();
		while (rs.next()) {
			tableList.add(new View(this, schema, rs.getString("TABLE_NAME"))); 
		}
		return tableList;
	}

	public List<ITable> getTables(ISchema schema) throws SQLException {
		ResultSet rs = getTableInfo(schema, "TABLE"); 
		List<ITable> tableList = new ArrayList<ITable>();
		while (rs.next()) {
			tableList.add(new Table(this, schema, rs.getString("TABLE_NAME"))); 
		}
		return tableList;
	}

	private synchronized ResultSet getTableInfo(ISchema schema, String type)
			throws SQLException {
		String[] tableTypes = { type };

		String schemaPattern = "%"; 
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

		ResultSet rs = dbMetaData.getTables(null, schemaPattern, "%", 
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

	public synchronized ISqlType findSqlTypeByName(String name)
			throws SQLException {
		if (typeList == null) {
			getSqlTypes();
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

	public String getUniqueDatabaseName() throws SQLException {
		int count = 0;
		String uniqueName = DATABASE_BASE_NAME + ++count;

		while (getSchemas().contains(uniqueName)) {
			uniqueName = DATABASE_BASE_NAME + count++;
		}

		return uniqueName;
	}
}
