
package com.nayaware.dbtools.execute;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.IDatabaseInfo;

/**
 * Executor to execute SQL script
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlExecutor {

	private String script;

	public static final String SQL_EXECUTE_JOB_NAME = "sqlExecutorJob"; //$NON-NLS-1$

	private static final int TIME_OUT_SEC = 30;

	private ExecutionStatus executionStatus;

	private Connection connection;
	private IDatabaseInfo databaseInfo;

	public SqlExecutor(IDatabaseInfo databaseInfo, String script) {
		connection = databaseInfo.getConnection();
		this.script = script;
		this.databaseInfo = databaseInfo;
	}

	public SqlExecutor(IDatabaseInfo databaseInfo) {
		this(databaseInfo, ""); //$NON-NLS-1$
	}

	/**
	 * 
	 * @return job
	 */
	public Job asyncExecute() {
		Job dbJob = new Job(SQL_EXECUTE_JOB_NAME) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				final ExecutionStatus execStatus = execute();
				if (!execStatus.hasExceptions()) {
					return Status.OK_STATUS;
				} else {
					return Status.CANCEL_STATUS;
					// return new Status(IStatus.ERROR,
					// DbExplorerPlugin.PLUGIN_ID, 1, "", null);
				}
			}
		};
		dbJob.schedule();
		return dbJob;
	}

	/**
	 * Execute the SQL script
	 */
	public synchronized ExecutionStatus execute() {
		boolean cancelled = false;
		executionStatus = new ExecutionStatus(script);

		List<StatementInfo> statements = new SqlSplitter(script)
				.getStatements();

		for (Iterator<StatementInfo> i = statements.iterator(); i.hasNext();) {

			cancelled = Thread.currentThread().isInterrupted();
			if (cancelled) {
				break;
			}
			StatementInfo info = i.next();
			String sql = info.getSQL();

			if (sql.toLowerCase().startsWith("select")) { //$NON-NLS-1$
				executeQuery(sql);
			} else {
				executeUpdate(sql);
			}
		}
		return executionStatus;
	}

	public ExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(ExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
	}

	/**
	 * Execute statements, such as select that returns ResultSet
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private void executeQuery(String sql) {
		Statement stmt = null;
		try {
			IConnectionType connectionType = databaseInfo.getConnectionConfig()
					.getConnectionType();
			if (!connectionType.hasScrollableResultSetSupport()) {
				stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
			} else {
				stmt = connection.createStatement(
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
			}
			setQueryTimeout(stmt);
			ResultSet resultSet = stmt.executeQuery(sql);
			executionStatus.addResult(resultSet);
			executionStatus.addWarning(stmt.getWarnings());
		} catch (SQLException exc) {
			executionStatus.addException(exc);
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ExecutionStatus executeQuery(String sql, int offset, int pageSize) {
		executionStatus = new ExecutionStatus(sql);
		Statement stmt = null;
		try {
			IConnectionType connectionType = databaseInfo.getConnectionConfig()
					.getConnectionType();
			if (!connectionType.hasScrollableResultSetSupport()) {
				stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
			} else {
				stmt = connection.createStatement(
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
			}
			stmt.setFetchSize(pageSize);
			setQueryTimeout(stmt);
			ResultSet resultSet = stmt.executeQuery(sql);
			executionStatus.addResult(resultSet);
			executionStatus.addWarning(stmt.getWarnings());
		} catch (SQLException exc) {
			executionStatus.addException(exc);
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return executionStatus;
	}

	/**
	 * Execute statements, such as insert, update, or delete and return no
	 * results
	 * 
	 * @param con
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private void executeUpdate(String sql) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			setQueryTimeout(stmt);
			int updateCount = stmt.executeUpdate(sql);
			executionStatus.addUpdateCount(updateCount);
			stmt.close();
		} catch (SQLException exc) {
			executionStatus.addException(exc);
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void setQueryTimeout(Statement stmt) {
		try {
			stmt.setQueryTimeout(TIME_OUT_SEC);
		} catch (SQLException exc) {
			// Some JDBC drivers don't implement, so they throw
			// "NOT Implemented"
			// exception which can be safely ignored
		}
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
