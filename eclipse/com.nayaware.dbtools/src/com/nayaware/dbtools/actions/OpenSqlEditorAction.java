
package com.nayaware.dbtools.actions;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.editors.sql.SqlEditor;
import com.nayaware.dbtools.editors.sql.SqlEditorInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.ScriptNode;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Opens the SQL Editor for editing and executing SQL script
 * 
 * @author Winston Prakash
 * @version 1.0
 */

public class OpenSqlEditorAction extends AbstractNodeAction {
	public final static String ID = "com.nayaware.dbtools.actions.openSchemaDesignerAction"; //$NON-NLS-1$

	public OpenSqlEditorAction(Viewer viewer) {
		super(viewer);
		initialize();
	}

	public OpenSqlEditorAction(AbstractNode node) {
		super(node);
		initialize();
	}

	private void initialize() {
		setId(ID);
		setId(ID);
		setText(Messages.OpenSqlEditorAction_1);
		setToolTipText(Messages.OpenSqlEditorAction_2);
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.SQL_EDITOR));
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		String sqlScript = ""; //$NON-NLS-1$
		AbstractNode node = getNode();
		if (!(node instanceof ScriptNode)) {
			while (!(node instanceof ConnectionNode)) {
				if (node.getDatbaseObject() instanceof ISchema) {
					IConnectionConfig conConfig = node.getDatbaseObject()
							.getDatabaseInfo().getConnectionConfig();
					if (conConfig.getDbType() == IConnectionType.MYSQL) {
						ISchema schema = (ISchema) node.getDatbaseObject();
						sqlScript = "USE " + schema.getName() + ";"; //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				node = node.getParent();
			}
		}
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		SqlEditorInput input = new SqlEditorInput(getNode().getDatbaseObject()
				.getDatabaseInfo(), node, sqlScript);
		try {
			page.openEditor(input, SqlEditor.ID, true);
		} catch (PartInitException exc) {
			ErrorManager.showException(exc);
		}
	}
}