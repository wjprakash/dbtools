
package com.nayaware.dbtools.querybuilder.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.nayaware.dbtools.querybuilder.model.QbTableNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.querybuilder.parts.QbTableNodeEditPart;
import com.nayaware.dbtools.querybuilder.parts.QueryBuilderEditPart;

/**
 * A command to resize and/or move a shape. The command can be undone or redone.
 * 
 * @author Winston Prakash
 */
public class QbTableNodeSetBoundsCommand extends Command {

	private final Rectangle newBounds;

	private final ChangeBoundsRequest request;

	private QbTableNodeEditPart tableNodeEditPart;

	private QueryData queryData;

	public QbTableNodeSetBoundsCommand(QueryData queryData,
			QbTableNodeEditPart tableNodeEditPart, ChangeBoundsRequest req,
			Rectangle newBounds) {
		this.queryData = queryData;
		if (tableNodeEditPart == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.tableNodeEditPart = tableNodeEditPart;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		setLabel(Messages.getString("QbTableNodeSetBoundsCommand.0")); //$NON-NLS-1$
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
		QueryBuilderEditPart parent = (QueryBuilderEditPart) tableNodeEditPart
				.getParent();
		QbTableNode tableNode = (QbTableNode) tableNodeEditPart.getModel();
		tableNode.setSize(newBounds.getSize());
		tableNode.setLocation(newBounds.getLocation());
		parent.setLayoutConstraint(tableNodeEditPart, tableNodeEditPart
				.getFigure(), newBounds);
		queryData.setDirty(true);
	}

}
