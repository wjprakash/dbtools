
package com.nayaware.dbtools.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.IQuery;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ISchemaDiagram;
import com.nayaware.dbtools.api.IScript;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.IView;
import com.nayaware.dbtools.util.PersistanceManager;

/**
 * Model for Database Explorer Connection Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class Connection extends AbstractDatabaseObject implements
		IConnection {
	private List<ISchema> schemaList;
	private List<ITable> tableList;
	private List<IView> viewList;
	private List<IScript> scriptList;
	private List<IQuery> queryList;
	private List<ISchemaDiagram> schemaDiagramList;

	public Connection(IDatabaseInfo databaseInfo) {
		super(databaseInfo, databaseInfo.getName());
	}

	public boolean hasSchemaSupport() throws SQLException {
		return getDatabaseInfo().hasSchemaSupport();
	}

	public synchronized List<ISchema> getSchemaList() throws SQLException {
		if (schemaList == null) {
			schemaList = new ArrayList<ISchema>();
			refreshSchemaList();
		}
		return schemaList;
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

	public List<String> getTableNames() throws SQLException {
		return getTableNames(false);
	}

	public List<String> getTableNames(boolean refresh) throws SQLException {
		List<String> names = new ArrayList<String>();
		for (ITable table : getTableList(true)) {
			names.add(table.getName());
		}
		return names;
	}

	@Override
	public void refresh() throws SQLException {
		getDatabaseInfo().refresh();
		refreshSchemaList();
		if (schemaList.isEmpty()) {
			refreshTableList();
			refreshViewList();
		}
	}

	private void refreshSchemaList() throws SQLException {
		schemaList = getDatabaseInfo().getSchemas();
	}

	private void refreshTableList() throws SQLException {
		tableList = getDatabaseInfo().getTables(null);
	}

	private void refreshViewList() throws SQLException {
		viewList = getDatabaseInfo().getViews(null);
	}

	public synchronized List<IScript> getScriptList() {
		if (scriptList == null) {
			scriptList = PersistanceManager.getInstance().getScripts(
					getDatabaseInfo());
			for (IScript script : scriptList) {
				script.setParent(this);
			}
		}
		return scriptList;
	}

	public synchronized void addScript(IScript script) {
		if (scriptList == null) {
			scriptList = new ArrayList<IScript>();
		}
		script.setParent(this);
		scriptList.add(script);
	}

	public void addQuery(IQuery query) {
		if (queryList == null) {
			queryList = new ArrayList<IQuery>();
		}
		query.setParent(this);
		queryList.add(query);
	}

	public List<IQuery> getQueryList() {
		if (queryList == null) {
			queryList = PersistanceManager.getInstance().getQueries(
					getDatabaseInfo());
			for (IQuery query : queryList) {
				query.setParent(this);
			}
		}
		return queryList;
	}

	public List<ISchemaDiagram> getSchemaDiagramList() {
		if (schemaDiagramList == null) {
			schemaDiagramList = PersistanceManager.getInstance()
					.getSchemaDiagrams(getDatabaseInfo());
			for (ISchemaDiagram schemaDiagram : schemaDiagramList) {
				schemaDiagram.setParent(this);
			}
		}
		return schemaDiagramList;
	}

	public void addSchemaDiagram(ISchemaDiagram schemaDiagram) {
		if (schemaDiagramList == null) {
			schemaDiagramList = new ArrayList<ISchemaDiagram>();
		}
		schemaDiagram.setParent(this);
		schemaDiagramList.add(schemaDiagram);
	}

	@Override
	public void removeChild(AbstractDatabaseObject child) {
		if (child instanceof IScript) {
			scriptList.remove(child);
		} else if (child instanceof IQuery) {
			queryList.remove(child);
		} else if (child instanceof ISchemaDiagram) {
			schemaDiagramList.remove(child);
		}
	}

	public ISchema findSchemaByName(String schemaName) throws SQLException {
		for (ISchema schema : getSchemaList()) {
			if (schema.getName().equals(schemaName)) {
				return schema;
			}
		}
		return null;
	}
}
