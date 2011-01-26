
package com.nayaware.dbtools.querybuilder.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.querybuilder.figures.QbTableNodeFigure;
import com.nayaware.dbtools.querybuilder.model.QbTableNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.querybuilder.policies.QbTableNodeComponentEditPolicy;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class QbTableNodeEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private QueryData query;

	public QbTableNodeEditPart(QbTableNode tableNode, QueryData query) {
		setModel(tableNode);
		tableNode.addPropertyChangeListener(this);
		this.query = query;
	}

	@Override
	protected IFigure createFigure() {
		return new QbTableNodeFigure((QbTableNode) getModel(), query);
	}

	@Override
	protected void refreshVisuals() {
		QbTableNodeFigure tableFigure = (QbTableNodeFigure) getFigure();
		QueryBuilderEditPart parent = (QueryBuilderEditPart) getParent();
		QbTableNode tableNode = (QbTableNode) getModel();
		Point location = tableFigure.getLocation();
		Rectangle constraint = new Rectangle(location.x, location.y, -1, -1);
		if (tableNode.getLocation() != null) {
			constraint.setLocation(tableNode.getLocation());
		}
		if (tableNode.getSize() != null) {
			constraint.setSize(tableNode.getSize());
		}
		parent.setLayoutConstraint(this, tableFigure, constraint);
	}

	@Override
	protected List<AbstractNode> getModelChildren() {
		QbTableNode tableNode = (QbTableNode) getModel();
		return tableNode.getQbColumnNodeList();
	}

	/**
	 * @return the Content pane for adding or removing child figures
	 */
	@Override
	public IFigure getContentPane() {
		QbTableNodeFigure figure = (QbTableNodeFigure) getFigure();
		return figure.getColumnListFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new QbTableNodeComponentEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (AbstractNode.NODE_CHILDREN_MODIFIED.equals(property)
				|| AbstractNode.NODE_MODIFIED.equals(property)) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					refresh();
				}
			});
		}
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		activate();
		QbTableNodeFigure tableFigure = (QbTableNodeFigure) getFigure();
		if (value != EditPart.SELECTED_NONE) {
			tableFigure.setSelected(true);
		} else {
			tableFigure.setSelected(false);
		}
	}

}
