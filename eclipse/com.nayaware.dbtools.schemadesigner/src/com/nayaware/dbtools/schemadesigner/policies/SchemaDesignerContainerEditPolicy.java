
package com.nayaware.dbtools.schemadesigner.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.schemadesigner.commands.TableNodeCreateCommand;
import com.nayaware.dbtools.schemadesigner.model.Schemata;

/**
 * This policy governs the addition of Columns to the Table
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerContainerEditPolicy extends ContainerEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Class<?> childClass = (Class<?>) request.getNewObjectType();
		if (ITable.class.isAssignableFrom(childClass)) {
			ITable table = (ITable) request.getNewObject();
			Schemata schemata = (Schemata) getHost().getModel();
			Point location = request.getLocation();
			TableNodeCreateCommand command = new TableNodeCreateCommand(table,
					schemata, location);
			return command;
		} else {
			return null;
		}
	}

}
