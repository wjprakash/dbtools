
package com.nayaware.dbtools.schemadesigner.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;

/**
 * Figure for the Schema Designer Column Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeFigure extends Figure {

	boolean selected = false;
	Label nameLabel;
	SdColumnNode sdColumnNode;

	public ColumnNodeFigure(SdColumnNode sdColumnNode) {
		super();
		this.sdColumnNode = sdColumnNode;
		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(false);
		layout.setHorizontal(true);
		setLayoutManager(layout);
		setBorder(new ColumnFigureBorder());
		setBackgroundColor(ColorConstants.tooltipBackground);
		setForegroundColor(ColorConstants.blue);
		String label = sdColumnNode.getName() + " (" //$NON-NLS-1$
				+ sdColumnNode.getColumnNode().getColumn().getType().getName()
				+ ")"; //$NON-NLS-1$
		nameLabel = new Label(label, sdColumnNode.getColumnNode().getIcon());
		add(nameLabel);
	}

	private class ColumnFigureBorder extends AbstractBorder {

		public Insets getInsets(IFigure figure) {
			return new Insets(5, 3, 3, 1);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			if (selected) {
				Rectangle rect = getPaintRectangle(figure, insets);
				graphics.drawRectangle(new Rectangle(rect.x + 1, rect.y + 1,
						rect.width - 2, rect.height - 2));
			}
		}
	}

	public void update() {
		String label = sdColumnNode.getName() + " (" //$NON-NLS-1$
				+ sdColumnNode.getColumnNode().getColumn().getType().getName()
				+ ")"; //$NON-NLS-1$
		nameLabel.setText(label);
		nameLabel.setIcon(sdColumnNode.getColumnNode().getIcon());
		nameLabel.revalidate();
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
}
