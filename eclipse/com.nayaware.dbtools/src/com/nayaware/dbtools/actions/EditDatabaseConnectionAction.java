
package com.nayaware.dbtools.actions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.ui.ConnectionWizard;

/**
 * Add database connection action that opens the Add Connection Wizard
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class EditDatabaseConnectionAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.editDatabaseConnectionAction"; //$NON-NLS-1$
	IWorkbenchWindow window;

	public EditDatabaseConnectionAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.EditDatabaseConnectionAction_1);
		setToolTipText(Messages.EditDatabaseConnectionAction_2);
		// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	@Override
	public void run() {
		if ((node.getDatbaseObject() instanceof IConnection)) {
			IConnection database = (IConnection) node.getDatbaseObject();
			ConnectionWizard connectionWizard = new ConnectionWizard(database
					.getDatabaseInfo().getConnectionConfig());
			IWorkbench workbench = PlatformUI.getWorkbench();
			Shell shell = workbench.getActiveWorkbenchWindow().getShell();
			WizardDialog wizardDialog = new WizardDialog(shell,
					connectionWizard);
			int ret = wizardDialog.open();
			if (ret == IDialogConstants.OK_ID) {
				IConnectionConfig connectionConfig = connectionWizard
						.getConnectionConfig();
				ConnectionManager connectionManager = ConnectionManager
						.getInstance();
				connectionManager.updateConnectionConfig(connectionConfig);
			}
		}
	}

	@Override
	public boolean isEnabled() {
		return getNode().getDatbaseObject().getDatabaseInfo() != null;
	}
}
