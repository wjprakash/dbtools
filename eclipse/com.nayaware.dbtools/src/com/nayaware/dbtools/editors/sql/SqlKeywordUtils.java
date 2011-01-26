
package com.nayaware.dbtools.editors.sql;

/**
 * SQL Keyword utility
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlKeywordUtils {

	public static final String[] KEYWORDS = { "ACCESS", "ADD", "ALL", "ALTER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"AND", "ANY", "AS", "ASC", "AUDIT", "BETWEEN", "BEGIN", "BOTH", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			"BY", "CACHE", "CASCADE", "CASE", "CHECK", "CHARSET", "CLUSTER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"COLUMN", "COMMENT", "COMMIT", "COMPRESS", "CONNECT", "CONSTRAINT", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"CREATE", "CROSS", "CURRENT", "CYCLE", "DEFAULT", "DELETE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"DELIMITER", "DESC", "DECLARE", "DISTINCT", "DROP", "ENGINE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"ELSE", "END", "ESCAPE", "EXCEPT", "EXCLUSIVE", "EXISTS", "FILE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"FUNCTION", "FOR", "FOREIGN", "FROM", "GRANT", "GROUP", "HAVING", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"IDENTIFIED", "IF", "IMMEDIATE", "IN", "INCREMENT", "INDEX", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"INITIAL", "INNER", "INSERT", "INTERSECT", "INTO", "IS", "JOIN", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"KEY", "LEADING", "LEVEL", "LEFT", "LIKE", "LOCK", "MARGE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"MATCH", "MATCHED", "MAXEXTENTS", "MAXVALUE", "MINUS", "MINVALUE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"MLSLABEL", "MODE", "MODIFY", "NATURAL", "NOAUDIT", "NOCOMPRESS", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"NOCYCLE", "NOMAXVALUE", "NOMINVALUE", "NOT", "NOWAIT", "NULL", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"NUMBER", "OF", "OFFLINE", "ON", "ONLINE", "ONLY", "OPTION", "OR", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			"ORDER", "OUTER", "PACKAGE", "PCTFREE", "PRIMARY", "PRIOR", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"PRIVILEGES", "PROCEDURE", "PUBLIC", "RAW", "READ", "RENAME", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"RESOURCE", "RETURN", "REVOKE", "RIGHT", "ROLLBACK", "ROW", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"ROWID", "ROWNUM", "ROWS", "REFERENCES", "SCHEMA", "SELECT", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"SEQUENCE", "SESSION", "SET", "SHARE", "SIZE", "SHOW", "START", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"SUCCESSFUL", "SYNONYM", "SYSDATE", "TABLE", "TEMPORARY", "THEN", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"TO", "TRAILING", "TRIGGER", "TRUNCATE", "TYPE", "UID", "UNION", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"UNIQUE", "UPDATE", "USE", "USER", "USING", "VALIDATE", "VALUES", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"VIEW", "WHENEVER", "WHEN", "WHERE", "WITH" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	public static final String[] KEYWORD_DATATYPE = { "BFILE", "BINARY_DOUBLE", //$NON-NLS-1$ //$NON-NLS-2$
			"BINARY_FLOAT", "BLOB", "CHAR", "CHARACTER", "CHAR VARYING", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"CHARACTER VARYING", "CLOB", "DATE", "DEC", "DECIMAL", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"DOUBLE PRECISION", "FLOAT", "INTERVAL YEAR TO MONTH", "INT", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"INTEGER", "INTERVAL", "INTERVAL DAY TO SECOND", "LONG", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"LONG RAW", "NATIONAL CHAR", "NATIONAL CHARACTER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"NATIONAL CHARACTER VARYING", "NATIONAL CHAR VARYING", "NCHAR", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"NCHAR VARYING", "NUMBER", "NUMERIC", "NVARCHAR2", "RAW", "REAL", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"ROWID", "SMALLINT", "TIME", "TIMESTAMP", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"TIMESTAMP WITH LOCAL TIMEZONE", "TIMESTAMP WITH TIMEZONE", //$NON-NLS-1$ //$NON-NLS-2$
			"VARCHAR", "VARCHAR2" }; //$NON-NLS-1$ //$NON-NLS-2$

	public static final String[] KEYWORD_FUNCTION = { "ABS", "ACOS", //$NON-NLS-1$ //$NON-NLS-2$
			"ADD_MONTHS", "ASCII", "ASIN", "ATAN", "AVG", "CEIL", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"CHARTOROWID", "CHECK", "CHR", "COALESCE", "CONCAT", "CONVERT", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"COS", "COSH", "COUNT", "DECODE", "DUMP", "EXP", "FLOOR", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"GREATEST", "HEXTORAW", "INITCAP", "INSTR", "INSTRB", "LAST_DAY", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"LEAST", "LENGTH", "LENGTHB", "LN", "LOG", "LOWER", "LPAD", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"LTRIM", "MAX", "MIN", "MOD", "MONTHS_BETWEEN", "NEXT_DAY", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"NULLIF", "NVL", "NVL2", "POWER", "RAWTOHEX", "REPLACE", "ROUND", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"ROWIDTOCHAR", "RPAD", "RTRIM", "SIGN", "SIN", "SINH", "SQRT", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"STDDEV", "SUBSTR", "SUBSTRB", "SUM", "SYSDATE", "TAN", "TANH", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"TO_CHAR", "TO_DATE", "TO_MULTI_BYTE", "TO_NUMBER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"TO_SINGLE_BYTE", "TRIM", "TRUNC", "UID", "UPPER", "USER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"USERENV", "VARIANCE", "VSIZE" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String[] BEGIN_SQL_KEYWORD = { "ALTER", "COMMENT", //$NON-NLS-1$ //$NON-NLS-2$
			"CREATE", "DELETE", "DROP", "GRANT", "INSERT", "MARGE", "REVOKE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"SELECT", "TRUNCATE", "UPDATE" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String[] MULTI_KEYWORD = { "CREATE OR REPLACE", //$NON-NLS-1$
			"CROSS JOIN", "COMMENT ON", "FOR UPDATE", "FULL JOIN", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"FULL OUTER JOIN", "GROUP BY", "INCREMENT BY", "INNER JOIN", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"JOIN", "LEFT JOIN", "LEFT OUTER JOIN", "NATURAL JOIN", "ORDER BY", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"RIGHT JOIN", "RIGHT OUTER JOIN", "START WITH", "UNION ALL", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"WHEN MATCHED THEN", "WHEN NOT MATCHED THEN", "WITH CHECK OPTION", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"WITH READ ONLY" }; //$NON-NLS-1$

	public static String[] getKeywordNamess() {
		return KEYWORDS;
	}

	public static String[] getDataTypeNames() {
		return KEYWORD_DATATYPE;
	}

	public static String[] getFunctionNames() {
		return KEYWORD_FUNCTION;
	}

	public static String[] getAll() {
		String[] allKeywords = new String[KEYWORDS.length
				+ KEYWORD_DATATYPE.length + KEYWORD_FUNCTION.length];
		int count = 0;
		for (String keyword : KEYWORDS) {
			allKeywords[count++] = keyword;
		}
		for (String keyword : KEYWORD_DATATYPE) {
			allKeywords[count++] = keyword;
		}
		for (String keyword : KEYWORD_FUNCTION) {
			allKeywords[count++] = keyword;
		}
		return allKeywords;
	}
}
