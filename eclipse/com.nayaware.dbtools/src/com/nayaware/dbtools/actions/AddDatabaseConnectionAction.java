
package com.nayaware.dbtools.actions;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.ui.ConnectionWizard;

/**
 * Add database connection action that opens the Add Connection Wizard
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class AddDatabaseConnectionAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.addConnectionAction"; //$NON-NLS-1$
	IWorkbenchWindow window;

	public AddDatabaseConnectionAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.AddConnectionAction_0);
		setToolTipText(Messages.AddConnectionAction_1);
		// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		ConnectionWizard wizard = new ConnectionWizard();
		IWorkbench workbench = PlatformUI.getWorkbench();
		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.open();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
