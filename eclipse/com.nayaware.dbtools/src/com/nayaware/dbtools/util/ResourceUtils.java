
package com.nayaware.dbtools.util;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Utilities for resources such as fonts and colors
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ResourceUtils {

	private static HashMap<RGB, Color> colorMap = new HashMap<RGB, Color>();
	private static HashMap<String, Font> fontMap = new HashMap<String, Font>();
	private static HashMap<Font, Font> fontToBoldFontMap = new HashMap<Font, Font>();
	private static HashMap<Integer, Cursor> idToCursorMap = new HashMap<Integer, Cursor>();

	/**
	 * Gets the system color based on specific ID
	 */
	public static Color getColor(int systemColorID) {
		Display display = Display.getCurrent();
		return display.getSystemColor(systemColorID);
	}

	/**
	 * Gets the color for given Red, Green & Blue values
	 */
	public static Color getColor(int r, int g, int b) {
		return getColor(new RGB(r, g, b));
	}

	/**
	 * Gets the color for given RGB value
	 */
	public static Color getColor(RGB rgb) {
		Color color = colorMap.get(rgb);
		if (color == null) {
			Display display = Display.getCurrent();
			color = new Color(display, rgb);
			colorMap.put(rgb, color);
		}
		return color;
	}

	/**
	 * Disposes all the cached colors
	 */
	public static void disposeColors() {
		for (Iterator<Color> iter = colorMap.values().iterator(); iter
				.hasNext();)
			iter.next().dispose();
		colorMap.clear();
	}

	/**
	 * Returns a font based on its name, height and style
	 */
	public static Font getFont(String name, int height, int style) {
		return getFont(name, height, style, false, false);
	}

	/**
	 * Returns a font based on its name, height and style.
	 */
	public static Font getFont(String name, int size, int style,
			boolean strikeout, boolean underline) {
		String fontName = name + '|' + size + '|' + style + '|' + strikeout
				+ '|' + underline;
		Font font = fontMap.get(fontName);
		if (font == null) {
			FontData fontData = new FontData(name, size, style);
			if (strikeout || underline) {
				try {
					Class<?> logFontClass = Class
							.forName("org.eclipse.swt.internal.win32.LOGFONT"); //$NON-NLS-1$
					Object logFont = FontData.class
							.getField("data").get(fontData); //$NON-NLS-1$
					if (logFont != null && logFontClass != null) {
						if (strikeout) {
							logFontClass
									.getField("lfStrikeOut").set(logFont, new Byte((byte) 1)); //$NON-NLS-1$
						}
						if (underline) {
							logFontClass
									.getField("lfUnderline").set(logFont, new Byte((byte) 1)); //$NON-NLS-1$
						}
					}
				} catch (Throwable e) {
					System.err
							.println("Unable to set underline or strikeout" + " (probably on a non-Windows platform). " + e); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			font = new Font(Display.getCurrent(), fontData);
			fontMap.put(fontName, font);
		}
		return font;
	}

	/**
	 * Return a bold version of the give font
	 */
	public static Font getBoldFont(Font baseFont) {
		Font font = fontToBoldFontMap.get(baseFont);
		if (font == null) {
			FontData fontDatas[] = baseFont.getFontData();
			FontData data = fontDatas[0];
			font = new Font(Display.getCurrent(), data.getName(), data
					.getHeight(), SWT.BOLD);
			fontToBoldFontMap.put(baseFont, font);
		}
		return font;
	}

	/**
	 * Disposes all the cached fonts
	 */
	public static void disposeFonts() {
		for (Iterator<Font> iter = fontMap.values().iterator(); iter.hasNext();)
			iter.next().dispose();
		fontMap.clear();
		for (Iterator<Font> iter = fontToBoldFontMap.values().iterator(); iter
				.hasNext();)
			iter.next().dispose();
		fontToBoldFontMap.clear();
	}

	public static Cursor getCursor(int id) {
		Integer key = new Integer(id);
		Cursor cursor = idToCursorMap.get(key);
		if (cursor == null) {
			cursor = new Cursor(Display.getDefault(), id);
			idToCursorMap.put(key, cursor);
		}
		return cursor;
	}

	/**
	 * Disposes all the cached cursors
	 */
	public static void disposeCursors() {
		for (Iterator<Cursor> iter = idToCursorMap.values().iterator(); iter
				.hasNext();)
			iter.next().dispose();
		idToCursorMap.clear();
	}

	public static void dispose() {
		disposeFonts();
		disposeColors();
		disposeCursors();
	}
}
