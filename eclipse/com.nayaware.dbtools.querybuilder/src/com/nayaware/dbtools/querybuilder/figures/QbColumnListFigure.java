
package com.nayaware.dbtools.querybuilder.figures;

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
public class QbColumnListFigure extends Figure {

	final Figure view = new Figure();

	public QbColumnListFigure() {
		super();
		setLayoutManager(new ToolbarLayout());
		setBorder(new ColumnListFigureBorder());
		ScrollPane scrollpane = new ScrollPane();
		view.setLayoutManager(new FlowLayout(FlowLayout.VERTICAL));
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		view.setLayoutManager(layout);
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
