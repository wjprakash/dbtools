
package com.nayaware.dbtools.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;

import com.nayaware.dbtools.editors.ISqlExecutionCapableEditor;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Execute the SQL script in
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExecuteSqlAction extends Action implements Runnable {

	public final static String ID = "com.nayaware.dbtools.actions.executeSqlAction"; //$NON-NLS-1$

	private IEditorPart targetEditor;

	public ExecuteSqlAction() {
		setId(ID);
		setText(Messages.ExecuteSqlAction_1);
		setToolTipText(Messages.ExecuteSqlAction_2);
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.EXECUTE));
	}

	public IEditorPart getTargetEditor() {
		return targetEditor;
	}

	public void setTargetEditor(IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}

	@Override
	public void run() {

		if (targetEditor instanceof ISqlExecutionCapableEditor) {
			ISqlExecutionCapableEditor sqlExecutionCapableEditor = (ISqlExecutionCapableEditor) targetEditor;
			sqlExecutionCapableEditor.execute();
		}

	}
}
