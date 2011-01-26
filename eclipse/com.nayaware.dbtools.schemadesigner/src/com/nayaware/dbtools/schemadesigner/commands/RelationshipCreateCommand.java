
package com.nayaware.dbtools.schemadesigner.commands;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.schemadesigner.model.Relationship;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Command to create the Table relationship
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class RelationshipCreateCommand extends Command {
	private SdColumnNode source;
	private SdColumnNode target;

	private Schemata schemata;

	public RelationshipCreateCommand(Schemata schemata, SdColumnNode source) {
		this.schemata = schemata;
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel(Messages.getString("RelationshipCreateCommand.0")); //$NON-NLS-1$
		this.source = source;
		// this.lineStyle = lineStyle;
	}

	@Override
	public boolean canExecute() {
		// disallow source -> source connections
		if ((source == null) || (target == null) || (source == target)
				|| (source.getParent() == target.getParent())) {
			return false;
		}
		// return false, if the source -> target connection exists already
		for (Iterator<Relationship> iter = source.getSourceRelationships()
				.iterator(); iter.hasNext();) {
			Relationship conn = iter.next();
			if (conn.getSource().equals(target)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void execute() {
		IColumn pkColumn = (IColumn) target.getDatbaseObject();
		if (!pkColumn.isPrimaryKey()) {
			ErrorManager.showError(Messages
					.getString("RelationshipCreateCommand.1")); //$NON-NLS-1$
			return;
		}

		// return false if the source is already a foreign key and attempts to
		// connect to different PK table

		IColumn fkColumn = (IColumn) source.getDatbaseObject();
		if (fkColumn.isForeignKey()) {
			String pkTable = fkColumn.getForeignKeyReferenceList().get(0)
					.getPrimaryKeyTable();
			if (!pkColumn.getTable().getName().equals(pkTable)) {
				ErrorManager.showError(Messages.getString("RelationshipCreateCommand.3") //$NON-NLS-1$
						+ pkTable);
				return;
			}
		}

		schemata.addRelationship(source, target);

	}

	@Override
	public void redo() {
		// connection.reconnect();
	}

	/**
	 * Set the target end point for the connection.
	 */
	public void setTarget(SdColumnNode target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}

	@Override
	public void undo() {
		// connection.disconnect();
	}
}
