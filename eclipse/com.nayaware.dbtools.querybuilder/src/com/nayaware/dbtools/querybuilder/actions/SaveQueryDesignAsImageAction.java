
package com.nayaware.dbtools.querybuilder.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.util.ImageUtils;

/**
 * An action to save the canvas of schema designer as image
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SaveQueryDesignAsImageAction extends Action {

	public final static String ID = "com.nayaware.dbtools.actions.saveQueryDesignAsImageAction"; //$NON-NLS-1$
	private GraphicalViewer graphicalViewer;
	protected String defaultFileName;
	
	public SaveQueryDesignAsImageAction(GraphicalViewer viewer) {
		graphicalViewer = viewer;
		setId(ID);
		setText(Messages.getString("SaveQueryDesignAsImageAction.0")); //$NON-NLS-1$
		setToolTipText(Messages.getString("SaveQueryDesignAsImageAction.1")); //$NON-NLS-1$
		setImageDescriptor(ImageUtils
				.getImageDescriptor(ImageUtils.SAVE_AS_IMAGE));
		defaultFileName = Messages.getString("SaveQueryDesignAsImageAction.2"); //$NON-NLS-1$
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		FileDialog dlg = new FileDialog(shell, SWT.SAVE);
		dlg.setFilterPath(System.getProperty("user.home")); //$NON-NLS-1$
		dlg.setFileName(defaultFileName);
		dlg.setFilterNames(new String[] {
				Messages.getString("SaveQueryDesignAsImageAction.4"), //$NON-NLS-1$
				Messages.getString("SaveQueryDesignAsImageAction.5") }); //$NON-NLS-1$
		String saveLocation = dlg.open();
		if (saveLocation != null) {
			ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) graphicalViewer
					.getEditPartRegistry().get(LayerManager.ID);
			IFigure rootFigure = ((LayerManager) rootEditPart)
					.getLayer(LayerConstants.PRINTABLE_LAYERS);
			Rectangle rootFigureBounds = rootFigure.getBounds();
			Control figureCanvas = graphicalViewer.getControl();

			Image img = new Image(Display.getDefault(), rootFigureBounds.width,
					rootFigureBounds.height);
			GC imageGC = new GC(img);
			figureCanvas.print(imageGC); // This is Eclipse 3.4 only API

			ImageLoader imgLoader = new ImageLoader();
			imgLoader.data = new ImageData[] { img.getImageData() };
			imgLoader.save(saveLocation, SWT.IMAGE_JPEG);

			imageGC.dispose();
			img.dispose();
		}
	}
}
