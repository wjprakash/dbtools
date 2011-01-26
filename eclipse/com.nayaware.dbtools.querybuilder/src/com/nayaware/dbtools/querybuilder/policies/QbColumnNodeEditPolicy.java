
package com.nayaware.dbtools.querybuilder.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.nayaware.dbtools.querybuilder.commands.JoinCreateCommand;
import com.nayaware.dbtools.querybuilder.model.QbColumnNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * Edit policy to manage the connection creation for joins from one column to
 * another
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbColumnNodeEditPolicy extends GraphicalNodeEditPolicy {

	private QueryData queryData;

	public QbColumnNodeEditPolicy(QueryData queryData) {
		this.queryData = queryData;
	}

	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		JoinCreateCommand cmd = (JoinCreateCommand) request.getStartCommand();
		cmd.setTarget((QbColumnNode) getHost().getModel());
		return cmd;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		QbColumnNode source = (QbColumnNode) getHost().getModel();
		JoinCreateCommand cmd = new JoinCreateCommand(queryData, source);
		request.setStartCommand(cmd);
		return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
