
package com.nayaware.dbtools.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.nayaware.dbtools.DbExplorerPlugin;

/**
 * Default preference values initializer.
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = DbExplorerPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(PreferenceConstants.COLOR_DEFAULT, "0,0,0"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.COLOR_KEYWORD, "128,0,64"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.COLOR_STRING, "0,0,255"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.COLOR_COMMENT, "0,128,0"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.COLOR_FUNCTION, "209,16,1"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.COLOR_DATA_TYPE, "0,128,128"); //$NON-NLS-1$
	}

}
