
package com.nayaware.dbtools.querybuilder.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.nayaware.dbtools.querybuilder.commands.JoinDeleteCommand;
import com.nayaware.dbtools.querybuilder.model.Join;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class QbJoinComponentEditPolicy extends ComponentEditPolicy {

	private QueryData queryData;

	public QbJoinComponentEditPolicy(QueryData queryData) {
		this.queryData = queryData;
	}

	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		Join join = (Join) getHost().getModel();
		return new JoinDeleteCommand(queryData, join);
	}
}
