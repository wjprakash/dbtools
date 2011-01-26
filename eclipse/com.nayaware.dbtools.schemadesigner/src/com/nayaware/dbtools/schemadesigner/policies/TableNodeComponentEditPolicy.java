
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.nayaware.dbtools.schemadesigner.commands.TableNodeDeleteCommand;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Object schemata = getHost().getParent().getModel();
		Object tableNode = getHost().getModel();
		if ((schemata instanceof Schemata)
				&& (tableNode instanceof SdTableNode)) {
			return new TableNodeDeleteCommand((Schemata) schemata,
					(SdTableNode) tableNode);
		}
		return super.createDeleteCommand(deleteRequest);
	}
}
