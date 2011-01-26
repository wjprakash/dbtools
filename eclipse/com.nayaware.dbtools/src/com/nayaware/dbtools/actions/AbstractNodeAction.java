
package com.nayaware.dbtools.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import com.nayaware.dbtools.nodes.AbstractNode;

/**
 * Abstract Node Action
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class AbstractNodeAction extends Action implements Runnable {
	protected AbstractNode node;
	protected Viewer viewer;

	public AbstractNodeAction(Viewer viewer) {
		this.viewer = viewer;
	}

	public AbstractNodeAction(AbstractNode node) {
		this.node = node;
	}

	@Override
	public boolean isEnabled() {
		if (getNode() == null) {
			return false;
		}
		if (getNode().getDatbaseObject() == null) {
			return false;
		} else if (getNode().getDatbaseObject().getDatabaseInfo() == null) {
			return false;
		}
		return getNode().getDatbaseObject().getDatabaseInfo().isConnected();
	}

	public AbstractNode getNode() {
		if (viewer != null) {
			// Try getting the selected node from viewer
			StructuredSelection selection = (StructuredSelection) viewer
					.getSelection();
			if (selection.getFirstElement() instanceof AbstractNode) {
				return (AbstractNode) selection.getFirstElement();
			}
		}
		return node;
	}
}
