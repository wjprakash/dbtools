
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.nayaware.dbtools.schemadesigner.commands.ColumnNodeDeleteCommand;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * Edit policy to delete column node from the table
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Object tableNode = getHost().getParent().getModel();
		Object columnNode = getHost().getModel();
		if ((tableNode instanceof SdTableNode)
				&& (columnNode instanceof SdColumnNode)) {
			return new ColumnNodeDeleteCommand((SdTableNode) tableNode,
					(SdColumnNode) columnNode);
		}
		return super.createDeleteCommand(deleteRequest);
	}
}
