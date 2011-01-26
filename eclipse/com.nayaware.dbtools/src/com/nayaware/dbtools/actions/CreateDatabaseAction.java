
package com.nayaware.dbtools.actions;

import java.sql.SQLException;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISqlHelper;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.ui.CreateDatabaseDialog;
import com.nayaware.dbtools.ui.SqlEditorDialog;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Action that opens the create table dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class CreateDatabaseAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.createDatabaseAction"; //$NON-NLS-1$

	public CreateDatabaseAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.CreateDatabaseAction_1);
		setToolTipText(Messages.CreateDatabaseAction_2);
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
		if ((node.getDatbaseObject() instanceof IConnection)) {
			IConnection dbConnection = (IConnection) node.getDatbaseObject();
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			CreateDatabaseDialog createDatabaseDialog = new CreateDatabaseDialog(
					shell, dbConnection.getDatabaseInfo()
							.getUniqueDatabaseName());
			int ret = createDatabaseDialog.open();
			if (ret == IDialogConstants.OK_ID) {
				ISqlHelper sqlHelper = dbConnection.getDatabaseInfo()
						.getConnectionConfig().getConnectionType()
						.getSqlHelper();
				String sqlScript = sqlHelper.generateCreateDatabaseStatement(
						createDatabaseDialog.getDatabaseName(), node
								.getDatbaseObject().getDatabaseInfo());
				SqlExecutor sqlExecutor = new SqlExecutor(dbConnection
						.getDatabaseInfo(), sqlScript);
				if (createDatabaseDialog.getShowAdvanced()) {
					showSqlEditorDialog(shell, dbConnection.getDatabaseInfo(),
							sqlExecutor);
				} else {
					Job job = sqlExecutor.asyncExecute();
					try {
						job.join();
						if (job.getResult() == Status.OK_STATUS) {
							node.refresh();
						} else {
							// showSqlEditorDialog(shell, database
							// .getDatabaseInfo(), sqlExecutor);
							ErrorManager.showException(sqlExecutor
									.getExecutionStatus().getExceptions()
									.get(0));
						}
					} catch (InterruptedException exc) {
						ErrorManager.showException(exc);
					}
				}
			}
		}
	}

	private void showSqlEditorDialog(Shell shell, IDatabaseInfo dbInfo,
			SqlExecutor sqlExecutor) {
		String title = Messages.CreateDatabaseAction_0;
		SqlEditorDialog sqlEditorDialog = new SqlEditorDialog(title, shell,
				dbInfo, sqlExecutor);
		int ret = sqlEditorDialog.open();
		if (ret == IDialogConstants.OK_ID) {
			node.refresh();
		}
	}
}
