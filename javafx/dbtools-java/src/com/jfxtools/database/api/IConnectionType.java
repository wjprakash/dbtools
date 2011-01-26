package com.jfxtools.database.api;

public interface IConnectionType {
	
	public static final String TEMPLATE_HOST = "<HOST>"; 
	public static final String TEMPLATE_PORT = "<PORT>"; 
	public static final String TEMPLATE_DB = "<DB>"; 
	public static final String TEMPLATE_DB_PATH = "<DB_PATH>"; 
	
	public static final int UNKNOWN = -1;

	public static final int ORACLE = 1;

	public static final int MYSQL = 2;

	public static final int SYBASE = 3;

	public static final int DERBY_CLIENT = 4;

	public static final int DERBY_EMBEDDED = 5;

	public static final int SQLITE = 6;

	public static final int POSTGRESQL = 7;

	public static final int DB2 = 8;

	public static final int SQLSERVER = 9;

	public static final int INTERBASE = 10;

	public static final int POINTBASE = 11;

	public static final int FIREBIRD = 12;

	public static final int HYPERSONICSQL = 13;

	public static final int INFORMIX = 14;
	
	/**
	 * Type for this connection. Must be one of the constants of IConnectionType
	 * Ex: IConnectionType.MYSQL (type for MySQL connection)
	 * @return int - connection type
	 */
	public int getType();
	
	/**
	 * Get the driver name for this connection type
	 * Ex: com.mysql.jdbc.Driver (MySql driver)
	 * @return string - name of the driver
	 */
	public String getDriverName();
	
	/**
	 * Display name (usually server name) for this Connection type
	 * Ex: MySQL  (MySql Server Name)
	 * Will be used in the Add Connection Dialog
	 * @return string - Display name for the connection type
	 */
	public  String getServerName();
	
	public int getDefaultColumnSize(ISqlType sqlType);
	
	/**
	 * Default host name for the connection type if supported
	 * (usually for network databases such mysql)
	 * 
	 * Ex: localhost 
	 * Will be used in the Add Connection Dialog
	 * @return string - host name (could be empty string)
	 */
	public  String getDefaultHostName();
	
	/**
	 * Default database path for the connection type if supported
	 * (usually for network-less databases such as sqlite or embedded derby) 
	 * @return string - path of the database (could be empty string)
	 */
	public  String getDefaultDatabasePath();
	
	/**
	 * Default port name for the connection type
	 * Ex: 3306 (Default MySQL port number) 
	 * Will be used in the Add Connection Dialog
	 * @return string port number as string (could be empty string)
	 */
	public  String getDefaultPortNumber();
	
	/**
	 * Get a default user name for the connection type
	 * Ex: root (MySQL)
	 * @return string - default username (could be empty string)
	 */
	public  String getDefaultUsername();
	
	/**
	 * Get the default database name
	 * @return string - default database name (could be empty string)
	 */
	public  String getDefaultDatabaseName();
	
	/**
	 * Default SQL type used for this connection type
	 * Ex: varchar (for MySQL)
	 * @return string - Default SQL type 
	 */
	public  String getDefaultSqlType();
	
	/**
	 * Get the integer SQL type
	 * Ex: INTEGER (MySQL) int8 (Postgres)
	 * @return string - integer SQL type
	 */
	public String getIntegerSqlType();
	
	/**
	 * Return if LIMIT SQL Keyword is supported
	 * 
	 * @return boolean - true if LIMIT is supported
	 */
	public  boolean isLimitSupported();
	
	/**
	 * URL pattern for this connection type
	 * Ex:  jdbc:mysql://<HOST>:<PORT>/<DB> (MYSQL)
	 *      jdbc:oracle:thin:@<HOST>:<PORT>:<SID> (ORACLE)
	 *      jdbc:derby://<HOST>:<PORT>/<DB> (Derby Network)
	 *      jdbc:derby:<DB_PATH> (Derby Embedded)
	 *      jdbc:sqlite:/<DB_PATH> (SQLITE)
	 *      jdbc:postgresql://<HOST>:<PORT>/<DB> (POSTGRES)
	 *      jdbc:db2://<HOST>:<PORT>/<DB> (DB2)
	 *      jdbc:microsoft:sqlserver://<HOST>:<PORT>[;DatabaseName=<DB>] (SQLServer)
	 *      jdbc:interbase://<HOST>/<DB> (INTERBASE)
	 *      jdbc:firebirdsql:[//<HOST>[:<PORT>]/]<DB> (FIREBIRD)
	 *      jdbc:HypersonicSQL:<DB> (HYPERSONIC)
	 *      jdbc:informix-sqli://<HOST>:<PORT>/<DB>:INFORMIXSERVER=<SERVER_NAME> (INFORMIX)
	 *      
	 * @return string - URL pattern
	 */
	public String getUrlPattern();
	
	/**
	 * True if the connection type is for a network based database 
	 * Ex true for Network based database such as Mysql
	 *    false for File based database such as SQLite or Embedded Derby
	 * @return booelan - true if it is a network Database 
	 */
	public boolean isNetworkDatabase();
	
	/**
	 * Get the SQL Helper which helps to generate database specific SQL statements
	 * @return ISqlHelper - Database specific SQL Helper
	 */
	public ISqlHelper getSqlHelper();
	
	/**
	 * Check if the driver has Scrollable Result set support
	 * If Scrollable Result set support then Table Viewer would support pagination
	 * * @return boolean - true if supports scrollable result set 
	 */
	public boolean hasScrollableResultSetSupport();
}
