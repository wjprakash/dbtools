
package com.nayaware.dbtools.viewers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionGroupNode;

/**
 * Provides content for the explorer tree viewer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
final class ExplorerTreeContentProvider implements IStructuredContentProvider,
		ITreeContentProvider, PropertyChangeListener {

	TreeViewer treeViewer;

	public ExplorerTreeContentProvider(TreeViewer viewer) {
		treeViewer = viewer;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		if (parent instanceof ConnectionManager) {
			ConnectionGroupNode databaseGroupNode = new ConnectionGroupNode();
			databaseGroupNode.addPropertyChangeListener(this);
			return new Object[] { databaseGroupNode };
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof AbstractNode) {
			return ((AbstractNode) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof AbstractNode) {
			AbstractNode[] children = ((AbstractNode) parent).getChildren();
			for (AbstractNode node : children) {
				node.addPropertyChangeListener(this);
			}
			return ((AbstractNode) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof AbstractNode)
			return ((AbstractNode) parent).hasChildren();
		return false;
	}

	public synchronized void propertyChange(final PropertyChangeEvent evt) {
		final AbstractNode node = (AbstractNode) evt.getNewValue();
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				String property = evt.getPropertyName();
				treeViewer.refresh(node);
				if (AbstractNode.EXPAND_TO.equals(property)) {
					treeViewer.expandToLevel(node, 1);
				}
			}
		});
	}
}
