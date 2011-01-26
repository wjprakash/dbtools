
package com.nayaware.dbtools.actions;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.ui.CreateServerlessDatabaseDialog;

/**
 * Action to create a derby database
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class CreateDerbyDatabaseAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.createDerbyDatabaseAction"; //$NON-NLS-1$

	public CreateDerbyDatabaseAction(Viewer viewer) {
		super(viewer);
		initialize();
	}

	public CreateDerbyDatabaseAction(AbstractNode node) {
		super(node);
		initialize();
	}

	private void initialize() {
		setId(ID);
		setText(Messages.CreateEmbeddedDerbyDatabaseAction_1);
		setToolTipText(Messages.CreateEmbeddedDerbyDatabaseAction_2);
		// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .getImageDescriptor(ImageUtils.DATA_VIEW_ACTION));
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		CreateServerlessDatabaseDialog derbyConnectionDialog = new CreateServerlessDatabaseDialog(
				shell, IConnectionType.DERBY_EMBEDDED);
		derbyConnectionDialog.open();
	}
}
