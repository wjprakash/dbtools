
package com.nayaware.dbtools.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.ITableData;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Model for DB Explorer Table Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class Table extends AbstractDatabaseObject implements ITable {
	private List<IColumn> columnList;
	private List<IColumn> primaryKeyColumnList;
	private List<IColumn> foreignKeyColumnList;

	private ISchema schema;

	public Table() {
		this(null, null, Messages.getString("Table.0")); //$NON-NLS-1$
	}

	public Table(IDatabaseInfo databaseInfo, ISchema schema, String name) {
		super(databaseInfo, name);
		this.schema = schema;
	}

	public ISchema getSchema() {
		return schema;
	}

	public List<IColumn> getPrimaryKeyColumns() throws SQLException {
		if (primaryKeyColumnList == null) {
			primaryKeyColumnList = new ArrayList<IColumn>();
			refreshPrimaryKeyColumns();
		}
		return primaryKeyColumnList;
	}

	public List<IColumn> getForeignKeyColumns() throws SQLException {
		if (foreignKeyColumnList == null) {
			foreignKeyColumnList = new ArrayList<IColumn>();
			refreshForeignKeyColumns();
		}
		return foreignKeyColumnList;
	}

	public synchronized List<IColumn> getColumnList() throws SQLException {
		return getColumnList(false);
	}

	public synchronized List<IColumn> getColumnList(boolean refresh)
			throws SQLException {
		if (columnList == null) {
			columnList = new ArrayList<IColumn>();
			if (refresh) {
				refreshColumns();
			}
		}
		return columnList;
	}

	@Override
	public void refresh() throws SQLException {
		getDatabaseInfo().refresh();
		refreshPrimaryKeyColumns();
		refreshForeignKeyColumns();
		refreshColumns();
	}

	public List<String> getColumnNames() {
		List<String> names = new ArrayList<String>();
		if ((columnList != null) && (columnList.size() > 0)) {
			for (IColumn col : columnList) {
				names.add(col.getName());
			}
		}
		return names;
	}

	public IColumn[] getColumns(boolean refresh) throws SQLException {
		if (refresh) {
			getColumnList(true);
		}
		if ((columnList != null) && (columnList.size() > 0)) {
			return columnList.toArray(new Column[columnList.size()]);
		} else {
			return new IColumn[0];
		}
	}

	public synchronized IColumn removeColumn(IColumn oldColumn) {
		if ((columnList != null) && columnList.contains(oldColumn)) {
			int index = columnList.indexOf(oldColumn);
			columnList.remove(oldColumn);
			if (index > 0) {
				return columnList.get(index - 1);
			} else if (!columnList.isEmpty()) {
				return columnList.get(0);
			}
		}
		return null;
	}

	public synchronized void addColumn(IColumn newColumn) {
		if (columnList == null) {
			columnList = new ArrayList<IColumn>();
		}
		columnList.add(newColumn);
	}

	public synchronized ITableData getData() {
		// String sqlScript = SqlUtils.getSelectStatement(this);
		// ExecutionStatus execStatus = new SqlExecutor(getDatabaseInfo(),
		// sqlScript).execute();
		// if (execStatus.hasExceptions()) {
		// ErrorManager.showException(execStatus.getExceptions().get(0));
		// }
		// if (execStatus.hasResults()) {
		// ResultSet rs = execStatus.getResults().get(0);
		// return new TableData(this.getName(), rs, this);
		// }
		return new TableData(this);
	}

	public String getQualifiedName() {
		if (getSchema() != null) {
			String schemaName = quoteIdentifier(getDatabaseInfo(),
					getSchema().getName());
			String tableName = quoteIdentifier(getDatabaseInfo(),
					getName());
			return schemaName + "." + tableName; //$NON-NLS-1$
		} else {
			return getName();
		}
	}
	
	private  String quoteIdentifier(IDatabaseInfo databaseInfo,
			String identifier) {
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

	public IColumn findColumnByName(String colName) throws SQLException {
		return findColumnByName(colName, false);
	}

	public IColumn findColumnByName(String colName, boolean refresh)
			throws SQLException {
		for (IColumn column : getColumnList(refresh)) {
			if (column.getName().toUpperCase().trim().equals(
					colName.toUpperCase().trim())) {
				return column;
			}
		}
		return null;
	}

	private void refreshPrimaryKeyColumns() throws SQLException {
		primaryKeyColumnList = getDatabaseInfo().getPrimaryKeyColumns(this);
	}

	private void refreshForeignKeyColumns() throws SQLException {
		foreignKeyColumnList = getDatabaseInfo().getForeignKeyColumns(this);
	}

	private void refreshColumns() throws SQLException {
		columnList = getDatabaseInfo().getColumns(this);
	}
}
