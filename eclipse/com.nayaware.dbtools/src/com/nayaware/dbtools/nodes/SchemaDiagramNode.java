
package com.nayaware.dbtools.nodes;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import com.nayaware.dbtools.actions.DeleteAction;
import com.nayaware.dbtools.actions.OpenSchemaDesignerAction;
import com.nayaware.dbtools.api.ISchemaDiagram;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Node wrapper for the Script model
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDiagramNode extends AbstractNode {

	public SchemaDiagramNode(ISchemaDiagram schemaDiagram) {
		super(schemaDiagram, true);
		setImageKey(ImageUtils.SCHEMA_DIAGRAM);
	}

	public ISchemaDiagram getSchemaDiagram() {
		return (ISchemaDiagram) getDatbaseObject();
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new OpenSchemaDesignerAction(this), null,
				new DeleteAction(this) };
	}

	@Override
	public void handleDoubleClick(TreeViewer viewer) {
		new OpenSchemaDesignerAction(this).run();
	}

}
