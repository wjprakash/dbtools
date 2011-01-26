
package com.nayaware.dbtools.schemadesigner.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnListFigure extends Figure {
	final Figure view = new Figure();

	public ColumnListFigure() {
		super();
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(true);
		layout.setVertical(true);
		setLayoutManager(layout);

		setBorder(new ColumnListFigureBorder());
		ScrollPane scrollpane = new ScrollPane();
		FlowLayout layout1 = new FlowLayout();
		layout1.setHorizontal(false);
		layout1.setStretchMinorAxis(true);
		view.setLayoutManager(layout1);
		scrollpane.setContents(view);
		add(scrollpane);
	}

	public Figure getContentPane() {
		return view;
	}

	private static class ColumnListFigureBorder extends AbstractBorder {

		public Insets getInsets(IFigure figure) {
			return new Insets(5, 3, 3, 1);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
					tempRect.getTopRight());
		}
	}

}
