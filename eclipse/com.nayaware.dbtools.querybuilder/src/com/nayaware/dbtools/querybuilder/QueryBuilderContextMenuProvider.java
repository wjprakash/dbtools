
package com.nayaware.dbtools.querybuilder;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.nayaware.dbtools.querybuilder.actions.SaveQueryDesignAsImageAction;

/**
 * Context menu provider for the query builder
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;
	private GraphicalViewer viewer;

	public QueryBuilderContextMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;
		this.viewer = (GraphicalViewer) viewer;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);

		IAction action;
		// action = actionRegistry.getAction(ActionFactory.UNDO.getId());
		// menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		//
		// action = actionRegistry.getAction(ActionFactory.REDO.getId());
		// menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = actionRegistry.getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = new SaveQueryDesignAsImageAction(viewer);
		menu.appendToGroup(GEFActionConstants.GROUP_SAVE, action);
	}
}