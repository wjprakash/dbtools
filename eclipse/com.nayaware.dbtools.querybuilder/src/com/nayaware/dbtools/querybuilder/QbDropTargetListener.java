
package com.nayaware.dbtools.querybuilder;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import com.nayaware.dbtools.nodes.TableNode;
import com.nayaware.dbtools.querybuilder.model.QbTableNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * The drop target listener to listen to objects dropped in the queryData
 * builder design area
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbDropTargetListener extends TemplateTransferDropTargetListener {

	private QueryData queryData;

	public QbDropTargetListener(EditPartViewer viewer, QueryData query) {
		super(viewer);
		this.queryData = query;
	}

	@Override
	protected CreationFactory getFactory(Object template) {
		return new QbCreationFactory(template);
	}

	@Override
	protected void handleDrop() {
		Object droppedObject = getCreateRequest().getNewObject();
		if ((droppedObject != null) && (droppedObject instanceof TableNode)) {

			EditPartViewer viewer = getViewer();
			// Force a focus on the viewer
			viewer.getControl().forceFocus();
			TableNode tableNode = (TableNode) droppedObject;
			QbTableNode existingWrappedTableNode = queryData
					.findTableNodeByName(tableNode.getName());

			Object editpart = null;
			if (existingWrappedTableNode != null) {
				editpart = viewer.getEditPartRegistry().get(
						existingWrappedTableNode);
			}

			if (editpart != null) {
				// Force a layout and then select the editpart corresponding to
				// the model
				getViewer().flush();
				viewer.select((EditPart) editpart);
			} else {
				// Add the TableNode to the Schemata object, which would cause
				// the node to appear in the viewer
				if (tableNode.getTable().getDatabaseInfo() != queryData
						.getDatabaseInfo()) {
					ErrorManager.showError(Messages
							.getString("QbDropTargetListener.0")); //$NON-NLS-1$
					return;
				}
				QbTableNode wrappedTableNode = new QbTableNode(tableNode);
				wrappedTableNode.setLocation(getDropLocation());
				queryData.addTableNode(wrappedTableNode);
				editpart = viewer.getEditPartRegistry().get(wrappedTableNode);
				// Force a layout again.
				getViewer().flush();
				viewer.select((EditPart) editpart);
			}
		}
	}

	public class QbCreationFactory implements CreationFactory {

		private Object droppedObject;

		public QbCreationFactory(Object droppedObj) {
			droppedObject = droppedObj;
		}

		public Object getNewObject() {
			return droppedObject;
		}

		public Object getObjectType() {
			return droppedObject;
		}

	}
}
