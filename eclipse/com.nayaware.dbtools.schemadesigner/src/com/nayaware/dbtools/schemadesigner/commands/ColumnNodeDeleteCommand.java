
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * Command to delete the column node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeDeleteCommand extends Command {

	private SdTableNode tableNode;
	private SdColumnNode columnNode;

	public ColumnNodeDeleteCommand(SdTableNode tableNode,
			SdColumnNode columnNode) {
		this.tableNode = tableNode;
		this.columnNode = columnNode;
	}

	@Override
	public void execute() {
		tableNode.removeColumnNode(columnNode);
	}
}
