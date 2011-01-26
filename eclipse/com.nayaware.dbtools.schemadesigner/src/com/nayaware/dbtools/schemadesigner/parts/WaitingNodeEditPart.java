
package com.nayaware.dbtools.schemadesigner.parts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.WaitingNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class WaitingNodeEditPart extends AbstractGraphicalEditPart {

	public WaitingNodeEditPart(WaitingNode node) {
		setModel(node);
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = new Figure();
		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(false);
		layout.setHorizontal(true);
		figure.setLayoutManager(layout);
		WaitingNode waitingNode = (WaitingNode) getModel();
		Label name = new Label(waitingNode.getDisplayName(), waitingNode
				.getIcon());
		figure.add(name);
		return figure;
	}

	@Override
	protected void refreshVisuals() {
		Figure figure = (Figure) getFigure();
		if (getParent() instanceof SchemaDesignerEditPart) {
			SchemaDesignerEditPart parent = (SchemaDesignerEditPart) getParent();
			AbstractNode waitingNode = (AbstractNode) getModel();
			Point location = figure.getLocation();
			Rectangle constraint = new Rectangle(location.x, location.y, -1, -1);
			if (waitingNode.getLocation() != null) {
				constraint.setLocation(waitingNode.getLocation());
			}
			if (waitingNode.getSize() != null) {
				constraint.setSize(waitingNode.getSize());
			}
			parent.setLayoutConstraint(this, figure, constraint);
		}
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}
}
