
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;
import com.nayaware.dbtools.schemadesigner.parts.SchemaDesignerEditPart;
import com.nayaware.dbtools.schemadesigner.parts.TableNodeEditPart;

/**
 * A command to resize and/or move a shape. The command can be undone or redone.
 * 
 * @author Winston Prakash
 */
public class TableNodeSetBoundsCommand extends Command {

	private final Rectangle newBounds;

	private final ChangeBoundsRequest request;

	private TableNodeEditPart tableNodeEditPart;

	private Schemata schemata;

	public TableNodeSetBoundsCommand(Schemata schemata,
			TableNodeEditPart tableNodeEditPart, ChangeBoundsRequest req,
			Rectangle newBounds) {
		this.schemata = schemata;
		if (tableNodeEditPart == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.tableNodeEditPart = tableNodeEditPart;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		setLabel(Messages.getString("TableNodeSetBoundsCommand.0")); //$NON-NLS-1$
	}

	@Override
	public boolean canExecute() {
		Object type = request.getType();
		// make sure the Request is of a type we support:
		return (RequestConstants.REQ_MOVE.equals(type)
				|| RequestConstants.REQ_MOVE_CHILDREN.equals(type)
				|| RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN
				.equals(type));
	}

	@Override
	public void execute() {
		SchemaDesignerEditPart parent = (SchemaDesignerEditPart) tableNodeEditPart
				.getParent();
		SdTableNode tableNode = (SdTableNode) tableNodeEditPart.getModel();
		tableNode.setSize(newBounds.getSize());
		tableNode.setLocation(newBounds.getLocation());
		parent.setLayoutConstraint(tableNodeEditPart, tableNodeEditPart
				.getFigure(), newBounds);
		schemata.setDirty(true);
	}

}
