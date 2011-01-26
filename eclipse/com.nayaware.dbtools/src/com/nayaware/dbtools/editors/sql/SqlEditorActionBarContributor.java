
package com.nayaware.dbtools.editors.sql;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;

import com.nayaware.dbtools.actions.ExecuteSqlAction;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlEditorActionBarContributor extends
		BasicTextEditorActionContributor {

	private ExecuteSqlAction executeSqlAction = new ExecuteSqlAction();

	@Override
	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		// manager.add(executeSqlAction);
	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		createActions(targetEditor);
	}

	private void createActions(IEditorPart targetEditor) {
		executeSqlAction.setTargetEditor(targetEditor);
	}

}
