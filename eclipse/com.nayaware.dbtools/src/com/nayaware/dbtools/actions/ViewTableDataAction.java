
package com.nayaware.dbtools.actions;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.api.ITableData;
import com.nayaware.dbtools.api.IView;
import com.nayaware.dbtools.editors.TableDataEditor;
import com.nayaware.dbtools.editors.TableDataEditorInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Action to view the data in the database Table or View
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ViewTableDataAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.viewDataAction"; //$NON-NLS-1$

	public ViewTableDataAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.ViewDataAction_1);
		setToolTipText(Messages.ViewDataAction_2);
		// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		if ((node.getDatbaseObject() instanceof ITable)
				|| (node.getDatbaseObject() instanceof IView)) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();

			ITable table = (ITable) node.getDatbaseObject();
			ITableData data = table.getData();
			if (data != null) {
				TableDataEditorInput input = new TableDataEditorInput(table
						.getDatabaseInfo(), data);
				try {
					page.openEditor(input, TableDataEditor.ID, true);
				} catch (PartInitException exc) {
					ErrorManager.showException(exc);
				}
			}
		}
	}
}
