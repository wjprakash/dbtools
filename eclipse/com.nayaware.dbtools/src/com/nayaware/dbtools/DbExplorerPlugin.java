
package com.nayaware.dbtools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.nayaware.dbtools.util.ImageUtils;
import com.nayaware.dbtools.util.ResourceUtils;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class DbExplorerPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.nayaware.dbtools"; //$NON-NLS-1$

	public static final String SCHEMA_DESIGNER_EXTENSION_POINT = "schemaDesigner";

	public static final String SCHEMA_DIAGRAM_VIEWER_EXTENSION_POINT = "schemaViewer";
	
	public static String JDBC_DRIVER_EXTENSION_POINT = "jdbcDriver"; //$NON-NLS-1$
	
	public static String QUERY_BUILDER_EXTENSION_POINT = "queryBuilder"; //$NON-NLS-1$

	// The shared instance
	private static DbExplorerPlugin dbExplorerPlugin;

	public DbExplorerPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		dbExplorerPlugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		dbExplorerPlugin = null;
		super.stop(context);
		ImageUtils.dispose();
		ResourceUtils.dispose();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static DbExplorerPlugin getDefault() {
		return dbExplorerPlugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
