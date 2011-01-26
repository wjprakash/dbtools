
package com.nayaware.dbtools.viewers;

import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.DrillDownAdapter;

import com.nayaware.dbtools.api.ConnectionManager;

/**
 * Tree viewer to explore the contents of a database connection, such as tables,
 * views and their columns
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExplorerTreeViewer extends TreeViewer {

	private DrillDownAdapter drillDownAdapter;

	public ExplorerTreeViewer(Composite parent, int constraints) {
		super(parent, constraints);
		initialize();
		enableDragAndDrop();
	}

	public void addNavigationActions(IMenuManager manager) {
		drillDownAdapter.addNavigationActions(manager);
	}

	public void addNavigationActions(IToolBarManager manager) {
		drillDownAdapter.addNavigationActions(manager);
	}

	private void initialize() {
		drillDownAdapter = new DrillDownAdapter(this);
		setContentProvider(new ExplorerTreeContentProvider(this));
		setLabelProvider(new ExplorerTreeLabelProvider());
		setSorter(new NameSorter());

		setInput(ConnectionManager.getInstance());
		expandToLevel(2);
	}

	private void enableDragAndDrop() {
		addDragSupport(DND.DROP_COPY, new Transfer[] { TemplateTransfer
				.getInstance() }, new DragSourceAdapter() {
			Object dragData;

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = dragData;
			}

			@Override
			public void dragStart(DragSourceEvent event) {
				dragData = ((StructuredSelection) getSelection())
						.getFirstElement();

			}
		});
	}

	class NameSorter extends ViewerSorter {
	}
}
