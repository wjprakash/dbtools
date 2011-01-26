
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.api.IColumn;
import com.nayaware.dbtools.api.ITable;
import com.nayaware.dbtools.model.Column;
import com.nayaware.dbtools.nodes.ColumnNode;
import com.nayaware.dbtools.nodes.TableNode;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * Schema designer command that can create database objects
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeCreateCommand extends Command {

	private static final int DEFAULT_WIDTH = 220;
	private static final int DEFAULT_HEIGHT = 200;

	ITable table;
	Schemata schemata;
	Point location;

	public TableNodeCreateCommand(ITable table, Schemata schemata,
			Point location) {
		this.table = table;
		table.setDatabaseInfo(schemata.getDatabaseInfo());
		this.schemata = schemata;
		this.location = location;
	}

	@Override
	public boolean canExecute() {
		return (table != null) && (schemata != null);
	}

	@Override
	public void execute() {
		IColumn column = new Column(schemata.getDatabaseInfo(), table, "id"); //$NON-NLS-1$
		column.setPrimaryKeyFlag(true);
		column.setNullAllowed(false);
		column.setAutoIncrement(true);
		column.setSqlType(schemata.getDatabaseInfo().findSqlTypeByName(
				"INTEGER")); //$NON-NLS-1$
		SdColumnNode columnNode = new SdColumnNode(new ColumnNode(column));

		SdTableNode tableNode = new SdTableNode(new TableNode(table), schemata);
		tableNode.addColumnNode(columnNode);
		tableNode.setName(schemata.getUniqueTableName());
		tableNode.setLocation(location);
		tableNode.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		schemata.addTableNode(tableNode);
	}

}
