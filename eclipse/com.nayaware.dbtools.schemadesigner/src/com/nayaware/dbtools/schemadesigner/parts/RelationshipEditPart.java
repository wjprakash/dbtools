
package com.nayaware.dbtools.schemadesigner.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.swt.graphics.Color;

import com.nayaware.dbtools.schemadesigner.model.Relationship;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.policies.RelationshipComponentEditPolicy;

/**
 * Edit part for the relationship connection
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class RelationshipEditPart extends AbstractConnectionEditPart {

	private Schemata schemata;

	public static final Color selected = ColorConstants.red;
	public static final Color normal = ColorConstants.darkBlue;

	public RelationshipEditPart(Schemata schemata, Relationship model) {
		setModel(model);
		this.schemata = schemata;
	}

	@Override
	protected IFigure createFigure() {
		PolylineConnection polylineConnection = (PolylineConnection) super
				.createFigure();
		polylineConnection.setTargetDecoration(new PolygonDecoration());
		polylineConnection.setForegroundColor(normal);
		polylineConnection.setLineWidth(2);
		return polylineConnection;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RelationshipComponentEditPolicy(schemata));
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
