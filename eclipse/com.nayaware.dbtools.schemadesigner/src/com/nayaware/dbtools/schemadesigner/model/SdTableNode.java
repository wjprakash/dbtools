
package com.nayaware.dbtools.schemadesigner.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.IColumn.IForeignKeyReference;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ColumnNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.SchemaNode;
import com.nayaware.dbtools.nodes.TableNode;

/**
 * Table Node wrapper for Schema Designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SdTableNode extends AbstractNode {

	public final static String COLUMN_ADDED = "columnAdded"; //$NON-NLS-1$
	public final static String COLUMN_REMOVED = "columnRemoved"; //$NON-NLS-1$

	private static final int DEFAULT_WIDTH = 200;
	private static final int DEFAULT_HEIGHT = 250;

	private List<SdColumnNode> relationshipColumnNodes = new ArrayList<SdColumnNode>();

	private boolean selected = false;

	private TableNode tableNode;

	private Schemata schemata;

	private List<AbstractNode> sdColumnNodeList;

	public SdTableNode(TableNode tableNode, Schemata schemata) {
		super(tableNode.getDatbaseObject());
		this.tableNode = tableNode;
		this.schemata = schemata;
		setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}

	public TableNode getTableNode() {
		return tableNode;
	}

	public void setTableNode(TableNode tableNode) {
		this.tableNode = tableNode;
	}

	public List<SdColumnNode> getRelationshipColumnNodes() {
		return relationshipColumnNodes;
	}

	public void setRelationshipColumnNodes(List<SdColumnNode> joinedColumnNodes) {
		this.relationshipColumnNodes = joinedColumnNodes;
	}

	public void addRelationshipColumnNode(SdColumnNode columnNode) {
		if (!relationshipColumnNodes.contains(columnNode)) {
			relationshipColumnNodes.add(columnNode);
		}
	}

	public void removeRelationshipColumnNode(SdColumnNode columnNode) {
		if (relationshipColumnNodes.contains(columnNode)) {
			relationshipColumnNodes.remove(columnNode);
		}
	}

	public boolean hasJoinedColumnNodes() {
		return relationshipColumnNodes.size() > 0;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public synchronized void addColumnNode(SdColumnNode columnNode) {
		if (sdColumnNodeList == null) {
			sdColumnNodeList = new ArrayList<AbstractNode>();
		}
		IColumn column = columnNode.getColumnNode().getColumn();
		column.setTable(getTableNode().getTable());
		column.setDatabaseInfo(getTableNode().getTable().getDatabaseInfo());
		getTableNode().getTable().addColumn(column);
		columnNode.setParent(this);
		sdColumnNodeList.add(columnNode);
		firePropertyChange(COLUMN_ADDED, null, columnNode);
	}

	public synchronized void removeColumnNode(SdColumnNode sdColumnNode) {
		if (sdColumnNodeList != null) {
			sdColumnNodeList.remove(sdColumnNode);
			getTableNode().removeChild(sdColumnNode.getColumnNode());
			getTableNode().getTable().removeColumn(
					sdColumnNode.getColumnNode().getColumn());
			schemata.removeColumnNodeRelationships(sdColumnNode);
			firePropertyChange(COLUMN_REMOVED, null, sdColumnNode);
		}
	}

	public synchronized List<AbstractNode> getSdColumnNodeList() {
		return getSdColumnNodeList(false);
	}

	public synchronized List<AbstractNode> getSdColumnNodeList(boolean refresh) {
		if ((sdColumnNodeList == null) && refresh) {
			tableNode.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					String property = evt.getPropertyName();
					if (property.equals(AbstractNode.NODE_CHILDREN_MODIFIED)) {
						setColumnNodes(tableNode.getColumnNodeList(false));
						firePropertyChange(NODE_CHILDREN_MODIFIED, null,
								sdColumnNodeList);
					}
				}
			});
			// Refresh and get the Column Node list
			setColumnNodes(tableNode.getColumnNodeList(true));
		}
		return sdColumnNodeList;
	}

	private void setColumnNodes(List<AbstractNode> columnNodeList) {
		sdColumnNodeList = new ArrayList<AbstractNode>();
		for (AbstractNode node : columnNodeList) {
			if (node instanceof ColumnNode) {
				SdColumnNode sdColumnNode = new SdColumnNode((ColumnNode) node);
				sdColumnNode.setParent(this);
				sdColumnNodeList.add(sdColumnNode);
			} else {
				sdColumnNodeList.add(node);
			}
		}
	}

	public SdColumnNode findColumnNodeByName(String columnName) {
		if (sdColumnNodeList != null)
			for (AbstractNode node : sdColumnNodeList) {
				if (node instanceof SdColumnNode) {
					if (columnName.equals(node.getName())) {
						return (SdColumnNode) node;
					}
				}
			}
		return null;
	}

	public void establishRelationship() {

		if (sdColumnNodeList != null) {
			AbstractNode parentNode = tableNode.getParent();
			while (!(parentNode instanceof ConnectionNode)) {
				if (parentNode == null) {
					return;
				}
				parentNode = parentNode.getParent();
			}
			ConnectionNode connectionNode = (ConnectionNode) parentNode;
			for (AbstractNode node : sdColumnNodeList) {
				if (node instanceof SdColumnNode) {
					SdColumnNode fkColumnNode = (SdColumnNode) node;
					IColumn column = fkColumnNode.getColumnNode().getColumn();
					if (column.isForeignKey()) {
						List<IForeignKeyReference> fkReferences = column
								.getForeignKeyReferenceList();
						for (IForeignKeyReference fkReference : fkReferences) {

							String pkTableSchema = fkReference
									.getPrimaryKeyTableShema();
							String pkTableName = fkReference
									.getPrimaryKeyTable();
							String pkColumnName = fkReference
									.getPrimaryKeyColumn();

							SchemaNode pkSchemaNode = connectionNode
									.findSchemaNodeByName(pkTableSchema);
							SdTableNode pkTableNode = schemata
									.findSdTableNode(pkSchemaNode
											.findTableNodeByName(pkTableName));

							SdColumnNode pkColumnNode = pkTableNode
									.findColumnNodeByName(pkColumnName);

							// Add the Entity relationship information to the
							// columns
							Relationship relationship = new Relationship(
									fkColumnNode, pkColumnNode);

							fkColumnNode.addRelationship(relationship);
							pkColumnNode.addRelationship(relationship);
						}
					}
				}
			}
		}
	}

	public boolean hasColumnNodes() {
		return (sdColumnNodeList != null) && !sdColumnNodeList.isEmpty();
	}
}
