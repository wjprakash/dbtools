
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.schemadesigner.commands.ColumnNodeCreateCommand;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * This edit policy governs the restriction of component placement and creation
 * in the schema designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeLayoutEditPolicy extends FlowLayoutEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Class<?> childClass = (Class<?>) request.getNewObjectType();
		if (IColumn.class.isAssignableFrom(childClass)) {
			IColumn column = (IColumn) request.getNewObject();
			SdTableNode tableNode = (SdTableNode) getHost().getModel();
			ColumnNodeCreateCommand command = new ColumnNodeCreateCommand(
					column, tableNode);
			return command;
		} else {
			return null;
		}
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return null;
	}

	@Override
	protected void showLayoutTargetFeedback(Request request) {
		// Do nothing
	}

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		return null;
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return null;
	}

}
