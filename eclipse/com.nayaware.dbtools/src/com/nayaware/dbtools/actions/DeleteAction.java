
package com.nayaware.dbtools.actions;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Action that opens the create table dialog
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DeleteAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.deleteAction"; //$NON-NLS-1$

	public DeleteAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.DeleteAction_1);
		setToolTipText(Messages.DeleteAction_2);
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.DELETE));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		AbstractNode parent = getNode().getParent();
		getNode().delete();
		parent.refresh();
	}
}
