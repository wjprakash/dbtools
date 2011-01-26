
package com.nayaware.dbtools.actions;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.model.Table;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.ui.InputDialog;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Action that opens the copy table dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class CopyTableAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.copyTableAction"; //$NON-NLS-1$

	public CopyTableAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.CopyTableAction_1);
		setToolTipText(Messages.CopyTableAction_2);
		// setImageDescriptor(ImageUtils
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		if ((node.getDatbaseObject() instanceof ITable)) {
			Table table = (Table) node.getDatbaseObject();
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			InputDialog inputDialog = new InputDialog(shell, Messages.CopyTableAction_3,
					Messages.CopyTableAction_4, table.getName() + Messages.CopyTableAction_5);

			int ret = inputDialog.open();
			if (ret == IDialogConstants.OK_ID) {
				ISqlHelper sqlHelper = table.getDatabaseInfo().getConnectionConfig().getConnectionType().getSqlHelper();
				String sqlScript = sqlHelper.generateTableCopyStatement(table,
						inputDialog.getInput());
				SqlExecutor sqlExecutor = new SqlExecutor(table
						.getDatabaseInfo(), sqlScript);

				Job job = sqlExecutor.asyncExecute();
				try {
					job.join();
					if (job.getResult() == Status.OK_STATUS) {
						node.getParent().refresh();
					} else {
						// SqlEditorDialog sqlEditorDialog = new
						// SqlEditorDialog(
						// title, shell, table.getDatabaseInfo(),
						// sqlExecutor);
						// ret = sqlEditorDialog.open();
						// if (ret == IDialogConstants.OK_ID) {
						// node.getParent().refresh();
						// }
						ErrorManager.showException(sqlExecutor
								.getExecutionStatus().getExceptions().get(0));
					}
				} catch (InterruptedException exc) {
					ErrorManager.showException(exc);
				}
			}
		}
	}
}
