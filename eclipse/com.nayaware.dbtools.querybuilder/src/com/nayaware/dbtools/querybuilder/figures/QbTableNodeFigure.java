
package com.nayaware.dbtools.querybuilder.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;

import com.nayaware.dbtools.querybuilder.model.QbTableNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * Figure for the Query Builder Table Column Node wrapper
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbTableNodeFigure extends Figure {

	public static Color tableColor = new Color(null, 255, 255, 206);
	private QbColumnListFigure qbColumnListFigure = new QbColumnListFigure();

	private QueryData queryData;
	InternalFrame frame = new InternalFrame();

	public QbTableNodeFigure(QbTableNode qbTableNode, QueryData queryData) {
		super();
		this.queryData = queryData;
		setOpaque(true);

		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(tableColor);
		setForegroundColor(ColorConstants.black);
		// Label name = new Label(sdTableNode.getName(), sdTableNode.getIcon());
		// name.setForegroundColor(ColorConstants.black);
		// add(name);
		frame.setLabel(qbTableNode.getName());
		frame.add(qbColumnListFigure);
		frame.setPreferredSize(new Dimension(200, 300));
		add(frame);
	}

	public IFigure getColumnListFigure() {
		return qbColumnListFigure.getContentPane();
	}

	public void setSelected(boolean selected) {
		frame.setSelected(selected);
		repaint();
	}
}
