
package com.nayaware.dbtools.actions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.csv.ExportCsvDialog;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Action that opens the create table dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExportCsvAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.exportCsvAction"; //$NON-NLS-1$

	public ExportCsvAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.ExportCsvAction_1);
		setToolTipText(Messages.ExportCsvAction_2);
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.CSV));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		if ((node.getDatbaseObject() instanceof ITable)) {

			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();

			ExportCsvDialog exportCsvDialog = new ExportCsvDialog(shell,
					(ITable) node.getDatbaseObject());
			int ret = exportCsvDialog.open();
			if (ret == IDialogConstants.OK_ID) {
				// TODO show success message
			}
		}
	}
}
