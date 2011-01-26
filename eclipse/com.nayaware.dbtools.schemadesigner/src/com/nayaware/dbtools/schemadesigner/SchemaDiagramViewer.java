
package com.nayaware.dbtools.schemadesigner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.parts.SchemaDesignerEditPartFactory;

/**
 * Viewer to view the Entity relationship of a database schema. Read only view.
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDiagramViewer extends GraphicalEditor {

	public static final String ID = "com.nayaware.dbtools.schemaDiagramViewer"; //$NON-NLS-1$
	private Schemata schemata;

	public SchemaDiagramViewer() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		schemata = (Schemata) getEditorInput().getAdapter(Schemata.class);
		setPartName(input.getName());
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(schemata);
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = this.getGraphicalViewer();
		viewer.setEditPartFactory(new SchemaDesignerEditPartFactory(schemata,
				false));

		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		List<String> zoomLevels = new ArrayList<String>(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);

		viewer.setRootEditPart(root);
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

		// Add Support for mouse wheel
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);

		ContextMenuProvider provider = new SchemaDesignerContextMenuProvider(
				viewer, getActionRegistry(), false);
		viewer.setContextMenu(provider);
		getSite().registerContextMenu(ID, provider, viewer);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class) {
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());
		}

		return super.getAdapter(type);
	}
}
