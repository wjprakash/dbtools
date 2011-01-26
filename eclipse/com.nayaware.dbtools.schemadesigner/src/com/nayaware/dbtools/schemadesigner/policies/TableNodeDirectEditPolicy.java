
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.nayaware.dbtools.schemadesigner.commands.TableNodeDirectEditCommand;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		Schemata schemata = (Schemata) getHost().getParent().getModel();
		SdTableNode tableNode = (SdTableNode) getHost().getModel();
		String newName = (String) request.getCellEditor().getValue();
		TableNodeDirectEditCommand command = new TableNodeDirectEditCommand(
				tableNode, schemata, newName);
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
	}

}
