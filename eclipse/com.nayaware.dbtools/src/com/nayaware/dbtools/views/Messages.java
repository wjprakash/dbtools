
package com.nayaware.dbtools.views;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.nayaware.dbtools.views.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
