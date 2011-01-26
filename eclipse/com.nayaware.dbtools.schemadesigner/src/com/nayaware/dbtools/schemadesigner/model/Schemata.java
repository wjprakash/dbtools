
package com.nayaware.dbtools.schemadesigner.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.IColumn.IForeignKeyReference;
import com.nayaware.dbtools.model.Column;
import com.nayaware.dbtools.model.Table;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ColumnNode;
import com.nayaware.dbtools.nodes.SchemaNode;
import com.nayaware.dbtools.nodes.TableGroupNode;
import com.nayaware.dbtools.nodes.TableNode;
import com.nayaware.dbtools.schemadesigner.commands.Messages;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Model representing the schema being designed
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class Schemata {

	public static final String SELECTED_COLUMN_NODE = "selectedColumnNode"; //$NON-NLS-1$
	public static final String SELECTED_TABLE_NODE = "selectedTableNode"; //$NON-NLS-1$
	public static final String SELECTED_SCHEMATA = "selectedSchemata"; //$NON-NLS-1$
	private SdColumnNode selectedColumnNode;
	private SdTableNode selectedTableNode;

	private IDatabaseInfo databaseInfo;

	private List<Relationship> relationships = new ArrayList<Relationship>();

	private List<AbstractNode> sdTableNodes = new ArrayList<AbstractNode>();

	private final static String TABLE_BASE_NAME = "NewTable"; //$NON-NLS-1$
	public static final String TABLE_NODES_MODIFIED = "tableNodeModified"; //$NON-NLS-1$
	public static final String PROP_DIRTY = "dirty"; //$NON-NLS-1$

	private String name = "Database1"; //$NON-NLS-1$

	protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(
			this);

	private boolean isDesigner;
	private boolean restoring;

	public Schemata(String name, AbstractNode connectionNode,
			boolean designerFlag) {
		setDatabaseInfo(connectionNode.getDatbaseObject().getDatabaseInfo());
		setName(name);
		this.isDesigner = designerFlag;
		if (!isDesigner) {
			Job.getJobManager().addJobChangeListener(
					new TableRefreshJobChangeListener());
		}
	}

	public Schemata(SchemaNode schemaNode, boolean isDesigner) {
		this(schemaNode.getName(), schemaNode, isDesigner);
		final TableGroupNode tableGroupNode = schemaNode.getTableGroupNode();
		tableGroupNode.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String property = evt.getPropertyName();
				if (property.equals(AbstractNode.NODE_CHILDREN_MODIFIED)) {
					setTableNodes(sdTableNodes = tableGroupNode
							.getTableNodeList(false));
					firePropertyChange(TABLE_NODES_MODIFIED, null, sdTableNodes);
					firePropertyChange(PROP_DIRTY, null, true);
				}
			}
		});
		// Refresh and get the table node lists
		setTableNodes(tableGroupNode.getTableNodeList(true));
	}

	public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}

	public IDatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		firePropertyChange(PROP_DIRTY, null, true);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue) {
		if (prop.equals(PROP_DIRTY) && restoring) {
			return;
		}
		if (listeners.hasListeners(prop)) {
			listeners.firePropertyChange(prop, old, newValue);
		}
	}

	public synchronized List<AbstractNode> getTableNodes() {
		return sdTableNodes;
	}

	public synchronized void setTableNodes(List<AbstractNode> tableNodes) {
		sdTableNodes = new ArrayList<AbstractNode>(tableNodes.size());
		for (AbstractNode node : tableNodes) {
			if (node instanceof TableNode) {
				sdTableNodes.add(new SdTableNode((TableNode) node, this));
			} else {
				sdTableNodes.add(node);
			}
		}
		firePropertyChange(TABLE_NODES_MODIFIED, null, sdTableNodes);
		firePropertyChange(PROP_DIRTY, null, true);
	}

	public void addTableNode(AbstractNode node) {
		if (!sdTableNodes.contains(node)) {
			if (node instanceof SdTableNode) {
				SdTableNode tableNode = (SdTableNode) node;
				ITable table = tableNode.getTableNode().getTable();
				table.setDatabaseInfo(databaseInfo);
			}
			sdTableNodes.add(node);
			firePropertyChange(TABLE_NODES_MODIFIED, null, node);
			firePropertyChange(PROP_DIRTY, null, true);
		}
	}

	public void removeTableNode(AbstractNode node) {
		if (sdTableNodes.contains(node)) {
			SdTableNode tableNode = (SdTableNode) node;
			for (AbstractNode col : tableNode.getSdColumnNodeList()) {
				SdColumnNode columnNode = (SdColumnNode) col;
				removeColumnNodeRelationships(columnNode);
			}
			sdTableNodes.remove(node);

			firePropertyChange(TABLE_NODES_MODIFIED, null, node);
			firePropertyChange(PROP_DIRTY, null, true);
		}
	}

	public <T> void copy(List<? super T> dest, List<? extends T> src) {
		for (T obj : src) {
			dest.add(obj);
		}
	}

	public void removeColumnNodeRelationships(SdColumnNode sdColumnNode) {
		List<Relationship> targetRelationships = new ArrayList<Relationship>(
				sdColumnNode.getTargetRelationships().size());
		// Stupid bug in Collections.copy().
		copy(targetRelationships, sdColumnNode.getTargetRelationships());
		for (Relationship relationship : targetRelationships) {
			removeRelationship(relationship);
		}
		List<Relationship> sourceRelationships = new ArrayList<Relationship>(
				sdColumnNode.getSourceRelationships().size());
		// Stupid bug in Collections.copy().
		copy(sourceRelationships, sdColumnNode.getSourceRelationships());
		for (Relationship relationship : sourceRelationships) {
			removeRelationship(relationship);
		}
	}

	public SdTableNode getSelectedTableNode() {
		return selectedTableNode;
	}

	public void setSelectedTableNode(SdTableNode selectedTableNode) {
		this.selectedTableNode = selectedTableNode;
		firePropertyChange(SELECTED_TABLE_NODE, null, selectedTableNode);
	}

	/**
	 * @param selectedColumnNode
	 *            the selectedColumnNode to set
	 */
	public void setSelectedColumnNode(SdColumnNode selectedColumnNode) {
		this.selectedColumnNode = selectedColumnNode;
		firePropertyChange(SELECTED_COLUMN_NODE, null, selectedColumnNode);
	}

	/**
	 * Add the relationship to the Schema Model
	 * 
	 * @param source
	 * @param target
	 */
	public void addRelationship(SdColumnNode source, SdColumnNode target) {
		IColumn pkColumn = (IColumn) target.getDatbaseObject();
		IColumn fkColumn = (IColumn) source.getDatbaseObject();

		fkColumn.setForeignKeyName(Messages
				.getString("RelationshipCreateCommand.2")); //$NON-NLS-1$
		fkColumn.setForeignKeyFlag(true);
		IForeignKeyReference fkReference = new Column.ForeignKeyReference(null,
				pkColumn.getTable().getName(), pkColumn.getName(), pkColumn
						.getPrimaryKeyName());
		fkColumn.addForeignKeyReference(fkReference);

		Relationship relationship = new Relationship(source, target);
		relationship.setForeignKeyReference(fkReference);
		source.addRelationship(relationship);
		target.addRelationship(relationship);
		relationships.add(relationship);
		firePropertyChange(PROP_DIRTY, null, true);
	}

	/**
	 * Remove the relationship from the schema model
	 * 
	 * @param relationship
	 */
	public void removeRelationship(Relationship relationship) {
		relationship.getSource().removeRelationship(relationship);
		relationship.getTarget().removeRelationship(relationship);
		relationships.remove(relationship);
		firePropertyChange(PROP_DIRTY, null, true);
	}

	/**
	 * @return the selectedColumnNode
	 */
	public SdColumnNode getSelectedColumnNode() {
		return selectedColumnNode;
	}

	public String getUniqueTableName() {
		List<String> tableNames = new ArrayList<String>();
		for (AbstractNode node : sdTableNodes) {
			tableNames.add(node.getName());
		}
		int counter = 0;
		String newTableName = TABLE_BASE_NAME + ++counter;
		while (tableNames.contains(newTableName)) {
			newTableName = TABLE_BASE_NAME + counter++;
		}
		return newTableName;
	}

	public boolean isUniqueTableName(String newName) {
		for (AbstractNode node : sdTableNodes) {
			if (node.getName().trim().equals(newName.trim())) {
				return false;
			}
		}
		return true;
	}

	public SdTableNode findSdTableNode(TableNode tabelNode) {
		for (AbstractNode sdTableNode : sdTableNodes) {
			if (((SdTableNode) sdTableNode).getTableNode() == tabelNode) {
				return (SdTableNode) sdTableNode;
			}
		}
		return null;
	}

	/**
	 * Persist the Schema model info to the XML
	 * 
	 * @param file
	 */
	public void persist(File file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element schemataElement = doc.createElement("schemata"); //$NON-NLS-1$
			doc.appendChild(schemataElement);

			Element schemaElement = doc.createElement("schema"); //$NON-NLS-1$
			schemaElement.setAttribute("name", getName()); //$NON-NLS-1$
			schemataElement.appendChild(schemaElement);
			for (AbstractNode tableNode : sdTableNodes) {
				Element tableElement = doc.createElement("table"); //$NON-NLS-1$
				SdTableNode sdTableNode = (SdTableNode) tableNode;
				persistTableNode(sdTableNode, tableElement);
				schemaElement.appendChild(tableElement);
				if (sdTableNode.hasColumnNodes()) {
					for (AbstractNode columnNode : sdTableNode
							.getSdColumnNodeList()) {
						Element columnElement = doc.createElement("column"); //$NON-NLS-1$
						SdColumnNode sdColumnNode = (SdColumnNode) columnNode;
						persistColumnNode(sdColumnNode, columnElement);
						tableElement.appendChild(columnElement);
					}
				}
			}

			for (Relationship relationship : relationships) {
				Element relationshipElement = doc.createElement("relationship"); //$NON-NLS-1$
				persistRelationship(relationship, relationshipElement);
				schemataElement.appendChild(relationshipElement);
			}

			Source source = new DOMSource(doc);

			Result result = new StreamResult(file);

			Transformer xformer;
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			xformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			xformer.transform(source, result);

		} catch (ParserConfigurationException exc) {
			ErrorManager.showException(exc);
		} catch (TransformerConfigurationException exc) {
			ErrorManager.showException(exc);
		} catch (TransformerFactoryConfigurationError exc) {
			ErrorManager.showException(exc);
		} catch (TransformerException exc) {
			ErrorManager.showException(exc);
		}
	}

	/**
	 * Persist the Table info to the XML
	 * 
	 * @param sdTableNode
	 * @param tableElement
	 */
	private void persistTableNode(SdTableNode sdTableNode, Element tableElement) {
		tableElement.setAttribute("name", sdTableNode.getName()); //$NON-NLS-1$
		if (sdTableNode.getSize() != null) {
			tableElement.setAttribute("width", String.valueOf(sdTableNode //$NON-NLS-1$
					.getSize().width));
			tableElement.setAttribute("height", String.valueOf(sdTableNode //$NON-NLS-1$
					.getSize().height));
		}
		if (sdTableNode.getLocation() != null) {
			tableElement.setAttribute("x", String.valueOf(sdTableNode //$NON-NLS-1$
					.getLocation().x));
			tableElement.setAttribute("y", String.valueOf(sdTableNode //$NON-NLS-1$
					.getLocation().y));
		}
	}

	/**
	 * Persist the Column info to the XML
	 * 
	 * @param sdColumnNode
	 * @param columnElement
	 */
	private void persistColumnNode(SdColumnNode sdColumnNode,
			Element columnElement) {

		IColumn column = sdColumnNode.getColumnNode().getColumn();
		columnElement.setAttribute("name", column.getName()); //$NON-NLS-1$

		if (column.isPrimaryKey()) {
			columnElement.setAttribute("key", "primary"); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (column.isForeignKey()) {
			columnElement.setAttribute("key", "foreign"); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (column.isIndexKey()) {
			columnElement.setAttribute("key", "index"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		columnElement.setAttribute("size", String.valueOf(column //$NON-NLS-1$
				.getColumnSize()));
		columnElement.setAttribute("defaultValue", column.getDefaultValue()); //$NON-NLS-1$
		columnElement.setAttribute("type", column.getType().getName()); //$NON-NLS-1$
		if (column.isNullAllowed()) {
			columnElement.setAttribute("nullAllowed", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			columnElement.setAttribute("nullAllowed", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (column.isAutoIncrement()) {
			columnElement.setAttribute("autoIncrement", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			columnElement.setAttribute("autoIncrement", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		columnElement.setAttribute("primaryKeyName", column.getPrimaryKeyName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Persist the Relationship info to the XML
	 * 
	 * @param relationship
	 * @param relationshipElement
	 */
	private void persistRelationship(Relationship relationship,
			Element relationshipElement) {
		SdColumnNode source = relationship.getSource();
		Element sourceColumnElement = relationshipElement.getOwnerDocument()
				.createElement("sourceColumn"); //$NON-NLS-1$
		persistColumnInfo(source, sourceColumnElement);
		relationshipElement.appendChild(sourceColumnElement);

		SdColumnNode target = relationship.getTarget();
		Element targetColumnElement = relationshipElement.getOwnerDocument()
				.createElement("targetColumn"); //$NON-NLS-1$
		persistColumnInfo(target, targetColumnElement);
		relationshipElement.appendChild(targetColumnElement);
	}

	private void persistColumnInfo(SdColumnNode sdColumnNode,
			Element sdColumnNodeElement) {
		sdColumnNodeElement.setAttribute("name", sdColumnNode.getName()); //$NON-NLS-1$
		ITable sourceTable = sdColumnNode.getColumnNode().getColumn()
				.getTable();
		sdColumnNodeElement.setAttribute("table", sourceTable.getName()); //$NON-NLS-1$
	}

	/**
	 * Restore the Scheme model from the saved XML
	 * 
	 * @param file
	 */
	public void restore(File file) {
		try {
			restoring = true;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Document doc = factory.newDocumentBuilder().parse(file);
			Element schemataElement = (Element) doc.getElementsByTagName(
					"schemata").item(0); //$NON-NLS-1$
			Element schemaElement = (Element) schemataElement
					.getElementsByTagName("schema").item(0); //$NON-NLS-1$
			setName(schemaElement.getAttribute("name"));
			NodeList tableNodeList = schemaElement
					.getElementsByTagName("table"); //$NON-NLS-1$
			for (int i = 0; i < tableNodeList.getLength(); i++) {
				Element tableNodeElement = (Element) tableNodeList.item(i);
				SdTableNode sdTableNode = restoreTableNode(tableNodeElement);
				NodeList columnNodeList = tableNodeElement
						.getElementsByTagName("column"); //$NON-NLS-1$
				for (int j = 0; j < columnNodeList.getLength(); j++) {
					Element columnNodeElement = (Element) columnNodeList
							.item(j);
					restoreColumnNode(sdTableNode, columnNodeElement);
				}
			}
			NodeList relationshipList = schemataElement
					.getElementsByTagName("relationship"); //$NON-NLS-1$
			for (int i = 0; i < relationshipList.getLength(); i++) {
				Element relationshipElement = (Element) relationshipList
						.item(i);
				restoreRelationship(relationshipElement);
			}
			restoring = false;
		} catch (SAXException exc) {
			ErrorManager.showException(exc);
		} catch (ParserConfigurationException exc) {
			ErrorManager.showException(exc);
		} catch (IOException exc) {
			ErrorManager.showException(exc);
		} finally {
			restoring = false;
		}
	}

	/**
	 * Restore the Table Node form the stored XML
	 * 
	 * @param tableNodeElement
	 * @return
	 */
	private SdTableNode restoreTableNode(Element tableNodeElement) {
		String tableName = tableNodeElement.getAttribute("name"); //$NON-NLS-1$
		TableNode tableNode = new TableNode(new Table(this.getDatabaseInfo(),
				null, tableName));
		SdTableNode sdTableNode = new SdTableNode(tableNode, this);
		String width = tableNodeElement.getAttribute("width"); //$NON-NLS-1$
		String height = tableNodeElement.getAttribute("height"); //$NON-NLS-1$
		String xPos = tableNodeElement.getAttribute("x"); //$NON-NLS-1$
		String yPos = tableNodeElement.getAttribute("y"); //$NON-NLS-1$
		sdTableNode.setSize(new Dimension(Integer.parseInt(width), Integer
				.parseInt(height)));
		sdTableNode.setLocation(new Point(Integer.parseInt(xPos), Integer
				.parseInt(yPos)));
		addTableNode(sdTableNode);
		return sdTableNode;
	}

	/**
	 * Restore the Column node from the saved XML
	 * 
	 * @param sdTableNode
	 * @param columnNodeElement
	 */
	private void restoreColumnNode(SdTableNode sdTableNode,
			Element columnNodeElement) {
		String columnName = columnNodeElement.getAttribute("name"); //$NON-NLS-1$
		IColumn column = new Column(this.getDatabaseInfo(), null, columnName);
		ColumnNode columnNode = new ColumnNode(column);
		SdColumnNode sdColumnNode = new SdColumnNode(columnNode);

		String key = columnNodeElement.getAttribute("key"); //$NON-NLS-1$
		if (key.equals("primary")) { //$NON-NLS-1$
			column.setPrimaryKeyFlag(true);
		} else if (key.equals("foreign")) { //$NON-NLS-1$
			column.setForeignKeyFlag(true);
		} else if (key.equals("index")) { //$NON-NLS-1$
			column.setIndexKeyFlag(true);
		}

		String size = columnNodeElement.getAttribute("size"); //$NON-NLS-1$
		column.setSize(Integer.parseInt(size));

		String defaultvalue = columnNodeElement.getAttribute("defaultValue"); //$NON-NLS-1$
		column.setDefaultValue(defaultvalue);

		String type = columnNodeElement.getAttribute("type"); //$NON-NLS-1$
		column.setSqlType(this.getDatabaseInfo().findSqlTypeByName(type));

		String nullAllowed = columnNodeElement.getAttribute("nullAllowed"); //$NON-NLS-1$
		if (nullAllowed.equals("true")) { //$NON-NLS-1$
			column.setNullAllowed(true);
		} else if (nullAllowed.equals("false")) { //$NON-NLS-1$
			column.setNullAllowed(false);
		}

		String autoIncrement = columnNodeElement.getAttribute("autoIncrement"); //$NON-NLS-1$
		if (autoIncrement.equals("true")) { //$NON-NLS-1$
			column.setAutoIncrement(true);
		} else if (autoIncrement.equals("false")) { //$NON-NLS-1$
			column.setAutoIncrement(false);
		}
		
		String primaryKeyName = columnNodeElement.getAttribute("primaryKeyName"); //$NON-NLS-1$ //$NON-NLS-2$
		column.setPrimaryKeyName(primaryKeyName);
		
		sdTableNode.addColumnNode(sdColumnNode);
	}

	private void restoreRelationship(Element relationshipElement) {

		Element sourceColumnElement = (Element) relationshipElement
				.getElementsByTagName("sourceColumn").item(0); //$NON-NLS-1$
		String sourceColumnName = sourceColumnElement.getAttribute("name"); //$NON-NLS-1$
		String sourceColumnTable = sourceColumnElement.getAttribute("table"); //$NON-NLS-1$

		SdTableNode sourceTableNode = findTableNodeByName(sourceColumnTable);
		SdColumnNode sourceColumnNode = sourceTableNode
				.findColumnNodeByName(sourceColumnName);

		Element targetColumnElement = (Element) relationshipElement
				.getElementsByTagName("targetColumn").item(0); //$NON-NLS-1$
		String targetColumnName = targetColumnElement.getAttribute("name"); //$NON-NLS-1$
		String targetColumnTable = targetColumnElement.getAttribute("table"); //$NON-NLS-1$

		SdTableNode targetTableNode = findTableNodeByName(targetColumnTable);
		SdColumnNode targetColumnNode = targetTableNode
				.findColumnNodeByName(targetColumnName);

		addRelationship(sourceColumnNode, targetColumnNode);
	}

	/**
	 * Find the wrapper Table node based on its name
	 * 
	 * @param name
	 * @return
	 */
	public SdTableNode findTableNodeByName(String name) {
		for (AbstractNode tableNode : sdTableNodes) {
			if (tableNode.getName().equals(name)) {
				return (SdTableNode) tableNode;
			}
		}
		return null;
	}

	public String generateSql() {
		List<AbstractNode> tableList = getTableNodes();
		StringBuffer sb = new StringBuffer();

		sb.append("DROP DATABASE IF EXISTS " + getName() + ";\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("CREATE DATABASE " + getName() + ";\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("USE " + getName() + ";\n\n"); //$NON-NLS-1$ //$NON-NLS-2$

		ISqlHelper sqlHelper = databaseInfo.getConnectionConfig()
				.getConnectionType().getSqlHelper();

		for (AbstractNode node : tableList) {
			if (node instanceof SdTableNode) {
				SdTableNode tableNode = (SdTableNode) node;
				ITable table = (ITable) tableNode.getTableNode()
						.getDatbaseObject();
				sb.append(sqlHelper.generateTableCreateStatement(table, false));
				sb.append("\n\n"); //$NON-NLS-1$
			}
		}

		for (AbstractNode node : tableList) {
			if (node instanceof SdTableNode) {
				SdTableNode tableNode = (SdTableNode) node;
				ITable table = (ITable) tableNode.getTableNode()
						.getDatbaseObject();
				String fkCreationString = sqlHelper
						.generateForeignKeyAddStatement(table);
				if (fkCreationString != null) {
					sb.append(sqlHelper.generateForeignKeyAddStatement(table));
					sb.append("\n\n"); //$NON-NLS-1$
				}
			}
		}

		sb.append("COMMIT;\n\n"); //$NON-NLS-1$

		return sb.toString();
	}

	public void setSelected(boolean b) {
		firePropertyChange(SELECTED_SCHEMATA, null, this);
	}

	public class TableRefreshJobChangeListener extends JobChangeAdapter {

		List<Job> jobs = new CopyOnWriteArrayList<Job>();

		@Override
		public void aboutToRun(IJobChangeEvent event) {
			if (event.getJob().getName().equals(TableNode.TABLE_REFRESH_JOB)) {
				jobs.add(event.getJob());
			}
		}

		@Override
		public void done(IJobChangeEvent event) {
			if (event.getJob().getName().equals(TableNode.TABLE_REFRESH_JOB)) {
				jobs.remove(event.getJob());
				if (jobs.isEmpty()) {
					for (AbstractNode node : sdTableNodes) {
						if (node instanceof SdTableNode) {
							((SdTableNode) node).establishRelationship();
						}
					}
				}
			}
		}
	}

	public void setDirty(boolean dirtyFlag) {
		firePropertyChange(PROP_DIRTY, null, dirtyFlag);
	}
}
