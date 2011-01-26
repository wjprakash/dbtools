
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeDeleteCommand extends Command {

	private Schemata schemata;
	private SdTableNode tableNode;

	public TableNodeDeleteCommand(Schemata schemata, SdTableNode tableNode) {
		this.schemata = schemata;
		this.tableNode = tableNode;
	}

	@Override
	public void execute() {
		schemata.removeTableNode(tableNode);
	}
}
