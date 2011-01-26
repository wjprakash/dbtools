
package com.nayaware.dbtools.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.nodes.AbstractNode;

/**
 * Add database connection action that opens the Add Connection Wizard
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class RemoveDatabaseConnectionAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.removeDatabaseConnectionAction"; //$NON-NLS-1$
	IWorkbenchWindow window;

	public RemoveDatabaseConnectionAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.RemoveDatabaseConnectionAction_1);
		setToolTipText(Messages.RemoveDatabaseConnectionAction_2);
		// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	@Override
	public boolean isEnabled() {
		return getNode().getDatbaseObject().getDatabaseInfo() != null;
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		if ((node.getDatbaseObject() instanceof IConnection)) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			String title = Messages.RemoveDatabaseConnectionAction_0;
			String message = Messages.RemoveDatabaseConnectionAction_3;
			if (MessageDialog.openConfirm(shell, title, message)) {
				IConnection database = (IConnection) node.getDatbaseObject();
				ConnectionManager connectionManager = ConnectionManager
						.getInstance();
				connectionManager.removeConnectionConfig(database
						.getDatabaseInfo().getConnectionConfig());
			}
		}
	}
}
