
package com.nayaware.dbtools.querybuilder.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LabeledContainer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class InternalFrame extends LabeledContainer {

	private boolean selected;

	private class InternalFrameBorder extends CompoundBorder {
		TitleBarBorder titlebar;

		InternalFrameBorder() {
			titlebar = new TitleBarBorder();
			titlebar.setTextColor(ColorConstants.white);
			titlebar.setBackgroundColor(ColorConstants.darkGray);
			inner = new CompoundBorder(new LineBorder(FigureUtilities
					.mixColors(ColorConstants.buttonDarker,
							ColorConstants.button), 3), new SchemeBorder(
					SchemeBorder.SCHEMES.LOWERED));
			outer = new CompoundBorder(new SchemeBorder(
					SchemeBorder.SCHEMES.RAISED), titlebar);
		}

		@Override
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			if (selected) {
				titlebar.setBackgroundColor(ColorConstants.darkBlue);
			} else {
				titlebar.setBackgroundColor(ColorConstants.darkGray);
			}
			super.paint(figure, graphics, insets);
		}
	}

	public InternalFrame() {
		setBorder(new InternalFrameBorder());
		setLayoutManager(new StackLayout());
		setOpaque(true);
		setRequestFocusEnabled(true);
	}

	public InternalFrame(String title) {
		this();
		setLabel(title);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}