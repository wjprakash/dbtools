
package com.nayaware.dbtools.util;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * Utilities that support operation on GEF Figures
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class FigureUtils {

	public static Figure createTransparentFigure(Color color, int alpha) {
		return new AlphaLayerFigure(color, alpha);
	}

	private static class AlphaLayerFigure extends Figure {

		private Color color;
		protected Image layerImage;
		private ImageData imageData;

		public AlphaLayerFigure(Color color, int alpha) {
			super();
			this.color = color;

			PaletteData palette = new PaletteData(new RGB[] { color.getRGB() });
			imageData = new ImageData(1, 1, 8, palette);
			imageData.alpha = alpha;
			imageData.setPixel(0, 0, 0);
			layerImage = new Image(null, imageData);

		}

		public void setAlpha(int alpha) {
			imageData.alpha = alpha;
			if (layerImage != null && !layerImage.isDisposed()) {
				layerImage.dispose();
			}
			layerImage = new Image(null, imageData);
			repaint();
		}

		public int getAlpha() {
			return imageData.alpha;
		}

		public void setColor(Color color) {
			this.color = color;
			imageData.palette.colors[0] = color.getRGB();
			if (layerImage != null && !layerImage.isDisposed()) {
				layerImage.dispose();
			}
			layerImage = new Image(null, imageData);
			repaint();
		}

		public Color getColor() {
			return color;
		}

		@Override
		public void paintFigure(Graphics g) {

			Rectangle rectangle = getClientArea();
			g.drawImage(layerImage, new Rectangle(layerImage.getBounds()),
					rectangle);
		}
	}
}
