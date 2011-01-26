
package com.nayaware.dbtools.querybuilder.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.SchemeBorder;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderFigure extends FreeformLayer {

	public QueryBuilderFigure() {
		super();
		setOpaque(true);
		setBorder(new MarginBorder(3));
		setLayoutManager(new FreeformLayout());
		// setBackgroundColor(ColorConstants.menuBackground);
		setBorder(new SchemeBorder(SchemeBorder.SCHEMES.LOWERED));
	}
}
