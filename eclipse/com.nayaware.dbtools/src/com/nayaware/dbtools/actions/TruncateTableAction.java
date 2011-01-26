
package com.nayaware.dbtools.actions;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.model.Table;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Action that opens the truncate table dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TruncateTableAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.truncateTableAction"; //$NON-NLS-1$

	public TruncateTableAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.TruncateTableAction_1);
		setToolTipText(Messages.TruncateTableAction_2);
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
			String title = Messages.TruncateTableAction_0;
			String message = Messages.TruncateTableAction_3;
			if (MessageDialog.openConfirm(shell, title, message)) {
				ISqlHelper sqlHelper = table.getDatabaseInfo()
						.getConnectionConfig().getConnectionType()
						.getSqlHelper();
				String sqlScript = sqlHelper
						.generateTableTruncateStatement(table);
				SqlExecutor sqlExecutor = new SqlExecutor(table
						.getDatabaseInfo(), sqlScript);

				Job job = sqlExecutor.asyncExecute();
				try {
					job.join();
					if (job.getResult() == Status.OK_STATUS) {
						node.getParent().refresh();
					} else {
						// SqlEditorDialog sqlEditorDialog = new
						// SqlEditorDialog(title, shell,
						// table.getDatabaseInfo(), sqlExecutor);
						// int ret = sqlEditorDialog.open();
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
