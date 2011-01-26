
package com.nayaware.dbtools.querybuilder.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.SchemaNode;
import com.nayaware.dbtools.nodes.TableGroupNode;
import com.nayaware.dbtools.nodes.TableNode;
import com.nayaware.dbtools.nodes.WaitingNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Model to hold the Query related data
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryData {

	public final static String TABLE_NODE_ADD = "tableNodeAdd"; //$NON-NLS-1$
	public final static String TABLE_NODE_REMOVE = "tableNodeRemove"; //$NON-NLS-1$
	public static final String COLUMN_NODE_ADD = "columnNodeAdd"; //$NON-NLS-1$
	public static final String COLUMN_NODE_REMOVE = "columnNodeRemove"; //$NON-NLS-1$
	public static final String PROP_DIRTY = "dirty"; //$NON-NLS-1$

	private List<AbstractNode> schemaNodes = new ArrayList<AbstractNode>();
	private List<AbstractNode> tableNodes = new ArrayList<AbstractNode>();
	private List<QbColumnNode> selectedColumnNodes = new ArrayList<QbColumnNode>();

	private List<Join> joins = new ArrayList<Join>();

	private IDatabaseInfo databaseInfo;

	protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(
			this);

	private ConnectionNode connectionNode;

	private int initializationProgress = 0;
	private boolean restoring = false;

	public QueryData(ConnectionNode node) {
		setDatabaseInfo(node.getDatbaseObject().getDatabaseInfo());
		connectionNode = node;
	}

	public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}

	public IDatabaseInfo getDatabaseInfo() {
		return databaseInfo;
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

	public List<AbstractNode> getTableNodes() {
		return tableNodes;
	}

	public void setTableNodes(List<AbstractNode> tableNodes) {
		this.tableNodes = tableNodes;
		firePropertyChange(PROP_DIRTY, null, true);
	}

	/**
	 * Add the table node to the query
	 * 
	 * @param qbTableNode
	 */
	public void addTableNode(QbTableNode qbTableNode) {
		if (!tableNodes.contains(qbTableNode)) {
			tableNodes.add(qbTableNode);
			AbstractNode parent = qbTableNode.getTableNode().getParent();
			if (parent instanceof TableGroupNode) {
				parent = parent.getParent();
			}
			if (!schemaNodes.contains(parent)) {
				schemaNodes.add(parent);
			}
			firePropertyChange(PROP_DIRTY, null, true);
			firePropertyChange(TABLE_NODE_ADD, null, qbTableNode);
		}
	}

	/**
	 * Remove the table node from the query
	 * 
	 * @param qbTableNode
	 */
	public void removeTableNode(QbTableNode qbTableNode) {
		if (tableNodes.contains(qbTableNode)) {
			List<QbColumnNode> joinedColumns = new ArrayList<QbColumnNode>(
					qbTableNode.getJoinedColumnNodes().size());
			copy(joinedColumns, qbTableNode.getJoinedColumnNodes());
			for (QbColumnNode columnNode : joinedColumns) {
				List<Join> sourceJoins = new ArrayList<Join>(columnNode
						.getSourceJoins().size());

				// Stupid bug in Collections.copy().
				// Collections.copy(sourceJoins, sdColumnNode.getSourceJoins());
				copy(sourceJoins, columnNode.getSourceJoins());

				for (Join join : sourceJoins) {
					join.getSource().removeJoin(join);
					join.getTarget().removeJoin(join);
				}

				List<Join> targetJoins = new ArrayList<Join>(columnNode
						.getTargetJoins().size());
				copy(targetJoins, columnNode.getTargetJoins());

				for (Join join : targetJoins) {
					join.getSource().removeJoin(join);
					join.getTarget().removeJoin(join);
				}
			}

			List<QbColumnNode> selectedColumns = qbTableNode
					.getSelectedColumnNodes();
			for (QbColumnNode columnNode : selectedColumns) {
				removeSelectedColumnNode(columnNode);
			}

			tableNodes.remove(qbTableNode);
			firePropertyChange(PROP_DIRTY, null, true);

			firePropertyChange(TABLE_NODE_REMOVE, null, qbTableNode);
		}
	}

	public <T> void copy(List<? super T> dest, List<? extends T> src) {
		for (T obj : src) {
			dest.add(obj);
		}
	}

	public void addSelectedColumnNode(QbColumnNode columnNode) {
		if (!selectedColumnNodes.contains(columnNode)) {
			selectedColumnNodes.add(columnNode);
			columnNode.setSelected(true);
			firePropertyChange(PROP_DIRTY, null, true);
			firePropertyChange(COLUMN_NODE_ADD, null, columnNode);
		}
	}

	public void removeSelectedColumnNode(QbColumnNode columnNode) {
		if (selectedColumnNodes.contains(columnNode)) {
			selectedColumnNodes.remove(columnNode);
			columnNode.setSelected(false);
			firePropertyChange(PROP_DIRTY, null, true);
			firePropertyChange(COLUMN_NODE_REMOVE, null, columnNode);
		}
	}

	public Object[] getColumnNodes() {
		return selectedColumnNodes.toArray(new QbColumnNode[selectedColumnNodes
				.size()]);
	}

	public String getSelectStatement() {
		StringBuffer selectStatement = new StringBuffer();
		if (tableNodes.size() > 0) {
			StringBuffer columnExpression = new StringBuffer();

			for (int i = 0; i < tableNodes.size(); i++) {
				QbTableNode tableNode = (QbTableNode) tableNodes.get(i);
				List<QbColumnNode> selectedColumnNodes = tableNode
						.getSelectedColumnNodes();
				if (selectedColumnNodes.size() > 0) {
					for (int j = 0; j < selectedColumnNodes.size(); j++) {
						QbColumnNode wrapperNode = selectedColumnNodes.get(j);
						columnExpression.append("\n   " //$NON-NLS-1$
								+ wrapperNode.getColumnNode()
										.getQualifiedName() + ","); //$NON-NLS-1$
					}
				}
			}

			StringBuffer tableExpression = new StringBuffer();
			if (hasJoin(Join.INNER)) {
				tableExpression.append(getJoinExpression(Join.INNER));
			}
			if (hasJoin(Join.LEFT)) {
				tableExpression.append(getJoinExpression(Join.LEFT));
			}
			if (hasJoin(Join.RIGHT)) {
				tableExpression.append(getJoinExpression(Join.RIGHT));
			}

			if (tableExpression.length() < 1) {
				for (AbstractNode node : tableNodes) {
					QbTableNode tableNode = (QbTableNode) node;
					tableExpression.append(" " //$NON-NLS-1$
							+ tableNode.getTableNode().getQualifiedName());
					tableExpression.append(","); //$NON-NLS-1$
				}
			}

			// GROUP BY CLAUSE
			List<QbColumnNode> groupByColumnNodes = new ArrayList<QbColumnNode>();
			for (QbColumnNode columnNode : selectedColumnNodes) {
				if (columnNode.isUsedForGroupBy()) {
					groupByColumnNodes.add(columnNode);
				}
			}

			StringBuffer groupByExpression = null;
			if (groupByColumnNodes.size() > 0) {
				groupByExpression = new StringBuffer();
				groupByExpression.append("\nGROUP BY"); //$NON-NLS-1$
				for (QbColumnNode columnNode : groupByColumnNodes) {
					groupByExpression.append("\n    " //$NON-NLS-1$
							+ columnNode.getColumnNode().getQualifiedName()
							+ ","); //$NON-NLS-1$
				}
			}

			// ORDER BY CLAUSE
			List<QbColumnNode> orderByColumnNodes = new ArrayList<QbColumnNode>();
			for (QbColumnNode columnNode : selectedColumnNodes) {
				if (!columnNode.getSortCriteria().equals("NONE") || (columnNode.getSortOrder() > 0)) { //$NON-NLS-1$
					orderByColumnNodes.add(columnNode);
				}
			}

			StringBuffer orderByExpression = null;
			if (orderByColumnNodes.size() > 0) {
				sortOrderByColumnNodes(orderByColumnNodes);

				orderByExpression = new StringBuffer();
				orderByExpression.append("\nORDER BY"); //$NON-NLS-1$
				for (QbColumnNode columnNode : orderByColumnNodes) {
					orderByExpression.append("\n    " //$NON-NLS-1$
							+ columnNode.getColumnNode().getQualifiedName());
					if (!columnNode.getSortCriteria().equals("NONE")) {
						orderByExpression
								.append(" " + columnNode.getSortCriteria()); //$NON-NLS-1$
					}
					orderByExpression.append(","); //$NON-NLS-1$
				}
			}

			// WHERE CLAUSE
			List<QbColumnNode> whereClauseColumnNodes = new ArrayList<QbColumnNode>();
			for (QbColumnNode columnNode : selectedColumnNodes) {
				if (!columnNode.getWhereClauseText().equals("<NONE>")
						&& !columnNode.getWhereClauseText().trim().equals("")) { //$NON-NLS-1$
					whereClauseColumnNodes.add(columnNode);
				}
			}

			StringBuffer whereClauseExpression = null;
			if (whereClauseColumnNodes.size() > 0) {
				sortOrderByColumnNodes(whereClauseColumnNodes);

				whereClauseExpression = new StringBuffer();
				whereClauseExpression.append("\nWHERE"); //$NON-NLS-1$
				int count = 0;
				for (QbColumnNode columnNode : whereClauseColumnNodes) {
					whereClauseExpression.append("\n    " //$NON-NLS-1$
							+ columnNode.getColumnNode().getQualifiedName()
							+ " " //$NON-NLS-1$
							+ columnNode.getWhereClauseText());
					if (++count < whereClauseColumnNodes.size()) {
						whereClauseExpression.append(" AND "); //$NON-NLS-1$
					}
				}
			}

			selectStatement.append("SELECT"); //$NON-NLS-1$
			String columnNameStr = columnExpression.toString();
			if (columnNameStr.length() > 1) {
				selectStatement.append(columnNameStr.substring(0, columnNameStr
						.length() - 1)); // Strip the last ","
			} else {
				selectStatement.append(" * "); //$NON-NLS-1$
			}

			selectStatement.append("\nFROM"); //$NON-NLS-1$
			String tableNameStr = tableExpression.toString();
			selectStatement.append(tableNameStr.substring(0, tableNameStr
					.length() - 1)); // Strip the last ","

			if (groupByExpression != null) {
				String groupByExpressionStr = groupByExpression.toString();
				selectStatement.append(groupByExpressionStr.substring(0,
						groupByExpressionStr.length() - 1)); // Strip the last ,
			}

			if (orderByExpression != null) {
				String orderByExpressionStr = orderByExpression.toString();
				selectStatement.append(orderByExpressionStr.substring(0,
						orderByExpressionStr.length() - 1)); // Strip the last ,
			}

			if (whereClauseExpression != null) {
				String whereClauseExpressionStr = whereClauseExpression
						.toString();
				selectStatement.append(whereClauseExpressionStr); // Strip the
				// // last ,
			}
		}
		return selectStatement.toString();
	}

	private String getJoinExpression(int joinType) {
		StringBuffer joinExpression = new StringBuffer();
		List<QbTableNode> tableNodesCopy = new ArrayList<QbTableNode>(
				tableNodes.size());
		for (AbstractNode tableNode : tableNodes) {
			tableNodesCopy.add((QbTableNode) tableNode);
		}

		while (!tableNodesCopy.isEmpty()) {
			// Find the Table Node chain that are joined and remove the
			// chained tables from table node copy
			List<QbTableNode> tableNodeJoinedChain = new ArrayList<QbTableNode>();
			populateTableNodeJoinedChain(tableNodesCopy, tableNodeJoinedChain,
					tableNodesCopy.get(0), joinType);
			for (int i = 0; i < tableNodeJoinedChain.size(); i++) {
				QbTableNode joinedTableNode = tableNodeJoinedChain.get(i);
				if (i > 0) {
					switch (joinType) {
					case Join.INNER:
						joinExpression.append("\n      INNER JOIN "); //$NON-NLS-1$
						break;
					case Join.LEFT:
						joinExpression.append("\n      LEFT OUTER JOIN "); //$NON-NLS-1$
						break;
					case Join.RIGHT:
						joinExpression.append("\n      RIGHT OUTER JOIN "); //$NON-NLS-1$
						break;
					}

					joinExpression.append(joinedTableNode.getTableNode()
							.getQualifiedName());
					List<QbColumnNode> joinedColumnNodes = joinedTableNode
							.getJoinedColumnNodes();
					List<Join> joins = new ArrayList<Join>();
					for (int j = 0; j < joinedColumnNodes.size(); j++) {
						QbColumnNode joinedColumnNode = joinedColumnNodes
								.get(j);
						joins.addAll(joinedColumnNode.getSourceJoins());
						joins.addAll(joinedColumnNode.getTargetJoins());
					}
					for (int k = 0; k < joins.size(); k++) {
						Join join = joins.get(k);
						QbColumnNode sourceColumnNode = join.getSource();
						QbColumnNode targetColumnNode = join.getTarget();
						if (k > 0) {
							joinExpression.append("\n                 AND "); //$NON-NLS-1$
						} else {
							joinExpression.append("\n            ON "); //$NON-NLS-1$
						}
						joinExpression.append(sourceColumnNode.getColumnNode()
								.getQualifiedName());
						joinExpression.append(" = "); //$NON-NLS-1$
						joinExpression.append(targetColumnNode.getColumnNode()
								.getQualifiedName());
					}
				} else {
					joinExpression
							.append("\n   " //$NON-NLS-1$
									+ joinedTableNode.getTableNode()
											.getQualifiedName());
				}
			}
			joinExpression.append(","); //$NON-NLS-1$
		}

		return joinExpression.toString();
	}

	private void sortOrderByColumnNodes(List<QbColumnNode> columnNodesToSort) {
		Collections.sort(columnNodesToSort, new Comparator<QbColumnNode>() {

			public int compare(QbColumnNode o1, QbColumnNode o2) {
				return o1.getSortOrder() - o2.getSortOrder();
			}

		});
	}

	private void populateTableNodeJoinedChain(List<QbTableNode> tableNodesCopy,
			List<QbTableNode> tableNodeChain, QbTableNode tableNode,
			int joinType) {
		if (!tableNodeChain.contains(tableNode)) {
			tableNodeChain.add(tableNode);
			tableNodesCopy.remove(tableNode);
		}
		if (tableNode.hasJoinedColumnNodes()) {
			List<QbColumnNode> joinedColumnNodes = tableNode
					.getJoinedColumnNodes();
			for (QbColumnNode columnNode : joinedColumnNodes) {
				List<Join> sourceJoins = columnNode.getSourceJoins();
				for (Join join : sourceJoins) {
					if (join.getType() == joinType) {
						QbTableNode joiningTableNode = join.getTarget()
								.getParent();
						if (tableNodesCopy.contains(joiningTableNode)) {
							populateTableNodeJoinedChain(tableNodesCopy,
									tableNodeChain, join.getTarget()
											.getParent(), joinType);
						}
					}
				}
			}
		}
	}

	private boolean hasJoin(int joinType) {
		for (AbstractNode node : tableNodes) {
			QbTableNode tableNode = (QbTableNode) node;
			if (tableNode.hasJoinedColumnNodes()) {
				List<QbColumnNode> joinedColumnNodes = tableNode
						.getJoinedColumnNodes();
				for (QbColumnNode columnNode : joinedColumnNodes) {
					List<Join> sourceJoins = columnNode.getSourceJoins();
					for (Join join : sourceJoins) {
						if (join.getType() == joinType) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void persist(File file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element schemataElement = doc.createElement("schemata"); //$NON-NLS-1$
			doc.appendChild(schemataElement);

			if (schemaNodes.size() > 0) {
				for (AbstractNode node : schemaNodes) {
					SchemaNode schemaNode = (SchemaNode) node;
					Element schemaElement = doc.createElement("schema"); //$NON-NLS-1$
					schemaElement.setAttribute("name", schemaNode.getName()); //$NON-NLS-1$
					schemataElement.appendChild(schemaElement);
				}
			}
			if (tableNodes.size() > 0) {
				for (int i = 0; i < tableNodes.size(); i++) {
					QbTableNode qbTableNode = (QbTableNode) tableNodes.get(i);
					Element tableElement = doc.createElement("table"); //$NON-NLS-1$
					tableElement.setAttribute("name", qbTableNode.getName()); //$NON-NLS-1$
					ISchema schema = qbTableNode.getTableNode().getTable()
							.getSchema();
					tableElement.setAttribute("schema", schema.getName()); //$NON-NLS-1$
					if (qbTableNode.getSize() != null) {
						tableElement.setAttribute("width", String //$NON-NLS-1$
								.valueOf(qbTableNode.getSize().width));
						tableElement.setAttribute("height", String //$NON-NLS-1$
								.valueOf(qbTableNode.getSize().height));
					}
					if (qbTableNode.getLocation() != null) {
						tableElement.setAttribute("x", String //$NON-NLS-1$
								.valueOf(qbTableNode.getLocation().x));
						tableElement.setAttribute("y", String //$NON-NLS-1$
								.valueOf(qbTableNode.getLocation().y));
					}
					schemataElement.appendChild(tableElement);
				}
			}

			if (selectedColumnNodes.size() > 0) {
				for (int j = 0; j < selectedColumnNodes.size(); j++) {
					QbColumnNode qbColumnNode = selectedColumnNodes.get(j);
					Element selectedColumnElement = doc
							.createElement("selectedColumn"); //$NON-NLS-1$
					selectedColumnElement.setAttribute("name", qbColumnNode //$NON-NLS-1$
							.getName());
					persistColumnInfo(qbColumnNode, selectedColumnElement);
					schemataElement.appendChild(selectedColumnElement);
				}
			}

			if (joins.size() > 0) {
				for (Join join : joins) {
					Element sourceJoinElement = doc.createElement("join"); //$NON-NLS-1$
					sourceJoinElement.setAttribute("type", String.valueOf(join //$NON-NLS-1$
							.getType()));

					QbColumnNode source = join.getSource();
					Element sourceColumnElement = doc
							.createElement("sourceColumn"); //$NON-NLS-1$
					persistColumnInfo(source, sourceColumnElement);
					sourceJoinElement.appendChild(sourceColumnElement);

					QbColumnNode target = join.getTarget();
					Element targetColumnElement = doc
							.createElement("targetColumn"); //$NON-NLS-1$
					persistColumnInfo(target, targetColumnElement);
					sourceJoinElement.appendChild(targetColumnElement);

					schemataElement.appendChild(sourceJoinElement);
				}
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
		} finally {
			restoring = false;
		}
	}

	private void persistColumnInfo(QbColumnNode qbColumnNode,
			Element qbColumnNodeElement) {
		qbColumnNodeElement.setAttribute("name", qbColumnNode.getName()); //$NON-NLS-1$
		ITable sourceTable = qbColumnNode.getColumnNode().getColumn()
				.getTable();
		ISchema sourceSchema = sourceTable.getSchema();
		qbColumnNodeElement.setAttribute("schema", sourceSchema.getName()); //$NON-NLS-1$
		qbColumnNodeElement.setAttribute("table", sourceTable.getName()); //$NON-NLS-1$

		qbColumnNodeElement.setAttribute(
				"sortCriteria", qbColumnNode.getSortCriteria()); //$NON-NLS-1$
		qbColumnNodeElement.setAttribute(
				"sortOrder", String.valueOf(qbColumnNode.getSortOrder())); //$NON-NLS-1$
		qbColumnNodeElement
				.setAttribute(
						"usedForGroupBy", String.valueOf(qbColumnNode.isUsedForGroupBy())); //$NON-NLS-1$
		qbColumnNodeElement
				.setAttribute("criteria", qbColumnNode.getCriteria()); //$NON-NLS-1$
		qbColumnNodeElement.setAttribute(
				"whereClause", qbColumnNode.getWhereClauseText()); //$NON-NLS-1$
		qbColumnNodeElement.setAttribute("orFlag", qbColumnNode.getOrFlag()); //$NON-NLS-1$
	}

	public void restore(File file) {
		try {
			restoring = true;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Document doc = factory.newDocumentBuilder().parse(file);
			Element schemataElement = (Element) doc.getElementsByTagName(
					"schemata").item(0); //$NON-NLS-1$

			NodeList schemaNodeList = schemataElement
					.getElementsByTagName("schema"); //$NON-NLS-1$
			for (int i = 0; i < schemaNodeList.getLength(); i++) {
				Element schemaNodeElement = (Element) schemaNodeList.item(i);
				String schemaName = schemaNodeElement.getAttribute("name"); //$NON-NLS-1$
				SchemaNode schemaNode = connectionNode
						.findSchemaNodeByName(schemaName);
				initializeSchemaNode(schemaNode, schemataElement);
			}

			if (initializationProgress == 0) {
				doRestore(schemataElement);
			}

		} catch (SAXException exc) {
			ErrorManager.showException(exc);
		} catch (ParserConfigurationException exc) {
			ErrorManager.showException(exc);
		} catch (IOException exc) {
			ErrorManager.showException(exc);
		}
	}

	private void doRestore(Element schemataElement) {
		NodeList tableNodeList = schemataElement.getElementsByTagName("table"); //$NON-NLS-1$
		for (int i = 0; i < tableNodeList.getLength(); i++) {
			Element tableNodeElement = (Element) tableNodeList.item(i);
			restoreTableNodes(tableNodeElement);
		}
		NodeList selectedColumnNodeList = schemataElement
				.getElementsByTagName("selectedColumn"); //$NON-NLS-1$
		for (int i = 0; i < selectedColumnNodeList.getLength(); i++) {
			Element selectedColumnNodeElement = (Element) selectedColumnNodeList
					.item(i);
			retoreColumnSelectionState(selectedColumnNodeElement);
		}

		NodeList joinNodeList = schemataElement.getElementsByTagName("join"); //$NON-NLS-1$
		for (int i = 0; i < joinNodeList.getLength(); i++) {
			Element joinElement = (Element) joinNodeList.item(i);
			restoreJoinState(joinElement);
		}
		restoring = false;
	}

	/**
	 * Initialize the schema node if it is not already initialized
	 * 
	 * @param schemaNode
	 */
	private void initializeSchemaNode(final SchemaNode schemaNode,
			final Element schemataElement) {
		// schemaNode may not be initialized
		if (schemaNode.getTableNodeList() == null) {
			initializationProgress++;
			TableGroupNode tableGroupNode = schemaNode.getTableGroupNode();
			tableGroupNode
					.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							String property = evt.getPropertyName();
							if (property
									.equals(AbstractNode.NODE_CHILDREN_MODIFIED)
									&& !(evt.getNewValue() instanceof WaitingNode)) {
								for (AbstractNode node : schemaNode
										.getTableNodeList()) {
									initializeTableNodes((TableNode) node,
											schemataElement);
								}
								initializationProgress--;
							}
						}
					});
			schemaNode.getTableNodeList(true);
		} else {
			for (AbstractNode node : schemaNode.getTableNodeList()) {
				initializeTableNodes((TableNode) node, schemataElement);
			}
		}
	}

	/**
	 * Initialize the table node if it is not already initialized
	 * 
	 * @param sdTableNode
	 */
	private void initializeTableNodes(final TableNode tableNode,
			final Element schemataElement) {
		if (tableNode.getColumnNodeList() == null) {
			initializationProgress++;
			tableNode.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					String property = evt.getPropertyName();
					if (property.equals(AbstractNode.NODE_CHILDREN_MODIFIED)
							&& !(evt.getNewValue() instanceof WaitingNode)) {
						initializationProgress--;
						if (initializationProgress == 0) {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									doRestore(schemataElement);
								}
							});
						}
					}
				}
			});
			tableNode.getColumnNodeList(true);
		}
	}

	private void restoreTableNodes(Element tableNodeElement) {
		String tableName = tableNodeElement.getAttribute("name"); //$NON-NLS-1$
		String schemaName = tableNodeElement.getAttribute("schema"); //$NON-NLS-1$
		SchemaNode schemaNode = connectionNode.findSchemaNodeByName(schemaName);
		TableNode tableNode = schemaNode.findTableNodeByName(tableName);
		QbTableNode qbTableNode = new QbTableNode(tableNode);
		String width = tableNodeElement.getAttribute("width"); //$NON-NLS-1$
		String height = tableNodeElement.getAttribute("height"); //$NON-NLS-1$
		String xPos = tableNodeElement.getAttribute("x"); //$NON-NLS-1$
		String yPos = tableNodeElement.getAttribute("y"); //$NON-NLS-1$
		qbTableNode.setSize(new Dimension(Integer.parseInt(width), Integer
				.parseInt(height)));
		qbTableNode.setLocation(new Point(Integer.parseInt(xPos), Integer
				.parseInt(yPos)));
		addTableNode(qbTableNode);
	}

	/**
	 * Restore selected state of the column
	 * 
	 * @param qbTableNode
	 * @param tableNodeElement
	 */
	private void retoreColumnSelectionState(Element selectedColumnNodeElement) {
		String columnName = selectedColumnNodeElement.getAttribute("name"); //$NON-NLS-1$
		String tableName = selectedColumnNodeElement.getAttribute("table"); //$NON-NLS-1$

		QbTableNode qbTableNode = findTableNodeByName(tableName);
		QbColumnNode columnNode = qbTableNode.findColumnNodeByName(columnName);

		try {

			String sortCriteria = selectedColumnNodeElement
					.getAttribute("sortCriteria");
			int sortOrder = Integer.parseInt(selectedColumnNodeElement
					.getAttribute("sortOrder")); //$NON-NLS-1$
			boolean usedForGroupBy = Boolean.valueOf(selectedColumnNodeElement
					.getAttribute("usedForGroupBy")); //$NON-NLS-1$
			String criteria = selectedColumnNodeElement
					.getAttribute("criteria"); //$NON-NLS-1$
			String whereClause = selectedColumnNodeElement
					.getAttribute("whereClause"); //$NON-NLS-1$
			String orFlag = selectedColumnNodeElement.getAttribute("orFlag"); //$NON-NLS-1$

			columnNode.setSortCriteria(sortCriteria);
			columnNode.setSortOrder(sortOrder);
			columnNode.setUsedForGroupBy(usedForGroupBy);
			columnNode.setCriteria(criteria);
			columnNode.setWhereClauseText(whereClause);
			columnNode.setOrFlag(orFlag);
		} catch (Exception exc) {
			ErrorManager.showException(exc);
		}

		addSelectedColumnNode(columnNode);
	}

	/**
	 * Restore the join states of the columns
	 * 
	 * @param joinElement
	 * @return
	 */
	private void restoreJoinState(Element joinElement) {

		Element sourceColumnElement = (Element) joinElement
				.getElementsByTagName("sourceColumn").item(0); //$NON-NLS-1$
		String sourceColumnName = sourceColumnElement.getAttribute("name"); //$NON-NLS-1$
		String sourceColumnTable = sourceColumnElement.getAttribute("table"); //$NON-NLS-1$

		QbTableNode sourceTableNode = findTableNodeByName(sourceColumnTable);
		QbColumnNode sourceColumnNode = sourceTableNode
				.findColumnNodeByName(sourceColumnName);

		Element targetColumnElement = (Element) joinElement
				.getElementsByTagName("targetColumn").item(0); //$NON-NLS-1$
		String targetColumnName = targetColumnElement.getAttribute("name"); //$NON-NLS-1$
		String targetColumnTable = targetColumnElement.getAttribute("table"); //$NON-NLS-1$

		QbTableNode targetTableNode = findTableNodeByName(targetColumnTable);
		QbColumnNode targetColumnNode = targetTableNode
				.findColumnNodeByName(targetColumnName);

		int type = Integer.parseInt(joinElement.getAttribute("type")); //$NON-NLS-1$

		addJoin(sourceColumnNode, targetColumnNode, type);
	}

	/**
	 * Find the wrapper Table node based on its name
	 * 
	 * @param name
	 * @return
	 */
	public QbTableNode findTableNodeByName(String name) {
		for (AbstractNode tableNode : tableNodes) {
			if (tableNode.getName().equals(name)) {
				return (QbTableNode) tableNode;
			}
		}
		return null;
	}

	public void addJoin(QbColumnNode source, QbColumnNode target, int joinType) {
		Join join = new Join(source, target, joinType);
		source.addJoin(join);
		target.addJoin(join);
		joins.add(join);
		firePropertyChange(PROP_DIRTY, null, true);
	}

	public void removeJoin(Join join) {
		join.getSource().removeJoin(join);
		join.getTarget().removeJoin(join);
		joins.remove(join);
		firePropertyChange(PROP_DIRTY, null, true);
	}

	public void setDirty(boolean dirtyFlag) {
		firePropertyChange(PROP_DIRTY, null, dirtyFlag);
	}
}
