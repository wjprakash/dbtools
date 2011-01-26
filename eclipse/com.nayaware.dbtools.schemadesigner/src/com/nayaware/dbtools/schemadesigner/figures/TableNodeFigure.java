
package com.nayaware.dbtools.schemadesigner.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;

import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * GEF for the table node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeFigure extends Figure {

	public static Color tableColor = new Color(null, 255, 255, 206);
	private ColumnListFigure columnListFigure = new ColumnListFigure();
	InternalFrame frame = new InternalFrame();

	SdTableNode tableNode;

	public TableNodeFigure(SdTableNode sdTableNode) {
		super();
		this.tableNode = sdTableNode;
		setOpaque(true);
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(tableColor);
		setForegroundColor(ColorConstants.black);
		frame.setLabel(sdTableNode.getName());
		frame.add(columnListFigure);
		frame.setPreferredSize(sdTableNode.getSize());
		add(frame);
	}

	public IFigure getContentPane() {
		return columnListFigure.getContentPane();
	}

	public void setSelected(boolean selected) {
		frame.setSelected(selected);
		repaint();
	}

	public void refreshTitle() {
		frame.setLabel(tableNode.getName());
	}

}
