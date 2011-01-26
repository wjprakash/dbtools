package com.jfxtools.database.api;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public interface IConnection extends IDatabaseObject {

	/**
	 * Check if this database has Schema support
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean hasSchemaSupport() throws SQLException;

	/**
	 * Get the Schema List
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ISchema> getSchemaList() throws SQLException;

	/**
	 * If there is no Schema support then get the list of tables else get the
	 * Schema list and then get the tables
	 * 
	 * @return List
	 *         <Table>
	 * @throws SQLException
	 */
	public List<ITable> getTableList() throws SQLException;

	public List<String> getTableNames() throws SQLException;

	public List<String> getTableNames(boolean refresh) throws SQLException;

	/**
	 * If there is no Schema support then get the list of views else get the
	 * Schema list and then get the views
	 * 
	 * @return List<View>
	 * @throws SQLException
	 */
	public List<IView> getViewList() throws SQLException;

	public void refresh() throws SQLException;

	public List<IScript> getScriptList();

	public List<IQuery> getQueryList();

	public List<ISchemaDiagram> getSchemaDiagramList();

	public void addScript(IScript script);

	public void addQuery(IQuery query);

	public void addSchemaDiagram(ISchemaDiagram schemaDiagram);

	public ISchema findSchemaByName(String schemaName) throws SQLException;

}