
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
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.editors.IQueryBuilderInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.QueryNode;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Action to open the QueryData Builder
 * 
 * @author Winston Prakash
 * @version 1.0
 */

public class OpenQueryBuilderAction extends AbstractNodeAction {
	public final static String ID = "com.nayaware.dbtools.actions.queryBuilderAction"; //$NON-NLS-1$

	public OpenQueryBuilderAction(Viewer viewer) {
		super(viewer);
		initialize();
	}

	public OpenQueryBuilderAction(AbstractNode node) {
		super(node);
		initialize();
	}

	private void initialize() {
		setId(ID);
		setText(Messages.QueryBuilderAction_1);
		setToolTipText(Messages.QueryBuilderAction_2);
		setImageDescriptor(ImageUtils
				.getImageDescriptor(ImageUtils.QUERY_BUILDER));
	}

	public boolean isEnabled() {
		boolean enabled = super.isEnabled();

		if (enabled) {
			IExtensionRegistry extensionRegistry = Platform
					.getExtensionRegistry();
			if (extensionRegistry != null) {
				IExtensionPoint extension = extensionRegistry
						.getExtensionPoint(DbExplorerPlugin.PLUGIN_ID,
								DbExplorerPlugin.QUERY_BUILDER_EXTENSION_POINT);

				if (extension != null) {
					IConfigurationElement[] elements = extension
					.getConfigurationElements();
					enabled = elements.length > 0;
				}else{
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
		AbstractNode connectionNode = getNode();
		while (!(connectionNode instanceof ConnectionNode)) {
			connectionNode = connectionNode.getParent();
		}

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IDatabaseInfo dbInfo = getNode().getDatbaseObject().getDatabaseInfo();
		AbstractNode inputNode;
		if (getNode() instanceof QueryNode) {
			inputNode = getNode();
		} else {
			inputNode = connectionNode;
		}

		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry != null) {
			IExtensionPoint extension = extensionRegistry.getExtensionPoint(
					DbExplorerPlugin.PLUGIN_ID,
					DbExplorerPlugin.QUERY_BUILDER_EXTENSION_POINT);

			if (extension != null) {
				try {
					IConfigurationElement[] elements = extension
							.getConfigurationElements();
					IConfigurationElement confElement = elements[0];
					String queryBuilderId = confElement.getAttribute("id");
					IQueryBuilderInput queryBuilderInput = (IQueryBuilderInput) confElement
							.createExecutableExtension("editorInput"); //$NON-NLS-1$
					queryBuilderInput.setDatabaseInfo(dbInfo);
					queryBuilderInput.setConnectionNode(connectionNode);
					queryBuilderInput.setNode(inputNode);
					page.openEditor(queryBuilderInput, queryBuilderId, true);
				} catch (InvalidRegistryObjectException exc) {
					ErrorManager.showException(exc);
				} catch (CoreException exc) {
					ErrorManager.showException(exc);
				}

			}
		}
	}
}