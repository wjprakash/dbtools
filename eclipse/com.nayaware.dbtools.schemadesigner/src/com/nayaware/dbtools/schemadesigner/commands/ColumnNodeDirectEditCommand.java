
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Direct edit command that allows to edit and set the Table Node name
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeDirectEditCommand extends Command {
	private String oldName;
	private String newName;

	private SdColumnNode columnNode;

	public ColumnNodeDirectEditCommand(SdColumnNode columnNode2, String newName) {
		this.columnNode = columnNode2;
		this.newName = newName;
	}

	@Override
	public boolean canExecute() {
		if ((newName == null) || newName.trim().equals("")) { //$NON-NLS-1$
			ErrorManager.showError(Messages.getString("ColumnNodeDirectEditCommand.1")); //$NON-NLS-1$
			return false;
		}
		SdTableNode tableNode = columnNode.getParent();
		if (tableNode.findColumnNodeByName(newName) != null) {
			ErrorManager.showError(Messages.getString("ColumnNodeDirectEditCommand.2")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		oldName = columnNode.getName();
		columnNode.setName(newName);
	}

	public void setName(String name) {
		newName = name;
	}

	@Override
	public void undo() {
		columnNode.setName(oldName);
	}
}
