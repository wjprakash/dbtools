
package com.nayaware.dbtools.tests;

import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.core.ConnectionConfig;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.execute.ExecutionStatus;
import com.nayaware.dbtools.execute.SqlExecutor;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class RemoteMySqlTest {

	public static IConnectionConfig createMySqlConnectionConfig() {
		String name = "Remote MySql";
		String url = "jdbc:mysql://****";
		String username = "*******";
		String password = "*******";
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
