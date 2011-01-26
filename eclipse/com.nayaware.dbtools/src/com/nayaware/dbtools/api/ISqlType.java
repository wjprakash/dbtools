
package com.nayaware.dbtools.api;


/**
 * SQL Type model
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface ISqlType extends IAbstractDatabaseObject {
	
	public static final String VARCHAR = "VARCHAR"; //$NON-NLS-1$

	public static final int SERACHABLE_NONE = 0;
	public static final int SEARCHABLE_LIKE_ONLY = 1;
	public static final int SEARCHABLE_EXCEPT_LIKE = 2;
	public static final int SEARCHABLE = 3;

	public boolean isNullAllowed();

	public void setNullAllowed(boolean nullAllowed);

	public boolean isAutoIncrementable();

	public void setAutoIncrementable(boolean autoIncrement);

	public String getLiteralPrefix();

	public void setLiteralPrefix(String literalPrefix);

	public String getLiteralSuffix();

	public void setLiteralSuffix(String literalSuffix);

	public int getSearchable();

	public void setSearchable(int searchable);
}