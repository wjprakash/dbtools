
package com.nayaware.dbtools.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;

import com.nayaware.dbtools.actions.DropDatabaseAction;
import com.nayaware.dbtools.actions.LaunchMigrationToolAction;
import com.nayaware.dbtools.actions.OpenQueryBuilderAction;
import com.nayaware.dbtools.actions.OpenSqlEditorAction;
import com.nayaware.dbtools.actions.OpenTableDesignerAction;
import com.nayaware.dbtools.actions.ViewSchemaDiagramAction;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Schema Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class SchemaNode extends AbstractNode {

	public List<TableNode> selectedTableNodes = new ArrayList<TableNode>();
	public TableGroupNode tableGroupNode;
	public ViewGroupNode viewGroupNode;

	public SchemaNode(ISchema schema) {
		super(schema);
		setImageKey(ImageUtils.SCHEMA);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected void initializeChildren() {
		super.initializeChildren();
		tableGroupNode = new TableGroupNode(getDatbaseObject());
		viewGroupNode = new ViewGroupNode(getDatbaseObject());
		addChild(tableGroupNode);
		addChild(viewGroupNode);
	}

	@Override
	public Action[] getActions() {
		return new Action[] {
				new OpenTableDesignerAction(this),
				null, // Separator
				new DropDatabaseAction(this),
				//new LaunchMigrationToolAction(this),
				null, // Separator
				new ViewSchemaDiagramAction(this),
				new OpenSqlEditorAction(this), new OpenQueryBuilderAction(this) };
	}

	public synchronized TableGroupNode getTableGroupNode() {
		if (tableGroupNode == null) {
			initializeChildren();
		}
		return tableGroupNode;
	}

	public synchronized ViewGroupNode getViewGroupNode() {
		if (viewGroupNode == null) {
			initializeChildren();
		}
		return viewGroupNode;
	}

	public List<TableNode> getSelectedTableNodes() {
		return selectedTableNodes;
	}

	public void setSelectedTableNodes(List<TableNode> selectedTableNodes) {
		this.selectedTableNodes = selectedTableNodes;
	}

	public void addToSelection(TableNode tableNode) {
		selectedTableNodes.add(tableNode);
	}

	public List<AbstractNode> getTableNodeList() {
		return getTableNodeList(false);
	}

	public List<AbstractNode> getTableNodeList(boolean refresh) {
		return getTableGroupNode().getTableNodeList(refresh);
	}

	public List<AbstractNode> getViewNodeList() {
		return getViewNodeList(false);
	}

	public List<AbstractNode> getViewNodeList(boolean refresh) {
		return getViewGroupNode().getViewNodeList(refresh);
	}

	public TableNode findTableNodeByName(String tableName) {
		List<AbstractNode> tableList = getTableGroupNode().getTableNodeList();
		if (tableList != null)
			for (AbstractNode node : tableList) {
				if (node instanceof TableNode) {
					if (tableName.equals(node.getName())) {
						return (TableNode) node;
					}
				}
			}
		return null;
	}

	public TableNode findViewNodeByName(String viewName) {
		List<AbstractNode> viewList = getViewGroupNode().getViewNodeList();
		if (viewList != null)
			for (AbstractNode node : viewList) {
				if (node instanceof TableNode) {
					if (viewName.equals(node.getName())) {
						return (TableNode) node;
					}
				}
			}
		return null;
	}
}
