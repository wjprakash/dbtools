
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Direct edit command that allows to edit and set the Table Node name
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeDirectEditCommand extends Command {
	private String oldName;
	private String newName;

	private SdTableNode tableNode;
	private Schemata schemata;

	public TableNodeDirectEditCommand(SdTableNode tableNode, Schemata schemata,
			String newName) {
		this.tableNode = tableNode;
		this.schemata = schemata;
		this.newName = newName;
	}

	@Override
	public boolean canExecute() {
		if ((newName == null) || newName.trim().equals("")) { //$NON-NLS-1$
			ErrorManager.showError(Messages.getString("TableNodeDirectEditCommand.1")); //$NON-NLS-1$
			return false;
		}
		if (!schemata.isUniqueTableName(newName)) {
			ErrorManager.showError(Messages.getString("TableNodeDirectEditCommand.2")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		oldName = tableNode.getName();
		tableNode.setName(newName);
	}

	public void setName(String name) {
		newName = name;
	}

	@Override
	public void undo() {
		tableNode.setName(oldName);
	}
}
