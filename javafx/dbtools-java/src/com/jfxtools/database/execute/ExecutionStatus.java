package com.jfxtools.database.execute;

import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL execution status
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExecutionStatus {

	private List<ResultSet> results = new ArrayList<ResultSet>();
	private String sqlScript;
	private List<Throwable> exceptions = new ArrayList<Throwable>();
	private int updateCount;
	private List<SQLWarning> sqlWarnings = new ArrayList<SQLWarning>();

	public ExecutionStatus(String script) {
		sqlScript = script;
	}

	public String getSqlScript() {
		return sqlScript;
	}

	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}

	public boolean hasResults() {
		return results.size() > 0;
	}

	public boolean hasExceptions() {
		return exceptions.size() > 0;
	}

	public List<Throwable> getExceptions() {
		return exceptions;
	}

	public void addException(Throwable exception) {
		exceptions.add(exception);
	}

	public List<ResultSet> getResults() {
		return results;
	}

	public void addResult(ResultSet result) {
		results.add(result);
	}

	public void addUpdateCount(int count) {
		updateCount += count;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public boolean hasWarnings() {
		return sqlWarnings.size() > 0;
	}

	public List<SQLWarning> getWarnings() {
		return sqlWarnings;
	}

	public void addWarning(SQLWarning warnings) {
		sqlWarnings.add(warnings);
	}
}
