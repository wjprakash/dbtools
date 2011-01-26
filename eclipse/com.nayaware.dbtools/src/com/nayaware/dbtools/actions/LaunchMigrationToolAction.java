
package com.nayaware.dbtools.actions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.ui.DatabaseMigrationDialog;

/**
 * Action that opens the drop table dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class LaunchMigrationToolAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.launchMigrationToolAction"; //$NON-NLS-1$

	public LaunchMigrationToolAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.LaunchMigrationToolAction_1);
		setToolTipText(Messages.LaunchMigrationToolAction_2);
		// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		DatabaseMigrationDialog databaseMigrationDialog = new DatabaseMigrationDialog(
				shell);
		int ret = databaseMigrationDialog.open();
		if (ret == IDialogConstants.OK_ID) {
			node.getParent().refresh();
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
