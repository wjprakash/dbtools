
package com.nayaware.dbtools.editors.sql.templates;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Template manager for SQL Editor content assistant
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlTemplateManager {

	private static final String CUSTOM_TEMPLATES_KEY = "com.nayaware.dbtools.customtemplates"; //$NON-NLS-1$

	private static SqlTemplateManager defaultInstance;

	private TemplateStore templateStore;

	private ContributionContextTypeRegistry contextTypeRegistry;

	private SqlTemplateManager() {

	}

	public static SqlTemplateManager getInstance() {
		if (defaultInstance == null) {
			defaultInstance = new SqlTemplateManager();
		}
		return defaultInstance;
	}

	public TemplateStore getTemplateStore() {
		if (templateStore == null) {
			templateStore = new ContributionTemplateStore(
					getContextTypeRegistry(), DbExplorerPlugin.getDefault()
							.getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
			try {
				templateStore.load();
			} catch (IOException exc) {
				ErrorManager.showException(exc);
			}
		}
		return templateStore;
	}

	public ContextTypeRegistry getContextTypeRegistry() {
		if (contextTypeRegistry == null) {
			contextTypeRegistry = new ContributionContextTypeRegistry();
			contextTypeRegistry
					.addContextType(SqlTemplateContextType.CONTEXT_TYPE_SQL);
			// contextTypeRegistry.addContextType(SqlTemplateContextType.
			// CONTEXT_TYPE_FUNCTION);
		}
		return contextTypeRegistry;
	}

	public IPreferenceStore getPreferenceStore() {
		return DbExplorerPlugin.getDefault().getPreferenceStore();
	}
}
