
package com.nayaware.dbtools.schemadesigner;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;

import com.nayaware.dbtools.model.Column;
import com.nayaware.dbtools.model.Table;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerPalette extends PaletteRoot {

	public SchemaDesignerPalette() {
		add(createToolsGroup());
		add(createComponentsDrawer());
	}

	private PaletteContainer createToolsGroup() {
		PaletteToolbar toolbar = new PaletteToolbar(Messages.getString("SchemaDesignerPalette.0")); //$NON-NLS-1$

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());

		return toolbar;
	}

	private static PaletteContainer createComponentsDrawer() {
		PaletteDrawer componentsDrawer = new PaletteDrawer(Messages.getString("SchemaDesignerPalette.1")); //$NON-NLS-1$

		ToolEntry component = new CombinedTemplateCreationEntry(Messages.getString("SchemaDesignerPalette.2"), //$NON-NLS-1$
				Messages.getString("SchemaDesignerPalette.3"), Table.class, //$NON-NLS-1$
				new SimpleFactory(Table.class), ImageUtils
						.getImageDescriptor(ImageUtils.TABLE), ImageUtils
						.getImageDescriptor(ImageUtils.TABLE32));
		componentsDrawer.add(component);

		component = new CombinedTemplateCreationEntry(Messages.getString("SchemaDesignerPalette.4"), //$NON-NLS-1$
				Messages.getString("SchemaDesignerPalette.5"), Column.class, //$NON-NLS-1$
				new SimpleFactory(Column.class), ImageUtils
						.getImageDescriptor(ImageUtils.COLUMN), ImageUtils
						.getImageDescriptor(ImageUtils.COLUMN32));
		componentsDrawer.add(component);

		component = new ConnectionCreationToolEntry(Messages.getString("SchemaDesignerPalette.6"), //$NON-NLS-1$
				Messages.getString("SchemaDesignerPalette.7"), //$NON-NLS-1$
				null, ImageUtils.getImageDescriptor(ImageUtils.RELATIONSHIP),
				ImageUtils.getImageDescriptor(ImageUtils.RELATIONSHIP32));
		componentsDrawer.add(component);

		return componentsDrawer;
	}
}
