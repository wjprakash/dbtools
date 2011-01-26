
package com.nayaware.dbtools.schemadesigner.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.schemadesigner.figures.ColumnNodeFigure;
import com.nayaware.dbtools.schemadesigner.figures.RelationshipConnectionSourceAnchor;
import com.nayaware.dbtools.schemadesigner.figures.RelationshipConnectionTargetAnchor;
import com.nayaware.dbtools.schemadesigner.model.Relationship;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.policies.ColumnNodeComponentEditPolicy;
import com.nayaware.dbtools.schemadesigner.policies.ColumnNodeDirectEditPolicy;
import com.nayaware.dbtools.schemadesigner.policies.ColumnNodeEditPolicy;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeEditPart extends AbstractGraphicalEditPart implements
		NodeEditPart, PropertyChangeListener {
	private Schemata schemata;

	private boolean isDesigner = false;
	private DirectEditManager directManager;

	public ColumnNodeEditPart(Schemata schemata, SdColumnNode columnNode,
			boolean isDesigner) {
		setModel(columnNode);
		this.schemata = schemata;
		columnNode.addPropertyChangeListener(this);
		this.isDesigner = isDesigner;
	}

	@Override
	protected IFigure createFigure() {
		return new ColumnNodeFigure((SdColumnNode) getModel());
	}

	// @Override
	// public DragTracker getDragTracker(Request request) {
	// if (isDesigner) {
	// getViewer().select(this);
	// return new ConnectionDragCreationTool();
	// } else {
	// return super.getDragTracker(request);
	// }
	// }

	@Override
	protected void createEditPolicies() {
		// Allow the creation of relationship between two tables
		if (isDesigner) {
			installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
					new ColumnNodeEditPolicy(schemata));
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
					new ColumnNodeDirectEditPolicy());
			installEditPolicy(EditPolicy.COMPONENT_ROLE,
					new ColumnNodeComponentEditPolicy());
		}
	}

	@Override
	protected List<Relationship> getModelSourceConnections() {
		SdColumnNode columnNode = (SdColumnNode) getModel();
		return columnNode.getSourceRelationships();
	}

	@Override
	protected List<Relationship> getModelTargetConnections() {
		SdColumnNode columnNode = (SdColumnNode) getModel();
		List<Relationship> relatioships = columnNode.getTargetRelationships();
		return columnNode.getTargetRelationships();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new RelationshipConnectionSourceAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new RelationshipConnectionTargetAnchor(getFigure());
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new RelationshipConnectionSourceAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new RelationshipConnectionTargetAnchor(getFigure());
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		if (isDesigner) {
			// activate();
			ColumnNodeFigure columnFigure = (ColumnNodeFigure) getFigure();
			if (value != EditPart.SELECTED_NONE)
				columnFigure.setSelected(true);
			else
				columnFigure.setSelected(false);
			schemata.setSelectedColumnNode((SdColumnNode) getModel());
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (SdColumnNode.COLUMN_MODIFIED.equals(evt.getPropertyName())
				|| AbstractNode.NODE_NAME_MODIFIED
						.equals(evt.getPropertyName())) {
			ColumnNodeFigure columnFigure = (ColumnNodeFigure) getFigure();
			columnFigure.update();
		} else if (SdColumnNode.RELATIONSHIP_ADDED
				.equals(evt.getPropertyName())
				|| SdColumnNode.RELATIONSHIP_REMOVED.equals(evt
						.getPropertyName())) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					refresh();
				}
			});
		}
	}

	@Override
	public void performRequest(Request req) {
		if (isDesigner) {
			if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)
					|| req.getType().equals(RequestConstants.REQ_OPEN)) {
				performDirectEdit();
				return;
			}
		}
		super.performRequest(req);
	}

	private void performDirectEdit() {
		if (directManager == null) {
			directManager = new ColumnNodeDirectEditManager(this);
		}
		directManager.show();
	}

}
