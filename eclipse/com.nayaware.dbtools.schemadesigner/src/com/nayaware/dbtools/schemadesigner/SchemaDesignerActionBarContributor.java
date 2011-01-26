
package com.nayaware.dbtools.schemadesigner;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;

import com.nayaware.dbtools.actions.ExecuteSqlAction;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerActionBarContributor extends
		BasicTextEditorActionContributor {

	private ExecuteSqlAction executeSqlAction = new ExecuteSqlAction();

	@Override
	public void contributeToMenu(IMenuManager menubar) {
		super.contributeToMenu(menubar);
		//MenuManager viewMenu = new MenuManager(Messages.getString("SchemaDesignerActionBarContributor.0")); //$NON-NLS-1$
		// viewMenu.add(getAction((ITextEditor) this,
		// GEFActionConstants.ZOOM_IN));
		// viewMenu.add(getAction((ITextEditor) this,
		// GEFActionConstants.ZOOM_OUT));
		// menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
	}

	@Override
	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		// manager.add(executeSqlAction);
		// String[] zoomStrings = new String[] { ZoomManager.FIT_ALL,
		// ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
		// manager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		createActions(targetEditor);
	}

	private void createActions(IEditorPart targetEditor) {
		executeSqlAction.setTargetEditor(targetEditor);
	}
}
