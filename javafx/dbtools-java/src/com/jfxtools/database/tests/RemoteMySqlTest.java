package com.jfxtools.database.tests;

import com.jfxtools.database.ConnectionConfig;
import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.api.IConnectionConfig;
import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.execute.ExecutionStatus;
import com.jfxtools.database.execute.SqlExecutor;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class RemoteMySqlTest {

	public static IConnectionConfig createMySqlConnectionConfig() {
		String name = "Remote MySql";
		String url = "jdbc:mysql://ajaxlibraryprojectw2.aptanacloud.com:3306/?allowMultiQueries=true&zeroDateTimeBehavior=convertToNull";
		String username = "wjprakash";
		String password = "wjp987";
		return new ConnectionConfig(name, url, IConnectionType.MYSQL, username, password);
	}

	public ExecutionStatus executeSql(IConnectionConfig dbConfig, String script) {
		IDatabaseInfo databaseInfo = new DatabaseInfo(dbConfig);
		SqlExecutor sqlExecutor = new SqlExecutor(databaseInfo, script);

		return sqlExecutor.execute();
	}

	public static void main(String[] args) {
		RemoteMySqlTest connectionTest = new RemoteMySqlTest();
		try {
			System.out.println("Starting execute");
			String script = "Update * from mysql.db";
			ExecutionStatus executionStatus = connectionTest
					.executeSql(ConnectionUtils
							.createRemoteMySqlConnectionConfig(), script);
			if (executionStatus.hasExceptions()) {
				executionStatus.getExceptions().get(0).printStackTrace();
			}
			System.out.println("Finishing execute");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
