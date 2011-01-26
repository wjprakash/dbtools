
package com.nayaware.dbtools.querybuilder.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.ConnectionDragCreationTool;

import com.nayaware.dbtools.querybuilder.figures.QbColumnNodeFigure;
import com.nayaware.dbtools.querybuilder.figures.QbJoinSourceAnchor;
import com.nayaware.dbtools.querybuilder.figures.QbJoinTargetAnchor;
import com.nayaware.dbtools.querybuilder.model.Join;
import com.nayaware.dbtools.querybuilder.model.QbColumnNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.querybuilder.policies.QbColumnNodeEditPolicy;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class QbColumnNodeEditPart extends AbstractGraphicalEditPart implements
		NodeEditPart, PropertyChangeListener {
	private QueryData queryData;

	public QbColumnNodeEditPart(QbColumnNode columnNode, QueryData query) {
		setModel(columnNode);
		columnNode.addPropertyChangeListener(this);
		this.queryData = query;
	}

	@Override
	protected IFigure createFigure() {
		return new QbColumnNodeFigure((QbColumnNode) getModel(), queryData);
	}

	@Override
	public DragTracker getDragTracker(Request request) {
		getViewer().select(this);
		return new ConnectionDragCreationTool();
	}

	@Override
	protected void createEditPolicies() {
		// Allow removal of the associated model element
		// installEditPolicy(EditPolicy.COMPONENT_ROLE, new
		// ShapeComponentEditPolicy());

		// Allow the creation of join between two columns in different tables
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new QbColumnNodeEditPolicy(queryData));
	}

	@Override
	protected List<Join> getModelSourceConnections() {
		QbColumnNode columnNode = (QbColumnNode) getModel();
		return columnNode.getSourceJoins();
	}

	@Override
	protected List<Join> getModelTargetConnections() {
		QbColumnNode columnNode = (QbColumnNode) getModel();
		return columnNode.getTargetJoins();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new QbJoinSourceAnchor(getFigure());
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new QbJoinSourceAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new QbJoinTargetAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new QbJoinTargetAnchor(getFigure());
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		QbColumnNodeFigure columnFigure = (QbColumnNodeFigure) getFigure();
		if (value != EditPart.SELECTED_NONE)
			columnFigure.setSelected(true);
		else
			columnFigure.setSelected(false);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		refresh();
		QbColumnNode columnNode = (QbColumnNode) getModel();
		if (columnNode.isSelected()) {
			QbColumnNodeFigure columnFigure = (QbColumnNodeFigure) getFigure();
			columnFigure.setChecked(columnNode.isSelected());
		}
	}
}
