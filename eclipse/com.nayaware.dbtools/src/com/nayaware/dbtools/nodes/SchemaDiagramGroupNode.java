
package com.nayaware.dbtools.nodes;

import java.util.List;

import org.eclipse.jface.action.Action;

import com.nayaware.dbtools.actions.OpenSchemaDesignerAction;
import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.ISchemaDiagram;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Script Group Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class SchemaDiagramGroupNode extends AbstractNode {

	public SchemaDiagramGroupNode(IConnection connection) {
		super(connection);
		setDisplayName(Messages.getString("SchemaDiagramGroupNode.0")); //$NON-NLS-1$
		setImageKey(ImageUtils.SCHEMA_DIAGRAM_FOLDER);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected void initializeChildren() {
		super.initializeChildren();
		IConnection connection = (IConnection) getDatbaseObject();
		List<ISchemaDiagram> scriptList = connection.getSchemaDiagramList();
		for (ISchemaDiagram script : scriptList) {
			addChild(new SchemaDiagramNode(script));
		}
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new RefreshAction(this), null, // Separator
				new OpenSchemaDesignerAction(this) };
	}

	public void addSchemaDiagramNode(SchemaDiagramNode schemaDiagramNode) {
		IConnection connection = (IConnection) getDatbaseObject();
		connection.addSchemaDiagram(schemaDiagramNode.getSchemaDiagram());
		addChild(schemaDiagramNode, true);
	}
}
