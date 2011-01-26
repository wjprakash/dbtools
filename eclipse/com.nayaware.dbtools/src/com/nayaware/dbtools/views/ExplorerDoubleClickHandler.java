
package com.nayaware.dbtools.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.nayaware.dbtools.nodes.AbstractNode;

/**
 * Connection explorer double click handler
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExplorerDoubleClickHandler implements IDoubleClickListener {

	public void doubleClick(DoubleClickEvent event) {
		TreeViewer viewer = (TreeViewer) event.getViewer();
		if (event.getSelection() instanceof StructuredSelection) {
			StructuredSelection selection = (StructuredSelection) event
					.getSelection();
			if (selection.getFirstElement() instanceof AbstractNode) {
				AbstractNode node = (AbstractNode) selection.getFirstElement();
				node.handleDoubleClick(viewer);
			}
		}
	}
}
