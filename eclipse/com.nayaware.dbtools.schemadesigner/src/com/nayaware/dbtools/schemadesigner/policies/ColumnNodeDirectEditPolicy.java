
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.nayaware.dbtools.schemadesigner.commands.ColumnNodeDirectEditCommand;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		SdColumnNode columnNode = (SdColumnNode) getHost().getModel();
		String newName = (String) request.getCellEditor().getValue();
		ColumnNodeDirectEditCommand command = new ColumnNodeDirectEditCommand(
				columnNode, newName);
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
	}
}
