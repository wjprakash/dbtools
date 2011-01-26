
package com.nayaware.dbtools.querybuilder.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.nayaware.dbtools.querybuilder.commands.QbTableNodeDeleteCommand;
import com.nayaware.dbtools.querybuilder.model.QbTableNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * Policy to handle the table node deletion
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbTableNodeComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Object query = getHost().getParent().getModel();
		Object tableNode = getHost().getModel();
		if ((query instanceof QueryData) && (tableNode instanceof QbTableNode)) {
			return new QbTableNodeDeleteCommand((QueryData) query,
					(QbTableNode) tableNode);
		}
		return super.createDeleteCommand(deleteRequest);
	}
}
