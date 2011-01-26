
package com.nayaware.dbtools.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.editors.ISchemaDiagramInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.QueryNode;
import com.nayaware.dbtools.nodes.SchemaNode;
import com.nayaware.dbtools.nodes.TableGroupNode;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Opens the Schema Diagram Viewer
 * 
 * @author Winston Prakash
 * @version 1.0
 */

public class ViewSchemaDiagramAction extends AbstractNodeAction {
	public final static String ID = "com.nayaware.dbtools.actions.viewSchemaDiagramAction"; //$NON-NLS-1$

	public ViewSchemaDiagramAction(AbstractNode node) {
		super(node);
		setId(ID);
		setText(Messages.ViewSchemaDiagramAction_1);
		setToolTipText(Messages.ViewSchemaDiagramAction_2);
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.ER_DIAGRAM));
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
								DbExplorerPlugin.SCHEMA_DIAGRAM_VIEWER_EXTENSION_POINT);

				if (extension != null) {
					IConfigurationElement[] elements = extension
							.getConfigurationElements();
					enabled = elements.length > 0;
				} else {
					enabled = false;
				}
			}
		}

		return enabled;
	}

	/**
	 * Run the action
	 */
	@Override
	public void run() {
		if ((node.getDatbaseObject() instanceof ISchema)) {
			SchemaNode schemaNode = null;
			if (node instanceof SchemaNode) {
				schemaNode = ((SchemaNode) node);
			}
			
			AbstractNode connectionNode = getNode();
			if (!(connectionNode instanceof QueryNode)) {
				while (!(connectionNode instanceof ConnectionNode)) {
					connectionNode = connectionNode.getParent();
				}
			}

			if (node instanceof TableGroupNode) {
				schemaNode = ((SchemaNode) node.getParent());
			}

			if (schemaNode != null) {
				
				IDatabaseInfo dbInfo = getNode().getDatbaseObject().getDatabaseInfo();

				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
				if (extensionRegistry != null) {
					IExtensionPoint extension = extensionRegistry.getExtensionPoint(
							DbExplorerPlugin.PLUGIN_ID,
							DbExplorerPlugin.SCHEMA_DIAGRAM_VIEWER_EXTENSION_POINT);

					if (extension != null) {
						try {
							IConfigurationElement[] elements = extension
									.getConfigurationElements();
							IConfigurationElement confElement = elements[0];
							String schemaViewerId = confElement.getAttribute("id");
							ISchemaDiagramInput schemaViewerInput = (ISchemaDiagramInput) confElement
									.createExecutableExtension("editorInput"); //$NON-NLS-1$
							schemaViewerInput.setDatabaseInfo(dbInfo);
							schemaViewerInput.setConnectionNode(connectionNode);
							schemaViewerInput.setSchemaNode(schemaNode);
							page.openEditor(schemaViewerInput, schemaViewerId, true);
						} catch (InvalidRegistryObjectException exc) {
							ErrorManager.showException(exc);
						} catch (CoreException exc) {
							ErrorManager.showException(exc);
						}

					}
				}
			} else {
				ErrorManager.showException(new NullPointerException(
						Messages.ViewSchemaDiagramAction_0));
			}
		}
	}
}