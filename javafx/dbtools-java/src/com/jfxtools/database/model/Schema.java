package com.jfxtools.database.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.api.IView;

/**
 * Model for DB Explorer Schema Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class Schema extends DatabaseObject implements ISchema {
	private List<ITable> tableList;
	private List<IView> viewList;

	public Schema(IDatabaseInfo dbMetadata, String name) {
		super(dbMetadata, name);
	}

	public synchronized List<ITable> getTableList() throws SQLException {
		return getTableList(true);
	}

	public synchronized List<ITable> getTableList(boolean refresh)
			throws SQLException {
		if (tableList == null) {
			tableList = new ArrayList<ITable>();
			if (refresh) {
				refreshTableList();
			}
		}
		return tableList;
	}

	public synchronized List<IView> getViewList() throws SQLException {
		if (viewList == null) {
			viewList = new ArrayList<IView>();
			refreshViewList();
		}
		return viewList;
	}

	@Override
	public void refresh() throws SQLException {
		getDatabaseInfo().refresh();
		refreshTableList();
		refreshViewList();
	}

	public List<String> getTableNames() throws SQLException {
		return getTableNames(false);
	}

	public List<String> getTableNames(boolean refresh) throws SQLException {
		List<String> names = new ArrayList<String>();
		for (ITable table : getTableList(refresh)) {
			names.add(table.getName());
		}
		return names;
	}

	public ITable findTableByName(String tableName) throws SQLException {
		for (ITable table : getTableList()) {
			if (table.getName().toUpperCase().trim().equals(
					tableName.toUpperCase().trim())) {
				return table;
			}
		}
		return null;
	}

	private void refreshTableList() throws SQLException {
		tableList = getDatabaseInfo().getTables(this);
	}

	private void refreshViewList() throws SQLException {
		viewList = getDatabaseInfo().getViews(this);
	}
}
