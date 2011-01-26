
package com.nayaware.dbtools.schemadesigner.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.nodes.ColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * Command to create the Column Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeCreateCommand extends Command {
	IColumn column;
	SdTableNode sdTableNode;
	private String COLUMN_BASE_NAME = Messages.getString("ColumnNodeCreateCommand.0"); //$NON-NLS-1$

	public ColumnNodeCreateCommand(IColumn column, SdTableNode tableNode) {
		this.column = column;
		this.sdTableNode = tableNode;
	}

	@Override
	public boolean canExecute() {
		return (column != null) && (sdTableNode != null);
	}

	@Override
	public void execute() {
		SdColumnNode columnNode = new SdColumnNode(new ColumnNode(column));
		columnNode.setName(getUniqueColumnName());
		sdTableNode.addColumnNode(columnNode);
	}

	public String getUniqueColumnName() {
		int count = 0;
		String newColumnName = COLUMN_BASE_NAME + ++count;
		List<String> columnNames = sdTableNode.getTableNode().getTable()
				.getColumnNames();
		while (columnNames.contains(newColumnName)) {
			newColumnName = COLUMN_BASE_NAME + count++;
		}
		return newColumnName;
	}
}
