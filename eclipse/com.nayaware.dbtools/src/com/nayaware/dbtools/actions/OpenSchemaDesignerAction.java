
package com.nayaware.dbtools.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IConnectionType;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.editors.ISchemaDesignerInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.QueryNode;
import com.nayaware.dbtools.nodes.SchemaDiagramNode;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Opens the schema designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */

public class OpenSchemaDesignerAction extends AbstractNodeAction {
	public final static String ID = "com.nayaware.dbtools.actions.openSchemaDesignerAction"; //$NON-NLS-1$

	public OpenSchemaDesignerAction(Viewer viewer) {
		super(viewer);
		initialize();
	}

	public OpenSchemaDesignerAction(AbstractNode node) {
		super(node);
		initialize();
	}

	private void initialize() {
		setId(ID);
		setText(Messages.OpenSchemaDesignerAction_1);
		setToolTipText(Messages.OpenSchemaDesignerAction_2);
		setImageDescriptor(ImageUtils
				.getImageDescriptor(ImageUtils.SCHEMA_DESIGNER));
	}

	@Override
	public boolean isEnabled() {
		boolean enabled = super.isEnabled();

		if (enabled) {
			IExtensionRegistry extensionRegistry = Platform
					.getExtensionRegistry();
			if (extensionRegistry != null) {
				IExtensionPoint extension = extensionRegistry
						.getExtensionPoint(DbExplorerPlugin.PLUGIN_ID,
								DbExplorerPlugin.SCHEMA_DESIGNER_EXTENSION_POINT);

				if (extension != null) {
					IConfigurationElement[] elements = extension
							.getConfigurationElements();
					enabled = elements.length > 0;
				} else {
					enabled = false;
				}
			}
		}

		if (enabled) {
			// Currently supports only MySQL
			IConnectionConfig connectionConfig = getNode().getDatbaseObject()
					.getDatabaseInfo().getConnectionConfig();
			if (connectionConfig.getDbType() == IConnectionType.MYSQL) {
				enabled = true;
			} else {
				enabled = false;
			}
		}

		return enabled;
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {

		AbstractNode connectionNode = getNode();
		if (!(connectionNode instanceof QueryNode)) {
			while (!(connectionNode instanceof ConnectionNode)) {
				connectionNode = connectionNode.getParent();
			}
		}

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IDatabaseInfo dbInfo = getNode().getDatbaseObject().getDatabaseInfo();

		AbstractNode inputNode;
		if (getNode() instanceof SchemaDiagramNode) {
			inputNode = getNode();
		} else {
			inputNode = connectionNode;
		}
		
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry != null) {
			IExtensionPoint extension = extensionRegistry.getExtensionPoint(
					DbExplorerPlugin.PLUGIN_ID,
					DbExplorerPlugin.SCHEMA_DESIGNER_EXTENSION_POINT);

			if (extension != null) {
				try {
					IConfigurationElement[] elements = extension
							.getConfigurationElements();
					IConfigurationElement confElement = elements[0];
					String schemaDesignerId = confElement.getAttribute("id");
					ISchemaDesignerInput schemaDesignerInput = (ISchemaDesignerInput) confElement
							.createExecutableExtension("editorInput"); //$NON-NLS-1$
					schemaDesignerInput.setDatabaseInfo(dbInfo);
					schemaDesignerInput.setConnectionNode(connectionNode);
					schemaDesignerInput.setNode(inputNode);
					page.openEditor(schemaDesignerInput, schemaDesignerId, true);
				} catch (InvalidRegistryObjectException exc) {
					ErrorManager.showException(exc);
				} catch (CoreException exc) {
					ErrorManager.showException(exc);
				}

			}
		}
	}
}