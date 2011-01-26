
package com.nayaware.dbtools.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import com.nayaware.dbtools.util.ImageUtils;

/**
 * Action to refresh the node in the database explorer tree view
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExplorerExpandAllAction extends Action implements Runnable {

	public final static String ID = "com.nayaware.dbtools.actions.explorerExpandAllAction"; //$NON-NLS-1$

	private TreeViewer viewer;

	public ExplorerExpandAllAction(TreeViewer viewer) {
		setId(ID);
		// setText(Messages.ExplorerExpandAllAction_1);
		setToolTipText(Messages.ExplorerExpandAllAction_2);
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.EXPAND_ALL));
		this.viewer = viewer;
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		viewer.expandAll();
	}
}
