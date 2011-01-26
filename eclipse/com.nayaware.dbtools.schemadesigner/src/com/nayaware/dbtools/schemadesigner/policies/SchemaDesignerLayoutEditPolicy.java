
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.schemadesigner.commands.TableNodeCreateCommand;
import com.nayaware.dbtools.schemadesigner.commands.TableNodeSetBoundsCommand;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.parts.TableNodeEditPart;
import com.nayaware.dbtools.util.FigureUtils;

/**
 * This edit policy governs the restriction of component placement and creation
 * in the schema designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerLayoutEditPolicy extends XYLayoutEditPolicy {

	private Schemata schemata;

	public SchemaDesignerLayoutEditPolicy(Schemata schemata) {
		this.schemata = schemata;
	}

	@Override
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child instanceof TableNodeEditPart) {
			return new TableNodeSetBoundsCommand(schemata,
					(TableNodeEditPart) child, request, (Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child, constraint);
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		// Not Used
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Class<?> childClass = (Class<?>) request.getNewObjectType();
		if (ITable.class.isAssignableFrom(childClass)) {
			// Return a command that can add a Table Node to the designer
			ITable table = (ITable) request.getNewObject();
			Schemata schemata = (Schemata) getHost().getModel();
			Point location = request.getLocation();
			return new TableNodeCreateCommand(table, schemata, location);
		} else {
			return null;
		}
	}

	/**
	 * Override to provide different feedback while moving
	 */
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof TableNodeEditPart) {
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
		}
		return null;
	}
}
