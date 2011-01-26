
package com.nayaware.dbtools.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISqlType;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Model representing a Connection Table Column
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class Column extends AbstractDatabaseObject implements IColumn {

	private ITable table;

	private boolean nullAllowed = true;
	private boolean autoIncrement = false;
	private ISqlType sqlType = null;
	private int columnSize = -1;
	private int decimalDigits = -1;
	private String defaultValue = ""; //$NON-NLS-1$
	private String flags = ""; //$NON-NLS-1$
	private String comment = ""; //$NON-NLS-1$

	private boolean primaryKeyFlag = false;
	private String primaryKeyName = ""; //$NON-NLS-1$
	private short primaryKeySequence;

	private boolean foreignKeyFlag = false;
	private String foreignKeyName = ""; //$NON-NLS-1$
	private short foreignKeySequence;

	private List<IForeignKeyReference> foreignKeyReferenceList;

	private boolean indexKeyFlag = false;

	public Column() {
		this(null, null, Messages.getString("Column.5")); //$NON-NLS-1$
	}

	public Column(IDatabaseInfo databaseInfo, ITable table, String name) {
		super(databaseInfo, name);
		this.table = table;
		foreignKeyReferenceList = new ArrayList<IForeignKeyReference>();
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

	public boolean isPrimaryKey() {
		return primaryKeyFlag;
	}

	public boolean isForeignKey() {
		return foreignKeyFlag;
	}

	public boolean isIndexKey() {
		return indexKeyFlag;
	}

	public int getColumnSize() {
		if (columnSize == -1) {
			// try to get the default column size
			IConnectionType dbConnectionType = getDatabaseInfo()
					.getConnectionConfig().getConnectionType();
			columnSize = dbConnectionType.getDefaultColumnSize(sqlType);
		}
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public String getForeignKeyName() {
		return foreignKeyName;
	}

	public void setForeignKeyName(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
	}

	public short getForeignKeySequence() {
		return foreignKeySequence;
	}

	public void setForeignKeySequence(short foreignKeySequence) {
		this.foreignKeySequence = foreignKeySequence;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public short getPrimaryKeySequence() {
		return primaryKeySequence;
	}

	public void setPrimaryKeySequence(short primaryKeySequence) {
		this.primaryKeySequence = primaryKeySequence;
	}

	public void setPrimaryKeyFlag(boolean primaryKeyFlag) {
		this.primaryKeyFlag = primaryKeyFlag;
	}

	public void setForeignKeyFlag(boolean foriegnKey) {
		this.foreignKeyFlag = foriegnKey;
	}

	public void setIndexKeyFlag(boolean indexKeyFlag) {
		this.indexKeyFlag = indexKeyFlag;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultvalue) {
		this.defaultValue = defaultvalue;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public boolean isNullAllowed() {
		return nullAllowed;
	}

	public void setNullAllowed(boolean nullAllowed) {
		this.nullAllowed = nullAllowed;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public synchronized ISqlType getType() {
		if (sqlType == null) {
			IConnectionType dbConnectionType = getDatabaseInfo()
					.getConnectionConfig().getConnectionType();
			String sqlTypeStr = dbConnectionType.getDefaultSqlType();
			sqlType = getDatabaseInfo().findSqlTypeByName(sqlTypeStr);
		}
		return sqlType;
	}

	public void setSqlType(ISqlType sqlType) {
		this.sqlType = sqlType;
	}

	public int getSize() {
		if (columnSize == -1) {
			if (this.getType().getName().equals("VARCHAR")) { //$NON-NLS-1$
				return 20;
			}
		}
		return columnSize;
	}

	public void setSize(int size) {
		columnSize = size;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void addForeignKeyReference(IForeignKeyReference fkReference) {
		if (!foreignKeyReferenceList.contains(fkReference)) {
			foreignKeyReferenceList.add(fkReference);
		}
	}

	public void removeForeignKeyReference(IForeignKeyReference fkReference) {
		foreignKeyReferenceList.remove(fkReference);
		if (foreignKeyReferenceList.isEmpty()) {
			primaryKeyFlag = false;
		}
	}

	public List<IForeignKeyReference> getForeignKeyReferenceList() {
		return foreignKeyReferenceList;
	}

	public String getQualifiedName() {
		String tableName = quoteIdentifier(getDatabaseInfo(), getTable()
				.getName());
		String columnName = quoteIdentifier(getDatabaseInfo(), getName());
		return tableName + "." + columnName; //$NON-NLS-1$
	}

	private String quoteIdentifier(IDatabaseInfo databaseInfo, String identifier) {
		String quoteString = null;
		try {
			quoteString = databaseInfo.getIdentifierQuoteString();
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
		}
		if (quoteString != null) {
			return quoteString + identifier + quoteString;
		} else {
			return identifier;
		}
	}

	public static class ForeignKeyReference implements IForeignKeyReference {
		private String primaryKeyTableShema;
		private String primaryKeyTable;
		private String primaryKeyColumn;
		private String primaryKeyName;

		public ForeignKeyReference(String primaryKeyTableShema,
				String primaryKeyTable, String primaryKeyColumn,
				String primaryKeyName) {
			this.primaryKeyTableShema = primaryKeyTableShema;
			this.primaryKeyTable = primaryKeyTable;
			this.primaryKeyColumn = primaryKeyColumn;
			this.primaryKeyName = primaryKeyName;
		}

		public String getPrimaryKeyName() {
			return primaryKeyName;
		}

		public void setPrimaryKeyName(String primaryKeyName) {
			this.primaryKeyName = primaryKeyName;
		}

		public String getPrimaryKeyTableShema() {
			return primaryKeyTableShema;
		}

		public void setPrimaryKeyTableShema(String primaryKeyTableShema) {
			this.primaryKeyTableShema = primaryKeyTableShema;
		}

		public String getPrimaryKeyTable() {
			return primaryKeyTable;
		}

		public void setPrimaryKeyTable(String primaryKeyTable) {
			this.primaryKeyTable = primaryKeyTable;
		}

		public String getPrimaryKeyColumn() {
			return primaryKeyColumn;
		}

		public void setPrimaryKeyColumn(String primaryKeyColumn) {
			this.primaryKeyColumn = primaryKeyColumn;
		}
	}
}
