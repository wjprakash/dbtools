
package com.nayaware.dbtools.querybuilder.parts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.nayaware.dbtools.nodes.WaitingNode;

/**
 * This Edit part represents the blank page when the query builder opens first
 * The part displays some informational message
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbBlankPageEditPart extends AbstractGraphicalEditPart {

	public QbBlankPageEditPart(WaitingNode blankPage) {
		setModel(blankPage);
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = new Figure();
		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(false);
		layout.setHorizontal(true);
		figure.setLayoutManager(layout);
		WaitingNode blankPage = (WaitingNode) getModel();
		Label name = new Label(blankPage.getDisplayName(), blankPage.getIcon());
		figure.add(name);
		return figure;
	}

	@Override
	protected void refreshVisuals() {
		Figure figure = (Figure) getFigure();
		if (getParent() instanceof QueryBuilderEditPart) {
			QueryBuilderEditPart parent = (QueryBuilderEditPart) getParent();
			Rectangle constraint = new Rectangle(75, 75, -1, -1);
			parent.setLayoutConstraint(this, figure, constraint);
		}
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}
}
