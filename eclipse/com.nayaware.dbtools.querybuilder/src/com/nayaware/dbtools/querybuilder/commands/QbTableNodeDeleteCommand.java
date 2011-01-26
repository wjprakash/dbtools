
package com.nayaware.dbtools.querybuilder.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.querybuilder.model.QbTableNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * Command to delete the table from the Query Builder design area
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbTableNodeDeleteCommand extends Command {

	private QueryData query;
	private QbTableNode tableNode;

	public QbTableNodeDeleteCommand(QueryData query, QbTableNode tableNode) {
		this.query = query;
		this.tableNode = tableNode;
	}

	@Override
	public void execute() {
		query.removeTableNode(tableNode);
	}
}
