
package com.nayaware.dbtools.editors.sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.preferences.PreferenceConstants;

/**
 * Color manager to manage the colors in the SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlEditorColorManager {
	public static final int COLOR_DEFAULT = 0;
	public static final int COLOR_COMMENT = 1;
	public static final int COLOR_STRING = 2;
	public static final int COLOR_KEYWORD = 3;
	public static final int COLOR_DATA_TYPE = 4;
	public static final int COLOR_FUNCTION = 5;

	protected Map<RGB, Color> fColorTable = new HashMap<RGB, Color>();
	protected IPreferenceStore preferenceStore;

	public SqlEditorColorManager() {
		this.preferenceStore = DbExplorerPlugin.getDefault()
				.getPreferenceStore();
	}

	/**
	 * Gets the color from a rgb
	 * 
	 * @param rgb
	 * @return - color object
	 */
	public Color getColor(RGB rgb) {
		Color color = fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}

	/**
	 * Gets the color based on preference key
	 * 
	 * @param prefKey
	 * @return - color object
	 */
	public Color getColor(int colorCode) {
		RGB rgb = new RGB(0, 0, 0);
		switch (colorCode) {
		case COLOR_DEFAULT:
			rgb = getColorFromPreference(PreferenceConstants.COLOR_DEFAULT);
			if (rgb == null) {
				rgb = new RGB(0, 0, 0);
			}
			break;
		case COLOR_COMMENT:
			rgb = getColorFromPreference(PreferenceConstants.COLOR_COMMENT);
			if (rgb == null) {
				rgb = new RGB(0, 255, 0);
			}
			break;
		case COLOR_STRING:
			rgb = getColorFromPreference(PreferenceConstants.COLOR_STRING);
			if (rgb == null) {
				rgb = new RGB(255, 0, 0);
			}
			break;
		case COLOR_KEYWORD:
			rgb = getColorFromPreference(PreferenceConstants.COLOR_KEYWORD);
			if (rgb == null) {
				rgb = new RGB(0, 0, 255);
			}
			break;
		case COLOR_DATA_TYPE:
			rgb = getColorFromPreference(PreferenceConstants.COLOR_DATA_TYPE);
			if (rgb == null) {
				rgb = new RGB(255, 0, 255);
			}
			break;
		case COLOR_FUNCTION:
			rgb = getColorFromPreference(PreferenceConstants.COLOR_FUNCTION);
			if (rgb == null) {
				rgb = new RGB(0, 255, 255);
			}
			break;
		}
		return getColor(rgb);
	}

	private RGB getColorFromPreference(String prefKey) {
		String colorName = preferenceStore.getString(prefKey);
		if ((colorName != null) && (!colorName.equals(""))) { //$NON-NLS-1$
			return StringConverter.asRGB(colorName);
		} else {
			return null;
		}
	}

	/**
	 * Disposes the color manager
	 */
	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext()) {
			Color color = e.next();
			if (color != null && !color.isDisposed()) {
				color.dispose();
			}
		}
	}
}
