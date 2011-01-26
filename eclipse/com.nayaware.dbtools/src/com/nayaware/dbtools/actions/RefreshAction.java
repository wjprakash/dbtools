
package com.nayaware.dbtools.actions;

import com.nayaware.dbtools.nodes.AbstractNode;

/**
 * Action to refresh the node in the database explorer tree view
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class RefreshAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.refreshAction"; //$NON-NLS-1$

	public RefreshAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.RefreshAction_1);
		setToolTipText(Messages.RefreshAction_2);
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
		node.refresh();
	}
}
