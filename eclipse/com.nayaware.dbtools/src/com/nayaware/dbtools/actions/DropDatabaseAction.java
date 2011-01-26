
package com.nayaware.dbtools.actions;

import java.sql.SQLException;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Action that opens the drop table dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DropDatabaseAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.dropDatabaseAction"; //$NON-NLS-1$

	public DropDatabaseAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.DropDatabaseAction_1);
		setToolTipText(Messages.DropDatabaseAction_2);
		// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	@Override
	public boolean isEnabled() {
		boolean enabled = super.isEnabled();
		if (enabled) {
			try {
				enabled = getNode().getDatbaseObject().getDatabaseInfo()
						.hasSchemaSupport();
			} catch (SQLException exc) {
				ErrorManager.showException(exc);
				enabled = false;
			}
		}
		return enabled;
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		if ((node.getDatbaseObject() instanceof ISchema)) {
			ISchema schema = (ISchema) node.getDatbaseObject();
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			String title = Messages.DropDatabaseAction_0;
			String message = Messages.DropDatabaseAction_3;
			if (MessageDialog.openConfirm(shell, title, message)) {
				ISqlHelper sqlHelper = schema.getDatabaseInfo()
						.getConnectionConfig().getConnectionType()
						.getSqlHelper();
				String sqlScript = sqlHelper
						.generateDropDatabaseStatement(schema);
				SqlExecutor sqlExecutor = new SqlExecutor(schema
						.getDatabaseInfo(), sqlScript);

				Job job = sqlExecutor.asyncExecute();
				try {
					job.join();
					if (job.getResult() == Status.OK_STATUS) {
						node.getParent().refresh();
					} else {
						// SqlEditorDialog sqlEditorDialog = new
						// SqlEditorDialog(title, shell,
						// schema.getDatabaseInfo(), sqlExecutor);
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
