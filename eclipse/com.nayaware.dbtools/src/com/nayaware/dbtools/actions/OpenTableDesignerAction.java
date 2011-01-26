
package com.nayaware.dbtools.actions;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.model.Table;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.SchemaNode;
import com.nayaware.dbtools.nodes.TableGroupNode;
import com.nayaware.dbtools.ui.TableDesignerDialog;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Action to open the Table Designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class OpenTableDesignerAction extends AbstractNodeAction {

	public final static String ID = "com.nayaware.dbtools.actions.createTableAction"; //$NON-NLS-1$
	private String baseNewTableName = Messages.OpenTableDesignerAction_0;

	public OpenTableDesignerAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.CreateTableAction_1);
		setToolTipText(Messages.CreateTableAction_2);
		setImageDescriptor(ImageUtils
				.getImageDescriptor(ImageUtils.TABLE_EDITOR));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		if ((node instanceof SchemaNode) || (node instanceof TableGroupNode)) {
			ISchema schema = null;
			if (node.getDatbaseObject() instanceof ISchema) {
				schema = (ISchema) node.getDatbaseObject();
			}
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			Table table = new Table(node.getDatbaseObject().getDatabaseInfo(),
					schema, getUniqueTableName());
			TableDesignerDialog tableDesignerDialog = new TableDesignerDialog(
					shell, table);
			int ret = tableDesignerDialog.open();
			if (ret == IDialogConstants.OK_ID) {
				node.refresh();
				if (node instanceof SchemaNode) {
					((SchemaNode) node).getTableGroupNode().expandViewTo();
				} else if (node instanceof TableGroupNode) {
					((TableGroupNode) node).expandViewTo();
				}
			}
		}
	}

	private String getUniqueTableName() {
		String newTableName = baseNewTableName;
		int dbType = node.getDatbaseObject().getDatabaseInfo()
				.getConnectionConfig().getDbType();
		if (dbType == IConnectionType.SQLITE) {
			// SQLITE table names are always upper case
			newTableName = newTableName.toUpperCase();
		}
		List<String> tableNames = null;
		try {
			if (node.getDatbaseObject() instanceof ISchema) {
				ISchema schema = (ISchema) node.getDatbaseObject();

				tableNames = schema.getTableNames(true);

			} else if (node.getDatbaseObject() instanceof IConnection) {
				IConnection database = (IConnection) node.getDatbaseObject();
				tableNames = database.getTableNames(true);
			}
			if (tableNames != null) {
				int count = 1;
				while (tableNames.contains(newTableName)) {
					newTableName += count++;
				}
			}
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
		}
		return newTableName;
	}
}
