
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.nayaware.dbtools.schemadesigner.commands.RelationshipCreateCommand;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;

/**
 * This Edit policy governs the creation and recreation of relationship
 * connection between tables
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeEditPolicy extends GraphicalNodeEditPolicy {

	private Schemata schemata;

	public ColumnNodeEditPolicy(Schemata schemata) {
		this.schemata = schemata;
	}

	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		RelationshipCreateCommand cmd = (RelationshipCreateCommand) request
				.getStartCommand();
		cmd.setTarget((SdColumnNode) getHost().getModel());
		return cmd;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		SdColumnNode source = (SdColumnNode) getHost().getModel();
		RelationshipCreateCommand cmd = new RelationshipCreateCommand(schemata,
				source);
		request.setStartCommand(cmd);
		return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}
}
