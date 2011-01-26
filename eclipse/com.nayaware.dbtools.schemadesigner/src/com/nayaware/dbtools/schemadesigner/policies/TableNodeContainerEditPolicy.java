
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.schemadesigner.commands.ColumnNodeCreateCommand;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * This policy governs the addition of Columns to the Table
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeContainerEditPolicy extends ContainerEditPolicy {

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

}
