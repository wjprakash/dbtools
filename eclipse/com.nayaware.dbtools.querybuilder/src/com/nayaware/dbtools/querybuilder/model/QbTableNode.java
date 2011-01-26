
package com.nayaware.dbtools.querybuilder.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ColumnNode;
import com.nayaware.dbtools.nodes.TableNode;

/**
 * Table Node wrapper for query builder
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbTableNode extends AbstractNode {

	private List<QbColumnNode> joinedColumnNodes = new ArrayList<QbColumnNode>();
	private List<QbColumnNode> selectedColumnNodes = new ArrayList<QbColumnNode>();
	private List<AbstractNode> qbColumnNodeList;

	private boolean selected = false;

	private TableNode tableNode;

	private static final int DEFAULT_WIDTH = 200;
	private static final int DEFAULT_HEIGHT = 250;

	public QbTableNode(TableNode tableNode) {
		super(tableNode.getDatbaseObject());
		this.tableNode = tableNode;
		setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}

	public TableNode getTableNode() {
		return tableNode;
	}

	public void setTableNode(TableNode tableNode) {
		this.tableNode = tableNode;
	}

	public List<QbColumnNode> getSelectedColumnNodes() {
		return selectedColumnNodes;
	}

	public void setSelectedColumnNodes(List<QbColumnNode> selectedColumnNodes) {
		this.selectedColumnNodes = selectedColumnNodes;
	}

	public void addSelectedColumnNode(QbColumnNode columnNode) {
		if (!selectedColumnNodes.contains(columnNode)) {
			selectedColumnNodes.add(columnNode);
		}
	}

	public void removeSelectedColumnNode(QbColumnNode columnNode) {
		if (selectedColumnNodes.contains(columnNode)) {
			selectedColumnNodes.remove(columnNode);
		}
	}

	public List<QbColumnNode> getJoinedColumnNodes() {
		return joinedColumnNodes;
	}

	public void setJoinedColumnNodes(List<QbColumnNode> joinedColumnNodes) {
		this.joinedColumnNodes = joinedColumnNodes;
	}

	public void addJoinedColumnNode(QbColumnNode columnNode) {
		if (!joinedColumnNodes.contains(columnNode)) {
			joinedColumnNodes.add(columnNode);
		}
	}

	public void removeJoinedColumnNode(QbColumnNode columnNode) {
		if (joinedColumnNodes.contains(columnNode)) {
			joinedColumnNodes.remove(columnNode);
		}
	}

	public boolean hasJoinedColumnNodes() {
		return joinedColumnNodes.size() > 0;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public synchronized List<AbstractNode> getQbColumnNodeList() {
		if (qbColumnNodeList == null) {
			List<AbstractNode> columnNodeList = tableNode.getColumnNodeList();
			if (columnNodeList == null) {
				tableNode
						.addPropertyChangeListener(new PropertyChangeListener() {
							public void propertyChange(PropertyChangeEvent evt) {
								String property = evt.getPropertyName();
								if (property
										.equals(AbstractNode.NODE_CHILDREN_MODIFIED)) {
									populateChildren(tableNode
											.getColumnNodeList());
									firePropertyChange(
											AbstractNode.NODE_CHILDREN_MODIFIED,
											null, qbColumnNodeList);
								}
							}
						});
				columnNodeList = tableNode.getColumnNodeList(true);
			}
			populateChildren(columnNodeList);
		}
		return qbColumnNodeList;

	}

	private synchronized List<AbstractNode> populateChildren(
			List<AbstractNode> columnNodeList) {
		qbColumnNodeList = new ArrayList<AbstractNode>();
		for (AbstractNode node : columnNodeList) {
			if (node instanceof ColumnNode) {
				QbColumnNode qbColumnNode = new QbColumnNode((ColumnNode) node);
				qbColumnNode.setParent(this);
				qbColumnNodeList.add(qbColumnNode);
			} else {
				qbColumnNodeList.add(node);
			}
		}
		return qbColumnNodeList;
	}

	public QbColumnNode findColumnNodeByName(String columnName) {
		for (AbstractNode node : getQbColumnNodeList()) {
			if (node.getName().equals(columnName)) {
				return (QbColumnNode) node;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return Messages.getString("QbTableNode.0") + getName() + Messages.getString("QbTableNode.1"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
