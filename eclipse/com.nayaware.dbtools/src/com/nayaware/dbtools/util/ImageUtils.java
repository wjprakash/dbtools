
package com.nayaware.dbtools.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.nayaware.dbtools.DbExplorerPlugin;

/**
 * Defines the Keys to identify the Images in the plugin and provide utilities
 * to obtain the images
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ImageUtils {

	/**
	 * Image Registry to register the loaded images
	 */
	private static final ImageRegistry imageRegistry = new ImageRegistry();

	/**
	 * Maps images to image decorators
	 */
	private static HashMap<Image, HashMap<Image, Image>> imageToDecoratorMap = new HashMap<Image, HashMap<Image, Image>>();

	private static final int MISSING_IMAGE_SIZE = 10;

	/**
	 * Image Key Constants
	 */
	public static final String DATABASE_GROUP = "databaseGroup.png"; //$NON-NLS-1$
	public static final String DATABASE = "database.png"; //$NON-NLS-1$
	public static final String SCHEMA = "schema.gif"; //$NON-NLS-1$
	public static final String TABLE_GROUP = "table_group.png"; //$NON-NLS-1$
	public static final String VIEW_GROUP = "view_group.png"; //$NON-NLS-1$
	public static final String TABLE = "table.gif"; //$NON-NLS-1$
	public static final String TABLE32 = "table.png"; //$NON-NLS-1$
	public static final String VIEW = "view.gif"; //$NON-NLS-1$
	public static final String COLUMN = "column.gif"; //$NON-NLS-1$
	public static final String COLUMN32 = "column.gif"; //$NON-NLS-1$
	public static final String PRIMARY_KEY_COLUM = "columnPrimary.png"; //$NON-NLS-1$
	public static final String FOREIGN_KEY_COLUM = "columnForeign.png"; //$NON-NLS-1$
	public static final String INDEX_COLUM = "columnIndex.png"; //$NON-NLS-1$
	public static final String NOT_NULL_COLUM = "columnNotNull.gif"; //$NON-NLS-1$
	public static final String CHECKED = "checked.png"; //$NON-NLS-1$
	public static final String UNCHECKED = "unchecked.png"; //$NON-NLS-1$

	public static final String RELATIONSHIP = "relationship.png"; //$NON-NLS-1$
	public static final String RELATIONSHIP32 = "relationship.png"; //$NON-NLS-1$

	public static final String EXECUTE = "runsql.png"; //$NON-NLS-1$

	public static final String BUSY = "hourglass.png"; //$NON-NLS-1$

	public static final String EXPAND_ALL = "expandAll.jpg"; //$NON-NLS-1$
	public static final String COLLAPSE_ALL = "collapseall.gif"; //$NON-NLS-1$

	public static final String SCHEMA_DESIGNER = "schemaDesigner.png"; //$NON-NLS-1$
	public static final String QUERY_BUILDER = "queryBuilder.png"; //$NON-NLS-1$
	public static final String SQL_EDITOR = "sqlEditor.png"; //$NON-NLS-1$

	public static final String ER_DIAGRAM = "schemaDiagram.gif"; //$NON-NLS-1$

	public static final String ADD = "add.gif"; //$NON-NLS-1$
	public static final String DELETE = "delete.gif"; //$NON-NLS-1$

	public static final String SAVE_AS_IMAGE = "saveAsImage.gif"; //$NON-NLS-1$

	public static final String INFORMATION = "information.png"; //$NON-NLS-1$

	public static final String ERROR = "error.png"; //$NON-NLS-1$

	public static final String SUCCESS = "success.gif"; //$NON-NLS-1$

	public static final String SCRIPT = "script.gif"; //$NON-NLS-1$
	public static final String SCRIPTS_FOLDER = "scriptsFolder.png"; //$NON-NLS-1$

	public static final String QUERY = "query.png"; //$NON-NLS-1$
	public static final String QUERIES_FOLDER = "queriesFolder.png"; //$NON-NLS-1$

	public static final String SCHEMA_DIAGRAM_FOLDER = "schemaDiagramFolder.png"; //$NON-NLS-1$
	public static final String SCHEMA_DIAGRAM = "schemaDiagram.png"; //$NON-NLS-1$

	public static final String IMPORT = "import_wiz.gif"; //$NON-NLS-1$
	public static final String EXPORT = "export_wiz.gif"; //$NON-NLS-1$

	public static final String UPDATE = "update.gif"; //$NON-NLS-1$

	public static final String SQL = "sql.png"; //$NON-NLS-1$

	public static final String TEMPLATE = "template.gif"; //$NON-NLS-1$
	public static final String KEYWORD = "keyword.gif"; //$NON-NLS-1$

	public static final String TABLE_EDITOR = "tableEditor.gif"; //$NON-NLS-1$

	public static final String CSV = "csv.gif"; //$NON-NLS-1$
	public static final String EXPORT_CSV = "csvExport.gif"; //$NON-NLS-1$
	public static final String IMPORT_CSV = "csvImport.gif"; //$NON-NLS-1$

	public static final String FIRST_PAGE = "firstPage.png"; //$NON-NLS-1$
	public static final String LAST_PAGE = "lastPage.png"; //$NON-NLS-1$
	public static final String NEXT_PAGE = "nextPage.png"; //$NON-NLS-1$
	public static final String PREVIOUS_PAGE = "previousPage.png"; //$NON-NLS-1$

	/**
	 * Style constant for placing decorator image in top left corner of base
	 * image.
	 */
	public static final int TOP_LEFT = 1;
	/**
	 * Style constant for placing decorator image in top right corner of base
	 * image.
	 */
	public static final int TOP_RIGHT = 2;
	/**
	 * Style constant for placing decorator image in bottom left corner of base
	 * image.
	 */
	public static final int BOTTOM_LEFT = 3;
	/**
	 * Style constant for placing decorator image in bottom right corner of base
	 * image.
	 */
	public static final int BOTTOM_RIGHT = 4;

	public static Image missingImage;

	public static synchronized Image getIcon(String imageKey) {
		if (imageRegistry.get(imageKey) == null) {

			try {
				imageRegistry.put(imageKey, getImageDescriptor(imageKey)
						.createImage(true));
			} catch (Exception exc) {
				// TODOD: Do not use exception catching for logic
				if (missingImage == null) {
					missingImage = getMissingImage();
				}
				return missingImage;
			}
		}
		return imageRegistry.get(imageKey);
	}

	public static Image getIcon(Class<?> clazz, String imageKey) {
		if (imageRegistry.get(imageKey) == null) {

			try {
				imageRegistry.put(imageKey, getImage(clazz
						.getResourceAsStream(imageKey)));
			} catch (Exception exc) {
				// TODOD: Do not use exception catching for logic
				if (missingImage == null) {
					missingImage = getMissingImage();
				}
				return missingImage;
			}
		}
		return imageRegistry.get(imageKey);
	}

	protected static Image getImage(InputStream is) {
		Display display = Display.getCurrent();
		ImageData data = new ImageData(is);
		if (data.transparentPixel > 0)
			return new Image(display, data, data.getTransparencyMask());
		return new Image(display, data);
	}

	public static ImageDescriptor getImageDescriptor(String imageKey) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				DbExplorerPlugin.PLUGIN_ID, "icons/" + imageKey); //$NON-NLS-1$
	}

	/**
	 * Returns an image composed of a base image decorated by another image
	 * 
	 * @param baseImage
	 *            Image The base image that should be decorated
	 * @param decorator
	 *            Image The image to decorate the base image
	 * @param corner
	 *            The corner to place decorator image
	 * @return Image The resulting decorated image
	 */
	public static Image decorateImage(final Image baseImage,
			final Image decorator, final int corner) {
		HashMap<Image, Image> decoratedMap = imageToDecoratorMap.get(baseImage);
		if (decoratedMap == null) {
			decoratedMap = new HashMap<Image, Image>();
			imageToDecoratorMap.put(baseImage, decoratedMap);
		}
		Image result = decoratedMap.get(decorator);
		if (result == null) {
			final Rectangle bid = baseImage.getBounds();
			final Rectangle did = decorator.getBounds();
			final Point baseImageSize = new Point(bid.width, bid.height);
			CompositeImageDescriptor compositImageDesc = new CompositeImageDescriptor() {
				@Override
				protected void drawCompositeImage(int width, int height) {
					drawImage(baseImage.getImageData(), 0, 0);
					if (corner == TOP_LEFT) {
						drawImage(decorator.getImageData(), 0, 0);
					} else if (corner == TOP_RIGHT) {
						drawImage(decorator.getImageData(), bid.width
								- did.width - 1, 0);
					} else if (corner == BOTTOM_LEFT) {
						drawImage(decorator.getImageData(), 0, bid.height
								- did.height - 1);
					} else if (corner == BOTTOM_RIGHT) {
						drawImage(decorator.getImageData(), bid.width
								- did.width - 1, bid.height - did.height - 1);
					}
				}

				@Override
				protected Point getSize() {
					return baseImageSize;
				}
			};
			result = compositImageDesc.createImage();
			decoratedMap.put(decorator, result);
		}
		return result;
	}

	private static Image getMissingImage() {
		Image image = new Image(Display.getCurrent(), MISSING_IMAGE_SIZE,
				MISSING_IMAGE_SIZE);
		//
		GC gc = new GC(image);
		gc.setBackground(ResourceUtils.getColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
		gc.dispose();
		//
		return image;
	}

	/**
	 * Dispose all of the cached images
	 */
	public static void dispose() {
		imageRegistry.dispose();
		for (Iterator<HashMap<Image, Image>> I = imageToDecoratorMap.values()
				.iterator(); I.hasNext();) {
			HashMap<Image, Image> decoratedMap = I.next();
			for (Iterator<Image> J = decoratedMap.values().iterator(); J
					.hasNext();) {
				Image image = J.next();
				image.dispose();
			}
		}
	}
}
