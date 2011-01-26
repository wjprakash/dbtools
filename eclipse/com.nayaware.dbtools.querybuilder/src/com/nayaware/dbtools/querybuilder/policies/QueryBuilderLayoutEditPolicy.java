
package com.nayaware.dbtools.querybuilder.policies;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.nayaware.dbtools.querybuilder.commands.QbTableNodeSetBoundsCommand;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.querybuilder.parts.QbTableNodeEditPart;
import com.nayaware.dbtools.util.FigureUtils;

/**
 * Layout policy used by the QueryData builder for laying out the tables
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderLayoutEditPolicy extends XYLayoutEditPolicy {

	private QueryData queryData;

	public QueryBuilderLayoutEditPolicy(QueryData queryData) {
		this.queryData = queryData;
	}

	@Override
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child instanceof QbTableNodeEditPart
				&& constraint instanceof Rectangle) {
			// return a command that can move and/or resize a Shape
			return new QbTableNodeSetBoundsCommand(queryData,
					(QbTableNodeEditPart) child, request,
					(Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child, constraint);
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	/**
	 * Override to provide different feedback while moving
	 */
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof QbTableNodeEditPart) {
			return new ResizableEditPolicy() {
				@Override
				protected IFigure createDragSourceFeedbackFigure() {
					// Use a transparent ghost rectangle for feedback
					Figure rectFigure = FigureUtils.createTransparentFigure(
							ColorConstants.red, 128);
					rectFigure.setForegroundColor(ColorConstants.white);
					rectFigure.setBounds(getInitialFeedbackBounds());
					addFeedback(rectFigure);
					return rectFigure;
				}
			};
		} else {
			return null;
		}
	}
}
