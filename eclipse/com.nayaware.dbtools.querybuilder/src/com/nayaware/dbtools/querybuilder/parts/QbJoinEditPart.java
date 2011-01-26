
package com.nayaware.dbtools.querybuilder.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.swt.graphics.Color;

import com.nayaware.dbtools.querybuilder.model.Join;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.querybuilder.policies.QbJoinComponentEditPolicy;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class QbJoinEditPart extends AbstractConnectionEditPart {

	private QueryData queryData;

	public static final Color selected = ColorConstants.red;
	public static final Color normal = ColorConstants.darkBlue;

	public QbJoinEditPart(Join join, QueryData queryData) {
		setModel(join);
		this.queryData = queryData;
	}

	@Override
	protected IFigure createFigure() {
		PolylineConnection polylineConnection = (PolylineConnection) super
				.createFigure();
		polylineConnection.setTargetDecoration(new PolygonDecoration());
		polylineConnection.setForegroundColor(normal);
		polylineConnection.setLineWidth(2);
		Join model = (Join) getModel();
		switch (model.getType()) {
		case Join.INNER:
			polylineConnection.setLineStyle(Graphics.LINE_SOLID);
			break;
		case Join.LEFT:
			polylineConnection.setLineStyle(Graphics.LINE_DASH);
			break;
		case Join.RIGHT:
			polylineConnection.setLineStyle(Graphics.LINE_DOT);
			break;
		}
		return polylineConnection;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new QbJoinComponentEditPolicy(queryData));
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new SelectionEditPolicy() {

					@Override
					protected void hideSelection() {
						PolylineConnection polylineConnection = (PolylineConnection) getFigure();
						polylineConnection.setForegroundColor(normal);
						polylineConnection.setLineWidth(2);
						polylineConnection.revalidate();
						polylineConnection.repaint();
					}

					@Override
					protected void showSelection() {
						PolylineConnection polylineConnection = (PolylineConnection) getFigure();
						polylineConnection.setForegroundColor(selected);
						polylineConnection.setLineWidth(2);
						polylineConnection.revalidate();
						polylineConnection.repaint();
					}

				});
	}
}
