
package com.nayaware.dbtools.querybuilder.parts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.nayaware.dbtools.nodes.WaitingNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class QbWaitingNodeEditPart extends AbstractGraphicalEditPart {

	public QbWaitingNodeEditPart(WaitingNode node) {
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
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
}
