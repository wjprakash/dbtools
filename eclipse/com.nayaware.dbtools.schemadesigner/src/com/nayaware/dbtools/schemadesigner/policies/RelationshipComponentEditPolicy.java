
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.nayaware.dbtools.schemadesigner.commands.RelationshipDeleteCommand;
import com.nayaware.dbtools.schemadesigner.model.Relationship;
import com.nayaware.dbtools.schemadesigner.model.Schemata;

/**
 * Policy to edit the Relationship component
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class RelationshipComponentEditPolicy extends ComponentEditPolicy {

	private Schemata schemata;

	public RelationshipComponentEditPolicy(Schemata schemata) {
		this.schemata = schemata;
	}

	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		Relationship relationship = (Relationship) getHost().getModel();
		return new RelationshipDeleteCommand(schemata, relationship);
	}
}
