
package com.nayaware.dbtools.querybuilder.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.CheckBox;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToggleModel;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import com.nayaware.dbtools.querybuilder.model.QbColumnNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * Figure for the Query Builder Column Node wrapper
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbColumnNodeFigure extends Figure {

	boolean selected = false;
	private Clickable checkBox;
	private QueryData queryData;
	private Label nameLabel;

	public QbColumnNodeFigure(final QbColumnNode qbColumnNode,
			final QueryData query) {
		super();
		this.queryData = query;

		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(false);
		layout.setHorizontal(true);
		setLayoutManager(layout);

		setBorder(new ColumnFigureBorder());

		setBackgroundColor(ColorConstants.tooltipBackground);
		setForegroundColor(ColorConstants.blue);
		setOpaque(true);

		checkBox = new CheckBox();
		if (qbColumnNode.isSelected()) {
			checkBox.setSelected(true);
		} else {
			checkBox.setSelected(false);
		}
		ButtonModel bModel = new ToggleModel();
		bModel.addChangeListener(new ChangeListener() {
			public void handleStateChanged(ChangeEvent e) {
				qbColumnNode.setSelected(checkBox.isSelected());
				if (checkBox.isSelected()) {
					queryData.addSelectedColumnNode(qbColumnNode);
					nameLabel.setForegroundColor(ColorConstants.darkGreen);
				} else {
					queryData.removeSelectedColumnNode(qbColumnNode);
					nameLabel.setForegroundColor(ColorConstants.blue);
				}
				nameLabel.revalidate();
				nameLabel.repaint();
			}
		});
		checkBox.setModel(bModel);
		add(checkBox);
		nameLabel = new Label(qbColumnNode.getName() + "(" //$NON-NLS-1$
				+ qbColumnNode.getColumnNode().getColumn().getType().getName()
				+ ")", qbColumnNode.getColumnNode().getIcon()); //$NON-NLS-1$
		if (qbColumnNode.isSelected()) {
			nameLabel.setForegroundColor(ColorConstants.darkGreen);
		}
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

	public void setChecked(boolean checked) {
		if (checked) {
			checkBox.setSelected(true);
			nameLabel.setForegroundColor(ColorConstants.darkGreen);
		} else {
			checkBox.setSelected(false);
			nameLabel.setForegroundColor(ColorConstants.blue);
		}
		nameLabel.revalidate();
		nameLabel.repaint();
		repaint();
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
}
