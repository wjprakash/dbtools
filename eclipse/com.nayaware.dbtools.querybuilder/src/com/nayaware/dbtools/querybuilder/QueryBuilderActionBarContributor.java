
package com.nayaware.dbtools.querybuilder;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;

import com.nayaware.dbtools.actions.ExecuteSqlAction;

/**
 * Action Bar contributor for the Query Builder
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderActionBarContributor extends
		BasicTextEditorActionContributor {

	private ExecuteSqlAction executeSqlAction = new ExecuteSqlAction();

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
