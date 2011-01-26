
package com.nayaware.dbtools.api;

import java.util.List;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public interface IColumn extends IAbstractDatabaseObject {

	public ITable getTable();

	public void setTable(ITable table);

	public boolean isPrimaryKey();

	public boolean isForeignKey();

	public boolean isIndexKey();

	public int getColumnSize();

	public void setColumnSize(int columnSize);

	public int getDecimalDigits();

	public void setDecimalDigits(int decimalDigits);

	public String getForeignKeyName();

	public void setForeignKeyName(String foreignKeyName);

	public short getForeignKeySequence();

	public void setForeignKeySequence(short foreignKeySequence);

	public String getPrimaryKeyName();

	public void setPrimaryKeyName(String primaryKeyName);

	public short getPrimaryKeySequence();

	public void setPrimaryKeySequence(short primaryKeySequence);

	public void setPrimaryKeyFlag(boolean primaryKeyFlag);

	public void setForeignKeyFlag(boolean foriegnKey);

	public void setIndexKeyFlag(boolean indexKeyFlag);

	public String getDefaultValue();

	public void setDefaultValue(String defaultvalue);

	public String getFlags();

	public void setFlags(String flags);

	public boolean isNullAllowed();

	public void setNullAllowed(boolean nullAllowed);

	public boolean isAutoIncrement();

	public void setAutoIncrement(boolean autoIncrement);

	public ISqlType getType();

	public void setSqlType(ISqlType sqlType);

	public int getSize();

	public void setSize(int size);

	public String getComment();

	public void setComment(String comment);

	public String getQualifiedName();

	public void addForeignKeyReference(IForeignKeyReference fkReference);

	public void removeForeignKeyReference(IForeignKeyReference fkReference);

	public List<IForeignKeyReference> getForeignKeyReferenceList();

	/**
	 * Holds the ForeignKey reference implementation
	 * 
	 * @author winston
	 * 
	 */
	public interface IForeignKeyReference {
		public String getPrimaryKeyTableShema();

		public void setPrimaryKeyTableShema(String primaryKeyTableShema);

		public String getPrimaryKeyTable();

		public void setPrimaryKeyTable(String primaryKeyTable);

		public String getPrimaryKeyColumn();

		public void setPrimaryKeyColumn(String primaryKeyColumn);

		public String getPrimaryKeyName();

		public void setPrimaryKeyName(String primaryKeyName);
	}
}