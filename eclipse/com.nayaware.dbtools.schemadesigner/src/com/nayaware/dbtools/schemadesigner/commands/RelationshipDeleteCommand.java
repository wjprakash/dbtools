
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.schemadesigner.model.Relationship;
import com.nayaware.dbtools.schemadesigner.model.Schemata;

/**
 * Command to delete the Relationship connection line
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class RelationshipDeleteCommand extends Command {

	private Relationship relationship;
	private Schemata schemata;

	public RelationshipDeleteCommand(Schemata schemata,
			Relationship relationship) {
		this.relationship = relationship;
		this.schemata = schemata;
	}

	@Override
	public void execute() {
		schemata.removeRelationship(relationship);
	}

}
