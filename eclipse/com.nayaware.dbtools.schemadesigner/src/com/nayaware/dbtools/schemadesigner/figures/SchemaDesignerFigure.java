
package com.nayaware.dbtools.schemadesigner.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.SchemeBorder;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerFigure extends FreeformLayer {

	public SchemaDesignerFigure() {
		super();
		setOpaque(true);
		setBorder(new MarginBorder(3));
		// setPreferredSize(new Dimension(600, 600));
		setLayoutManager(new FreeformLayout());
		// setBackgroundColor(ColorConstants.menuBackground);
		setBorder(new SchemeBorder(SchemeBorder.SCHEMES.LOWERED));
	}
}
