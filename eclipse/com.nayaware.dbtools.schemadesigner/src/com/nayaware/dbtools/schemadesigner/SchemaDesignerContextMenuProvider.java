
package com.nayaware.dbtools.schemadesigner;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.nayaware.dbtools.schemadesigner.actions.SaveSchemaDesignAsImageAction;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;
	private boolean isDesigner;
	private GraphicalViewer viewer;

	public SchemaDesignerContextMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry, boolean isDesigner) {
		super(viewer);
		this.actionRegistry = actionRegistry;
		this.isDesigner = isDesigner;
		this.viewer = (GraphicalViewer) viewer;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);

		IAction action;
		if (isDesigner) {
			action = actionRegistry.getAction(ActionFactory.DELETE.getId());
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		} else {
			action = new SaveSchemaDesignAsImageAction(viewer);
			menu.appendToGroup(GEFActionConstants.GROUP_SAVE, action);
		}
	}

}
